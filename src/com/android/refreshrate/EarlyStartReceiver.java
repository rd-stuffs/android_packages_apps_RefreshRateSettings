package com.android.refreshrate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.refreshrate.RefreshRateUtils;

public class EarlyStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        RefreshRateUtils.setFPS(0);
    }
}
