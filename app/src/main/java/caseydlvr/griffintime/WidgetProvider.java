package caseydlvr.griffintime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id: appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            // launch activity when clicking image or text
            Intent activityIntent = new Intent(context, MainActivity.class);
            PendingIntent activityPendtingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.imageView, activityPendtingIntent);
            remoteViews.setOnClickPendingIntent(R.id.textLinearLayout, activityPendtingIntent);

            // next time when clicking button
            Intent nextIntent = new Intent(context, GriffinTimeService.class);
            nextIntent.setAction(GriffinTimeService.ACTION_NEXT);
            PendingIntent nextPendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nextIntent.putExtra(GriffinTimeService.KEY_USE_FOREGROUND, true);
                nextPendingIntent = PendingIntent.getForegroundService(context, 0, nextIntent, 0);
            } else {
                nextPendingIntent = PendingIntent.getService(context, 0, nextIntent, 0);
            }
            remoteViews.setOnClickPendingIntent(R.id.nextButton, nextPendingIntent);

            // tell the service to update the widget with the current time
            // needed so the widget is populated with the correct time when the widget first loads
            Intent syncIntent = new Intent(context, GriffinTimeService.class);
            syncIntent.setAction(GriffinTimeService.ACTION_WIDGET_SYNC);
            syncIntent.putExtra(GriffinTimeService.KEY_WIDGET_ID, id);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                syncIntent.putExtra(GriffinTimeService.KEY_USE_FOREGROUND, true);
                context.startForegroundService(syncIntent);
            } else {
                context.startService(syncIntent);
            }

            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }
}
