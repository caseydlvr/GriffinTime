package caseydlvr.griffintime.actions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import caseydlvr.griffintime.GriffinTimeApp;
import caseydlvr.griffintime.actions.ActionHandler;

public class GriffinTimeActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            ActionHandler actionHandler =
                    ((GriffinTimeApp) context.getApplicationContext()).getActionHandler();
                    actionHandler.handleAction(intent.getAction());
        }
    }
}
