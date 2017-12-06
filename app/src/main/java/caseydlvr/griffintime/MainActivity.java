package caseydlvr.griffintime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "time_channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "Current time notification";
    private static final String KEY_NEXT = "next_key";
    private static final String PREFS_FILE = "caseydlvr.griffintime.preferences";
    private static final String KEY_SAVED_TIME = "key_saved_time";

    private GriffinTime mCurrentTime;
    private GriffinTimes mGriffinTimes;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyMgr;
    private BroadcastReceiver mNextReceiver;
    private NotificationCompat.BigTextStyle mBigTextStyle = new NotificationCompat.BigTextStyle();

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mPrefEditor;

    @BindView(R.id.timeText) TextView mTimeText;
    @BindView(R.id.nextText) TextView mNextText;
    @BindView(R.id.nextButton) Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSharedPreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mPrefEditor = mSharedPreferences.edit();
        mGriffinTimes = new GriffinTimes();
        setupNotifications();

        // handling next from notification
        // should be able to register the BroadcastReceiver in the app manifest
        IntentFilter filter = new IntentFilter();
        filter.addAction(KEY_NEXT);

        mNextReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(KEY_NEXT)) {
                    nextTime();
                }
            }
        };

        registerReceiver(mNextReceiver, filter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGriffinTimes.setCurrentTime(mSharedPreferences.getInt(KEY_SAVED_TIME, 0));
        mCurrentTime = mGriffinTimes.getCurrent();
        updateViews();
        updateNotification();
        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        persistCurrentTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // normally should be in onPause, but then notification action won't work
        // unless activity is open. This is a hack to keep the notification action working
        // even when the Activity is paused/stopped until I work out some sort of service to
        // handle the notification action
        unregisterReceiver(mNextReceiver);
    }

    private void updateViews() {
        mTimeText.setText(mCurrentTime.getTime());
        mNextText.setText(mCurrentTime.getNextCriteria());
    }

    private void updateNotification() {
        mBigTextStyle.bigText(mCurrentTime.getNextCriteria());
        mBuilder.setContentTitle("It's " + mCurrentTime.getTime())
                .setContentText(mCurrentTime.getNextCriteria());
    }

    private void setupNotifications() {
        mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_MIN);

            channel.enableVibration(false);

            mNotifyMgr.createNotificationChannel(channel);
        }

        // Intent for using the next action from the notification
        Intent nextIntent = new Intent(KEY_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0);

        // Intent for clicking the notification
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_clock)
                .addAction(R.drawable.ic_stat_check, getString(R.string.notification_next_action), nextPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_STATUS)
                .setStyle(mBigTextStyle)
                .setColor(getResources().getColor(R.color.primaryLightColor))
                .setOngoing(true)
                .setContentIntent(resultPendingIntent);
    }

    private void persistCurrentTime() {
        mPrefEditor.putInt(KEY_SAVED_TIME, mGriffinTimes.getCurrentInt());
        mPrefEditor.apply();
    }

    @OnClick (R.id.nextButton)
    public void nextTime() {
        mCurrentTime = mGriffinTimes.getNext();
        updateViews();
        updateNotification();
        persistCurrentTime();

        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
