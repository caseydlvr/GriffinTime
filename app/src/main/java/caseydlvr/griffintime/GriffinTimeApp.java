package caseydlvr.griffintime;

import android.app.Application;

public class GriffinTimeApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initNotification();
    }

    private void initNotification() {
        if (GriffinTimeStorage.useNotification(this)) {
            new GriffinTimeNotification(this).notifyCurrentTime();
        }
    }
}
