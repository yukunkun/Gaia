package com.gaiamount.module_down_up_load.upload.fragments;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;

/**
 * Created by haiyang-lu on 16-6-1.
 */
public class BackStoryFrag extends BaseUploadFrag {

    private EditText mEditText;
    private String backStory;
    @Override
    protected void setTitle(TextView title) {
        title.setText(getResources().getString(R.string.back_story));
    }

    @Override
    protected void restore() {
        GaiaApp.getAppInstance().getUpdateWorksBean().getA().setBackStory("");
    }

    @Override
    protected void setSaveBtn(TextView saveBtn) {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backStory=mEditText.getText().toString().trim();
                if(backStory!=null&&!backStory.isEmpty()&&backStory.length()!=0){
                    GaiaApp.getAppInstance().getUpdateWorksBean().getA().setBackStory(backStory);
                }else {
                    GaiaApp.showToast("幕后故事为空");
                }
                onFragmentStateChangeListener.onFinish(UploadType.BACK_STORY);
            }
        });
    }

    @Override
    protected View getContent() {
        return getInflater().inflate(R.layout.fragment_back_story,null);
    }

    @Override
    protected void initView(View content) {
        mEditText = (EditText) content.findViewById(R.id.edit_text);
    }
}
