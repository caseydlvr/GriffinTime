package caseydlvr.griffintime;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

public class GriffinTimeWidgets {

    private Context mContext;
    private AppWidgetManager mAppWidgetManager;

    public GriffinTimeWidgets(Context context) {
        mContext = context;
        mAppWidgetManager = AppWidgetManager.getInstance(context);
    }

    public void updateAll(GriffinTime currentTime) {
        int[] widgetIds = mAppWidgetManager
                .getAppWidgetIds(new ComponentName(mContext, GriffinTimeWidget.class));

        GriffinTimeWidget.updateWidgets(mContext, mAppWidgetManager, widgetIds, currentTime);
    }
}
