package com.android.refreshrate;

import android.app.Activity;
import android.app.ActionBar;

import android.content.Context;
import android.content.om.IOverlayManager;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;

import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;

import com.android.refreshrate.RefreshRateUtils;

public class MainSettingsFragment extends PreferenceFragment {

    private static final String KEY_MIN_REFRESH_RATE = "pref_min_refresh_rate";
    private static final String KEY_POWER_SAVE_REFRESH_RATE = "pref_power_save_refresh_rate";
    private static final String KEY_POWER_SAVE_REFRESH_RATE_SWITCH = "pref_power_save_refresh_rate_switch";

    private IOverlayManager mOverlayService;
    private PowerManager mPowerManagerService;

    private ListPreference mPrefMinRefreshRate;
    private ListPreference mPrefPowerSaveRefreshRate;
    private SwitchPreference mPrefPowerSaveRefreshRateSwitch;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mOverlayService = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));
        mPowerManagerService = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.refresh_rate_settings);
        mPrefMinRefreshRate = (ListPreference) findPreference(KEY_MIN_REFRESH_RATE);
        mPrefMinRefreshRate.setOnPreferenceChangeListener(PrefListener);
        mPrefPowerSaveRefreshRate = (ListPreference) findPreference(KEY_POWER_SAVE_REFRESH_RATE);
        mPrefPowerSaveRefreshRate.setOnPreferenceChangeListener(PrefListener);
        mPrefPowerSaveRefreshRateSwitch = (SwitchPreference) findPreference(KEY_POWER_SAVE_REFRESH_RATE_SWITCH);
        mPrefPowerSaveRefreshRateSwitch.setOnPreferenceChangeListener(PrefListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPrefMinRefreshRate.setValue(Integer.toString(RefreshRateUtils.getRefreshRate(getActivity())));
        mPrefMinRefreshRate.setSummary(mPrefMinRefreshRate.getEntry());
        mPrefPowerSaveRefreshRate.setValue(Integer.toString(RefreshRateUtils.getPowerSaveRefreshRate(getActivity())));
        mPrefPowerSaveRefreshRate.setSummary(mPrefPowerSaveRefreshRate.getEntry());
        mPrefPowerSaveRefreshRateSwitch.setChecked(RefreshRateUtils.getPowerSaveRefreshRateSwitch(getActivity()));
    }

    private final Preference.OnPreferenceChangeListener PrefListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    final String key = preference.getKey();

                    if (KEY_MIN_REFRESH_RATE.equals(key)) {
                        RefreshRateUtils.setRefreshRate(getActivity(), Integer.parseInt((String) value));
                        if (!mPowerManagerService.isPowerSaveMode()) {
                            RefreshRateUtils.setFPS(Integer.parseInt((String) value));
                        }
                        int minRefreshRateIndex = mPrefMinRefreshRate
                                .findIndexOfValue((String) value);
                        mPrefMinRefreshRate
                                .setSummary(mPrefMinRefreshRate.getEntries()[minRefreshRateIndex]);
                    } else if (KEY_POWER_SAVE_REFRESH_RATE.equals(key)) {
                        RefreshRateUtils.setPowerSaveRefreshRate(getActivity(), Integer.parseInt((String) value));
                        if (mPowerManagerService.isPowerSaveMode()) {
                            RefreshRateUtils.setFPS(Integer.parseInt((String) value));
                        }
                        int powerSaveRefreshRateIndex = mPrefPowerSaveRefreshRate
                                .findIndexOfValue((String) value);
                        mPrefPowerSaveRefreshRate
                                .setSummary(mPrefPowerSaveRefreshRate.getEntries()[powerSaveRefreshRateIndex]);
                    } else if (KEY_POWER_SAVE_REFRESH_RATE_SWITCH.equals(key)) {
                        RefreshRateUtils.setPowerSaveRefreshRateSwitch(getActivity(), (boolean) value);
                        if ((boolean) value && mPowerManagerService.isPowerSaveMode()) {
                            RefreshRateUtils.setFPS(RefreshRateUtils.getPowerSaveRefreshRate(getActivity()));
                        } else {
                            RefreshRateUtils.setFPS(RefreshRateUtils.getRefreshRate(getActivity()));
                        }
                        mPrefPowerSaveRefreshRate.setEnabled((boolean) value ? true : false);
                    }
                    return true;
                }
    };
}
