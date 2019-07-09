package com.gaiamount.module_creator.sub_module_group.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.apis.file_api.FileApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.beans.GroupCreateInfo;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupDescFrag;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupKeywordsFrag;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupNameFrag;
import com.gaiamount.module_down_up_load.upload_manage.WorkSettingStep1Activity;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LHY
 * 新建小组
 */
public class GroupCreateActivity extends AppCompatActivity {
    public static final String FRAGMENT_TAG_SET_GROUP_NAME = "set_group_name";
    public static final String FRAGMENT_TAG_SET_GROUP_KEYWORDS = "set_group_keywords";
    public static final String FRAGMENT_TAG_SET_GROUP_DESC = "set_group_desc";

    private static final int PICK = 1343;
    private static final int CROP = 4321;
    /**
     * 居中的标题
     */
    @Bind(R.id.title)
    TextView mTV_Title;
    /**
     * 顶部toolbar控件
     */
    @Bind(R.id.toolbar)
    Toolbar mAddGroupToolbar;
    /**
     * 添加小组封面的文字提示，当图片选取成功后隐藏
     */
    @Bind(R.id.add_group_cover_text)
    TextView addGroupCoverDesc;
    /**
     * 显示选取的图片
     */
    @Bind(R.id.add_group_cover_img)
    ImageView addGroupCoverImg;
    /**
     * 图片上传中的进度条，成功后则隐藏
     */
    @Bind(R.id.add_group_cover_progress)
    ProgressBar addGroupCoverProgress;

