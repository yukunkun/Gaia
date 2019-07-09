package com.gaiamount.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.SPUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * Created by haiyang-lu on 16-3-15.
 *
 */
public class LoginBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> headerlists = intent.getStringArrayListExtra("headerlist");
        //获取cookie并保存
        Cookie cookie;
        for (int i = 0; i < headerlists.size(); i++) {
            Log.i("header内容", headerlists.get(i));
            //处理字符串
            String str = headerlists.get(i);
            String[] split = str.split(";");
            cookie = new BasicClientCookie("Set-Cookie", split[0]);
            //保存在内存中
            GaiaApp.store.add(cookie);
            //设置token
            if(split[0].contains("t=")) {
                GaiaApp.setToken(split[0]);
            }
        }

        GaiaApp.loginStatus = true;

        //保存cookie到本地
        SPUtils.getInstance(context).save(SPUtils.SPKEY_COOKIE, GaiaApp.getToken());

        //关闭splash
//        GaiaApp.getAppInstance().getActivityList().clear();

        //进入主页
        ActivityUtil.startHomeActivity(context);

    }
}
