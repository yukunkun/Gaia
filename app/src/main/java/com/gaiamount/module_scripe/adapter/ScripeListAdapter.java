package com.gaiamount.module_scripe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.util.ActivityUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yukun on 16-10-21.
 */
public class ScripeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList <ScripeInfo> scripeInfos;

    public ScripeListAdapter(Context context, ArrayList<ScripeInfo> scripeInfos) {
        this.context = context;
        this.scripeInfos = scripeInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(context).inflate(R.layout.scripe_list_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyViewHolder){
            
            ((MyViewHolder) holder).textViewTitle.setText(scripeInfos.get(position).getTitle());
            ((MyViewHolder) holder).textViewContent.setText(scripeInfos.get(position).getOutline());
            ((MyViewHolder) holder).textViewLook.setText(scripeInfos.get(position).getBrowserCount()+"");
            ((MyViewHolder) holder).textViewLike.setText(scripeInfos.get(position).getCollectCount()+"");
            int isFree = scripeInfos.get(position).getIsFree();

            if(isFree==1){
                ((MyViewHolder) holder).textViewShow.setText("仅展示");
                ((MyViewHolder) holder).textViewShow.setTextColor(context.getResources().getColor(R.color.color_33bbff));

            }else {
                ((MyViewHolder) holder).textViewShow.setText(scripeInfos.get(position).getPrice()+"");
            }
            int state = scripeInfos.get(position).getState();
            if(state==1){
                ((MyViewHolder) holder).textViewTag.setText("已完结");
                ((MyViewHolder) holder).textViewTag.setBackgroundResource(R.drawable.shape_scripe_tag);

            }else {

                ((MyViewHolder) holder).textViewTag.setText("连载中");
                ((MyViewHolder) holder).textViewTag.setBackgroundResource(R.drawable.shape_scripe_tag_over);
            }
            ((MyViewHolder) holder).textViewTime.setText(getTime(scripeInfos.get(position).getTime()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startScripeDetailActivity(context,scripeInfos.get(position).getSid());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return scripeInfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  textViewTag,textViewTitle,textViewContent,textViewShow,textViewTime,
        textViewLook,textViewLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewTag=(TextView) itemView.findViewById(R.id.scripe_tag);
            textViewTitle=(TextView) itemView.findViewById(R.id.scripe_title);
            textViewContent=(TextView) itemView.findViewById(R.id.scripe_content);
            textViewShow=(TextView) itemView.findViewById(R.id.scripe_show);
            textViewTime=(TextView) itemView.findViewById(R.id.scripe_time);
            textViewLook=(TextView) itemView.findViewById(R.id.scripe_look);
            textViewLike=(TextView) itemView.findViewById(R.id.scripe_like);

        }
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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date curDate = new Date(msgTime);//获取时间
            return formatter.format(curDate);
        }
    }

    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

}
