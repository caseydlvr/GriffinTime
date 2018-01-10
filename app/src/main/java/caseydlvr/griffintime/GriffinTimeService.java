package caseydlvr.griffintime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class GriffinTimeService extends Service {
    public static final String ACTION_NOTIFY = "caseydlvr.griffintime.NOTIFY";
    public static final String ACTION_DISMISS = "caseydlvr.griffintime.DISMISS";
    public static final String ACTION_ONGOING = "caseydlvr.griffintime.ONGOING";
    public static final String ACTION_OFFGOING = "caseydlvr.griffintime.OFFGOING";
    public static final String ACTION_NEXT = "caseydlvr.griffintime.NEXT";
    public static final String ACTION_WIDGET_SYNC = "caseydlvr.griffintime.WIDGET_SYNC";
    public static final String KEY_WIDGET_ID = "key_widget_id";
    public static final String KEY_USE_FOREGROUND = "key_use_foreground";
    private static final String NOTIFICATION_CHANNEL_ID = "time_channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "Current time notification";
    private static final String NOTIFICATION_CHANNEL_ID_FG = "time_channel_foreground";
    private static final String NOTIFICATION_CHANNEL_NAME_FG = "Widget update foreground notification";
    private static final String PREFS_FILE = "caseydlvr.griffintime.preferences";
    private static final String KEY_SAVED_TIME = "key_saved_time";
    private static final int NOTIFICATION_ID = 1;
    private static final int NOTIFICATION_ID_FG = 2;

    private GriffinTime mCurrentTime;
    private GriffinTimes mGriffinTimes;
    private SharedPreferences.Editor mPrefEditor;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyMgr;
    private NotificationCompat.BigTextStyle mBigTextStyle = new NotificationCompat.BigTextStyle();
    private Notification mNotificationFg;
    private AppWidgetManager mAppWidgetManager;
    private IBinder mBinder = new LocalBinder();
    private boolean mUseNotification;
    private boolean mIsOngoing;

    @Override
    public void onCreate() {
        SharedPreferences settingPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mUseNotification = settingPrefs.getBoolean(SettingsActivity.KEY_SHOW_NOTIFICATION, true);
        mIsOngoing = settingPrefs.getBoolean(SettingsActivity.KEY_ONGOING_NOTIFICATION, true);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mPrefEditor = sharedPreferences.edit();
        mGriffinTimes = new GriffinTimes();
        mGriffinTimes.setCurrentTime(sharedPreferences.getInt(KEY_SAVED_TIME, 0));
        mCurrentTime = mGriffinTimes.getCurrent();
        setupNotifications();
        updateNotification();

        mAppWidgetManager = AppWidgetManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean useForeground = intent.getBooleanExtra(KEY_USE_FOREGROUND, false);

        if (useForeground) {
            startForeground(NOTIFICATION_ID_FG, mNotificationFg);
        }

        switch (intent.getAction()) {
            case ACTION_NEXT:
                nextTime();
                break;
            case ACTION_NOTIFY:
                mUseNotification = true;
                updateNotification();
                break;
            case ACTION_DISMISS:
                mUseNotification = false;
                mNotifyMgr.cancel(NOTIFICATION_ID);
                break;
            case ACTION_ONGOING:
                mIsOngoing = true;
                updateNotification();
                break;
            case ACTION_OFFGOING:
                mIsOngoing = false;
                updateNotification();
                break;
            case ACTION_WIDGET_SYNC:
                updateWidget(intent.getIntExtra(KEY_WIDGET_ID, -1));
                break;
        }

        if (useForeground) {
            stopForeground(true);
        }

        stopSelf(startId);

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void nextTime() {
        mCurrentTime = mGriffinTimes.getNext();
        persistCurrentTime();
        updateNotification();
        updateWidgets();
    }

    public GriffinTime getCurrentTime() {
        return mCurrentTime;
    }

    private void setupNotifications() {
        mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_MIN);
            channel.enableVibration(false);
            mNotifyMgr.createNotificationChannel(channel);

            // for foreground notification when using widgets
            NotificationChannel fgChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID_FG,
                    NOTIFICATION_CHANNEL_NAME_FG,
                    NotificationManager.IMPORTANCE_LOW);
            fgChannel.enableVibration(false);
            mNotifyMgr.createNotificationChannel(fgChannel);
        }

        // Intent for using the next action from the notification
        Intent nextIntent = new Intent(this, GriffinTimeService.class);
        nextIntent.setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent =
                PendingIntent.getService(
                        this,
                        0,
                        nextIntent,
                        0
                );

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
                .setContentIntent(resultPendingIntent);

        mNotificationFg = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_FG)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_clock)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentTitle("Griffin Time Widgets")
                .setContentText("Updating widgets...")
                .build();
    }

    private void updateNotification() {
        mBigTextStyle.bigText(mCurrentTime.getNextCriteria());
        mBuilder.setContentTitle("It's " + mCurrentTime.getTime())
                .setContentText(mCurrentTime.getNextCriteria())
                .setOngoing(mIsOngoing);

        if (mUseNotification) mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
        else                  mNotifyMgr.cancel(NOTIFICATION_ID);
    }

    private void updateWidgets() {
        int[] widgetIds = mAppWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));

        for (int id : widgetIds) {
            updateWidget(id);
        }
    }

    private void updateWidget(int id) {
        // invalid ID
        if (id < 0) return;

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
        remoteViews.setTextViewText(R.id.timeText, "It's " + mCurrentTime.getTime());
        remoteViews.setTextViewText(R.id.nextText, mCurrentTime.getNextCriteria());
        mAppWidgetManager.updateAppWidget(id, remoteViews);
    }

    private void persistCurrentTime() {
        mPrefEditor.putInt(KEY_SAVED_TIME, mGriffinTimes.getCurrentInt());
        mPrefEditor.apply();
    }

    class LocalBinder extends Binder {
        GriffinTimeService getService() {
            return GriffinTimeService.this;
        }
    }
}
