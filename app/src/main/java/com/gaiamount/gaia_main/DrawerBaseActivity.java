package com.gaiamount.gaia_main;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.home.HomeActivity;
import com.gaiamount.gaia_main.settings.SettingsActivity;
import com.gaiamount.gaia_main.signin_signup.LoginActivity;
import com.gaiamount.module_creator.sub_module_mygroup.MyGroupActivity;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.BroadcastUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;

import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-7-27.
 * 带有侧滑菜单的activity的基类
 * 注意：布局文件是各自的子类所提供，所以要求drawerLayout,NavigationView，toolbar等需要父类操作的控件的Id必须一致
 * toolbar:toolbar
 * navigationView:nav_view
 * drawerLayout:drawer_layout
 */
public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    /**
     * 用户头像
     */
    protected ImageView mAvatar;
    /**
     * 用户昵称
     */
    protected TextView mNickName;

    private BroadcastReceiver userInfoChangedBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUserData();
        }
    };

    private BroadcastReceiver logoutBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //重置用户状态
            mAvatar.setImageResource(R.mipmap.ic_avatar_default);
            mNickName.setText(R.string.user_logout);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播
        BroadcastUtils.registerUserInfoChangedBroadcast(this, userInfoChangedBroadcast);
        BroadcastUtils.registerLogout(this, logoutBroadcast);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解注册接受者
        unregisterBCR();
    }

    private void unregisterBCR() {
        //解注册广播
        if (logoutBroadcast != null) {
            unregisterReceiver(logoutBroadcast);
            logoutBroadcast = null;
        }
        if (userInfoChangedBroadcast != null) {
            unregisterReceiver(userInfoChangedBroadcast);
            userInfoChangedBroadcast = null;
        }
    }

    protected void initHeaderView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ImageView logout = (ImageView) headerView.findViewById(R.id.iv_logout);
        mAvatar = (ImageView) headerView.findViewById(R.id.iv_user_avatar);
        mNickName = (TextView) headerView.findViewById(R.id.nav_name);
        mNickName.setOnClickListener(this);
        logout.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            startActivity(new Intent(DrawerBaseActivity.this, HomeActivity.class));
        } else if (id == R.id.nav_search) {
            ActivityUtil.startGlobalSearchActivity(this, null, 0);
        } else if (id == R.id.nav_upload) {
            ActivityUtil.startUploadManagerActivity(this);
        } else if (id == R.id.nav_cache) {
            ActivityUtil.startdownloadActivity(this);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(DrawerBaseActivity.this, SettingsActivity.class));
        }
        //lhy 2016.8.15关闭账本
//        else if (id == R.id.nav_account_book) {
//            startActivity(new Intent(DrawerBaseActivity.this, AccountBookActivity.class));
//        }
        else if (id == R.id.nav_my_group) {
            startActivity(new Intent(DrawerBaseActivity.this, MyGroupActivity.class));
        } /*else if (id == R.id.nav_collection) {
            ActivityUtil.startCollectionActivity(getApplicationContext(), GaiaApp.getUserInfo().id);
        }*/ else if (id == R.id.nav_history) {
            ActivityUtil.startHistoryActivity(DrawerBaseActivity.this);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_logout:
                logout();
                break;
            case R.id.iv_user_avatar:
                if (GaiaApp.loginStatus) {//已登录
                    ActivityUtil.startPersonalActivity(this, GaiaApp.getUserInfo().id, mAvatar, mNickName);
                } else {//未登陆
                    Intent intent = new Intent(DrawerBaseActivity.this, LoginActivity.class);
                    startActivityForResult(intent, LoginActivity.REQUEST_CODE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 由具体子类调用
     * 更新数据：用户头像+用户昵称
     */
    protected void updateUserData() {
        String nickName = GaiaApp.getUserInfo().nickName;
        String avatar = GaiaApp.getUserInfo().avatar;
        if (nickName != null) {
            this.mNickName.setText(nickName);
        } else {
            this.mNickName.setText(R.string.user_logout);
        }
        //kun
//        if (avatar != null && !TextUtils.isEmpty(avatar) && !"null".equals(avatar)) {
//                if(!avatar.endsWith(".jpg")&&!avatar.endsWith(".png")&&!avatar.endsWith(".jpeg")){
//                    Glide.with(this).load(Configs.COVER_PREFIX+avatar+".jpg").placeholder(R.mipmap.ic_avatar_default).into(mAvatar);
//                }else {
//                    //加载正确的avatar
//                    Glide.with(this).load(Configs.COVER_PREFIX + avatar).placeholder(R.mipmap.ic_avatar_default).into(mAvatar);
//                }
//        }else {
            //加载空的avatar
            Glide.with(this).load(Configs.COVER_PREFIX+avatar).placeholder(R.mipmap.ic_avatar_default).into(mAvatar);
//        }
            //ImageUtils.getInstance(this).getAvatar(mAvatar, avatar);
    }

    public void logout() {
        String tokenValue = GaiaApp.getTokenValue();
        String token = GaiaApp.getToken();

        //将token放入头部
        NetworkUtils.aHttpClient.addHeader("Set-Cookie", token);

        ProgressDialog dialog = ProgressDialog.show(this, null, getString(R.string.logging_out));

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(HomeActivity.class, dialog) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast(getString(R.string.logout_success));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                GaiaApp.getAppInstance().setUserInfo(null);
                GaiaApp.getAppInstance().clearActivityList();
                //无论成功还是失败，都登出,删除cookie
                BroadcastUtils.sendLogoutBroadcast();
                finish();
            }
        };

        AccountApiHelper.logout(tokenValue, this, handler);
    }

    /**
     * 由具体子类调用
     */
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_nav_toggle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    updateUserData();
//                    sendReceive();
                    drawer.openDrawer(Gravity.LEFT);
                }

            }
        });
    }
}
