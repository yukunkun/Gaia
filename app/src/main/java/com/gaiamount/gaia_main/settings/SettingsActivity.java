package com.gaiamount.gaia_main.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.BaseActionBarActivity;

public class SettingsActivity extends AppCompatActivity {

    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mIvBack = findViewById(R.id.iv_back);
        getFragmentManager().beginTransaction().replace(R.id.setting_fragment_container, SettingsFragment.newInstance()).commit();
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
