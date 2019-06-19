package caseydlvr.griffintime;

import android.content.Context;
import android.preference.PreferenceManager;

public class GriffinTimeStorage {

    private static final String PREFS_FILE = "caseydlvr.griffintime.preferences";
    private static final String KEY_SAVED_TIME = "key_saved_time";

    public static int getCurrentTime(Context context) {
        return context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
                .getInt(KEY_SAVED_TIME, 0);
    }

    public static void persistCurrentTime(Context context, int currentTime) {
        context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_SAVED_TIME, currentTime)
                .apply();
    }

    public static boolean useNotification(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SettingsActivity.KEY_SHOW_NOTIFICATION, true);
    }

    public static boolean isOngoing(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SettingsActivity.KEY_ONGOING_NOTIFICATION, true);
    }
}
