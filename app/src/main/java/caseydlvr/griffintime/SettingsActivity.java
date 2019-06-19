package caseydlvr.griffintime;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_SHOW_NOTIFICATION = "pref_show_notification";
    public static final String KEY_ONGOING_NOTIFICATION = "pref_ongoing_notification";
    private SharedPreferences mSharedPreferences;

    private SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferenceChangeListener =
            (sharedPreferences, key) -> {
                switch (key) {
                    case KEY_SHOW_NOTIFICATION:
                        if (sharedPreferences.getBoolean(key, true)) {
                            ActionHandler.handleAction(this, ActionHandler.ACTION_NOTIFY);
                        } else {
                            ActionHandler.handleAction(this, ActionHandler.ACTION_DISMISS);
                        }
                        break;
                    case KEY_ONGOING_NOTIFICATION:
                        ActionHandler.handleAction(this, ActionHandler.ACTION_NOTIFY);
                        break;
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);;

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);
    }
}
