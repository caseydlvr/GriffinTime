package caseydlvr.griffintime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * BroadcastReceiver for the BOOT_COMPLETED system action. This receiver doesn't run any of its own
 * code, but it being triggered causes the Application's onCreate() to run, which does the
 * necessary initialization to trigger the GriffinTimeNotification on boot. Without this, the
 * GriffinTime notification won't appear after boot until the app is launched by the user.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // notifications initialized in App onCreate when this receiver runs
    }
}
