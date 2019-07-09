package com.gaiamount.module_im.secret_chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.secret_chat.activity.ContactChatActivity;
import com.gaiamount.module_im.secret_chat.activity.GroupChatActivity;
import com.gaiamount.module_im.secret_chat.bean.ContactInfo;
import com.gaiamount.module_im.secret_chat.bean.ContentInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-7-14.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ContentInfo> contentInfos;
    public RecyclerViewAdapter(Context context, List<ContentInfo> contentInfos){
        this.context=context;
        this.contentInfos=contentInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        RecyclerView.ViewHolder holder=null;
        view = LayoutInflater.from(context).inflate(R.layout.secret_recycler_item, null);
        holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder)holder).textViewContent.setText(contentInfos.get(position).getBody());
        ((MyViewHolder)holder).textViewName.setText(contentInfos.get(position).getOtherNickName());
        ((MyViewHolder)holder).textViewTime.setText(contentInfos.get(position).getTime());
        if (contentInfos.get(position).getUnReadCount()==0){
            ((MyViewHolder)holder).textViewCishu.setVisibility(View.GONE);
        }else{
            ((MyViewHolder)holder).textViewCishu.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).textViewCishu.setText(contentInfos.get(position).getUnReadCount()+"");
        }
        Glide.with(context).load(Configs.COVER_PREFIX+contentInfos.get(position).getAvatar())
                .placeholder(R.mipmap.ic_avatar_default).into(((MyViewHolder)holder).circleImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contentInfos.get(position).getGroup().equals("group")){
//                if(contentInfos.get(position).getUid().length()>5){
                    Intent intent=new Intent(context, GroupChatActivity.class);
                    intent.putExtra("imid", contentInfos.get(position).getUid());
                    intent.putExtra("otherAvatar",contentInfos.get(position).getAvatar());
                    intent.putExtra("otherNickName",contentInfos.get(position).getOtherNickName());
                    intent.putExtra("groupId",contentInfos.get(position).getGroupId());
                    context.startActivity(intent);
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(contentInfos.get(position).getUid());
                    //指定会话消息未读数清零
                    if(conversation!=null){
                        conversation.markAllMessagesAsRead();
                    }
                }else {
                    Intent intent=new Intent(context, ContactChatActivity.class);
                    intent.putExtra("otherId", contentInfos.get(position).getUid());
                    intent.putExtra("otherAvatar",contentInfos.get(position).getAvatar());
                    intent.putExtra("otherNickName",contentInfos.get(position).getOtherNickName());
                    context.startActivity(intent);
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(contentInfos.get(position).getUid());
                    //指定会话消息未读数清零
                    if(conversation!=null){
                        conversation.markAllMessagesAsRead();
                    }
                }

                contentInfos.get(position).setUnReadCount(0);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentInfos.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textViewName,textViewContent,textViewTime,textViewCishu;
        public MyViewHolder(View itemView) {
            super(itemView);
            textViewCishu= (TextView) itemView.findViewById(R.id.cishu);
            circleImageView= (CircleImageView) itemView.findViewById(R.id.sec_head);
            textViewName= (TextView) itemView.findViewById(R.id.sec_username);
            textViewContent= (TextView) itemView.findViewById(R.id.sec_content);
            textViewTime= (TextView) itemView.findViewById(R.id.sec_time);
        }
    }
}
