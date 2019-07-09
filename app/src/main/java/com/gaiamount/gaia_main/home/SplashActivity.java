package com.gaiamount.gaia_main.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.BroadcastUtils;
import com.gaiamount.util.ImUtil;
import com.gaiamount.util.SPUtils;
import com.gaiamount.util.UserUtils;
import com.hyphenate.chat.EMClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * created by LHY
 * 应用启动页，如果用户之前登陆过，直接进入主页
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        GaiaApp.getAppInstance().getActivityList().add(this);

        doSomePrepare();

        //尝试自动登录
        attemptAutoLogin();

    }

    /**
     * 设置过程动画
     */
    private void setAnimation() {
        final ImageView logo = (ImageView) findViewById(R.id.splash_logo);
        final LinearLayout container = (LinearLayout) findViewById(R.id.splash_container);

        final Animation alphaAnimation = new AlphaAnimation(0.5f, 1f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        TranslateAnimation translateAnimation =
                new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0);
        translateAnimation.setDuration(1500);
        translateAnimation.setFillAfter(true);

        logo.startAnimation(translateAnimation);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                logo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.setVisibility(View.VISIBLE);
                container.startAnimation(alphaAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 尝试自动登陆
     */
    public void attemptAutoLogin() {
        //获取用户Cookie
        String cookieStr = (String) SPUtils.getInstance(this).load(SPUtils.SPKEY_COOKIE, "");
        GaiaApp.setToken(cookieStr);
        //获取用户id
        long id = (long) SPUtils.getInstance(this).load(SPUtils.SPKEY_UID, 0L);
        if (id == 0 || cookieStr == null || "".equals(cookieStr)) {
            //sp中没有用户信息，表示没有登陆信息
            GaiaApp.getAppInstance().loginStatus = false;
            //设置动画
            setAnimation();
        } else {
            final ProgressDialog dialog = ProgressDialog.show(this, null,getString(R.string.logging_in));
            //赋值
            GaiaApp.getAppInstance().uId = id;

            JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //保存用户信息
                    JSONObject jsonObject = response.optJSONObject("o");
                    //转换成userInfo并更新到app中
                    UserUtils.switchJsonToUserInfoAndSet(jsonObject);

                    BroadcastUtils.sendLoginBroadcast(headers);
                    dialog.dismiss();
                    finish();
                }
            };
            UserUtils.autoLogin(SplashActivity.this, handler);

        }


    }

    /**
     * 做些准备工作
     */
    private void doSomePrepare() {
        //注册一个监听连接状态的listener
//        EMClient.getInstance().addConnectionListener(ImUtil.getImUtil(this).getEaseConnectionListener());
        //加载默认设置
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);
    }


    /**
     * 去登陆
     *
     * @param v
     */
    public void login(View v) {
        ActivityUtil.startLoginActivity(this);
    }

    /**
     * 去注册
     *
     * @param v
     */
    public void register(View v) {
        ActivityUtil.startRegisterActivity(this);
    }

}
