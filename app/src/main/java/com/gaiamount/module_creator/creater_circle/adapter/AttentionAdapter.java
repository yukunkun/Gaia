package com.gaiamount.module_creator.creater_circle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.PersonApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.creater_circle.bean.Attention;
import com.gaiamount.module_creator.creater_circle.bean.interfaces;
import com.gaiamount.module_im.secret_chat.MySecretActivity;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.image.ImageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-7-25.
 */
public class AttentionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Attention> attentionList;
//    interfaces.SetOnItemClickListener onItemClickListener;

    public AttentionAdapter(Context context,List attentionList){
        this.context=context;
        this.attentionList=attentionList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.attentionlayout, null);
        AttViewHolder attViewHolder=new AttViewHolder(inflate,context,viewType);
        return attViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AttViewHolder)holder).textViewName.setText(attentionList.get(position).getNickName());
        ((AttViewHolder)holder).textViewMess.setText(attentionList.get(position).getCreationCount()+"创作·"+attentionList.get(position).getLikeCount()+"粉丝");
        ImageUtils.getInstance(context).getAvatar(((AttViewHolder)holder).circleImageViewHead,attentionList.get(position).getAvatar());
    }

    @Override
    public int getItemCount() {
        return attentionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class AttViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageViewHead;
        LinearLayout linearLayoutSiXin,linearLayoutAttent;
        TextView textViewName,textViewMess;
        public AttViewHolder(View itemView, final Context context, final int viewType) {
            super(itemView);
            circleImageViewHead= (CircleImageView) itemView.findViewById(R.id.attent_head);
            linearLayoutSiXin= (LinearLayout) itemView.findViewById(R.id.sixin);
            linearLayoutAttent= (LinearLayout) itemView.findViewById(R.id.attention);
            textViewName= (TextView) itemView.findViewById(R.id.circle_name);
            textViewMess= (TextView) itemView.findViewById(R.id.circle_mes);
            linearLayoutSiXin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MySecretActivity.class);
                    context.startActivity(intent);
                }
            });
            linearLayoutAttent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonApiHelper.addAttention(attentionList.get(viewType).getId(),0,context);
                    GaiaApp.showToast(context.getString(R.string.cancel_attention));
                    attentionList.remove(viewType);
                    notifyItemRemoved(viewType);
                }
            });
            circleImageViewHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startPersonalActivity(context,attentionList.get(viewType).getId());
                }
            });
        }
    }
//    public void setOnBack(interfaces.SetOnItemClickListener onItemClickListener){
//        this.onItemClickListener=onItemClickListener;
//    }
}
