package caseydlvr.griffintime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GriffinTimeActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            ActionHandler.handleAction(context, intent.getAction());
        }
    }
}
