package caseydlvr.griffintime.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import caseydlvr.griffintime.GriffinTimeApp;
import caseydlvr.griffintime.model.GriffinTime;

public class GriffinTimeWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        GriffinTime currentTime =
                ((GriffinTimeApp) context.getApplicationContext()).getRepository().getCurrentTime();

        new GriffinTimeWidgets(context, appWidgetManager)
                .updateWidgets(appWidgetIds, currentTime);
    }
}
