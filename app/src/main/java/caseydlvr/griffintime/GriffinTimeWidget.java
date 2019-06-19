package caseydlvr.griffintime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class GriffinTimeWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        GriffinTime currentTime =
                GriffinTimes.getGriffinTimeByInt(GriffinTimeStorage.getCurrentTime(context));

        updateWidgets(context, appWidgetManager, appWidgetIds, currentTime);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, GriffinTime currentTime)  {
        RemoteViews widgetViews = buildWidgetViews(context, currentTime);

        for (int id : appWidgetIds) {
            appWidgetManager.updateAppWidget(id, widgetViews);
        }
    }

    public static RemoteViews buildWidgetViews(Context context, GriffinTime currentTime) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        // populate views
        remoteViews.setTextViewText(R.id.timeText, "It's " + currentTime.getTime());
        remoteViews.setTextViewText(R.id.nextText, currentTime.getNextCriteria());

        // launch activity when clicking image or text
        PendingIntent launchPendingIntent = buildLaunchPendingIntent(context);
        remoteViews.setOnClickPendingIntent(R.id.imageView, launchPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.textLinearLayout, launchPendingIntent);

        // next time when clicking button
        remoteViews.setOnClickPendingIntent(R.id.nextButton, buildNextTimePendingIntent(context));

        return remoteViews;
    }

    private static PendingIntent buildLaunchPendingIntent(Context context) {
        Intent activityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent buildNextTimePendingIntent(Context context) {
        Intent nextIntent = new Intent(context, GriffinTimeActionReceiver.class);
        nextIntent.setAction(ActionHandler.ACTION_NEXT);

        return PendingIntent.getBroadcast(
                context,
                0,
                nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
