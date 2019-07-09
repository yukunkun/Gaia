package com.gaiamount.module_down_up_load.upload.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gaiamount.R;

/**
 * Created by haiyang-lu on 16-3-16.
 * 上传相关的fragment的基类
 */
public abstract class BaseUploadFrag extends PreferenceFragment {
    private LayoutInflater inflater;
    protected Context context;
    protected OnFragmentStateChangeListener onFragmentStateChangeListener;

    public LayoutInflater getInflater() {
        return inflater;
    }

    /**
     * 获取activity实例
     * @param context host实例
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_base_upload, null);

        Button back = (Button) rootView.findViewById(R.id.back);
        TextView titleView = (TextView) rootView.findViewById(R.id.header_title);
        Button save = (Button) rootView.findViewById(R.id.save);
        //设置返回按钮和保存按钮的具体行为，由子类实现
        setBackBtn(back);
        setTitle(titleView);
        setSaveBtn(save);

        //获取具体内容
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.content);
        View content = getContent();
        //初始化其中的控件
        initView(content);

        //添加到viewgroup中
        viewGroup.addView(content);

        //返回整个视图
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        restore();
    }


    protected abstract void setTitle(TextView title);

    protected void setBackBtn(TextView backBtn) {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFragmentStateChangeListener!=null) {
                    onFragmentStateChangeListener.onClose();
                }
            }
        });
    }
    protected abstract void restore();
    protected abstract void setSaveBtn(TextView saveBtn);

    protected abstract View getContent();

    protected abstract void initView(View content);



    public void setOnFragmentStateChangeListener(OnFragmentStateChangeListener onFragmentStateChangeListener) {
        this.onFragmentStateChangeListener = onFragmentStateChangeListener;
    }

    public interface OnFragmentStateChangeListener{
        void onClose();
        void onFinish(int type);
    }

}
