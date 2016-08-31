package de.jablab.sebschlicht.kits.activities.preferences;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.WindowManager;
import de.jablab.sebschlicht.kits.R;

public class PlaySeriesPreferences extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.play_series_preferences);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        String[] keys = new String[] {
                "playback_device_auto", "playback_device_device", "playback_device_server"
        };

        if (key.startsWith("playback_device")) {
            for (String disableKey : keys) {
                if (!key.equals(disableKey)) {
                    CheckBoxPreference checkBox =
                            (CheckBoxPreference) this.findPreference(disableKey);
                    checkBox.setChecked(false);
                }
            }
        }

        // stay checked
        return (Boolean) newValue;
    }
}
