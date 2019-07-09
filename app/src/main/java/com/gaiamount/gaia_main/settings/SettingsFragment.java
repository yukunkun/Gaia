package com.gaiamount.gaia_main.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.DataCleanManager;
import com.gaiamount.util.network.AppUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-7-25.
 */
public class SettingsFragment extends PreferenceFragment {

    private SharedPreferences mPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener mListener;
    private Preference preference;
    private  boolean tag=true;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(getString(R.string.key_language))) {
                    //切换语言
                    Settings settings = Settings.newInstance(getActivity());
                    if (settings.getLanguage().equals(getResources().getStringArray(R.array.language_value)[0])) {//中文
                        AppUtil.changeLanguage(getActivity(), AppUtil.CHINESE);
                    } else if (settings.getLanguage().equals(getResources().getStringArray(R.array.language_value)[1])) {//英文
                        AppUtil.changeLanguage(getActivity(), AppUtil.ENGLISH);
                    }
                    //重启activity
                    getActivity().startActivity(new Intent().setClass(getActivity(), SettingsActivity.class));
                    getActivity().finish();
                }
            }
        };
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mPrefs.registerOnSharedPreferenceChangeListener(mListener);

        getVersion();
        //设置版本号
        preference = getPreferenceScreen().findPreference(getString(R.string.key_version_update));
    }

    private void getVersion() {
        JsonHttpResponseHandler handler=new MJsonHttpResponseHandler(SettingsFragment.class){

            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONObject jsonObject = response.optJSONObject("o");
                String latest = jsonObject.optString("Latest");

                try {
                    PackageInfo pi=getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    int versionCode = pi.versionCode;

                    if(versionCode<Integer.valueOf(latest)){
                        preference.setSummary(R.string.new_version);
                        tag=false;
                    }else {
                        preference.setSummary(Settings.newInstance(getActivity()).getVersionName());
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        AccountApiHelper.getVersion(handler);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mListener != null && mPrefs != null) {
            mPrefs.unregisterOnSharedPreferenceChangeListener(mListener);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        if (getString(R.string.key_clean_cache).equals(key)) {
            DataCleanManager.cleanApplicationData(getActivity(), getActivity().getFilesDir().getAbsolutePath());
            GaiaApp.showToast(getString(R.string.clean_cache_finished));
        }
        if(key.equals(getString(R.string.key_version_update))){
            //更新页面的跳转
            if(!tag) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(Configs.WanDouJiaLoad);
                intent.setData(content_url);
                getActivity().startActivity(intent);
            }
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


}
