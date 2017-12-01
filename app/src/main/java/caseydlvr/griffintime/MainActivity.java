package caseydlvr.griffintime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    private GriffinTime mCurrentTime;
    private GriffinTimes mGriffinTimes;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyMgr;
    private NotificationChannel mChannel;

    @BindView(R.id.timeText) TextView mTimeText;
    @BindView(R.id.nextText) TextView mNextText;
    @BindView(R.id.nextButton) Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGriffinTimes = new GriffinTimes();
        mCurrentTime = mGriffinTimes.getCurrent();
        updateViews();
        setupNotifications();

        // handling next from notification
        // should be able to register the BroadcastReceiver in the app manifest
        IntentFilter filter = new IntentFilter();
        filter.addAction(KEY_NEXT);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(KEY_NEXT)) {
                    nextTime();
                }
            }
        };

        // do I need to unregister this at some point? unregisterReceiver()
        // get error about this Activity leaking the IntentReceiver when restarting the app
        registerReceiver(receiver, filter);

    }

    private void updateViews() {
        mTimeText.setText(mCurrentTime.getTime());
        mNextText.setText(mCurrentTime.getNextCriteria());
    }

    private void updateNotification() {
        mBuilder.setContentTitle(mCurrentTime.getTime())
                .setContentText(mCurrentTime.getNextCriteria());
    }

    private void setupNotifications() {
        mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_MIN);

            mChannel.enableVibration(false);

            mNotifyMgr.createNotificationChannel(mChannel);
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
                .setSmallIcon(R.drawable.ic_clock)
                .setContentTitle(mCurrentTime.getTime())
                .setContentText(mCurrentTime.getNextCriteria())
                .addAction(R.drawable.ic_stat_check, getString(R.string.notification_next_action), nextPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_STATUS)
                .setOngoing(true)
                .setContentIntent(resultPendingIntent);
    }

    @OnClick (R.id.nextButton)
    public void nextTime() {
        mCurrentTime = mGriffinTimes.getNext();
        updateViews();
        updateNotification();

        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
