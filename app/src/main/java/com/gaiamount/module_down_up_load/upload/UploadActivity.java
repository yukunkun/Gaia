package com.gaiamount.module_down_up_load.upload;

import android.Manifest;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.file_api.FileApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;
import com.gaiamount.gaia_main.settings.about_us.UserSendAgreementActivity;
import com.gaiamount.util.encrypt.SHA256;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = 221;
    private static final int GET_VIDEO = 1234;
    private static final int GET_COVER = 442;
    private static final int CROP = 23432;
    private final String TAG = this.getClass().getSimpleName();
    /**
     * 标题
     */
    @Bind(R.id.title)
    TextView mTitle;
    /**
     * 标题栏
     */
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    /**
     * 视频缩略图
     */
    @Bind(R.id.video_thumbnail)
    ImageView mVideoThumbnail;

    @Bind(R.id.work_name)
    TextView mWorkName;
    @Bind(R.id.isPublic)
    TextView mIsPublic;
    @Bind(R.id.spec)
    TextView mSpec;
    @Bind(R.id.group)
    TextView mGroup;
    @Bind(R.id.download)
    TextView mDownload;

    @Bind(R.id.item_upload_video_type)
    RelativeLayout mItemUploadVideoType;
    @Bind(R.id.item_upload_work_info)
    RelativeLayout mItemUploadWorkInfo;
    @Bind(R.id.item_upload_download_set)
    RelativeLayout mItemUploadDownloadSet;

    @Bind(R.id.item_upload_choose_category)
    RelativeLayout mItemUploadChooseCategory;
    @Bind(R.id.item_upload_set_tag)
    RelativeLayout mItemUploadSetTag;


    @Bind(R.id.item_upload_back_story)
    RelativeLayout mItemUploadBackStory;
    @Bind(R.id.upload_btn)
    Button mUploadBtn;
    private VideoInfo mVideoInfo;

    @Bind(R.id.goto_detail)
    TextView gotoDetail;
    private RelativeLayout mItemUploadChange;
    private Cursor cursor;
    private Uri fileUri;
    private String pathFromURI;
    private String videoDefaultName;
    private String postFix;
    private String subpostFix;
    private long fid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        mTitle.setText(getString(R.string.activity_title_upload));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mItemUploadChange = (RelativeLayout) findViewById(R.id.item_upload_changes);
        setListener();

        //初始化部分参数的默认值
        updateUserInfo(UploadType.VIDEO_TYPE);

    }

    private void setListener() {
        mVideoThumbnail.setOnClickListener(this);
        mItemUploadVideoType.setOnClickListener(this);
        mItemUploadWorkInfo.setOnClickListener(this);
        mItemUploadChooseCategory.setOnClickListener(this);
        mItemUploadSetTag.setOnClickListener(this);
        mItemUploadDownloadSet.setOnClickListener(this);
        mUploadBtn.setOnClickListener(this);
        mItemUploadBackStory.setOnClickListener(this);
        gotoDetail.setOnClickListener(this);
        /* kun */
        mItemUploadChange.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, UploadEditActivity.class);
        switch (v.getId()) {
            case R.id.video_thumbnail:
                //选择视频
                try{
                    getPermission();
                }catch(Exception e){
                    GaiaApp.showToast("没有获得授权,请授权后再上传");
                }

//                Intent intent2 = new Intent();
//                intent2.setAction(Intent.ACTION_PICK);
//                intent2.setType("video/*");
//                startActivityForResult(intent2, GET_VIDEO);
                return;
            case R.id.item_upload_video_type:
                intent.putExtra("type", UploadType.VIDEO_TYPE);
                break;
            case R.id.item_upload_work_info:
                intent.putExtra("type", UploadType.WORK_INFO);
                break;
            case R.id.item_upload_choose_category:
                intent.putExtra("type", UploadType.CHOOSE_CATEGORY);
                break;
            case R.id.item_upload_set_tag:
                intent.putExtra("type", UploadType.SET_TAG);
                break;
            case R.id.item_upload_download_set:
                intent.putExtra("type", UploadType.DOWNLOAD_SET);
                break;
            /**
             * 幕后故事
             */
            case R.id.item_upload_back_story:
                intent.putExtra("type", UploadType.BACK_STORY);
                break;
             /*  kun 转码*/
            case R.id.item_upload_changes:
                intent.putExtra("type", UploadType.CHANGE_RATIO);
                break;

            case R.id.upload_btn:
                startUpload();//上传
                return;
            case R.id.goto_detail:
                startActivity(new Intent(UploadActivity.this, UserSendAgreementActivity.class));
                return;

            default:
                break;

        }
        startActivityForResult(intent, UploadEditActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.bottomin, R.anim.topout);
    }

    /**
     * 准备上传参数
     *
     * @return
     */
    private JSONObject prepareParams() {
        UpdateWorksBean.ABean a = GaiaApp.getAppInstance().getUpdateWorksBean().getA();

        JSONObject jobj = new JSONObject();
        try {
            //必选参数
//            jobj.put("uid", GaiaApp.getUserInfo().id);
            jobj.put("name", a.getName());
            jobj.put("type", a.getType());
//            jobj.put("format", mVideoInfo.postFix);
            jobj.put("client", Configs.CLIENT_APP);
//            jobj.put("size", mVideoInfo.videoSize);
            jobj.put("keywords", a.getKeywords());
            int allowDownload = a.getAllowDownload();
            int allowCharge = a.getAllowCharge();
            jobj.put("allowDownload", allowDownload);
            jobj.put("allowCharge", a.getAllowCharge());
//            if (allowDownload == 1) {//设置允许下载的参数
//                jobj.put("have4k", a.getHave4k());
//                jobj.put("have2k", a.getHave2k());
//                jobj.put("have1080", a.getHave1080());
//                jobj.put("have720", a.getHave720());
//                jobj.put("allowCharge", allowCharge);
//
//                jobj.put("priceOriginal", a.getPriceOriginal());
//                jobj.put("price4K", a.getPrice4K());
//                jobj.put("price2K", a.getPrice2K());
//                jobj.put("price1080", a.getPrice1080());
//                jobj.put("price720", a.getPrice720());
//            }
            jobj.put("requirePassword", a.getRequirePassword());
            if(a.getRequirePassword()==1){
                jobj.put("password", SHA256.bin2hex(a.getPassword())); //加密上传
            }
            jobj.put("photographer", a.getPhotographer());  //
            jobj.put("cutter", a.getCutter());  //
            jobj.put("colorist", a.getColorist()); //
            jobj.put("director", a.getDirector()); //
            jobj.put("macheine", a.getMachine()); //
            jobj.put("lens", a.getLens());   //
            jobj.put("address", a.getAddress());  //
            jobj.put("description", a.getBackStory());  //
            jobj.put("yuv",a.getYuv());
            jobj.put("bit",a.getBit());
            jobj.put("crf",a.getCrf());
            jobj.put("cv",a.getAvc());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jobj;
    }

    /**
     * 开始上传
     */
    private void startUpload() {

        if (!flag_download_set || !flag_tag || !flag_video_content || !flag_choose_category || !flag_video_type) {
            GaiaApp.showToast("必选信息不能为空(视频类型,作品信息,下载设置,选择分类设置标签)");
            return;
        }

        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(UploadActivity.class){


            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);

                JSONObject jsonObject = response.optJSONObject("o");
                String fileName = jsonObject.optString("fileName");
                String token = jsonObject.optString("token");
                fid = jsonObject.optLong("v");

                GaiaApp.getAppInstance().getUpdateWorksBean().getA().setFid(fid);

                Intent intent = new Intent();
                intent.putExtra("video_path", mVideoInfo.filePath);
                intent.putExtra("input_key", fileName);
                intent.putExtra("token", token);
                setResult(RESULT_OK, intent);

                //关闭此页面
                finish();
            }
        };

        if(postFix!=null&&postFix.length()!=0){
            subpostFix = postFix.substring(1, postFix.length());
        }else {
            subpostFix="mp4";
        }

        FileApiHelper.getUpdate(Long.valueOf(videoSize),subpostFix,handler);


