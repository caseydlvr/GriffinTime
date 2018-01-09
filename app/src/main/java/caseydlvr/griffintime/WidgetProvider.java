package caseydlvr.griffintime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int[] realAppWidgetIds = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(new ComponentName(context, WidgetProvider.class));

        for (int id: realAppWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setOnClickPendingIntent(R.id.imageView, pendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.textLinearLayout, pendingIntent);

            remoteViews.setTextViewText(R.id.timeText, "It's Lunch");
            remoteViews.setTextViewText(R.id.nextText, "Have you finished the last bite of your sandwich or soup?");

            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }
}
