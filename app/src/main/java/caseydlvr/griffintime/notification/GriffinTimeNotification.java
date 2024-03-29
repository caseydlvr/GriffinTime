package caseydlvr.griffintime.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import caseydlvr.griffintime.actions.ActionHandler;
import caseydlvr.griffintime.data.Repository;
import caseydlvr.griffintime.model.GriffinTime;
import caseydlvr.griffintime.actions.ActionReceiver;
import caseydlvr.griffintime.ui.MainActivity;
import caseydlvr.griffintime.R;

public class GriffinTimeNotification {

    private static final String NOTIFICATION_CHANNEL_ID = "griffin_time_channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "Current time notification";
    private static final int NOTIFICATION_ID = 1;

    private final NotificationCompat.BigTextStyle mBigTextStyle = new NotificationCompat.BigTextStyle();
    private final NotificationCompat.Builder mBuilder;
    private final NotificationManager mNotifyMgr;
    private final Context mContext;
    private final Repository mRepository;

    public GriffinTimeNotification(Context context, Repository repository) {
        mContext = context;
        mRepository = repository;
        mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        initNotificationChannel();
        mBuilder = initNotification();
    }

    public void dismiss() {
        mNotifyMgr.cancel(NOTIFICATION_ID);
    }

    public void notifyCurrentTime() {
        if (!mRepository.useNotification()) return;

        GriffinTime currentTime = mRepository.getCurrentTime();

        mBigTextStyle.bigText(currentTime.getNextCriteria());
        mBuilder.setContentTitle("It's " + currentTime.getTime())
                .setContentText(currentTime.getNextCriteria())
                .setOngoing(mRepository.isOngoing());

        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_MIN);
            channel.enableVibration(false);
            mNotifyMgr.createNotificationChannel(channel);
        }
    }

    private NotificationCompat.Builder initNotification() {
        return new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_clock)
                .addAction(R.drawable.ic_stat_check, mContext.getString(R.string.notification_next_action), buildNextPendingIntent())
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_STATUS)
                .setStyle(mBigTextStyle)
                .setColor(mContext.getResources().getColor(R.color.primaryLightColor))
                .setContentIntent(buildResultPendingIntent());
    }

    private PendingIntent buildNextPendingIntent() {
        Intent nextIntent = new Intent(mContext, ActionReceiver.class);
        nextIntent.setAction(ActionHandler.ACTION_NEXT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.getBroadcast(
                    mContext,
                    0,
                    nextIntent,
                    PendingIntent.FLAG_IMMUTABLE
            );
        } else {
            return PendingIntent.getBroadcast(
                    mContext,
                    0,
                    nextIntent,
                    0
            );
        }
    }

    private PendingIntent buildResultPendingIntent() {
        Intent resultIntent = new Intent(mContext, MainActivity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.getActivity(
                    mContext,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            return PendingIntent.getActivity(
                    mContext,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }
}
