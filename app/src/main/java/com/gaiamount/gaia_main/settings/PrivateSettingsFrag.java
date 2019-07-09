package com.gaiamount.gaia_main.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.gaiamount.R;

/**
 * Created by haiyang-lu on 16-7-25.
 */
public class PrivateSettingsFrag extends PreferenceFragment {
    public static PrivateSettingsFrag newInstance() {

        Bundle args = new Bundle();

        PrivateSettingsFrag fragment = new PrivateSettingsFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.private_settings);
    }
}
