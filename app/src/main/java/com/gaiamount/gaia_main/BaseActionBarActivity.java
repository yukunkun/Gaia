package com.gaiamount.gaia_main;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.gaiamount.R;

/**
 * Created by haiyang-lu on 16-4-29.
 * 带有actionbar的基类
 */
public class BaseActionBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar supportActionBar = getSupportActionBar();
//        initActionBar(supportActionBar);
    }

    protected void initActionBar(ActionBar supportActionBar) {

        if(supportActionBar!=null) {
            //设置actionbar无阴影
            supportActionBar.setElevation(0);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            setIndicatorColor(supportActionBar,R.color.title_color);
        }
    }

    protected void setIndicatorColor(ActionBar supportActionBar,int resId) {
        Drawable upArrow = ContextCompat.getDrawable(this, android.support.design.R.drawable.abc_ab_share_pack_mtrl_alpha);
        upArrow.setColorFilter(getResources().getColor(resId), PorterDuff.Mode.SRC_ATOP);
        supportActionBar.setHomeAsUpIndicator(upArrow);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
