package caseydlvr.griffintime;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

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
        mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_clock)
                .setContentTitle(mCurrentTime.getTime())
                .setContentText(mCurrentTime.getNextCriteria());

        // .addAction(icon, title, intent) for an action button in the notification

        mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "my notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);

            mChannel.setDescription("Channel Description");
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotifyMgr.createNotificationChannel(mChannel);
        }

        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
    }

    @OnClick (R.id.nextButton)
    public void nextTime() {
        mCurrentTime = mGriffinTimes.getNext();
        updateViews();
        updateNotification();

        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
