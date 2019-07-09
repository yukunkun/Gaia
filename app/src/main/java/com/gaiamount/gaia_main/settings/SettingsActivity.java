package com.gaiamount.gaia_main.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gaiamount.R;
import com.gaiamount.gaia_main.BaseActionBarActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction().replace(R.id.setting_fragment_container, SettingsFragment.newInstance()).commit();
    }
}
