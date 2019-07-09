package com.gaiamount.module_scripe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_academy.bean.CommentInfo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-8-18.
 */
public class ScripeCommAdapter extends BaseAdapter {
    private ArrayList<CommentInfo> commentInfo;
    private Context context;

    public ScripeCommAdapter(Context context, ArrayList<CommentInfo> commentInfo) {
        this.commentInfo = commentInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return commentInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return commentInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentViewHolder viewHolder=null;
        CommentInfo info = commentInfo.get(position);
        if(convertView==null){
            viewHolder=new CommentViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.comment_list_item,null);
            viewHolder.imageViewHead= (CircleImageView) convertView.findViewById(R.id.comment_head);
            viewHolder.textViewName= (TextView) convertView.findViewById(R.id.comment_name);
            viewHolder.textViewTime= (TextView) convertView.findViewById(R.id.comment_time);
            viewHolder.textViewGrade= (TextView) convertView.findViewById(R.id.comment_grade);
            viewHolder.textViewContent= (TextView) convertView.findViewById(R.id.comment_content);
            viewHolder.ratingBar= (RatingBar) convertView.findViewById(R.id.comment_ratingbar);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (CommentViewHolder) convertView.getTag();
        }
        viewHolder.textViewName.setText(info.getNickName());
        viewHolder.ratingBar.setVisibility(View.GONE);

        viewHolder.textViewContent.setText(info.getContent());
        viewHolder.textViewTime.setText(getTime(info.getTime()));
        Glide.with(context).load(Configs.COVER_PREFIX+info.getAvatar())
                .placeholder(R.mipmap.ic_avatar_default).into(viewHolder.imageViewHead);

        return convertView;
    }

    class CommentViewHolder{
        CircleImageView imageViewHead;
        TextView textViewName,textViewTime,textViewGrade,textViewContent;
        RatingBar ratingBar;
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
