package com.gaiamount.gaia_main.signin_signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.BaseActionBarActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.BroadcastUtils;
import com.gaiamount.util.ImUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.UserUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class LoginActivity extends BaseActionBarActivity implements OnClickListener {
    public static final int REQUEST_CODE = 121;
    public static final String TAG = "LoginActivity";
    /**
     * 1=邮箱登陆2=手机登陆 3=昵称登陆
     */
    private static final int TYPE_EMIAL = 1;
    private static final int TYPE_PHONE = 2;
    private static final int TYPE_NICK = 3;

    /**
     * 帐号控件
     */
    @Bind(R.id.email)
    AutoCompleteTextView mAccount;

    /**
     * 密码控件
     */
    @Bind(R.id.password)
    EditText mPasswordView;
    /**
     * 登陆按钮
     */
    @Bind(R.id.email_sign_in_button)
    Button mSignIn;
    /**
     * 注册按钮
     */
    @Bind(R.id.sign_in_login_btn)
    Button mSignUp;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mToolbar.inflateMenu(R.menu.menu_login);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_forget_pwd) {
                    startActivity(new Intent(LoginActivity.this, ResetPwdActivity.class));
                }
                return true;
            }
        });
        TextView title = (TextView) mToolbar.findViewById(R.id.title);
//        title.setText(R.string.login);

        setListener();
    }


    private void setListener() {
        mSignIn.setOnClickListener(this);
        mSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                attemptLogin();
                break;
            case R.id.sign_in_login_btn:
                startActivity(new Intent(LoginActivity.this, SignUpStep1Activity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 尝试登陆
     */
    private void attemptLogin() {
        //重置
        mAccount.setError(null);
        mPasswordView.setError(null);
        //保存值
        String account = mAccount.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //验证密码合法性
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(account) || account.length() > 30 || account.length() < 6) {
            mAccount.setError(getString(R.string.error_field_required));
            focusView = mAccount;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus(); //设置焦点
        } else {
            login(getAccountType(account), account, password);//登陆
        }
    }

    private int getAccountType(String account) {
        if (account.contains("@")) {
            return TYPE_EMIAL;
        } else if (TextUtils.isDigitsOnly(account)) {
            return TYPE_PHONE;
        } else {
            return TYPE_NICK;
        }
    }


    private boolean isPasswordValid(String password) {
        //            String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,16}";
//            Pattern pattern = Pattern.compile(reg);
//            Matcher matcher = pattern.matcher(password);
//            Log.i(TAG,""+matcher.matches());
//            return matcher.matches();
        return password.length() >= 0 && password.length() < 20;
    }

    int allowRequestTimes = 1;

    /**
     * 开始请求网络登陆
     *
     * @param type
     * @param account  账号
     * @param password 密码
     */
    private void login(final int type, final String account, final String password) {
        ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.logging_in));
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(LoginActivity.class, dialog) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                int b = response.optInt("b");
                if (b == 1) {//登陆成功的处理
                    loginSuccess(headers, response);
                } else {//登陆失败的处理
                    //再次请求一次，使用昵称
                    if (type == TYPE_PHONE && allowRequestTimes > 0) {
                        login(TYPE_NICK, account, password);
                        allowRequestTimes--;
                    }
                    loginFailed(response);
                }
            }
        };
        AccountApiHelper.login(account, password, type, this, jsonHttpResponseHandler);
    }

    /**
     * 登陆成功后，所要做的操作
     *
     * @param headers
     * @param response
     */
    private void loginSuccess(Header[] headers, JSONObject response) {

        //提示用户登陆成功
        GaiaApp.showToast(getResources().getString(R.string.login_success));

        JSONObject jsonObject = response.optJSONObject("o");
        //转换成userInfo并更新到app中
        UserUtils.switchJsonToUserInfoAndSet(jsonObject);
//        //发送广播，通知更新
        BroadcastUtils.sendLoginBroadcast(headers);

        //登陆环信账号
        /**
         * 暂时的聊天的修改
         */
//        ImUtil.getImUtil(this).loginEase();

        setResult(RESULT_OK);
        finish();

    }

    /**
     * 登陆失败，登陆失败后，所要做的操作
     */
    private void loginFailed(JSONObject response) {
        int i = response.optInt("i");
        switch (i) {
            case 30001:
                GaiaApp.showToast("未匹配上账号");
                break;
            case 31301:
                GaiaApp.showToast("密码错误");
                //让密码可见5s
                mPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                mPasswordView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }, 3000);
                break;
            case 32601:
                GaiaApp.showToast("手机账号不存在");
                break;
            case 501:
                GaiaApp.showToast("邮箱账号不存在");
                break;
            default:
                break;
        }
    }

}