//        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(UploadActivity.class) {
//            @Override
//            public void onGoodResponse(JSONObject response) {
//                super.onGoodResponse(response);
//                JSONObject o = response.optJSONObject("o");
//                String inputKey = o.optString("inputKey") + "." + mVideoInfo.postFix;
//                String token = o.optString("token");
//
//                Intent intent = new Intent();
//                intent.putExtra("video_path", mVideoInfo.filePath);
//                intent.putExtra("input_key", inputKey);
//                intent.putExtra("token", token);
//                setResult(RESULT_OK, intent);
//
//                //关闭此页面
//                finish();
//            }
//        };
//        FileApiHelper.uploadVideo(jobj, this, jsonHttpResponseHandler);
//
    }
    int videoSize = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UploadEditActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            updateUserInfo(data.getIntExtra("type", -1));
        }

        //视频
        if (requestCode == GET_VIDEO && resultCode == RESULT_OK) {
            flag_video_content = true;

            fileUri = data.getData();
            if(fileUri==null){
                GaiaApp.showToast("获取视屏资源失败");
                return;//为空,必须退出
            }

            //平板就返回true
//            boolean isTable=(getApplicationContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
//            if(isTable){
//                pathFromURI = fileUri.getPath();
//            }else {
//                pathFromURI = getRealPathFromURI(fileUri);
//            }
            //获取到资源的地址 两种获取地址的方法,作出判断
            if(fileUri.getPath().startsWith("/storage")){
                pathFromURI=fileUri.getPath();
            }else {
                pathFromURI=getRealPathFromURI(fileUri);
            }

            //获取到资源的地址
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
//                pathFromURI = getRealPathFromURI(fileUri);
//            }else{
//                pathFromURI = fileUri.getPath();
//            }

            videoDefaultName = pathFromURI.substring(pathFromURI.lastIndexOf("/")+1);
            GaiaApp.getAppInstance().getUpdateWorksBean().getA().setName(videoDefaultName);
            postFix = pathFromURI.substring(pathFromURI.lastIndexOf("."));

            try {
                InputStream is  = getContentResolver().openInputStream(fileUri);
                videoSize = is.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mVideoInfo = new VideoInfo(videoDefaultName,postFix, pathFromURI, videoSize);
            //更新界面
            Bitmap bitmap = ImageUtils.getVideoThumbnail(pathFromURI);
            mVideoThumbnail.setImageBitmap(bitmap);

        }
    }

    private boolean flag_video_content;
    private boolean flag_video_type = true;
    private boolean flag_choose_category;
    private boolean flag_download_set;
    private boolean flag_tag;

    public void updateUserInfo(int type) {
        UpdateWorksBean updateWorksBean = GaiaApp.getAppInstance().getUpdateWorksBean();//不为空，所以不做非空判断
        //根据类型做相应的保存操作
        switch (type) {
            case UploadType.VIDEO_TYPE:
                //更新flag
                flag_video_type = true;
                int requirePassword = updateWorksBean.getA().getRequirePassword();
                mIsPublic.setText(getString(R.string.video_type) + ":" +
                        (requirePassword == 0 ? getString(R.string.public_video) : getString(R.string.private_video)));
                break;
            case UploadType.WORK_INFO:
                mWorkName.setText(updateWorksBean.getA().getName());
                break;
            case UploadType.CHOOSE_CATEGORY:
                flag_choose_category = true;
                break;
            case UploadType.SET_TAG:
                flag_tag = true;
                break;
            case UploadType.DOWNLOAD_SET:
                flag_download_set = true;
                int allowDownload = updateWorksBean.getA().getAllowDownload();
                mDownload.setText(getString(R.string.download) + ":" +
                        (allowDownload == 1 ? getString(R.string.allow_download) : getString(R.string.disable_download)));
                break;
            case UploadType.ADD_TO_SPECIAL:
                break;
            case UploadType.ADD_TO_GROUP:
                break;
            case UploadType.ADD_WATERMARK:
                break;
            /*幕后故事*/
            case UploadType.BACK_STORY:

                break;
            /*kun*/
            case UploadType.CHANGE_RATIO:  //转码页面
                break;
            default:
                Log.e(TAG, "传入的tag有误");
                break;

        }
    }

    class VideoInfo {
        String videoDefaultName;
        String postFix;
        String filePath;
        long videoSize;

        public VideoInfo(String videoDefaultName,String postFix, String filePath, long videoSize) {
            this.videoDefaultName = videoDefaultName;
            this.postFix = postFix;
            this.filePath = filePath;
            this.videoSize = videoSize;
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor!=null){
            cursor.close();
        }
    }
    //6.0的权限请求
    private void getPermission() {

        if (ContextCompat.checkSelfPermission(UploadActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_PICK);
            intent2.setType("video/*");
            startActivityForResult(intent2, GET_VIDEO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //6.0权限访问
                    Intent intent2 = new Intent();
                    intent2.setAction(Intent.ACTION_PICK);
                    intent2.setType("video/*");
                    startActivityForResult(intent2, GET_VIDEO);

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
}
