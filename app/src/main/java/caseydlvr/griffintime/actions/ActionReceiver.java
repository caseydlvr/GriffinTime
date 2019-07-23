package caseydlvr.griffintime.actions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import caseydlvr.griffintime.GriffinTimeApp;

public class ActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            ActionHandler actionHandler =
                    ((GriffinTimeApp) context.getApplicationContext()).getActionHandler();
                    actionHandler.handleAction(intent.getAction());
        }
    }
}
