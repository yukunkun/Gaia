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
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yukun on 16-8-4.
 */
public class SearchScripeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<ScripeInfo> materialInfo;
    private final StringUtil mStringUtil;
    public SearchScripeAdapter(Context context, ArrayList<ScripeInfo> mixInfos){
        this.context=context;
        this.materialInfo=mixInfos;
        mStringUtil = StringUtil.getInstance();

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.search_globle_scripe_item, null);
        MixViewHolder holder=new MixViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ScripeInfo mixInfo = materialInfo.get(position);
        ((MixViewHolder)holder).textViewName.setText(mixInfo.getTitle());
        ((MixViewHolder)holder).textViewColl.setText( mixInfo.getBrowserCount()+"浏览 "+mixInfo.getCollectCount()+context.getString(R.string.collection));
//        if(mixInfo.getUpDateTime().length()>9){
//            ((MixViewHolder)holder).textViewTime.setText(mixInfo.getUpDateTime()/*.substring(0,9)*/);
//        }

        int isFree = mixInfo.getIsFree();
        if(isFree==0){
            ((MixViewHolder)holder).textViewPrice.setText(mixInfo.getPrice()+"");
        }

        //設置寬高
        ViewGroup.LayoutParams layoutParams = ((MixViewHolder)holder).imageViewImg.getLayoutParams();
        layoutParams.width=(int)(ScreenUtils.instance().getWidth()*0.35);
        layoutParams.height = getHeight();
        ((MixViewHolder)holder).imageViewImg.setLayoutParams(layoutParams);

        setImageHeight(((MixViewHolder)holder).imageViewImg);
        Glide.with(context).load(Configs.COVER_PREFIX +mixInfo.getCover()).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).imageViewImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startScripeDetailActivity(context,mixInfo.getSid());
            }
        });

    }

    @Override
    public int getItemCount() {
        return materialInfo.size();
    }

    class MixViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewColl,textViewName,textViewTime,textViewPrice;
        public ImageView imageViewImg;
        public MixViewHolder(View itemView) {
            super(itemView);
            textViewColl= (TextView) itemView.findViewById(R.id.search_scripe_coll);
            textViewPrice= (TextView) itemView.findViewById(R.id.search_scripe_price);
            textViewName= (TextView) itemView.findViewById(R.id.search_sripe_title);
            textViewTime= (TextView) itemView.findViewById(R.id.search_scripe_time);
            imageViewImg= (ImageView) itemView.findViewById(R.id.search_scripe_cover);
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
