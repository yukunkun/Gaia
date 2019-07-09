package com.gaiamount.module_im.secret_chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_im.secret_chat.activity.GroupChatActivity;
import com.gaiamount.module_im.secret_chat.bean.GroupBean;
import com.gaiamount.util.image.ImageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-7-18.
 */
public class GroupRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<GroupBean> groupList;
    public GroupRecyAdapter(Context context, List<GroupBean> groupList) {
        this.context=context;
        this.groupList=groupList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.group_item_fragment, null);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder)holder).textViewName.setText(groupList.get(position).getName());
//        Glide.with(context).load(Configs.COVER_PREFIX+groupList.get(position).get).into(((MyViewHolder)holder).circleImageView);
        ImageUtils.getInstance(context).getAvatar(((MyViewHolder)holder).circleImageView, groupList.get(position).getBackground());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, GroupChatActivity.class);
                intent.putExtra("imid",groupList.get(position).getImId());
                intent.putExtra("otherAvatar",groupList.get(position).getBackground());
                intent.putExtra("otherNickName",groupList.get(position).getName());
                intent.putExtra("groupId",groupList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textViewName;
        public MyViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.group_head);
            textViewName= (TextView) itemView.findViewById(R.id.group_username);
        }
    }
}
