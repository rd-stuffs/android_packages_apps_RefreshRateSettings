package com.android.refreshrate;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;

public class MainSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.refresh_rate_settings);
    }

}
