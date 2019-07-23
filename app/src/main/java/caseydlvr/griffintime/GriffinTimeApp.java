package caseydlvr.griffintime;

import android.app.Application;

import caseydlvr.griffintime.actions.ActionHandler;
import caseydlvr.griffintime.data.GriffinTimeRepository;
import caseydlvr.griffintime.data.SharedPreferencesStorage;
import caseydlvr.griffintime.model.GriffinTimes;
import caseydlvr.griffintime.notification.GriffinTimeNotification;

public class GriffinTimeApp extends Application {

    GriffinTimeRepository mRepository;
    ActionHandler mActionHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        mRepository = new GriffinTimeRepository(
                new GriffinTimes(),
                new SharedPreferencesStorage(this)
        );
        mActionHandler = new ActionHandler(this, mRepository);

        initNotification();
    }

    public GriffinTimeRepository getRepository() {
        return mRepository;
    }

    public ActionHandler getActionHandler() {
        return mActionHandler;
    }

    private void initNotification() {
        if (mRepository.useNotification()) {
            new GriffinTimeNotification(this)
                    .notify(mRepository.getCurrentTime(), mRepository.isOngoing());
        }
    }
}
