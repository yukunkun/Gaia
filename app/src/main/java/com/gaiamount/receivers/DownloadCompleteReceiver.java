package com.gaiamount.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gaiamount.gaia_main.GaiaApp;

/**
 * Created by haiyang-lu on 16-5-30.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        GaiaApp.showToast("下载完成");

    }
}
