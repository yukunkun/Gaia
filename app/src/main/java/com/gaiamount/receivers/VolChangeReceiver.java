package com.gaiamount.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VolChangeReceiver extends BroadcastReceiver {
    public VolChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){

        }
    }
}
