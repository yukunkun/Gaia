package com.gaiamount.module_creator.creater_circle.adapter;

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
import com.gaiamount.module_creator.creater_circle.bean.GroupMessage;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.image.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yukun on 16-7-25.
 */
public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<GroupMessage> groupMessageList;
    public GroupAdapter( Context context, List<GroupMessage> groupMessageList){
        this.context=context;
        this.groupMessageList=groupMessageList;
    }
    int sreenWidth;
    private void getScreeWidth() {
        sreenWidth = ScreenUtils.instance().getWidth();
    }

    private void setItemHeight(View itemView) {
        ImageView itemImageView = (ImageView) itemView.findViewById(R.id.circle_group_image);
        ViewGroup.LayoutParams layoutParams = itemImageView.getLayoutParams();
        getScreeWidth();
        layoutParams.height = (int) ((sreenWidth/3.7));//3.7:1
        itemImageView.setLayoutParams(layoutParams);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.circle_group_layout, null);
        setItemHeight(inflate);
        GroupViewHolder holder=new GroupViewHolder(inflate);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Glide.with(context).load(Configs.COVER_PREFIX+groupMessageList.get(position).getBackground()).placeholder(R.mipmap.bg_group).into(((GroupViewHolder)holder).imageViewImg);
        ((GroupViewHolder)holder).textViewGroupName.setText(groupMessageList.get(position).getName());
        ((GroupViewHolder)holder).textViewCreate.setText(groupMessageList.get(position).getCreationCount()+"创作·"+groupMessageList.get(position).getSeconds()+"访问");
        ((GroupViewHolder)holder).textViewContent.setText(groupMessageList.get(position).getDescription());
        ((GroupViewHolder)holder).textViewUser.setText(groupMessageList.get(position).getNickName());
        ((GroupViewHolder)holder).textViewTime.setText(getTime(groupMessageList.get(position).getTime())+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startGroupActivity(context,groupMessageList.get(position).getId());
            }
        });
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
                return 12+i+context.getString(R.string.time_hours_ago);
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
    @Override
    public int getItemCount() {
        return groupMessageList.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewImg;
        TextView textViewGroupName,textViewCreate, textViewContent,textViewUser,textViewTime;
        public GroupViewHolder(View itemView) {
            super(itemView);
            imageViewImg= (ImageView) itemView.findViewById(R.id.circle_group_image);
            textViewGroupName= (TextView) itemView.findViewById(R.id.circle_name);
            textViewCreate= (TextView) itemView.findViewById(R.id.circle_create);
            textViewContent= (TextView) itemView.findViewById(R.id.circle_content);
            textViewUser= (TextView) itemView.findViewById(R.id.circle_user);
            textViewTime= (TextView) itemView.findViewById(R.id.circle_time);
        }
    }

}
