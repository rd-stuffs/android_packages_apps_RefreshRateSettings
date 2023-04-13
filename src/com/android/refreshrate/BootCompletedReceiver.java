package com.android.refreshrate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.PowerManager;

import com.android.refreshrate.PowerSaveModeChangeReceiver;
import com.android.refreshrate.RefreshRateUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            return;
        }
        RefreshRateUtils.setFPS(RefreshRateUtils.getRefreshRate(context));
        IntentFilter filter = new IntentFilter();
        PowerSaveModeChangeReceiver receiver = new PowerSaveModeChangeReceiver();
        filter.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED);
        context.getApplicationContext().registerReceiver(receiver, filter);
    }
}
