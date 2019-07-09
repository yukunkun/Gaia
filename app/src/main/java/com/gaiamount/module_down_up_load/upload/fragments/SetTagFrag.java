package com.gaiamount.module_down_up_load.upload.fragments;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;

/**
 * Created by haiyang-lu on 16-3-18.
 * 设置标签的页面
 */
public class SetTagFrag extends BaseUploadFrag {

    private EditText mEditText;

    @Override
    protected void setTitle(TextView title) {
        title.setText(getResources().getString(R.string.set_tag));
    }

    @Override
    protected void restore() {
        String keywords = GaiaApp.getAppInstance().getUpdateWorksBean().getA().getKeywords();
        mEditText.setText(keywords);
    }

    @Override
    protected void setSaveBtn(TextView saveBtn) {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = mEditText.getText().toString().trim();
                Log.d("设置的标签",tag);
                String[] split = tag.split(",");
                if(split==null||split.length<2){
                    GaiaApp.showToast("至少2个标签,请用英文逗号隔开");
                    return;
                }else {
                    GaiaApp.getAppInstance().getUpdateWorksBean().getA().setKeywords(tag);
                    onFragmentStateChangeListener.onFinish(UploadType.SET_TAG);
                }
            }
        });
    }

    @Override
    protected View getContent() {
        return getInflater().inflate(R.layout.fragment_set_tag,null);
    }

    @Override
    protected void initView(View content) {
        mEditText = (EditText) content.findViewById(R.id.edit_text);
    }

}
