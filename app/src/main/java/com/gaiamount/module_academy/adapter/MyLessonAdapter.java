package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_academy.bean.LessonInfo;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yukun on 16-8-2.
 */
public class MyLessonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MixInfo> lessonInfos;
    Context context;
    public MyLessonAdapter(Context context, ArrayList<MixInfo> lessonInfos){
        this.context=context;
        this.lessonInfos=lessonInfos;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.academy_mix_item, null);
        LessonViewHolder holder=new LessonViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MixInfo mixInfo = lessonInfos.get(position);
        ((LessonViewHolder)holder).textViewLearn.setText(StringUtil.getInstance().stringForLeare(mixInfo.getLearningCount()));
        ((LessonViewHolder)holder).textViewIntroduce.setText(mixInfo.getName());
        ((LessonViewHolder)holder).textViewChapter.setText(mixInfo.getChaptCount()+"章"+mixInfo.getHourCount()+"节");
        ((LessonViewHolder)holder).textViewScan.setText(mixInfo.getPlayCount()+"");
        ((LessonViewHolder)holder).textViewLike.setText(mixInfo.getBrowseCount()+"");
        ((LessonViewHolder)holder).textViewGrade.setText(mixInfo.getGrade()+"");
        ((LessonViewHolder)holder).textViewNickname.setText(mixInfo.getNickName());

        //設置寬高
        ViewGroup.LayoutParams layoutParams = ((LessonViewHolder)holder).imageViewImg.getLayoutParams();
        layoutParams.height = getHeight();
        ((LessonViewHolder)holder).imageViewImg.setLayoutParams(layoutParams);

        Glide.with(context).load(Configs.COVER_PREFIX +mixInfo.getCover()).placeholder(R.mipmap.bg_general).into(((LessonViewHolder)holder).imageViewImg);
        Glide.with(context).load(Configs.COVER_PREFIX +mixInfo.getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(((LessonViewHolder)holder).imageViewAvator);

        final int type = mixInfo.getType();
        //判断视频类型
        if(type==1){
            ((LessonViewHolder)holder).imageViewDevide.setImageResource(R.mipmap.ic_tw);
        }else if(type==0){
            ((LessonViewHolder)holder).imageViewDevide.setImageResource(R.mipmap.ic_video);
        }else if(type==2){
            ((LessonViewHolder)holder).imageViewDevide.setImageResource(R.mipmap.ic_live);
        }

        int progress = mixInfo.getHasLeanCount();
        int len = mixInfo.getHourCount();
        if(len!=0){
            int percent=(int)progress*100/len;
            if(percent==100){
                ((LessonViewHolder) holder).textViewPrice.setText(context.getResources().getString(R.string.finished));
                ((LessonViewHolder) holder).textViewPrice.setTextColor(context.getResources().getColor(R.color.color_42bd56));
            }else {
                ((LessonViewHolder) holder).textViewPrice.setText(percent+"%");
                ((LessonViewHolder) holder).textViewPrice.setTextColor(context.getResources().getColor(R.color.color_ff5773));
            }
        }
        //官方推荐
        int proprity = mixInfo.getProprity();
        if(proprity==1){
            if((((LessonViewHolder)holder).imageViewRecommend).getVisibility()==View.GONE)
                ((LessonViewHolder)holder).imageViewRecommend.setVisibility(View.VISIBLE);
        }else if(proprity==0){
            ((LessonViewHolder)holder).imageViewRecommend.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startAcademyDetailActivity(Long.valueOf(mixInfo.getCid()),context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonInfos.size();
    }

    class LessonViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewIntroduce,textViewPrice,textViewLearn,textViewChapter,textViewScan,textViewLike,textViewGrade,textViewNickname;
        public ImageView imageViewImg, imageViewDevide,imageViewRecommend,imageViewAvator;
        public LessonViewHolder(View itemView) {
            super(itemView);
            textViewIntroduce= (TextView) itemView.findViewById(R.id.academy_mix_lesson_name);
            textViewPrice= (TextView) itemView.findViewById(R.id.academy_mix_price);
            textViewLearn= (TextView) itemView.findViewById(R.id.academy_mix_learn_num);
            textViewChapter=(TextView) itemView.findViewById(R.id.academy_textview_learn);
            textViewScan= (TextView) itemView.findViewById(R.id.academy_mix_watchs);
            textViewLike=(TextView) itemView.findViewById(R.id.academy_mix_likes);
            textViewGrade=(TextView) itemView.findViewById(R.id.academy_mix_stars);
            textViewNickname=(TextView) itemView.findViewById(R.id.academy_mix_username);
            imageViewImg= (ImageView) itemView.findViewById(R.id.academy_img_map);
            imageViewDevide= (ImageView) itemView.findViewById(R.id.academy_devide);
            imageViewAvator=(ImageView)itemView.findViewById(R.id.academy_mix_head);
            imageViewRecommend= (ImageView) itemView.findViewById(R.id.academy_recomend);
            //点击事件
        }
    }
    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width/2)*0.52);
        return itemHeight;
    }

}
