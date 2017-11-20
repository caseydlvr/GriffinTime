package caseydlvr.griffintime;

public class GriffinTime {
    private String mTime;
    private String mNextCriteria;

    public GriffinTime(String time, String nextCriteria) {
        mTime = time;
        mNextCriteria = nextCriteria;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getNextCriteria() {
        return mNextCriteria;
    }

    public void setNextCriteria(String nextCriteria) {
        mNextCriteria = nextCriteria;
    }
}
