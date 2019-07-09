package com.gaiamount.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_workpool.WorkPoolActivity;

/**
 * Created by yukun on 16-7-7.
 */
public class StateBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NetState(context);
    }
    private void NetState(Context context) {
        if (isNetworkAvailable(context)) {
            Intent intent=new Intent(context, WorkPoolActivity.class);
            intent.putExtra("tag","tag");
            context.startActivity(intent);
        }
        else {
            GaiaApp.showToast("当前没有可用网络");
        }
    }
    public boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++) {

                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
