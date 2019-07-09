package com.gaiamount.module_user.user_edit;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.apis.file_api.FileApiHelper;
import com.gaiamount.gaia_main.BaseActionBarActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.home.NewDialog;
import com.gaiamount.module_charge.account_book.AccountActivity;
import com.gaiamount.module_user.adapters.UserInfoListAdapter;
import com.gaiamount.module_user.fragment.ChooseJobFrag;
import com.gaiamount.module_user.fragment.EditGenderFrag;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.gaia_main.signin_signup.ResetPwdActivity;
import com.gaiamount.module_user.task.AddressInitTask;
import com.gaiamount.util.BroadcastUtils;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.UIUtils;
import com.gaiamount.util.UserUtils;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.UriToPath;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserInfoActivity extends BaseActionBarActivity {

    private static final int PICK = 1;
    private static final int CROP = 2;
    private ListView mList;
    private UserInfoListAdapter mAdapter;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private String imageAbsolutePath;
    private static FileOutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mList = (ListView) findViewById(R.id.personal_user_info_list);

        //获取用户数据
        UserInfo userInfo = GaiaApp.getUserInfo();

        //准备列表
        String[] list = getResources().getStringArray(R.array.personal_user_info_list);
        mAdapter = new UserInfoListAdapter(this, userInfo, list);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserInfoActivity.this, UserInfoEditActivity.class);
                if (position == 1) {//头像
                    Intent intent1 = new Intent();
                    intent1.setType("image/*");
                    intent1.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intent1, PICK);
                    return;
                } else if (position == 2) { //昵称
                    intent.putExtra("theme", UIUtils.ActionBarTheme);
                    intent.putExtra("kind", UserInfoEditActivity.USERNAME);
                } else if (position == 3) {
                    new EditGenderFrag().show(getSupportFragmentManager(), "edit_gender");
                    return;
                } else if (position == 4) {
                    //弹出选择框
                    new ChooseJobFrag().show(getFragmentManager(), "choose_job");
                    return;
                } else if (position == 5) {//地区
                    new AddressInitTask(UserInfoActivity.this, true).execute("四川省", "成都市", "成华区");
                    return;
                } else if (position == 6) {//个性签名
                    intent.putExtra("theme", UIUtils.ActionBarTheme);
                    intent.putExtra("kind", UserInfoEditActivity.SIGNATURE);
                } else if (position == 7) {//个人简介
                    intent.putExtra("theme", UIUtils.ActionBarTheme);
                    intent.putExtra("kind", UserInfoEditActivity.RESUME);
                }
                else if (position == 8) {//个性域名
                    return;
                }
//                else if (position == 9) {
//                    return;
//                }
                else if (position == 9) {
                    startActivity(new Intent(UserInfoActivity.this, ResetPwdActivity.class));
                    return;
                } else if (position == 10) {
                    startActivity(new Intent(UserInfoActivity.this, AccountActivity.class));
                    return;
                } else if (position == 11) {
                    return;
                } else if (position == 12) {

                } else if (position == 13) {
                    intent.putExtra("theme", UIUtils.ActionBarTheme);
                    intent.putExtra("kind", UserInfoEditActivity.EMAIL);
                } else if (position == 15) {
                    // TODO: 16-5-27 第三方登陆验证关联
                }

                startActivityForResult(intent, UserInfoEditActivity.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                UserInfo userInfo = GaiaApp.getUserInfo();
                mAdapter.setUserInfo(userInfo);
                mAdapter.notifyDataSetChanged();
            }
        };
        filter = new IntentFilter(BroadcastUtils.ACTION_USERINFO_CHANGED);
        registerReceiver(receiver, filter);

        updateUserInfo();
    }

    /**
     * 用于更新界面的方法
     */
    public void updateUserInfo() {
        UserUtils.getUserInfoFromNet(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserInfoEditActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            updateUserInfo();
        } else if (requestCode == PICK && resultCode == RESULT_OK) {
            //裁剪
            ImageUtils.getInstance(this).cropImageUri(this, data.getData(), 240, 240, CROP);
        } else if (requestCode == CROP && resultCode == RESULT_OK) {
            //上传图片到服务器

            if(data.getData()!=null){
                imageAbsolutePath = UriToPath.getImageAbsolutePath(UserInfoActivity.this, data.getData());
            }else if(data.getExtras()!=null){
                Bundle bundle = data.getExtras();
                Bitmap  photo = (Bitmap) bundle.get("data"); //get bitmap
                File destDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gaiamount/head.jpg");
                try {
                    out = new FileOutputStream(destDir);
                    photo.compress(Bitmap.CompressFormat.PNG, 0, out); //0 压缩率 30 是压缩率，表示压缩70%;
                    out.flush();
                    out.close();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                imageAbsolutePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/gaiamount/head.jpg";
                File file=new File(imageAbsolutePath);
                if(!file.exists()){
                    GaiaApp.showToast("获取失败");
                }
            }
            final ProgressDialog dialog = ProgressDialog.show(this, "", "正在提交");
            JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(UserInfoActivity.class) {
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    String inputKey = response.optJSONObject("o").optString("inputKey");
                    String token = response.optJSONObject("o").optString("token");
                    uploadImage(inputKey, token, imageAbsolutePath, dialog);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dialog.dismiss();
                }
            };

            long uid = GaiaApp.getUserInfo().id;
            String format = imageAbsolutePath.substring(imageAbsolutePath.lastIndexOf(".") + 1);//后缀
            //调用接口，准备上传
            FileApiHelper.uploadUserAvatar(uid, format, this, jsonHttpResponseHandler);
        }
    }

    public void uploadImage(final String inputKey, String token, String path, final ProgressDialog dialog) {
        UploadManager uploadManager = new UploadManager();
//        Log.i("----uploadImagePath",path);
        uploadManager.put(path, inputKey, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                LogUtil.d(UserInfoActivity.class,"key:"+key+"info"+info+"response:"+response.toString());
                if (info.isOK()) {
                    GaiaApp.showToast("修改成功");
                    dialog.dismiss();
                    //更新到本地
                    updateUserInfo();
                }
            }
        }, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gaiamount/head.jpg");
        if(f.exists()){
            f.delete();
        }
    }
}
