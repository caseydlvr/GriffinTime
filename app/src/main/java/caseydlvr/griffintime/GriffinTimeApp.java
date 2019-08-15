package caseydlvr.griffintime;

import android.app.Application;

import caseydlvr.griffintime.actions.ActionHandler;
import caseydlvr.griffintime.data.Repository;
import caseydlvr.griffintime.data.SharedPreferencesStorage;
import caseydlvr.griffintime.model.GriffinTimes;
import caseydlvr.griffintime.notification.GriffinTimeNotification;

public class GriffinTimeApp extends Application {

    Repository mRepository;
    ActionHandler mActionHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        mRepository = new Repository(
                new GriffinTimes(),
                new SharedPreferencesStorage(this)
        );
        mActionHandler = new ActionHandler(this, mRepository);

        initNotification();
    }

    public Repository getRepository() {
        return mRepository;
    }

    public ActionHandler getActionHandler() {
        return mActionHandler;
    }

    private void initNotification() {
        new GriffinTimeNotification(this, mRepository).notifyCurrentTime();
    }
}
