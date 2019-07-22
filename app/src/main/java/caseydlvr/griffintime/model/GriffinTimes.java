package caseydlvr.griffintime.model;

public class GriffinTimes {
    private final GriffinTime[] griffinTimes =  new GriffinTime[]{
            new GriffinTime(
                    "9:30",
                    "Have you eaten breakfast and are hungry again?"),
            new GriffinTime(
                    "Lunch",
                    "Have you finished the last bite of your sandwich or soup?"),
            new GriffinTime(
                    "4:20",
                    "Is it dark out?"),
            new GriffinTime(
                    "7:00",
                    "Are you sleepy?"),
            new GriffinTime(
                    "2 AM",
                    "Have you woken up in the morning?")
    };

    private int mCurrentTime;

    public GriffinTimes() {
        mCurrentTime = 0;
    }

    public GriffinTimes(int currentTime) {
        mCurrentTime = currentTime;
    }

    public GriffinTime next() {
        if (mCurrentTime == griffinTimes.length - 1) {
            mCurrentTime = 0;
        }
        else {
            mCurrentTime++;
        }

        return griffinTimes[mCurrentTime];
    }

    public GriffinTime getCurrent() {
        return griffinTimes[mCurrentTime];
    }

    public int getCurrentInt() {
        return mCurrentTime;
    }

    public GriffinTime get(int i) {
        return griffinTimes[i];
    }

    public void setCurrentTime(int i) {
        mCurrentTime = i;
    }
}
