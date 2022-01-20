package com.android.refreshrate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.android.refreshrate.RefreshUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            return;
        }
        RefreshUtils.initialize(context);
    }
}
