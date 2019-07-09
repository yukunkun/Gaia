package com.gaiamount.module_creator.sub_module_album;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.apis.file_api.FileApiHelper;
import com.gaiamount.dialogs.OperatePassDialogFrag;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.OnEventLearn;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.UriToPath;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetUpAlbumActivity extends AppCompatActivity {

    public static final String GID = "gid";
    public static final String ALBUM_TYPE = "album_type";
    private static final int CHOOSE_COVER = 12;
    private static final int CROP = 13;
    private AlbumDetail albumDetail;
    /**
     * toolbar控件
     */
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    /**
     * 添加和显示专辑封面的控件
     */
    @Bind(R.id.add_album_cover)
    ImageView mIv_AlbumCover;
    /**
     * 专辑名称
     */
    @Bind(R.id.add_album_name)
    EditText mET_AlbumName;
    /**
     * 专辑是否加密
     */
    @Bind(R.id.add_album_power)
    Switch mSw_AlbumPower;
    /**
     * 专辑如果加密，设置其密码
     */
    @Bind(R.id.album_pwd)
    EditText mET_AlbumPwd;
    /**
     * 显示密码的控件
     */
    @Bind(R.id.album_pwd_txt)
    TextView mTV_AlbumPwdTxt;
    /**
     * 专辑描述
     */
    @Bind(R.id.add_album_desc)
    EditText mET_AlbumDesc;

    /**
     * 上传进度条
     */
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    /**
     * 文字“添加封面"
     */
    @Bind(R.id.cover_txt)
    TextView mTV_CoverHint;

    /**
     * 输入密码的容器
     */
    @Bind(R.id.album_pwd_container)
    RelativeLayout mRL_AlbumPwdContainer;

    /**
     * 小组id
     */
    private long mGid;

    /**
     * 专辑类型（0个人 1小组）
     */
    private int mAlbumType;

    private String reSet="";
    /**
     * 创建专辑所需信息的对象
     */
    private AlbumInfo mAlbumInfo = new AlbumInfo();
    private long mAid;
    private TextView textViewTitle;
    private String format;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_album);
        ButterKnife.bind(this);

        mGid = getIntent().getLongExtra(GID, -1);
        mAlbumType = getIntent().getIntExtra(ALBUM_TYPE, -1);
        reSet=getIntent().getStringExtra("reSet");
        mAid = getIntent().getLongExtra("aid", -1);
        textViewTitle = (TextView) findViewById(R.id.album_title);
        if(reSet==null){
            reSet="";
        }
        if(reSet.equals("reSet")){//表示为修改album
            textViewTitle.setText("设置专辑");

            getDetail(null);//都是没密码的,还没有做判断
            setToolbarReSet();

        }else {//这是新建album
            setToolbar();
        }
            mSw_AlbumPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mSw_AlbumPower.setText("加密");
                        mRL_AlbumPwdContainer.setVisibility(View.VISIBLE);
                        mAlbumInfo.albumPower = 0;
                    } else {
                        mSw_AlbumPower.setText("公开");
                        mRL_AlbumPwdContainer.setVisibility(View.INVISIBLE);
                        mAlbumInfo.albumPower = 1;
                    }
                }
            });
    }

    private void setToolbarReSet() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_finish);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_finish) {
                    mAlbumInfo.albumName = mET_AlbumName.getText().toString();
                    mAlbumInfo.albumDesc = mET_AlbumDesc.getText().toString();
                    if (mAlbumInfo.albumPower == 0) {//如果加密
                        mAlbumInfo.albumPwd = mET_AlbumPwd.getText().toString();
                    } else {
                        mAlbumInfo.albumPwd = "";
                    }

                    //执行修改
                    final MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(SetUpAlbumActivity.class) {
                        @Override
                        public void onGoodResponse(JSONObject response) {
                            super.onGoodResponse(response);
                            final OperatePassDialogFrag instance = OperatePassDialogFrag.newInstance("修改成功");
                            instance.show(getSupportFragmentManager(), null);
                            EventBus.getDefault().post(new OnEventLearn(10));

                            mToolbar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setResult(RESULT_OK);
                                    finish();
                                    instance.dismiss();
                                }
                            },2000);
                        }
                    };

                    AlbumApiHelper.updateAlbum(mAid,mAlbumInfo.albumName, mAlbumInfo.albumCover, mAlbumInfo.albumDesc, mAlbumType, mGid, mAlbumInfo.albumPower, mAlbumInfo.albumPwd, SetUpAlbumActivity.this, handler);
                }
                return true;
            }
        });
    }

    private void getDetail(String pwd) {
            //验证密码是否正确，同时如果正确，返回专辑详情
            MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbumDetailActivity.class) {
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    JSONObject a = response.optJSONObject("o");
                    albumDetail = GsonUtil.getInstannce().getGson().fromJson(a.toString(), AlbumDetail.class);

                    Glide.with(getApplicationContext()).load(Configs.COVER_PREFIX+albumDetail.getBgImg()).into(mIv_AlbumCover);
                    mET_AlbumDesc.setText(albumDetail.getContent());
                    mET_AlbumName.setText(albumDetail.getName());
                    int isPublic = albumDetail.getIsPublic();
                    if(isPublic==1){
                        mSw_AlbumPower.setChecked(false);
                    }
                }

                @Override
                public void onBadResponse(JSONObject response) {
                    super.onBadResponse(response);

                }
            };
            AlbumApiHelper.getAlbumDetail(mAid, pwd, SetUpAlbumActivity.this, handler);

    }

    /**
     * 设置toolbar点击事件
     */
    private void setToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_finish);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_finish) {
                    mAlbumInfo.albumName = mET_AlbumName.getText().toString();
                    mAlbumInfo.albumDesc = mET_AlbumDesc.getText().toString();
                    if (mAlbumInfo.albumPower == 0) {//如果加密
                        mAlbumInfo.albumPwd = mET_AlbumPwd.getText().toString();
                    } else {
                        mAlbumInfo.albumPwd = "";
                    }



                    //执行创建
                    final MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(SetUpAlbumActivity.class) {
                        @Override
                        public void onGoodResponse(JSONObject response) {
                            super.onGoodResponse(response);
                            final OperatePassDialogFrag instance = OperatePassDialogFrag.newInstance("创建成功");
                            instance.show(getSupportFragmentManager(), null);
                            mToolbar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setResult(RESULT_OK);
                                    finish();
                                    instance.dismiss();
                                }
                            },2000);
                        }
                    };
                    AlbumApiHelper.createAlbum(mAlbumInfo.albumName, mAlbumInfo.albumCover, mAlbumInfo.albumDesc, mAlbumType, mGid, mAlbumInfo.albumPower, mAlbumInfo.albumPwd, SetUpAlbumActivity.this, handler);
                }
                return true;
            }
        });
    }

    /**
     * ①点击添加专辑封面
     *
     * @param v
     */
    public void clickToAddAlbumCover(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_COVER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_COVER && resultCode == RESULT_OK) {
            Uri data1 = data.getData();
            //裁剪
            ImageUtils.getInstance(this).cropImageUri(this, data1, 360, 202, CROP);
        } else if (requestCode == CROP && resultCode == RESULT_OK) {
            //显示
            Bitmap bitmap = null;
            if(data.getData()!=null){
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }else if(data.getExtras()!=null){
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
            }

            mIv_AlbumCover.setImageBitmap(bitmap);
            //上传
            mTV_CoverHint.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);

            String imageAbsolutePath = UriToPath.getImageAbsolutePath(this, data.getData());
            if(imageAbsolutePath==null){

                File f = new File("/sdcard/gaiamount/", "cover.jpg");
                if (f.exists()) {
                    f.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
              //  saveBitmap(bitmap);
                imageAbsolutePath="/sdcard/gaiamount/cover.jpg";
                format="jpg";
            }else {
                format = imageAbsolutePath.substring(imageAbsolutePath.lastIndexOf(".") + 1);
            }

            UpCompletionHandler completionHandler = new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        mProgressBar.setVisibility(View.GONE);
                        GaiaApp.showToast("图片上传成功");

                        mAlbumInfo.albumCover = key;

                    } else {
                        GaiaApp.showToast("图片上传失败");
                    }
                }
            };

            UpProgressHandler progressHandler = new UpProgressHandler() {
                @Override
                public void progress(String key, double percent) {
                    mProgressBar.setProgress((int) (percent * 100));
                }
            };

            //调用接口
            long uId = GaiaApp.getUserInfo().id;
            FileApiHelper.uploadAlbumCover(uId, format, this, imageAbsolutePath, completionHandler, progressHandler);

        }
    }
    public void saveBitmap(Bitmap bm) {
        File f = new File("/sdcard/gaiamount/", "cover.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    class AlbumInfo {

        /**
         * 专辑封面的地址
         */
        String albumCover = "";
        /**
         * 专辑名称
         */
        String albumName = "";
        /**
         * 专辑权限
         */
        int albumPower = 1;
        /**
         * 专辑密码
         */
        String albumPwd = "";
        /**
         * 专辑描述
         */
        String albumDesc = "";
    }
}
