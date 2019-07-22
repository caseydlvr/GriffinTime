package caseydlvr.griffintime.data;

public interface GriffinTimeStorage {

    int getCurrentTime();
    void persistCurrentTime(int currentTime);
    boolean useNotification();
    boolean isOngoing();
}
