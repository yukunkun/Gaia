package com.gaiamount.gaia_main.signin_signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.gaiamount.R;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.DataProgressor;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResetPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ACCOUNT_TYPE_EMAIL = 1;
    private static final int ACCOUNT_TYPE_PHONE = 2;
    public static final String TAG = "ResetPwdActivity";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.find_pwd_account_type)
    Spinner mAccoutType;
    @Bind(R.id.find_pwd_account_et)
    EditText accountText;
    @Bind(R.id.step_1_next)
    Button mSubmitBtn;

    @Bind(R.id.ver_code_et)
    EditText mVertifyCodeText;
    @Bind(R.id.resend_ver_code_btn)
    Button mResendVertifyCode;

    @Bind(R.id.new_password)
    EditText mNewPasswordText;
    @Bind(R.id.sign_seen_pwd)
    CheckBox mPwdVisibility;

    @Bind(R.id.find_pwd_done_btn)
    Button mDone;

    private View step1;//第一个页面布局，获取验证码成功后，进入第二个布局
    private View step2;//第二个页面布局，填写验证码成功后，提交到服务器，并返回结果

    private String prefix = "";//区号，默认为空，必须初始化
    private String mAccount;
    private String mPassword;

    private int accountType = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pwd);
        ButterKnife.bind(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        step2=findViewById(R.id.step_2_lay);
        setUpView();
    }


    private void setUpView() {
        mDone.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        mResendVertifyCode.setOnClickListener(this);

        final String[] strings = getResources().getStringArray(R.array.account_type);
        mAccoutType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                accountText.setHint(strings[position]);
                switch (position) {
                    case 0:
                        mSubmitBtn.setText("获取验证码");
                        accountType = ACCOUNT_TYPE_PHONE;
                        break;
                    case 1:
                        mSubmitBtn.setText("发送到邮箱");
                        accountType = ACCOUNT_TYPE_EMAIL;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                accountText.setHint(strings[0]);
                mSubmitBtn.setText("获取验证码");
                accountType = ACCOUNT_TYPE_PHONE;
            }
        });
        mPwdVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mNewPasswordText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mNewPasswordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_1_next:
                sendCode();
                break;
            case R.id.find_pwd_done_btn:
                resetPwd();
                break;
            case R.id.resend_ver_code_btn:
                sendCode();
                break;

        }
    }


    /**
     * 发送验证码，点击获取验证码时使用
     */
    private void sendCode() {
        if (accountType == 1) {
            mAccount = accountText.getText().toString();
        } else if (accountType == 2) {
            mAccount = prefix + accountText.getText().toString();
        }
        ProgressDialog dialog = ProgressDialog.show(this, "", "正在发送验证码");
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(ResetPwdActivity.class, dialog) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("发送成功");
                if (accountType == 2) {
//                    step1.setVisibility(View.GONE);
                    step2.setVisibility(View.VISIBLE);
                } else {
                    //提示邮件发送成功
                    ActivityUtil.startSendMailSuccessfulActivityForResult(ResetPwdActivity.this, mAccount);
                    finish();
                }
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
//                step1.setVisibility(View.VISIBLE);
//                step2.setVisibility(View.GONE);
            }
        };
        int type = 1;//随便传值，接口必传，实际无用

        AccountApiHelper.sendVerifyCode(accountType, mAccount, mAccount, type, 1, this, jsonHttpResponseHandler);

    }

    /**
     * 提交新的密码，只有手机用户才能使用次方法
     */
    private void resetPwd() {
        mPassword = mNewPasswordText.getText().toString();
        String vertifyCode = mVertifyCodeText.getText().toString().trim();
        String encyptPwd = DataProgressor.encyptPwd(mAccount, mPassword);
        int f = 2;//目前只有手机一种方式

        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(ResetPwdActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                //回到主界面
                GaiaApp.showToast("密码重置成功，请重新登陆");
                finish();
            }
        };
        AccountApiHelper.resetPwd(mAccount, encyptPwd, vertifyCode, f, this, jsonHttpResponseHandler);

    }

}
