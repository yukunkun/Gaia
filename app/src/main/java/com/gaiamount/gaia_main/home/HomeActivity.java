package com.gaiamount.gaia_main.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.gaiamount.R;
import com.gaiamount.gaia_main.DrawerBaseActivity;
import com.gaiamount.gaia_main.signin_signup.LoginActivity;
import com.gaiamount.module_down_up_load.upload_manage.UploadManager;
import com.gaiamount.module_im.message_center.MessageActivity;
import com.gaiamount.module_material.activity.util.M3U8Service;


public class HomeActivity extends DrawerBaseActivity {
    /**
     * 2S内按下back键2次退出
     */
    private static final long BACK_PRESSED_TIME = 2000;
    /**
     * 主页fragment
     */
    private HomeFrag mHomeFrag;
    private Toast toast;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //父类方法
        initHeaderView();
        //父类方法
        updateUserData();
        //父类方法
        initToolbar();

        initContentView();

        getPermission();
        //消息提醒
        com.gaiamount.gaia_main.home.bean.Message message=new com.gaiamount.gaia_main.home.bean.Message();
        message.setMessage(getApplicationContext());
    }

    int backPressedTime = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                backPressedTime = 0;
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //如果drawerLayout开着，则关闭
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            boolean isUploading = UploadManager.getInstance(getApplicationContext()).isUploading();
            if (isUploading) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage(R.string.upload_unfinished)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UploadManager.getInstance(getApplicationContext()).cancelUpload();
                                HomeActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //ignore
                            }
                        });
                builder.create().show();
            } else {
                //如果已经关闭，执行默认步骤
                backPressedTime++;
                handler.sendEmptyMessageDelayed(0, BACK_PRESSED_TIME);

                if (backPressedTime == 2) {
                    toast.cancel();
                    M3U8Service.PORT=9080;
                    //退出
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                System.exit(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    super.onBackPressed();
                } else {
                    toast=Toast.makeText(HomeActivity.this, getString(R.string.click_before_exit), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_message) {
            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
        }/*if (id == R.id.action_circle) {
            ActivityUtil.startCircleActivity(this);
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //用户登陆成功，更新界面 头像+用户基本信息
            updateUserData();
        }
    }


    private void initContentView() {
        if (mHomeFrag == null) {
            mHomeFrag = HomeFrag.newInstance();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.main_content_container, mHomeFrag).commit();

    }

    private void getPermission() {

        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //6.0权限访问
                } else { //权限被拒绝
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("需要赋予访问存储的权限，不开启将无法正常工作！且可能被强制退出登录")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(receiver);
//    }
}