    /**
     * 标志当前显示的fragment在fragmentList中的位置，默认不存在
     */
    public int currentFragment = -1;
    public List<Fragment> mFragmentList;
    /**
     * 小组信息的对象模型
     */
    private GroupCreateInfo mGroupCreateInfo = new GroupCreateInfo();
    private FileOutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);
        //必须设置为supportActionbar，因为fragment会用到actionbar
        //设置标题为空必须在setsupportactionbar之前调用
        mAddGroupToolbar.setTitle("");
        setSupportActionBar(mAddGroupToolbar);
        setToolbarTitle("创建小组");
        mAddGroupToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        prepareFragments();
    }

    private void prepareFragments() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(SetGroupNameFrag.newInstance(mGroupCreateInfo));
        mFragmentList.add(SetGroupKeywordsFrag.newInstance(mGroupCreateInfo));
        mFragmentList.add(SetGroupDescFrag.newInstance(mGroupCreateInfo));

    }

    /**
     * 设置toolbar标题
     */
    public void setToolbarTitle(String title) {
        mTV_Title.setText(title);
    }

    /**
     * 点击设置封面
     *
     * @param v
     */
    public void addGroupCover(View v) {

        getPermission();
//        //拾取图片
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK && resultCode == RESULT_OK) {
            //对选择的图片进行裁剪
            ImageUtils.getInstance(this).cropImageUri(this, data.getData(), 630, 270, CROP);
        }
        if (requestCode == CROP && resultCode == RESULT_OK) {
            //成功
            addGroupCoverDesc.setVisibility(View.INVISIBLE);
            //展示
            ContentResolver cr = getContentResolver();
            Bitmap bitmap = null;
            if(data.getData()!=null){
                try {
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(data.getData()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                Bundle bundle = data.getExtras();
                bitmap=(Bitmap) bundle.get("data"); //get bitmap
            }
            addGroupCoverImg.setImageBitmap(bitmap);
            addGroupCoverImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //上传
            addGroupCoverProgress.setVisibility(View.VISIBLE);
            String imageAbsolutePath="";

            if(data.getData()!=null){
                imageAbsolutePath = UriToPath.getImageAbsolutePath(GroupCreateActivity.this, data.getData());
            }else if(data.getExtras()!=null){
                Bundle bundle = data.getExtras();
                Bitmap  photo = (Bitmap) bundle.get("data"); //get bitmap
                File destDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gaiamount/cover.jpg");
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
                imageAbsolutePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/gaiamount/cover.jpg";
                File file=new File(imageAbsolutePath);
                if(!file.exists()){
                    GaiaApp.showToast("获取失败");
                }
            }

            final String finalImageAbsolutePath = imageAbsolutePath;
            JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(WorkSettingStep1Activity.class) {
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    String inputKey = response.optJSONObject("o").optString("inputKey");
                    String token = response.optJSONObject("o").optString("token");
                    uploadImage(inputKey, token, finalImageAbsolutePath);
                }
            };
            long uid = GaiaApp.getUserInfo().id;
            String format = imageAbsolutePath.substring(imageAbsolutePath.lastIndexOf(".") + 1);
            FileApiHelper.uploadGroupCover(uid, format, 0, this, jsonHttpResponseHandler);
        }
    }

    /**
     * 上传作品封面
     *
     * @param inputKey 名称
     * @param token    七牛凭证
     * @param path     本地文件路径
     */
    public void uploadImage(final String inputKey, String token, String path) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(path, inputKey, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    addGroupCoverProgress.setVisibility(View.GONE);
                    //设置小组背景
                    mGroupCreateInfo.background = key;
                } else {
                    GaiaApp.showToast("图片上传失败");
                }
            }

        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                addGroupCoverProgress.setProgress((int) (percent * 100));
            }
        }, null));
    }

    /**
     * 点击设置小组名称
     *
     * @param v
     */
    public void clickToSetName(View v) {
        currentFragment = 0;
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out).
                add(R.id.add_group_container, mFragmentList.get(0), FRAGMENT_TAG_SET_GROUP_NAME)
                .addToBackStack(null).commit();

    }

    /**
     * 点击设置小组关键字
     *
     * @param v
     */
    public void clickToSetKeywords(View v) {
        currentFragment = 1;
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out).add(R.id.add_group_container, mFragmentList.get(1), FRAGMENT_TAG_SET_GROUP_KEYWORDS)
                .addToBackStack(null).commit();
    }

    /**
     * 点击设置小组封面
     *
     * @param v
     */
    public void clickToSetDesc(View v) {
        currentFragment = 2;
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out).
                add(R.id.add_group_container, mFragmentList.get(2), FRAGMENT_TAG_SET_GROUP_DESC)
                .addToBackStack(null).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            //出栈
            getSupportFragmentManager().popBackStack();
            //判断信息是否完整

            if (currentFragment == 0) {
                SetGroupNameFrag fragment = (SetGroupNameFrag) mFragmentList.get(0);
                mGroupCreateInfo = fragment.getGroupBean();
                if (mGroupCreateInfo.name == null || TextUtils.isEmpty(mGroupCreateInfo.name)) {
                    GaiaApp.showToast("请完善名称");
                    return true;
                }
                getSupportFragmentManager().beginTransaction().detach(fragment).commit();
            } else if (currentFragment == 1) {
                SetGroupKeywordsFrag frag = (SetGroupKeywordsFrag) mFragmentList.get(1);
                mGroupCreateInfo = frag.getGroupBean();
                if (mGroupCreateInfo.keywords == null || TextUtils.isEmpty(mGroupCreateInfo.keywords)) {
                    GaiaApp.showToast("请完善关键字信息");
                    return true;
                }
                getSupportFragmentManager().beginTransaction().detach(frag).commit();
            } else if ((currentFragment == 2)) {
                SetGroupDescFrag frag = (SetGroupDescFrag) mFragmentList.get(2);
                mGroupCreateInfo = frag.getGroupBean();
                if (mGroupCreateInfo.description == null || TextUtils.isEmpty(mGroupCreateInfo.description)) {
                    GaiaApp.showToast("请填写描述");
                    return true;
                }
                getSupportFragmentManager().beginTransaction().detach(frag).commit();
            } else {

                if (mGroupCreateInfo.background == null || TextUtils.isEmpty(mGroupCreateInfo.background)) {
                    GaiaApp.showToast("请先上传专辑封面");
                    return true;
                }

                //请求服务器，保存信息
                JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupCreateActivity.class) {
                    @Override
                    public void onGoodResponse(JSONObject response) {
                        super.onGoodResponse(response);
                        GaiaApp.showToast("创建成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onBadResponse(JSONObject response) {
                        super.onBadResponse(response);
                        if (response.optInt("i") == 31604) {
                            GaiaApp.showToast("你所能创建的小组个数已大上限");
                        } else if (response.optInt("i") == 31603) {
                            GaiaApp.showToast("小组名称已存在");
                        } else if (response.optInt("i") == 32003) {
                            GaiaApp.showToast("域名已存在");
                        }
                    }
                };
                GroupApiHelper.createGroup(mGroupCreateInfo.name, mGroupCreateInfo.keywords, mGroupCreateInfo.isExamine,
                        mGroupCreateInfo.description, mGroupCreateInfo.background, GroupCreateActivity.this, jsonHttpResponseHandler);
            }

        }
        return true;

    }

    //6.0的权限请求
    private void getPermission() {

        if (ContextCompat.checkSelfPermission(GroupCreateActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(GroupCreateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            //拾取图片
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //6.0权限访问
                    //拾取图片
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setToolbarTitle("创建小组");
    }
}
