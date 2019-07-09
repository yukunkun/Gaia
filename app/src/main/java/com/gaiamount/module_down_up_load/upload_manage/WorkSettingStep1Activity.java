package com.gaiamount.module_down_up_load.upload_manage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.apis.file_api.FileApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.UploadEditActivity;
import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.UriToPath;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.FileNotFoundException;

public class WorkSettingStep1Activity extends AppCompatActivity {

    public static final String WORK_ID = "work_id";
    public static final String CONTENT_TYPE = "content_type";
    private static final int VIDEO_REQUEST_CODE = 101;
    private int mWorkId;
    private int mContentType;
    private ImageView mVideoThumbNail;
    private TextView mHint;

    private boolean isVideoPublic = true;

    private ProgressBar mUploadProgress;
    private TextView mIsPublic;
    private TextView mSpecial;
    private TextView mGroup;
    private TextView mDownload;
    private TextView mWorkName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_setting_step1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.work_setting_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_accent_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();

        parseIntent();
    }
    private UpdateWorksBean mUpdateWorksBean;
    /**
     * 获取传递的作品信息
     */
    private void parseIntent() {
        mWorkId = getIntent().getIntExtra(WORK_ID, -1);
        mContentType = getIntent().getIntExtra(CONTENT_TYPE, -1);
        //获取作品信息
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(WorkSettingStep1Activity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                mUpdateWorksBean = GsonUtil.getInstannce().getGson().fromJson(response.toString(),UpdateWorksBean.class);
                GaiaApp.getAppInstance().setUpdateWorksBean(mUpdateWorksBean);
                setUpView();

            }
        };
        FileApiHelper.getUpdateVideoInfo(mWorkId,mContentType,this,jsonHttpResponseHandler);
    }



    private void initView() {
        mVideoThumbNail = (ImageView) findViewById(R.id.work_setting_video_thumbnail);
        mIsPublic = (TextView) findViewById(R.id.work_setting_isPublic);
        mSpecial = (TextView) findViewById(R.id.work_setting_spec);
        mGroup = (TextView) findViewById(R.id.work_setting_group);
        mDownload = (TextView) findViewById(R.id.work_setting_download);
        mWorkName = (TextView) findViewById(R.id.work_setting_name);
        mHint = (TextView) findViewById(R.id.work_setting_hint);
        mUploadProgress = (ProgressBar) findViewById(R.id.work_setting_upload_progress);

    }

    private void setUpView() {
        String screenshot = mUpdateWorksBean.getA().getScreenshot();
        String cover = mUpdateWorksBean.getA().getCover();
        String name = mUpdateWorksBean.getA().getName();
        int requirePassword = mUpdateWorksBean.getA().getRequirePassword();
        int allowDownload = mUpdateWorksBean.getA().getAllowDownload();
        int allowCharge = mUpdateWorksBean.getA().getAllowCharge();

        //cover
        ImageUtils.getInstance(this).showImage(mVideoThumbNail,cover,screenshot);
        //如何cover存在，则隐藏缩略图文字
        if (cover!=null) {
            mHint.setVisibility(View.INVISIBLE);
        }

        //作品名称
        mWorkName.setText(name);
        //性质
        if (requirePassword==1) {
            mIsPublic.setText(mIsPublic.getText()+"公开作品");
        }else {
            mIsPublic.setText(mIsPublic.getText()+"私有作品");
        }
        //专辑
        mSpecial.setText(mSpecial.getText()+"未添加到任何专辑");
        //小组
        mGroup.setText(mGroup.getText()+"-----");

        //下载
        CharSequence text = mDownload.getText();
        if (allowDownload==1) {
            if (allowCharge==1) {
                mDownload.setText(text +"可下载"+"-"+"付费下载");
            }else {
                mDownload.setText(text + "可下载"+"-"+"免费下载");
            }
        }else {
            mDownload.setText(text +"不可下载");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_delete_work) {
            deleteWork();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择图片后
        if (requestCode==VIDEO_REQUEST_CODE&&resultCode==RESULT_OK) {
            ContentResolver cr = this.getContentResolver();
            //展示图片
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(data.getData()));
                mVideoThumbNail.setImageBitmap(bitmap);
                mVideoThumbNail.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //关闭文字提示
            mHint.setVisibility(View.INVISIBLE);
            //上传图片
            mUploadProgress.setVisibility(View.VISIBLE);
            final String imageAbsolutePath = UriToPath.getImageAbsolutePath(WorkSettingStep1Activity.this, data.getData());
            JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(WorkSettingStep1Activity.class){
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    String inputKey = response.optJSONObject("o").optString("inputKey");
                    String token = response.optJSONObject("o").optString("token");
                    uploadImage(inputKey,token, imageAbsolutePath);
                }

            };
            long uid = GaiaApp.getUserInfo().id;
            long wid = mUpdateWorksBean.getA().getId();
            String format = imageAbsolutePath.substring(imageAbsolutePath.lastIndexOf(".")+1);
            FileApiHelper.uploadWorkCover(uid,format,wid,this,jsonHttpResponseHandler);


        }else if (requestCode== UploadEditActivity.REQUEST_CODE&&resultCode==RESULT_OK) {


        }else if (requestCode==WorkSettingStep2Activity.REQUEST_CODE&&resultCode==RESULT_OK) {
            //如果保存成功，则也关闭这个页面
            finish();
        }

    }

    /**
     * 上传作品封面
     * @param inputKey 名称
     * @param token 七牛凭证
     * @param path 本地文件路径
     */
    public  void uploadImage(final String inputKey, String token, String path) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(path, inputKey, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    mUploadProgress.setVisibility(View.GONE);
                    GaiaApp.getAppInstance().getUpdateWorksBean().getA().setCover(key);
                }else {
                    GaiaApp.showToast("图片上传失败");
                }
            }

        },new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                mUploadProgress.setProgress((int) (percent*100));
            }
        },null));
    }

    /**
     * 删除作品
     */
    private void deleteWork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("警告")
                .setMessage("确定删除该作品么")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog progressDialog = ProgressDialog.show(WorkSettingStep1Activity.this, "", "正在删除");
                        JsonHttpResponseHandler handler = new MJsonHttpResponseHandler(WorkSettingStep1Activity.class, progressDialog) {
                            @Override
                            public void onGoodResponse(JSONObject response) {
                                super.onGoodResponse(response);
                                GaiaApp.showToast("删除成功");
                            }
                        };
                        //执行请求
                        WorksApiHelper.deleteWork(mWorkId, mContentType, WorkSettingStep1Activity.this, handler);
                    }
                })
                .setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 修改图片缩略图
     *
     * @param v
     */
    public void clickThumbNail(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,VIDEO_REQUEST_CODE);
    }

    /**
     * 更改视频类型
     *
     * @param v
     */
    public void clickVideoType(View v) {
        Intent intent = new Intent(this,UploadEditActivity.class);
        intent.putExtra("type", UploadType.VIDEO_TYPE);
        startActivityForResult(intent, UploadEditActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.bottomin, R.anim.topout);
    }

    /**
     * 更改作品信息
     *
     * @param v
     */
    public void clickWorkInfo(View v) {
        Intent intent = new Intent(this,UploadEditActivity.class);
        intent.putExtra("type",UploadType.WORK_INFO);
        startActivityForResult(intent, UploadEditActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.bottomin, R.anim.topout);
    }

    /**
     * 点击进入下一步
     * @param v
     */
    public void clickNextStep(View v) {
        //判断当前视频的类型
        Intent intent = new Intent(this,WorkSettingStep2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WorkSettingStep2Activity.UPDATE_WORKS,mUpdateWorksBean);
        intent.putExtras(bundle);
        if (isVideoPublic) {
            intent.putExtra(WorkSettingStep2Activity.VIDEO_TYPE,WorkSettingStep2Activity.PUBLIC);
        }else {
            intent.putExtra(WorkSettingStep2Activity.VIDEO_TYPE,WorkSettingStep2Activity.PRIVATE);
        }
        startActivityForResult(intent,WorkSettingStep2Activity.REQUEST_CODE);
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    /**
     * 点击了解协议详情
     * @param v
     */
    public void clickProtocolDetail(View v) {
        // TODO: 16-6-1
    }


}
