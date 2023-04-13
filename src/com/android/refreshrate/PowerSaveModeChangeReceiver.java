package com.android.refreshrate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.android.refreshrate.RefreshRateUtils;

public class PowerSaveModeChangeReceiver extends BroadcastReceiver {

    private boolean shouldSwitchRefreshRate(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean userSwitch = RefreshRateUtils.getPowerSaveRefreshRateSwitch(context);
        return pm.isPowerSaveMode() && userSwitch;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        int normalRefreshRate = RefreshRateUtils.getRefreshRate(context);
        int powerSaveRefreshRate = RefreshRateUtils.getPowerSaveRefreshRate(context);

        RefreshRateUtils.setFPS(shouldSwitchRefreshRate(context) ? powerSaveRefreshRate : normalRefreshRate);
    }
}
