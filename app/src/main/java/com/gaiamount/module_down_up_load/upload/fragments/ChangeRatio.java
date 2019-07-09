package com.gaiamount.module_down_up_load.upload.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;

/**
 * Created by yukun on 16-9-12.
 */
public class ChangeRatio extends BaseUploadFrag {


    private RelativeLayout relativeFree;
    private RelativeLayout relativePro_1;
//    private RelativeLayout relativePro_2;
//    private RelativeLayout relativePro_3;
    private RelativeLayout relativeBns_1;
//    private RelativeLayout relativeBns_2;
//    private RelativeLayout relativeBns_3;
//    private TextView textViewFree;
//    private TextView textViewPro_1;
//    private TextView textViewPro_2;
//    private TextView textViewPro_3;
//    private TextView textViewBns_1;
//    private TextView textViewBns_2;
//    private TextView textViewBns_3;
//    private ImageView imageViewFree;
//    private ImageView imageViewPro_1;
//    private ImageView imageViewPro_2;
//    private ImageView imageViewPro_3;
//    private ImageView imageViewBns_1;
//    private ImageView imageViewBns_2;
//    private ImageView imageViewBns_3;

    private boolean tag_1=true;
    private boolean tag_2=true;
    private boolean tag_3=true;
    private boolean tag_4=true;
    private boolean tag_5=true;
    private boolean tag_6=true;
    private boolean tag_7=true;

    private int leave=0; //0 非会员  1 一级会员  2 二级会员
    private int choose=1; //0 表示未选中,1-7分别表示选中的位置
    private RadioGroup radioGroupAVC;
    private RadioGroup radioGroupYUV;
    private RadioGroup radioGroupCRF;
    private int avc=264;
    private int yuv=420;
    private int bit=8;
    private int crf=23;

    @Override
    protected void setTitle(TextView title) {
        title.setText("转码设置");
    }

    @Override
    protected void restore() {
        GaiaApp.getAppInstance().getUpdateWorksBean().getA().setAvc(avc);
        GaiaApp.getAppInstance().getUpdateWorksBean().getA().setYuv(yuv);
        GaiaApp.getAppInstance().getUpdateWorksBean().getA().setCrf(crf);
        GaiaApp.getAppInstance().getUpdateWorksBean().getA().setBit(bit);    }

    @Override
    protected void setSaveBtn(TextView saveBtn) {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GaiaApp.getAppInstance().getUpdateWorksBean().getA().setAvc(avc);
                GaiaApp.getAppInstance().getUpdateWorksBean().getA().setYuv(yuv);
                GaiaApp.getAppInstance().getUpdateWorksBean().getA().setCrf(crf);
                GaiaApp.getAppInstance().getUpdateWorksBean().getA().setBit(bit);


                onFragmentStateChangeListener.onFinish(UploadType.CHANGE_RATIO);
            }
        });
    }

    @Override
    protected View getContent() {
        return getInflater().inflate(R.layout.fragment_change_ratio,null);
    }

    @Override
    protected void initView(View content) {
        radioGroupAVC = (RadioGroup) content.findViewById(R.id.avc);
        radioGroupYUV = (RadioGroup) content.findViewById(R.id.yuv);
        radioGroupCRF = (RadioGroup) content.findViewById(R.id.crf);


        relativeFree = (RelativeLayout) content.findViewById(R.id.real_free);
        relativePro_1 = (RelativeLayout) content.findViewById(R.id.real_pro_adv_1);
//        relativePro_2 = (RelativeLayout) content.findViewById(R.id.real_pro_adv_2);
//        relativePro_3 = (RelativeLayout) content.findViewById(R.id.real_pro_adv_3);
        relativeBns_1 = (RelativeLayout) content.findViewById(R.id.real_bns_1);
//        relativeBns_2 = (RelativeLayout) content.findViewById(R.id.real_bns_2);
//        relativeBns_3 = (RelativeLayout) content.findViewById(R.id.real_bns_3);
//
//        textViewFree = (TextView) content.findViewById(R.id.text_free);
//        textViewPro_1 = (TextView) content.findViewById(R.id.text_pro_adv_1);
//        textViewPro_2 = (TextView) content.findViewById(R.id.text_pro_adv_2);
//        textViewPro_3 = (TextView) content.findViewById(R.id.text_pro_adv_3);
//        textViewBns_1 = (TextView) content.findViewById(R.id.text_bns_1);
//        textViewBns_2 = (TextView) content.findViewById(R.id.text_bns_2);
//        textViewBns_3 = (TextView) content.findViewById(R.id.text_bns_3);
//
//        imageViewFree = (ImageView) content.findViewById(R.id.image_free);
//        imageViewPro_1 = (ImageView) content.findViewById(R.id.image_pro_adv_1);
//        imageViewPro_2 = (ImageView) content.findViewById(R.id.image_pro_adv_2);
//        imageViewPro_3 = (ImageView) content.findViewById(R.id.image_pro_adv_3);
//        imageViewBns_1 = (ImageView) content.findViewById(R.id.image_bns_1);
//        imageViewBns_2 = (ImageView) content.findViewById(R.id.image_bns_2);
//        imageViewBns_3 = (ImageView) content.findViewById(R.id.image_bns_3);

        //默认选中一个
//        imageViewFree.setVisibility(View.VISIBLE);
        int isVip=GaiaApp.getAppInstance().getUserInfo().isVip;
        if(isVip==0){
            leave=0;//非会员
        }else {
            int vipLeave=GaiaApp.getAppInstance().getUserInfo().vipLevel;
            if(vipLeave==2||vipLeave==3){
                leave=1;//一级会员
            }else if(vipLeave==4){
                leave=2;//二级会员
            }
        }
        ((RadioButton)radioGroupYUV.getChildAt(0)).setChecked(true);
        ((RadioButton)radioGroupAVC.getChildAt(0)).setChecked(true);
        ((RadioButton)radioGroupCRF.getChildAt(1)).setChecked(true);
        setListener();
    }

    private void setListener() {
//        Log.i("----leax",leave+"");
        if(leave==0){
            radioGroupYUV.setClickable(false);
            radioGroupCRF.setClickable(false);
            radioGroupAVC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        if(((RadioButton)group.getChildAt(i)).isChecked()){
                            if(i==0){
                                avc = 264;
                            }else if(i==1){
                                avc = 265;
                            }
                        }
                    }
                }
            });
        }else if(leave==1||leave==2){

            radioGroupAVC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        if(((RadioButton)group.getChildAt(i)).isChecked()){
                            if(i==0){
                                avc = 264;
                            }else if(i==1){
                                avc = 265;
                            }
                        }
                    }
                }
            });


            radioGroupYUV.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        if(((RadioButton)group.getChildAt(i)).isChecked()){
                            if(i==0){
                                yuv = 420;
                                bit = 8;
                            }else if(i==1){
                                yuv = 422;
                                bit = 10;
                            }else if(i==2){
                                yuv=444;
                                bit = 10;
                            }
                        }
                    }
                }
            });

            radioGroupCRF.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        if(((RadioButton)group.getChildAt(i)).isChecked()){
                            if(i==0){
                                crf = 17;
                            }else if(i==1){
                                crf = 23;
                            }else if(i==2){
                                crf = 29;
                            }
                        }
                    }
                }
            });
        }
    }
}
