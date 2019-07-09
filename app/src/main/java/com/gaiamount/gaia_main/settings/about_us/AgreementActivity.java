package com.gaiamount.gaia_main.settings.about_us;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gaiamount.R;

/**
 * kun 通知我们gaiament
 */
public class AgreementActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
    }


    //用户使用协议的跳转
    public void UserContent(View view) {
        Intent intent=new Intent(this,UserSendAgreementActivity.class);
        startActivity(intent);
    }
    //用户出售协议的跳转
    public void UserSell(View view) {
        Intent intent=new Intent(this,UseAgreementActivity.class);
        startActivity(intent);
    }
    //结束Activity
    public void UserBack(View view) {
        finish();
        overridePendingTransition(0,R.anim.push_left_out);

    }
}
