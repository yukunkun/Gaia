package com.gaiamount.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gaiamount.util.ActivityUtil;

/**
 * Created by haiyang-lu on 16-5-30.
 */
public class DownloadNotificationClickedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ActivityUtil.startdownloadActivity(context);

    }
}
