package caseydlvr.griffintime;

public class GriffinTimes {
    private GriffinTime[] mGriffinTimes;
    private int mCurrentTime;

    public GriffinTimes() {
        mCurrentTime = 0;
        mGriffinTimes = new GriffinTime[5];

        mGriffinTimes[0] = new GriffinTime(
                "9:30",
                "Have you eaten breakfast and are hungry again?"
        );

        mGriffinTimes[1] = new GriffinTime(
                "Lunch",
                "Have you finished the last bite of your sandwich or soup?"
        );

        mGriffinTimes[2] = new GriffinTime(
                "4:20",
                "Is it dark out?"
        );

        mGriffinTimes[3] = new GriffinTime(
                "7:00",
                "Are you sleepy?"
        );

        mGriffinTimes[4] = new GriffinTime(
                "2 AM",
                "Have you woken up in the morning?"
        );
    }

    public GriffinTime getNext() {
        if (mCurrentTime == mGriffinTimes.length - 1) {
            mCurrentTime = 0;
        }
        else {
            mCurrentTime++;
        }

        return mGriffinTimes[mCurrentTime];
    }

    public GriffinTime getCurrent() {
        return mGriffinTimes[mCurrentTime];
    }

    public int getCurrentInt() {
        return mCurrentTime;
    }

    public GriffinTime get(int i) {
        return mGriffinTimes[i];
    }

    public void setCurrentTime(int i) {
        mCurrentTime = i;
    }
}
