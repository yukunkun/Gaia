package com.gaiamount.gaia_main.settings.about_us;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/*
* kun
* 联系我们页面的逻辑处理
* 2016-7-12
*
* */
public class CallUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_us);
    }

    //拨打电话
    public void CallNum(View view) {
        getPermission();//获取权限
    }

    private void getPermission() {

            if (ContextCompat.checkSelfPermission(CallUsActivity.this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CallUsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

            }else {
                //有权限
                callPhone();//拨打电话
            }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("需要赋予拨打电话的权限,否则无法正常使用")
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
    public void CallUsBack(View view) {
        finish();
        overridePendingTransition(0,R.anim.push_left_out);
    }
    //下载微信二维码到本地
    public void weiXinLoad(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_weixin);
        saveBitmap(bitmap,getString(R.string.weixin));
    }
    //下载微薄二维码到本地
    public void WeiBoLoad(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_weibo);
        saveBitmap(bitmap,getString(R.string.weibo));
    }
    //将二维码图片下载到本地
    public void saveBitmap(Bitmap bm,String name) {
        File f = new File("/sdcard/gaiamount/", name + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            GaiaApp.showToast(getString(R.string.erweima));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callPhone(){
        String strMobile = getString(R.string.tel_gaiament_number);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+strMobile));
        try{
            this.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
