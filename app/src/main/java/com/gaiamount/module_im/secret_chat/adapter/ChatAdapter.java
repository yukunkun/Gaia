package com.gaiamount.module_im.secret_chat.adapter;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.secret_chat.activity.ChatImageActivity;
import com.gaiamount.module_im.secret_chat.bean.ContentInfo;
import com.gaiamount.util.ScreenUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by yukun on 16-7-18.
 */
public class ChatAdapter extends BaseAdapter {
    Context context;
    List<ContentInfo> contentInfos;

    public ChatAdapter(Context context, ArrayList<ContentInfo> contentInfos ){
        this.context=context;
        this.contentInfos=contentInfos;
    }

    @Override
    public int getCount() {
        return contentInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return contentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ContentInfo info = contentInfos.get(position);
       if(info.getType()==0){
//        if(info.getChose().equals(GaiaApp.getAppInstance().getUserInfo().id)){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        final ContentInfo info = contentInfos.get(position);
        if(convertView==null){
            if(getItemViewType(position)==0){//自己的消息
                convertView=LayoutInflater.from(context).inflate(R.layout.chat_my_right,parent,false);
            }else {
                convertView=LayoutInflater.from(context).inflate(R.layout.chat_you_left,parent,false);
            }

            holder=new ViewHolder();
            holder.imageViewYouHead= (CircleImageView) convertView.findViewById(R.id.you_head);
            holder.imageViewMyHead= (CircleImageView) convertView.findViewById(R.id.my_head);
            holder.textViewYou= (TextView) convertView.findViewById(R.id.you_content);
            holder.textViewMy= (TextView) convertView.findViewById(R.id.my_contant);
            holder.linearLayoutYou= (LinearLayout) convertView.findViewById(R.id.you_layout);
            holder.imageViewYou= (ImageView) convertView.findViewById(R.id.imageYous);
            holder.imageViewMy= (ImageView) convertView.findViewById(R.id.imageMys);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
//        if(info.getChose().equals(GaiaApp.getAppInstance().getUserInfo().id+"")){
          if(info.getType()==0){
            if (info.getBody()==null){
                holder.textViewMy.setVisibility(View.GONE);
                holder.imageViewMy.setVisibility(View.VISIBLE);
                Glide.with(context).load(info.getImageUri()).override(ScreenUtils.dp2Px(context,220),ScreenUtils.dp2Px(context,150)).into(holder.imageViewMy);
            }else {
                holder.textViewMy.setVisibility(View.VISIBLE);
                holder.imageViewMy.setVisibility(View.GONE);
                holder.textViewMy.setText(info.getBody());
            }
            Glide.with(context).load(Configs.COVER_PREFIX+info.getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(holder.imageViewMyHead);
              //图片的点击事件
              (holder.imageViewMy).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent=new Intent(context, ChatImageActivity.class);
                      intent.putExtra("imagePath",info.getImageUri());
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      context.startActivity(intent);
                  }
              });
        }else if(contentInfos.get(position).getOtherId().equals(contentInfos.get(position).getUid())){
            if (info.getBody()==null){
                holder.imageViewYou.setVisibility(View.VISIBLE);
                holder.textViewYou.setVisibility(View.GONE);
                Glide.with(context).load(info.getImageUri()).override(ScreenUtils.dp2Px(context,220),ScreenUtils.dp2Px(context,150)).into(holder.imageViewYou);
            }else {
                holder.textViewYou.setVisibility(View.VISIBLE);
                holder.imageViewYou.setVisibility(View.GONE);
                holder.textViewYou.setText(info.getBody());
            }
            Glide.with(context).load(Configs.COVER_PREFIX+info.getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(holder.imageViewYouHead);
               //图片的点击事件,放大观看
              (holder.imageViewYou).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent=new Intent(context, ChatImageActivity.class);
                      intent.putExtra("imagePath",info.getImageUri());
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      context.startActivity(intent);
                  }
              });
          }
        return convertView;
    }

    public void update(List<ContentInfo> list) {
        this.contentInfos = list;
        notifyDataSetChanged();
    }
    static class ViewHolder{
        TextView textViewYou,textViewMy;
        CircleImageView imageViewYouHead,imageViewMyHead;
        LinearLayout linearLayoutYou;
        ImageView imageViewYou,imageViewMy;
    }
}
