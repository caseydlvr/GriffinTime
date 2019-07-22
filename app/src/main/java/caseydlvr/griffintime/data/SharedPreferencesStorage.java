package caseydlvr.griffintime.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import caseydlvr.griffintime.ui.SettingsActivity;

public class SharedPreferencesStorage implements GriffinTimeStorage {

    private static final String PREFS_FILE = "caseydlvr.griffintime.preferences";
    private static final String KEY_SAVED_TIME = "key_saved_time";

    private SharedPreferences mAppPreferences;
    private SharedPreferences mSettingsPreferences;

    public SharedPreferencesStorage(Context context) {
        mAppPreferences =  context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mSettingsPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getCurrentTime() {
        return mAppPreferences
                .getInt(KEY_SAVED_TIME, 0);
    }

    public void persistCurrentTime(int currentTime) {
        mAppPreferences
                .edit()
                .putInt(KEY_SAVED_TIME, currentTime)
                .apply();
    }

    public boolean useNotification() {
        return mSettingsPreferences
                .getBoolean(SettingsActivity.KEY_SHOW_NOTIFICATION, true);
    }

    public boolean isOngoing() {
        return mSettingsPreferences
                .getBoolean(SettingsActivity.KEY_ONGOING_NOTIFICATION, true);
    }
}
