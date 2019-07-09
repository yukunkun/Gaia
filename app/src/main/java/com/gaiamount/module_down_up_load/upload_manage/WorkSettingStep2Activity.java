package com.gaiamount.module_down_up_load.upload_manage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.BaseActionBarActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.UploadEditActivity;
import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class WorkSettingStep2Activity extends BaseActionBarActivity {

    public static final String VIDEO_TYPE = "video_type";
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 0;
    public static final int REQUEST_CODE = 1111;
    public static final String UPDATE_WORKS = "update_works";
    private UpdateWorksBean mUpdateWorksBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra(VIDEO_TYPE, -1);
        mUpdateWorksBean = (UpdateWorksBean) getIntent().getSerializableExtra(UPDATE_WORKS);
        if (type == PUBLIC) {
            setContentView(R.layout.activity_work_setting_step2_public);
        } else {
            setContentView(R.layout.activity_work_setting_step2_private);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UploadEditActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            updateUserInfo();
        }
    }


    /**
     * 更新用户数据和界面
     */
    private void updateUserInfo() {

    }

    /**
     * 点击提交修改
     *
     * @param v
     */
    public void clickCommit(View v) {
        //判断必填数据是否已填
        ProgressDialog dialog = ProgressDialog.show(this, "", "正在提交");
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(WorkSettingStep2Activity.class, dialog) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("修改成功");
                setResult(RESULT_OK);
                finish();
            }
        };
        UpdateWorksBean updateWorksBean = GaiaApp.getAppInstance().getUpdateWorksBean();
        WorksApiHelper.updateWorks(updateWorksBean, this, jsonHttpResponseHandler);
    }

    /**
     * 点击选择分类
     *
     * @param v
     */
    public void clickChooseCategory(View v) {
        Intent intent = new Intent(this, UploadEditActivity.class);
        intent.putExtra("type", UploadType.CHOOSE_CATEGORY);
        startActivityForResult(intent, UploadEditActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.bottomin, R.anim.topout);
    }

    /**
     * 点击设置标签
     *
     * @param v
     */
    public void clickTag(View v) {
        Intent intent = new Intent(this, UploadEditActivity.class);
        intent.putExtra("type", UploadType.SET_TAG);
        startActivityForResult(intent, UploadEditActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.bottomin, R.anim.topout);
    }

    /**
     * 点击编辑下载设置
     *
     * @param v
     */
    public void clickDownloadSet(View v) {
        Intent intent = new Intent(this, UploadEditActivity.class);
        intent.putExtra("type", UploadType.DOWNLOAD_SET);
        startActivityForResult(intent, UploadEditActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.bottomin, R.anim.topout);
    }

//    /**
//     * 点击编辑幕后故事
//     * @param v
//     */
//    public void clickBackStory(View v) {
//        Intent intent = new Intent(this,UploadEditActivity.class);
//        intent.putExtra("type", UploadType.BACK_STORY);
//        startActivityForResult(intent, UploadEditActivity.REQUEST_CODE);
//        overridePendingTransition(R.anim.bottomin, R.anim.topout);
//    }

}
