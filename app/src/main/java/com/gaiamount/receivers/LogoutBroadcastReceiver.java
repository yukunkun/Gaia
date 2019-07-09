package com.gaiamount.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.SPUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by haiyang-lu on 16-3-9.
 * 接受用户下线的通知
 */

public class LogoutBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 销毁用户信息
        GaiaApp.getAppInstance().setUserInfo(null);

        //移除token
        GaiaApp.deleteToken();

        //重置status
        GaiaApp.loginStatus = false;

        //关闭之前所有的activity
        GaiaApp.getAppInstance().clearActivityList();
        

        //清楚缓存
        SPUtils.getInstance(context).clear();

        //提示用户登出成功
        GaiaApp.showToast("登出成功");

        //进入登陆界面
        ActivityUtil.startSplashActivity(context);

        Log.i("TAG","用户下线了");

        //环信账号登出
        //此方法为异步方法
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                LogUtil.d(LogoutBroadcastReceiver.class,"im账号登出成功");
            }

            @Override
            public void onProgress(int progress, String status) {
                LogUtil.d(LogoutBroadcastReceiver.class,"im账号登出中");
            }

            @Override
            public void onError(int code, String message) {
                LogUtil.d(LogoutBroadcastReceiver.class,"im账号登出错误");
            }
        });
    }
}
