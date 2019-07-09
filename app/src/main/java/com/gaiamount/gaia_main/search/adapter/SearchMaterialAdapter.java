package com.gaiamount.gaia_main.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yukun on 16-8-4.
 */
public class SearchMaterialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<MaterialInfo> materialInfo;
    private final StringUtil mStringUtil;
    public SearchMaterialAdapter(Context context, ArrayList<MaterialInfo> mixInfos){
        this.context=context;
        this.materialInfo=mixInfos;
        mStringUtil = StringUtil.getInstance();

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_search_academy, null);
        MixViewHolder holder=new MixViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MaterialInfo mixInfo = materialInfo.get(position);
        ((MixViewHolder)holder).textViewIntroduce.setText(mixInfo.getName());
        ((MixViewHolder)holder).textViewChapter.setText(mStringUtil.stringForTime(mixInfo.getDuration() * 1000));
        ((MixViewHolder)holder).textViewName.setText(mixInfo.getNickName());
        ((MixViewHolder)holder).textViewLearn.setText( mixInfo.getDownloadCount()+"下载 "+mixInfo.getLikeCount()+context.getString(R.string.collection));
//        ((MixViewHolder)holder).textViewTime.setText(getTime(mixInfo.getTime()));

        //設置寬高
        ViewGroup.LayoutParams layoutParams = ((MixViewHolder)holder).imageViewImg.getLayoutParams();
        layoutParams.width=(int)(ScreenUtils.instance().getWidth()*0.35);
        layoutParams.height = getHeight();
        ((MixViewHolder)holder).imageViewImg.setLayoutParams(layoutParams);

        setImageHeight(((MixViewHolder)holder).imageViewImg);
        String cover = mixInfo.getCover();
        String screenshot = mixInfo.getScreenshot();
        if (cover != null && !cover.isEmpty()) {
            Glide.with(context).load(Configs.COVER_PREFIX+cover).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).imageViewImg);
        } else if (screenshot != null && !screenshot.isEmpty()) {
//            if(mixInfo.getFlag()==1){
                screenshot = Configs.COVER_PREFIX +screenshot+"_18.png";
                Glide.with(context).load(screenshot).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).imageViewImg);
//            }
        }

//        Glide.with(context).load(Configs.COVER_PREFIX +mixInfo.getCover()).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).imageViewImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mixInfo.getCategory()==1){ //视频素材
                    ActivityUtil.startMaterialPlayActivity(context,mixInfo.getId(),1);
                }else if(mixInfo.getCategory()==2){ //模板素材
                    ActivityUtil.startMaterialPlayActivity(context,mixInfo.getId(),0);
                }

//                ActivityUtil.startMaterialPlayActivity(context,mixInfo.getId(),0);

            }
        });

    }

    @Override
    public int getItemCount() {
        return materialInfo.size();
    }

    class MixViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewIntroduce,textViewChapter,textViewName,textViewTime,textViewLearn;
        public ImageView imageViewImg, imageViewDevide;
        public MixViewHolder(View itemView) {
            super(itemView);
            textViewIntroduce= (TextView) itemView.findViewById(R.id.academy_title);
            textViewLearn= (TextView) itemView.findViewById(R.id.academy_playcount_collcount);
            textViewChapter=(TextView) itemView.findViewById(R.id.academy_duration);
            textViewName= (TextView) itemView.findViewById(R.id.academy_author);
            textViewTime= (TextView) itemView.findViewById(R.id.academy_create_time);
            imageViewDevide= (ImageView) itemView.findViewById(R.id.academy_choice);
            imageViewImg= (ImageView) itemView.findViewById(R.id.academy_cover);
        }
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width/2)*0.4);
        return itemHeight;
    }

    private void setImageHeight(View itemView) {
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height = getHeight();
        itemView.setLayoutParams(layoutParams);
    }

    private String getTime(Long msgTime) {
        long l = System.currentTimeMillis();
        if(l-msgTime<3600000){
            SimpleDateFormat formatter = new SimpleDateFormat    ("mm");
            Date curDate = new Date(msgTime);//获取时间
            String str1 = formatter.format(curDate);
            Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate2);
            int i = Integer.valueOf(str) - Integer.valueOf(str1);
            if(i>=0){
                return i+context.getString(R.string.time_minute_ago);
            }else {
                return 60+i+context.getString(R.string.time_minute_ago);
            }
        }
        if(3600000<l-msgTime&l-msgTime<86400000){
            SimpleDateFormat formatter = new SimpleDateFormat    ("hh");
            Date curDate = new Date(msgTime);//获取时间
            String str1 = formatter.format(curDate);
            Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate2);
            int i = Integer.valueOf(str) - Integer.valueOf(str1);
            if(i>=0){
                return i+context.getString(R.string.time_hours_ago);
            }else {
                return 24+i+context.getString(R.string.time_hours_ago);
            }
        }else if(l-msgTime>86400000 & l-msgTime<=86400000*2){
            return context.getString(R.string.one_day_ago);
        }else if(l-msgTime>86400000*2 & l-msgTime<=86400000*3){
            return context.getString(R.string.two_day_ago);
        }else if(l-msgTime>86400000*3 & l-msgTime<=86400000*4) {
            return context.getString(R.string.three_day_ago);
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat    ("yyyy/MM/dd");
            Date curDate = new Date(msgTime);//获取时间
            return formatter.format(curDate);
        }
    }
}
