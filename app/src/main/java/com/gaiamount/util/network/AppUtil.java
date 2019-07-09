package com.gaiamount.util.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.gaiamount.R;

import java.util.Locale;

/**
 * Created by haiyang-lu on 16-7-25.
 * app工具类
 */
public class AppUtil {
    public static final int ENGLISH = 0;
    public static final int CHINESE = 1;

    /**
     * 改变应用语言
     *
     * @param type 0 英文 1中文
     */
    public static void changeLanguage(Context context, int type) {
        Locale locale = context.getResources().getConfiguration().locale;
        if (type == ENGLISH) {
            setLanguage(context, Locale.ENGLISH);
        } else if (type == CHINESE) {
            setLanguage(context, Locale.CHINESE);
        }

    }

    public static void setLanguage(Context context, Locale locale) {
        //获取资源对象
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static String getVersionName(Context context) {
        String versionName = context.getString(R.string.default_version_name);
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return context.getString(R.string.current_version) + versionName;
    }
}
