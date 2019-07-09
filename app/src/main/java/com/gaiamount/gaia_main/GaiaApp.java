package com.gaiamount.gaia_main;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.gaia_main.settings.Settings;
import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.module_im.message_center.MyEmMessageListener;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.receivers.DownloadCompleteReceiver;
import com.gaiamount.receivers.DownloadNotificationClickedReceiver;
import com.gaiamount.receivers.LoginBroadcastReceiver;
import com.gaiamount.receivers.LogoutBroadcastReceiver;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.SPUtils;
import com.gaiamount.util.network.AppUtil;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

import static com.gaiamount.util.BroadcastUtils.registerDownloadComplete;
import static com.gaiamount.util.BroadcastUtils.registerDownloadNotificationClicked;
import static com.gaiamount.util.BroadcastUtils.registerLogin;
import static com.gaiamount.util.BroadcastUtils.registerLogout;

/**
 * Gaiamount app
 */
public class GaiaApp extends Application {
    private static final String TAG = "GaiaApp";
    /**
     * application实例
     */
    private static GaiaApp mApplication;
    /**
     * 用户id
     */
    public long uId = 0;
    /**
     * 用户登陆状态
     */
    public static boolean loginStatus = false;
    private List<Activity> activityList = new ArrayList<>();
    /**
     * cookie相关
     */
    public static List<Cookie> store = new ArrayList<>();
    private static String token;
    private static String imToken;

    private static long secretId;
    private static long secTime;
    private static long secServiceTime;

    public static long getSecServiceTime() {
        return secServiceTime;
    }

    public static void setSecServiceTime(long secServiceTime) {
        GaiaApp.secServiceTime = secServiceTime;
    }

    public long getSecretId() {
        return secretId;
    }

    public void setSecretId(long secretId) {
        this.secretId = secretId;
    }

    public long getSecTime() {
        return secTime;
    }

    public void setSecTime(long secTime) {
        this.secTime = secTime;
    }

    private BroadcastReceiver loginReceiver = new LoginBroadcastReceiver();
    private BroadcastReceiver logoutReceiver = new LogoutBroadcastReceiver();
    private BroadcastReceiver mDownloadCompleteReceiver = new DownloadCompleteReceiver();
    private BroadcastReceiver mDownloadNotificationClickedReceiver = new DownloadNotificationClickedReceiver();
    private static UserInfo userInfo;
    private UpdateWorksBean mUpdateWorksBean = new UpdateWorksBean();

    public UpdateWorksBean getUpdateWorksBean() {
        return mUpdateWorksBean;
    }

    public void setUpdateWorksBean(UpdateWorksBean updateWorksBean) {
        mUpdateWorksBean = updateWorksBean;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        registerBroadcast();

        //环信IM SDK初始化
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //。。。
        //初始化
//        EMClient.getInstance().init(mApplication, options);
//        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //注册消息监听器
//        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
        //Log开关
        LogUtil.isDebug = true;
        //bugly的初始化
        CrashReport.initCrashReport(getApplicationContext(), "e46b656ba2", true);
        loadSettings();
    }

    /**
     * 加载本地设置
     */
    private void loadSettings() {
        Settings settings = Settings.newInstance(this);
        if (settings.getLanguage().equals(getResources().getStringArray(R.array.language)[0]))
            AppUtil.changeLanguage(this, AppUtil.CHINESE);
        else if (settings.getLanguage().equals(getResources().getStringArray(R.array.language)[1]))
            AppUtil.changeLanguage(this, AppUtil.ENGLISH);
        settings.setVersionName(AppUtil.getVersionName(this));
    }

    private void registerBroadcast() {
        //注册广播
        registerLogin(this, loginReceiver);
        registerLogout(this, logoutReceiver);
        registerDownloadComplete(this, mDownloadCompleteReceiver);
        registerDownloadNotificationClicked(this, mDownloadNotificationClickedReceiver);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        /**
         * 关闭应用时解注册
         */
        if (loginReceiver != null) {
            unregisterReceiver(loginReceiver);
            loginReceiver = null;
        }
        if (logoutReceiver != null) {
            unregisterReceiver(logoutReceiver);
            logoutReceiver = null;
        }

        //解注册
//        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }

    /**
     * 吐司
     *
     * @param toast 字符串内容
     */
    public static void showToast(String toast) {
        Toast.makeText(getAppInstance(), toast, Toast.LENGTH_SHORT).show();
    }

    public static GaiaApp getAppInstance() {
        return mApplication;
    }

    /**
     * 设置用户信息，并保存到本地
     *
     * @param userInfo 用户信息
     */
    public void setUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            userInfo = new UserInfo();//创建一个空的用户信息对象
        }
        GaiaApp.userInfo = userInfo;
        //保存id到本地
        SPUtils.getInstance(this).save(SPUtils.SPKEY_UID, userInfo.id);
    }

    public static UserInfo getUserInfo() {
        if (userInfo == null) {
            return new UserInfo();
        } else {
            return userInfo;
        }
    }

    public static String getToken() {
        if (token != null) {
            return token;
        } else {
            return "";
        }
    }

    public static void setToken(String string) {
        token = string;
    }

    public static String getTokenValue() {
        String token = getToken();
        Log.d("GaiaApp,token:", token);
        if ("".equals(token)) {
            return "";
        }
        String s = token.split("=")[1];
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }

    public static void deleteToken() {
        //remove store中的值
        if (store != null && store.size() != 0) {
            for (int i = 0; i < store.size(); i++) {
                String tmp = store.get(i).getValue();
                if (tmp.contains("t=")) {
                    store.remove(i);//移除该值
                }
            }
        }
        //重置token的值
        token = "";
    }


    public List<Activity> getActivityList() {
        return activityList;
    }

    public void clearActivityList() {
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
    }

    /*IM消息监听*/
    private EMMessageListener mEMMessageListener = new MyEmMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            super.onMessageReceived(list);
            //导入消息到数据库
//            EMClient.getInstance().chatManager().importMessages(list);

        }
    };

    /*-----kun--------*/
    /*获取ImToken和设置ImToken*/
    public static String getImToken() {
        if (imToken != null) {
            return imToken;
        } else {
            return "";
        }
    }

    //设置ImToken值
    public static void setImToken(String string) {
        imToken = string;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 重启应用
     */
    public void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
