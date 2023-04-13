package com.android.refreshrate;

import android.content.Context;
import android.os.PowerManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.android.refreshrate.R;
import com.android.refreshrate.RefreshRateUtils;

import java.util.Arrays;

public class RefreshTileService extends TileService {

    private Context context;
    private Tile tile;

    private String[] refreshRates;
    private String[] refreshRateValues;
    private int currentRefreshRate;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        refreshRates = context.getResources().getStringArray(R.array.device_refresh_rates);
        refreshRateValues = context.getResources().getStringArray(R.array.device_refresh_rates_val);
    }

    private void updateCurrentRefreshRate() {
        currentRefreshRate = Arrays.asList(refreshRateValues).indexOf(Integer.toString(RefreshRateUtils.getRefreshRate(context)));
    }

    private void updateTileDescription() {
        tile.setContentDescription(refreshRates[currentRefreshRate]);
        tile.setSubtitle(refreshRates[currentRefreshRate]);
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        tile = getQsTile();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (RefreshRateUtils.getPowerSaveRefreshRateSwitch(context) && pm.isPowerSaveMode()) {
            String disableText = context.getResources().getString(R.string.refresh_rate_tile_disabled);
            tile.setState(Tile.STATE_UNAVAILABLE);
            tile.setSubtitle(disableText);
            tile.setContentDescription(disableText);
            tile.updateTile();
        } else {
            tile.setState(Tile.STATE_ACTIVE);
            updateCurrentRefreshRate();
            updateTileDescription();
        }
    }

    private int getRefreshRateVal() {
        return Integer.parseInt(refreshRateValues[currentRefreshRate]);
    }

    @Override
    public void onClick() {
        super.onClick();
        updateCurrentRefreshRate();
        if (currentRefreshRate == refreshRates.length - 1) {
            currentRefreshRate = 0;
        } else {
            currentRefreshRate++;
        }
        RefreshRateUtils.setRefreshRate(context, getRefreshRateVal());
        RefreshRateUtils.setFPS(getRefreshRateVal());
        updateTileDescription();
    }
}
