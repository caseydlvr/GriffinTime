package caseydlvr.griffintime.data;

import caseydlvr.griffintime.model.GriffinTime;
import caseydlvr.griffintime.model.GriffinTimes;

public class GriffinTimeRepository {

    private GriffinTimes mGriffinTimes;
    private GriffinTimeStorage mGriffinTimeStorage;

    public GriffinTimeRepository(GriffinTimes griffinTimes, GriffinTimeStorage griffinTimeStorage) {
        mGriffinTimes = griffinTimes;
        mGriffinTimeStorage = griffinTimeStorage;

        mGriffinTimes.setCurrentTime(mGriffinTimeStorage.getCurrentTime());
    }

    public GriffinTime next() {
        GriffinTime nextTime = mGriffinTimes.next();
        mGriffinTimeStorage.persistCurrentTime(mGriffinTimes.getCurrentInt());

        return nextTime;
    }

    public GriffinTime getCurrentTime() {
        return mGriffinTimes.getCurrent();
    }

    public boolean useNotification() {
        return mGriffinTimeStorage.useNotification();
    }

    public boolean isOngoing() {
        return mGriffinTimeStorage.isOngoing();
    }
}
