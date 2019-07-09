package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_im.secret_chat.adapter.RecyclerViewAdapter;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yukun on 16-8-4.
 */
public class MixAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<MixInfo> mixInfos;

    public MixAdapter(Context context,ArrayList<MixInfo> mixInfos){
        this.context=context;
        this.mixInfos=mixInfos;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.academy_mix_item, null);
        MixViewHolder holder=new MixViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MixInfo mixInfo = mixInfos.get(position);
        ((MixViewHolder)holder).textViewLearn.setText(StringUtil.getInstance().stringForLeare(mixInfo.getLearningCount()));
        ((MixViewHolder)holder).textViewIntroduce.setText(mixInfo.getName());
        ((MixViewHolder)holder).textViewChapter.setText(mixInfo.getChaptCount()+context.getResources().getString(R.string.academy_chapter)+mixInfo.getHourCount()+context.getResources().getString(R.string.academy_jie));
        ((MixViewHolder)holder).textViewScan.setText(mixInfo.getPlayCount()+"");
        ((MixViewHolder)holder).textViewLike.setText(mixInfo.getBrowseCount()+"");
        ((MixViewHolder)holder).textViewGrade.setText(mixInfo.getGrade()+"");
        ((MixViewHolder)holder).textViewNickname.setText(mixInfo.getNickName());

        //是否付费
        int allowFree = mixInfo.getAllowFree();
        if(allowFree==1){
            ((MixViewHolder)holder).textViewPrice.setText(R.string.for_free);
            ((MixViewHolder)holder).textViewPrice.setTextColor(context.getResources().getColor(R.color.color_42bd56));
        }else if(allowFree==0){
            ((MixViewHolder)holder).textViewPrice.setText(mixInfo.getPrice()+ context.getResources().getString(R.string.academy_yuan));
            ((MixViewHolder)holder).textViewPrice.setTextColor(context.getResources().getColor(R.color.color_ff5773));
        }

        //設置寬高
        ViewGroup.LayoutParams layoutParams = ((MixViewHolder)holder).imageViewImg.getLayoutParams();
        layoutParams.height = getHeight();
        ((MixViewHolder)holder).imageViewImg.setLayoutParams(layoutParams);


        Glide.with(context).load(Configs.COVER_PREFIX +mixInfo.getCover()).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).imageViewImg);
        Glide.with(context).load(Configs.COVER_PREFIX+mixInfo.getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(((MixViewHolder)holder).imageViewAvator);
        final int type = mixInfo.getType();
        //判断视频类型
        if(type==0){
            ((MixViewHolder)holder).imageViewDevide.setImageResource(R.mipmap.ic_video);
        }else if(type==1){
            ((MixViewHolder)holder).imageViewDevide.setImageResource(R.mipmap.ic_tw);
        }else if(type==2){
            ((MixViewHolder)holder).imageViewDevide.setImageResource(R.mipmap.ic_live);
        }
        //是否官方
        int proprity = mixInfo.getProprity();
        if(proprity==1){
            if((((MixViewHolder)holder).imageViewRecommend).getVisibility()==View.GONE)
            ((MixViewHolder)holder).imageViewRecommend.setVisibility(View.VISIBLE);
        }else if(proprity==0){
            ((MixViewHolder)holder).imageViewRecommend.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ActivityUtil.startAcademyDetailActivity(Long.valueOf(mixInfo.getId()),context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mixInfos.size();
    }

    class MixViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewIntroduce,textViewPrice,textViewLearn,textViewChapter,textViewScan,textViewLike,textViewGrade,textViewNickname;
        public ImageView imageViewImg, imageViewDevide,imageViewRecommend,imageViewAvator;
        public MixViewHolder(View itemView) {
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
