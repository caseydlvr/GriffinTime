package caseydlvr.griffintime;

import android.content.Context;

public class ActionHandler {
    public static final String ACTION_NOTIFY = "caseydlvr.griffintime.action.NOTIFY";
    public static final String ACTION_DISMISS = "caseydlvr.griffintime.action.DISMISS";
    public static final String ACTION_NEXT = "caseydlvr.griffintime.action.NEXT";

    public static void handleAction(Context context, String action) {
        switch (action) {
            case ACTION_NEXT:
                handleNext(context);
                break;
            case ACTION_NOTIFY:
                handleNotify(context);
                break;
            case ACTION_DISMISS:
                handleDismiss(context);
                break;
        }
    }

    private static void handleNext(Context context) {
        GriffinTimes griffinTimes = new GriffinTimes(GriffinTimeStorage.getCurrentTime(context));
        GriffinTime currentTime = griffinTimes.getNext();
        GriffinTimeStorage.persistCurrentTime(context, griffinTimes.getCurrentInt());

        new GriffinTimeNotification(context).notify(currentTime);
        new GriffinTimeWidgets(context).updateAll(currentTime);
    }

    private static void handleNotify(Context context) {
        new GriffinTimeNotification(context).notifyCurrentTime();
    }

    private static void handleDismiss(Context context) {
        new GriffinTimeNotification(context).dismiss();
    }
}
