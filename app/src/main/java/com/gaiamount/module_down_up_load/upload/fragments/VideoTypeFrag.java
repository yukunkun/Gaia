package com.gaiamount.module_down_up_load.upload.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.apis.file_api.FileApi;
import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;
import com.gaiamount.util.encrypt.SHA256;

/**
 * Created by haiyang-lu on 16-3-16.
 * 选择上传的视频类型：
 * 1：加密
 * 2：不加密
 */
public class VideoTypeFrag extends BaseUploadFrag {
    private CheckedTextView ctvPublic;
    private CheckedTextView ctvPrivate;
    private EditText mPassword;
    private InputMethodManager imm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void setTitle(TextView title) {
        title.setText(getResources().getString(R.string.video_type));
    }

    int type;
    @Override
    protected void restore() {
        UpdateWorksBean uploadInfo = GaiaApp.getAppInstance().getUpdateWorksBean();
        int requirePassword = uploadInfo.getA().getRequirePassword();
        if (requirePassword== FileApi.TYPE_PUBLIC) {
            type = FileApi.TYPE_PUBLIC;
        }else {
            type = FileApi.TYPE_PRIVATE;
            mPassword.setText(uploadInfo.getA().getPassword());
        }
        refresh(type);
    }


    @Override
    protected void setSaveBtn(TextView saveBtn) {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            UpdateWorksBean mUpdateWorksBean = GaiaApp.getAppInstance().getUpdateWorksBean();

            @Override
            public void onClick(View v) {
                if (ctvPublic.isChecked()) {
                    mUpdateWorksBean.getA().setRequirePassword(FileApi.TYPE_PUBLIC);//公开
                } else {
                    mUpdateWorksBean.getA().setRequirePassword(FileApi.TYPE_PRIVATE);//加密
                    if (mPassword.getText().toString().equals("")) {
                        GaiaApp.showToast("密码为空");
                        return;
                    }
                    mUpdateWorksBean.getA().setPassword(SHA256.bin2hex(mPassword.getText().toString().trim()));

                }

                //保存设置
                GaiaApp.getAppInstance().setUpdateWorksBean(mUpdateWorksBean);
                if (onFragmentStateChangeListener != null) {
                    onFragmentStateChangeListener.onFinish(UploadType.VIDEO_TYPE);
                }
            }
        });
    }

    @Override
    protected View getContent() {
        return getInflater().inflate(R.layout.layout_upload_video_type, null);
    }


    @Override
    protected void initView(View view) {
        ctvPublic = (CheckedTextView) view.findViewById(R.id.type_pulic);
        ctvPrivate = (CheckedTextView) view.findViewById(R.id.type_private);
        mPassword = (EditText) view.findViewById(R.id.password);

        //默认选中第一个
        refresh(FileApi.TYPE_PUBLIC);

        ctvPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(FileApi.TYPE_PUBLIC);
            }
        });
        ctvPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(FileApi.TYPE_PRIVATE);
            }
        });


    }

    /**
     * 根据类型改变控件状态
     * @param type 视频类型：公开0 私有1
     */
    private void refresh(int type) {
        if (type == 1) {//加密
            //显示输入法
            mPassword.setVisibility(View.VISIBLE);
            mPassword.requestFocus();
            imm.showSoftInput(mPassword,InputMethodManager.SHOW_IMPLICIT);
            ctvPublic.setChecked(false);
            ctvPrivate.setChecked(true);
            ctvPublic.setCheckMarkDrawable(null);
            ctvPrivate.setCheckMarkDrawable(R.mipmap.checked);


        } else if (type == 0) {//公开
            ctvPublic.setChecked(true);
            ctvPrivate.setChecked(false);
            ctvPublic.setCheckMarkDrawable(R.mipmap.checked);
            ctvPrivate.setCheckMarkDrawable(null);
            mPassword.setVisibility(View.GONE);
            //关闭输入法
            imm.hideSoftInputFromWindow(mPassword.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
}
