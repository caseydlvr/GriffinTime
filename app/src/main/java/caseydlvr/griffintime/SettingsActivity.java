package caseydlvr.griffintime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_SHOW_NOTIFICATION = "pref_show_notification";
    public static final String KEY_ONGOING_NOTIFICATION = "pref_ongoing_notification";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Intent intent = new Intent(SettingsActivity.this, GriffinTimeService.class);
                    switch (key) {
                        case KEY_SHOW_NOTIFICATION:
                            if (sharedPreferences.getBoolean(key, true)) {
                                intent.setAction(GriffinTimeService.ACTION_NOTIFY);
                            } else {
                                intent.setAction(GriffinTimeService.ACTION_DISMISS);
                            }
                            startService(intent);
                            break;
                        case KEY_ONGOING_NOTIFICATION:
                            if (sharedPreferences.getBoolean(key, true)) {
                                intent.setAction(GriffinTimeService.ACTION_ONGOING);
                            } else {
                                intent.setAction(GriffinTimeService.ACTION_OFFGOING);
                            }
                            startService(intent);
                            break;
                    }
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);;
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);

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
