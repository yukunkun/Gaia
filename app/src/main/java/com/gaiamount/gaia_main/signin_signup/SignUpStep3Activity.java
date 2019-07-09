package com.gaiamount.gaia_main.signin_signup;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.gaiamount.R;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.BaseActionBarActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.DataProgressor;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.UIUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignUpStep3Activity extends BaseActionBarActivity {

    private String vertify_code;
    private EditText userName;
    private EditText mPassword;
    private Button done;
    private String phone_number;
    private EditText mJob;
    private CheckBox mPwdVisibility;
    private boolean pwdVisibility;
    private Spinner mChooseJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step3);

        GaiaApp.getAppInstance().getActivityList().add(this);
        phone_number = getIntent().getStringExtra("phone_number");
        vertify_code = getIntent().getStringExtra("vertify_code");


        initView();
        userName.requestFocus();

        setListener();
    }


    private void initView() {
        userName = (EditText) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.password);
        mJob = (EditText) findViewById(R.id.sign_job);
        done = (Button) findViewById(R.id.sign_done);
        mPwdVisibility = (CheckBox) findViewById(R.id.sign_seen_pwd);
        mChooseJob = (Spinner) findViewById(R.id.choose_job);
    }

    private void setListener() {
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(userName.getText().toString()) && !TextUtils.isEmpty(s.toString())) {
                    done.setClickable(true);
                } else {
                    done.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();

                UIUtils.hideSoftInputMethod(SignUpStep3Activity.this,mPassword);
            }
        });

        mPwdVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pwdVisibility) {//当前是可见状态，设置为不可见
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pwdVisibility = false;
                    mPwdVisibility.setChecked(false);
                } else {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwdVisibility = true;
                    mPwdVisibility.setChecked(true);
                }
            }
        });
        final String[] jobList = getResources().getStringArray(R.array.job_list);

        mChooseJob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChooseJob.setPrompt("");
                mJob.setText(jobList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 点击完成注册账号
     */
    private void registerAccount() {
        //重置
        userName.setError(null);
        mPassword.setError(null);
        mJob.setError(null);
        String nickName = userName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String job = mJob.getText().toString().trim();
        //判断昵称和密码是否合法
        if(nickName.length()<6||nickName.length()>30) {
            userName.setError("请确认帐号长度");
            userName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(job)) {
            mJob.setError("请输入您的职业");
            mJob.requestFocus();
        }

        String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,16}";
        Pattern pattern = Pattern.compile(reg);
        boolean matcher = pattern.matcher(mPassword.getText().toString()).find();
        if (!matcher) {
            Log.e("SignUpStep3", "密码格式不正确");
            mPassword.setError("密码格式不正确");
            mPassword.requestFocus();
            return;
        }

        //加密
        String pwd_encrypt = DataProgressor.encyptPwd(phone_number, password);


        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(SignUpStep3Activity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("注册成功");
                GaiaApp.getAppInstance().clearActivityList();
                ActivityUtil.startLoginActivity(SignUpStep3Activity.this);
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                int i = response.optInt("i");
                if (i==30201) {//手机号已存在
                    Snackbar.make(mPassword,"手机号已注册",Snackbar.LENGTH_LONG).setAction("回到注册界面", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityUtil.startRegisterActivity(SignUpStep3Activity.this);
                            GaiaApp.getAppInstance().clearActivityList();
                        }
                    }).show();
                }
            }

            @Override
            public void onFailureResponse(int statusCode, Throwable throwable) {
                super.onFailureResponse(statusCode, throwable);
            }
        };

        AccountApiHelper.register(phone_number,vertify_code,pwd_encrypt,nickName,job,this,jsonHttpResponseHandler);

    }
}
