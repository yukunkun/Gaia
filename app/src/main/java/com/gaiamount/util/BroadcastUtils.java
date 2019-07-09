package com.gaiamount.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.gaiamount.gaia_main.GaiaApp;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by haiyang-lu on 16-4-7.
 * 所有自定义广播的action
 */
public class BroadcastUtils {
    public static final String ACTION_USERINFO_CHANGED = "com.gaiamount.userinfo_changed";
    public static final String ACTION_UPLOAD_FINISH = "com.gaiamount.uploadService.upload.finish";
    public static final String ACTION_USER_LOGOUT = "com.gaiamount.logout";
    public static final String COM_GAIAMOUNT_LOGIN = "com.gaiamount.login";

    /**
     * 发送一个登出的广播，所有在注册了该广播接受者的地方都会自动收到通知，然后进入界面的重置
     */
    public static void sendLogoutBroadcast() {
        //发送登出广播
        Intent bIntent = new Intent();
        bIntent.setAction(ACTION_USER_LOGOUT);
        GaiaApp.getAppInstance().sendBroadcast(bIntent);

    }

    /**
     * 注册广播接收器
     *
     * @param context  注册所需要的上下文对象
     * @param receiver 被注册的对象
     */
    public static void registerUserInfoChangedBroadcast(Context context, BroadcastReceiver receiver) {

        IntentFilter filter = new IntentFilter(ACTION_USERINFO_CHANGED);
        context.registerReceiver(receiver, filter);
    }

    /**
     * 发送用户信息已经更新的广播
     */
    public static void sendUserInfoChangedBroadcast() {
        //发送登出广播
        Intent bIntent = new Intent();
        bIntent.setAction(ACTION_USERINFO_CHANGED);
        GaiaApp.getAppInstance().sendBroadcast(bIntent);
    }

    /**
     * 发送一个用户登陆的广播，通知应用程序做出相应的修改
     */
    public static void sendLoginBroadcast(Header[] headers) {
        ArrayList<String> headerList = new ArrayList<>();
        for(int i = 0;i<headers.length;i ++) {
            if("Set-Cookie".equals(headers[i].getName())) {
                headerList.add(headers[i].getValue());
            }
        }
        //发送登入广播
        Intent intent = new Intent();
        intent.setAction(COM_GAIAMOUNT_LOGIN);
        intent.putStringArrayListExtra("headerlist", headerList);
        GaiaApp.getAppInstance().sendBroadcast(intent);
    }

    /**
     * 发送一个上传作品完成的广播
     */
    public static void registerUploadFinishBroadcastReceiver(Context context, BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter(ACTION_UPLOAD_FINISH);
        context.registerReceiver(receiver,intentFilter);
    }


    public static void registerLogout(Context context, BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter(ACTION_USER_LOGOUT);
        context.registerReceiver(receiver,intentFilter);
    }

    public static void registerLogin(Context context, BroadcastReceiver loginReceiver) {
        IntentFilter filter = new IntentFilter(COM_GAIAMOUNT_LOGIN);
        context.registerReceiver(loginReceiver, filter);
    }

    public static void registerDownloadComplete(Context context,BroadcastReceiver downloadCompleteReceiver) {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadCompleteReceiver,intentFilter);
    }

    public static void registerDownloadNotificationClicked(Context context,BroadcastReceiver downloadCompleteReceiver) {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        context.registerReceiver(downloadCompleteReceiver,intentFilter);
    }

}
