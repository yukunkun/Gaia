package com.gaiamount.gaia_main.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gaiamount.R;

/**
 * Created by haiyang-lu on 16-7-12.
 * 获取应用配置信息的类
 */
public class Settings {
    private static Settings sSettings;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private Settings(Context context) {
        mContext = context.getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static Settings newInstance(Context context) {
        if (sSettings == null) {
            sSettings = new Settings(context);
        }
        return sSettings;
    }

    public boolean getWifiDownloadSettings() {
        String key = mContext.getString(R.string.pref_key_wifi_download);
        return mSharedPreferences.getBoolean(key, true);
    }


    public String getDefinition() {
        String key = mContext.getString(R.string.pref_key_default_definition);
        return mSharedPreferences.getString(key, mContext.getResources().getStringArray(R.array.definition)[0]);
    }

    public String getLanguage() {
        String key = mContext.getString(R.string.key_language);
        return mSharedPreferences.getString(key, mContext.getString(R.string.default_language_value));
    }

    public boolean isShowRealName() {
        String key = mContext.getString(R.string.key_real_name);
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean isShowGender() {
        String key = mContext.getString(R.string.key_gender);
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean isShowPhone() {
        String key = mContext.getString(R.string.key_phone);
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean isShowEmail() {
        String key = mContext.getString(R.string.key_email);
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean isShowQQ() {
        String key = mContext.getString(R.string.key_qq);
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean isShowWeChat() {
        String key = mContext.getString(R.string.key_wechat);
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean isShowWeibo() {
        String key = mContext.getString(R.string.key_weibo);
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean isShowFaceBook() {
        String key = mContext.getString(R.string.key_facebook);
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean isShowTwitter() {
        String key = mContext.getString(R.string.key_twitter);
        return mSharedPreferences.getBoolean(key, false);
    }

    public String getVersionName() {
        String key = mContext.getString(R.string.key_version_update);
        return mSharedPreferences.getString(key,"");
    }

    public void setVersionName(String versionName) {
        String key = mContext.getString(R.string.key_version_update);
        mSharedPreferences.edit().putString(key,versionName).apply();
    }
}
