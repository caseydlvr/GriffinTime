package caseydlvr.griffintime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import caseydlvr.griffintime.actions.ActionHandler;
import caseydlvr.griffintime.actions.ActionReceiver;
import caseydlvr.griffintime.R;
import caseydlvr.griffintime.model.GriffinTime;
import caseydlvr.griffintime.ui.MainActivity;

public class GriffinTimeWidgets {

    private Context mContext;
    private AppWidgetManager mAppWidgetManager;

    public GriffinTimeWidgets(Context context, AppWidgetManager appWidgetManager) {
        mContext = context;
        mAppWidgetManager = appWidgetManager;
    }

    public void updateWidgets(int[] appWidgetIds, GriffinTime currentTime) {
        RemoteViews widgetViews = buildWidgetViews(currentTime);

        for (int id : appWidgetIds) {
            mAppWidgetManager.updateAppWidget(id, widgetViews);
        }
    }

    public void updateWidgets(GriffinTime currentTime) {
        int[] widgetIds = mAppWidgetManager
                .getAppWidgetIds(new ComponentName(mContext, GriffinTimeWidget.class));

        updateWidgets(widgetIds, currentTime);
    }

    private RemoteViews buildWidgetViews(GriffinTime currentTime) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget);

        // populate views
        remoteViews.setTextViewText(R.id.timeText, "It's " + currentTime.getTime());
        remoteViews.setTextViewText(R.id.nextText, currentTime.getNextCriteria());

        // launch activity when clicking image or text
        PendingIntent launchPendingIntent = buildLaunchPendingIntent();
        remoteViews.setOnClickPendingIntent(R.id.imageView, launchPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.textLinearLayout, launchPendingIntent);

        // next time when clicking button
        remoteViews.setOnClickPendingIntent(R.id.nextButton, buildNextTimePendingIntent());

        return remoteViews;
    }

    private PendingIntent buildLaunchPendingIntent() {
        Intent activityIntent = new Intent(mContext, MainActivity.class);

        return PendingIntent.getActivity(
                mContext,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent buildNextTimePendingIntent() {
        Intent nextIntent = new Intent(mContext, ActionReceiver.class);
        nextIntent.setAction(ActionHandler.ACTION_NEXT);

        return PendingIntent.getBroadcast(
                mContext,
                0,
                nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
