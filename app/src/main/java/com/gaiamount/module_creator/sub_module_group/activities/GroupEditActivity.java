package com.gaiamount.module_creator.sub_module_group.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.apis.file_api.FileApiHelper;
import com.gaiamount.dialogs.NoticeDialogFrag;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.beans.GroupCreateInfo;
import com.gaiamount.module_creator.beans.GroupDetailInfo;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupAdminFrag;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupDescFrag;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupKeywordsFrag;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupNameFrag;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupRequireExamine;
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

public class GroupEditActivity extends AppCompatActivity implements NoticeDialogFrag.NoticeDialogListener {

    public static final int CHOOSE_COVER = 1;
    private static final int CROP = 2;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.group_edit_cover)
    ImageView groupEditCover;
    @Bind(R.id.group_edit_progress)
    ProgressBar groupEditProgress;
    @Bind(R.id.group_edit_title)
    TextView groupEditTitle;
    @Bind(R.id.group_edit_is_examine)
    TextView groupExamine;

    public static final String GROUP_DETAIL_INFO = "group_detail_info";
    /**
     * 小组详细信息
     */
    private GroupDetailInfo mGroupDetailInfo;
    /**
     * 小组创建信息
     */
    private GroupCreateInfo mGroupCreateInfo = new GroupCreateInfo();

    public List<Fragment> mFragmentList;
    private String imageAbsolutePath;
    private String format;
    private FileOutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);
        ButterKnife.bind(this);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mGroupDetailInfo = (GroupDetailInfo) getIntent().getSerializableExtra(GROUP_DETAIL_INFO);
        initFragment();
        //显示封面
        ImageUtils.getInstance(this).getCover(groupEditCover, mGroupDetailInfo.getO().getGroup().getBackground());
    }

    public void setToolbarTitle(String title) {
        groupEditTitle.setText(title);
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        long id = mGroupDetailInfo.getO().getGroup().getId();
        mFragmentList.add(SetGroupNameFrag.newInstance(mGroupCreateInfo));
        mFragmentList.add(SetGroupKeywordsFrag.newInstance(mGroupCreateInfo));
        mFragmentList.add(SetGroupDescFrag.newInstance(mGroupCreateInfo));
        mFragmentList.add(SetGroupAdminFrag.newInstance(id));
        mFragmentList.add(SetGroupRequireExamine.newInstance());

    }

    public void clickToSetName(View v) {
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out).
                add(R.id.edit_group_container, mFragmentList.get(0))
                .addToBackStack(null).commit();
    }

    public void clickToSetKeywords(View v) {
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out).
                add(R.id.edit_group_container, mFragmentList.get(1))
                .addToBackStack(null).commit();
    }

    public void clickToSetDesc(View v) {
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out).
                add(R.id.edit_group_container, mFragmentList.get(2))
                .addToBackStack(null).commit();
    }

    public void clickToSetAdmin(View v) {
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out).
                add(R.id.edit_group_container, mFragmentList.get(3), "set_admin_fragment")
                .addToBackStack(null).commit();
    }

    public void clickToSetAddMode(View v) {
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out).
                add(R.id.edit_group_container, mFragmentList.get(4))
                .addToBackStack(null).commit();
    }


    /**
     * 点击选择小组封面
     *
     * @param v
     */
    public void clickToSetCover(View v) {
        //启动图片库，获取图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_COVER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_COVER && resultCode == RESULT_OK) {
            ImageUtils.getInstance(this).cropImageUri(this, data.getData(), 750, 220, CROP);
        } else if (requestCode == CROP && resultCode == RESULT_OK) {
            try {
                if(data.getData()!=null){
                    //显示
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    groupEditCover.setImageBitmap(bitmap);
                    //上传
                    imageAbsolutePath = UriToPath.getImageAbsolutePath(this, data.getData());

                }else if(data.getExtras()!=null){
                    Bundle bundle = data.getExtras();
                    Bitmap  photo = (Bitmap) bundle.get("data"); //get bitmap
                    groupEditCover.setImageBitmap(photo);

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

                format = imageAbsolutePath.substring(imageAbsolutePath.lastIndexOf(".") + 1);
                JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupEditActivity.class) {
                    @Override
                    public void onGoodResponse(JSONObject response) {
                        super.onGoodResponse(response);
                        String inputKey = response.optJSONObject("o").optString("inputKey");
                        String token = response.optJSONObject("o").optString("token");

                        UploadManager uploadManager = new UploadManager();

                        uploadManager.put(imageAbsolutePath, inputKey, token, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (info.isOK()) {
                                    groupEditProgress.setVisibility(View.GONE);
                                    setGroupBackground(key);
                                    GaiaApp.showToast("图片上传成功");
                                } else {
                                    GaiaApp.showToast("图片上传失败");
                                }
                            }

                        }, new UploadOptions(null, null, false, new UpProgressHandler() {
                            @Override
                            public void progress(String key, double percent) {
                                groupEditProgress.setProgress((int) (percent * 100));
                            }
                        }, null));
                    }
                };
                groupEditProgress.setVisibility(View.VISIBLE);
                FileApiHelper.uploadGroupCover(GaiaApp.getUserInfo().id, format, mGroupDetailInfo.getO().getGroup().getId(), this, jsonHttpResponseHandler);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 更新小组信息
     *
     * @param type 更新的类型
     */
    public void updateGroupInfo(final int type) {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupEditActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("修改成功");
                //关闭fragment
                getSupportFragmentManager().beginTransaction().detach(mFragmentList.get(type)).commit();
                getSupportFragmentManager().popBackStack();
            }
        };
        GroupApiHelper.updateGroupInfo(mGroupDetailInfo, this, jsonHttpResponseHandler);
    }

    /**
     * 设置是否审核，由相应fragment调用
     *
     * @param isExamine
     */
    public void setExamination(int isExamine) {
        mGroupDetailInfo.getO().getGroup().setIsExamine(isExamine);
        if(isExamine==0){
            groupExamine.setText("不审核");
        }else if (isExamine==1){
            groupExamine.setText("审核");
        }
        updateGroupInfo(4);
    }

    /**
     * 设置小组名称
     *
     * @param groupName
     */
    public void setGroupName(String groupName) {
        mGroupDetailInfo.getO().getGroup().setName(groupName);
        updateGroupInfo(0);
    }

    /**
     * 设置小组关键字
     *
     * @param keywords
     */
    public void setGroupKeywords(String keywords) {
        mGroupDetailInfo.getO().getGroup().setKeywords(keywords);
        updateGroupInfo(1);
    }


    /**
     * 设置小组背景
     *
     * @param background
     */
    public void setGroupBackground(String background) {
        mGroupDetailInfo.getO().getGroup().setBackground(background);
        updateGroupInfo(3);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        groupEditTitle.setText("编辑资料");
    }

    @Override
    public void onDialogPositiveClick(final DialogFragment dialogFragment) {
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {

    }
}
