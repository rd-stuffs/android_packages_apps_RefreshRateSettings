package com.android.refreshrate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;

public class RefreshRateUtils {

    public static int getRefreshRate(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("parts_pref", Context.MODE_PRIVATE);
        return sharedPref.getInt("refresh_rate", 0);
    }

    public static int getPowerSaveRefreshRate(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("parts_pref", Context.MODE_PRIVATE);
        return sharedPref.getInt("power_save_refresh_rate", 2);
    }

    public static boolean getPowerSaveRefreshRateSwitch(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("parts_pref", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("power_save_refresh_rate_switch", true);
    }

    public static void setRefreshRate(Context context, int refreshRate) {
        SharedPreferences sharedPref = context.getSharedPreferences("parts_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("refresh_rate", refreshRate);
        editor.commit();
    }

    public static void setPowerSaveRefreshRate(Context context, int refreshRate) {
        SharedPreferences sharedPref = context.getSharedPreferences("parts_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("power_save_refresh_rate", refreshRate);
        editor.commit();
    }

    public static void setPowerSaveRefreshRateSwitch(Context context, boolean state) {
        SharedPreferences sharedPref = context.getSharedPreferences("parts_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("power_save_refresh_rate_switch", state);
        editor.commit();
    }

    public static final void setFPS(int v) {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken("android.ui.ISurfaceComposer");
        data.writeInt(v);
        try {
            ServiceManager.getService("SurfaceFlinger").transact(1035, data, (Parcel) null, 0);
        } catch (RemoteException e) {
            // nothing we can do
        }
        data.recycle();
    }
}
