package com.gaiamount.gaia_main.settings.about_us;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiamount.R;

/*
* kun
* 关于我们页面
*
* */
public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout mLinearLayoutBack,mLinearLayoutIntroduce,mLinearLayoutUser,mLinearLayoutQue,mLinearLayoutCall;
    TextView mTextViewVerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
        setListener();
        getVersion();
    }
    //初始化view
    private void initView() {
        mLinearLayoutBack= (LinearLayout) findViewById(R.id.about_us_back);
        mLinearLayoutIntroduce=(LinearLayout) findViewById(R.id.introduce);
        mLinearLayoutUser=(LinearLayout) findViewById(R.id.user_content);
        mLinearLayoutQue=(LinearLayout) findViewById(R.id.question_back);
        mLinearLayoutCall=(LinearLayout) findViewById(R.id.call_us);
        mTextViewVerson= (TextView) findViewById(R.id.text_version);
    }
    //添加点击事件
    private void setListener() {
        mLinearLayoutBack.setOnClickListener(this);
        mLinearLayoutIntroduce.setOnClickListener(this);
        mLinearLayoutUser.setOnClickListener(this);
        mLinearLayoutQue.setOnClickListener(this);
        mLinearLayoutCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.about_us_back:
                finish();
               break;
            case R.id.introduce:
                Intent intent=new Intent(this,IntroduceActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,0);
                break;
            case R.id.user_content:
                Intent intent1=new Intent(this,AgreementActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.push_left_in,0);
                break;
            case R.id.question_back:
                Intent intent2=new Intent(this,QuestionBackActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.push_left_in,0);
                break;
            case R.id.call_us:
                Intent intent3=new Intent(this,CallUsActivity.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.push_left_in,0);
                break;
        }
    }
    //获取版本号并设置在textview里
    private void getVersion() {
        String appVersionName = getAppVersionName(getApplicationContext());
        mTextViewVerson.setText("V "+appVersionName);
//        mTextViewVerson.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    //获取版本号的方法
    private  String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("com.gaiamount", 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "1.0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

}
