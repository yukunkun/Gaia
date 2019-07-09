package com.gaiamount.module_scripe.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_creator.PersonApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.viewholder.NoScrollViewHolder;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_scripe.bean.EdidsInfo;
import com.gaiamount.module_scripe.bean.ListViewHolder;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-8-22.
 */
public class NoScrollListScripeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<EdidsInfo> edidsInfos;
    ListViewHolder holder=null;
    public NoScrollListScripeAdapter(Context context,ArrayList<EdidsInfo> edidsInfos) {
        this.context = context;
        this.edidsInfos = edidsInfos;
    }

    @Override
    public int getCount() {
        return edidsInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            convertView=LayoutInflater.from(context).inflate(R.layout.scripe_noscrol_list,parent,false);
            final TextView textViewAttent= (TextView) convertView.findViewById(R.id.scripe_attent);
            TextView textViewIntr= (TextView) convertView.findViewById(R.id.scripe_intro);
            TextView textViewName= (TextView) convertView.findViewById(R.id.scripe_name);
            ImageView imageViewHead= (CircleImageView) convertView.findViewById(R.id.scripe_head);

        textViewName.setText(edidsInfos.get(position).getNickName());
        textViewIntr.setText(edidsInfos.get(position).getSignature());
        //设置图片
        Glide.with(context).load(Configs.COVER_PREFIX+edidsInfos.get(position).getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(imageViewHead);

        if(edidsInfos.get(position).getIsFocus()==1){
            textViewAttent.setText("已关注");
            textViewAttent.setTextColor(context.getResources().getColor(R.color.white));
            textViewAttent.setBackgroundResource(R.drawable.shape_action_att);
        }

        imageViewHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startPersonalActivity(context,edidsInfos.get(position).getUid());
            }
        });

        if(edidsInfos.get(position).getUid()==GaiaApp.getAppInstance().getUserInfo().id){
            textViewAttent.setTextColor(context.getResources().getColor(R.color.a1a1a1));
            textViewAttent.setBackgroundResource(R.drawable.shape_attention_att_pray);
        }

        textViewAttent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edidsInfos.get(position).getUid()!=GaiaApp.getAppInstance().getUserInfo().id&&textViewAttent.getText().equals("关注")){

                    addAttention(1);
                    textViewAttent.setText("已关注");
                    textViewAttent.setTextColor(context.getResources().getColor(R.color.white));
                    textViewAttent.setBackgroundResource(R.drawable.shape_action_att);

                }else if(edidsInfos.get(position).getUid()!=GaiaApp.getAppInstance().getUserInfo().id&&textViewAttent.getText().equals("已关注")){
                    addAttention(0);
                    textViewAttent.setText("关注");
                    textViewAttent.setTextColor(context.getResources().getColor(R.color.color_ff5773));
                    textViewAttent.setBackgroundResource(R.drawable.shape_attention_att);
                }else if(edidsInfos.get(position).getUid()==GaiaApp.getAppInstance().getUserInfo().id){
                    GaiaApp.showToast("不能关注自己");
                }
            }

            private void addAttention(int i) {

                PersonApiHelper.addAttention(edidsInfos.get(position).getUid(),i,context);
                if(i==1){
                    GaiaApp.showToast("关注成功");
                }else if(i==0){
                    GaiaApp.showToast("取消成功");
                }

            }
        });
        return convertView;
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width)*0.52);
        return itemHeight;
    }
}
