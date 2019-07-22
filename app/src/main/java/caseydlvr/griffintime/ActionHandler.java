package caseydlvr.griffintime;

import android.content.Context;

import caseydlvr.griffintime.data.GriffinTimeRepository;
import caseydlvr.griffintime.model.GriffinTime;
import caseydlvr.griffintime.notification.GriffinTimeNotification;
import caseydlvr.griffintime.widget.GriffinTimeWidgets;

public class ActionHandler {
    public static final String ACTION_NOTIFY = "caseydlvr.griffintime.action.NOTIFY";
    public static final String ACTION_DISMISS = "caseydlvr.griffintime.action.DISMISS";
    public static final String ACTION_NEXT = "caseydlvr.griffintime.action.NEXT";

    private Context mContext;
    private GriffinTimeRepository mRepository;

    public ActionHandler(Context context, GriffinTimeRepository griffinTimeRepository) {
        mContext = context;
        mRepository = griffinTimeRepository;
    }

    public void handleAction(String action) {
        switch (action) {
            case ACTION_NEXT:
                handleNext();
                break;
            case ACTION_NOTIFY:
                handleNotify();
                break;
            case ACTION_DISMISS:
                handleDismiss();
                break;
        }
    }

    private void handleNext() {
        GriffinTime currentTime = mRepository.next();

        new GriffinTimeNotification(mContext, mRepository).notify(currentTime);
        new GriffinTimeWidgets(mContext).updateAll(currentTime);
    }

    private void handleNotify() {
        new GriffinTimeNotification(mContext, mRepository).notifyCurrentTime();
    }

    private void handleDismiss() {
        new GriffinTimeNotification(mContext, mRepository).dismiss();
    }
}
