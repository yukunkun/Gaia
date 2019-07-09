package com.gaiamount.module_material.viewholder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_material.MaterialMainActivity;
import com.gaiamount.module_material.activity.MaterialModelActivity;
import com.gaiamount.module_material.activity.MaterialVideosActivity;
import com.gaiamount.module_material.bean.EventPosition;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by yukun on 16-9-28.
 */
public class OriRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<MaterialMainActivity.Banner> banners;

    public OriRecyclerAdapter(Context context, List<MaterialMainActivity.Banner> banners) {
        this.context = context;
        this.banners = banners;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.ori_recycler_item, null);
        MViewHolder holder=new MViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MViewHolder)holder).textView.setText(banners.get(position).getName());
        Glide.with(context).load(Configs.COVER_PREFIX+banners.get(position).getImage()).placeholder(R.mipmap.adv).into(((MViewHolder)holder).imageView);
        ((MViewHolder)holder).cover.setImageResource(R.drawable.shape_material_cover);
        ((MViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(context, MaterialVideosActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("position",position+1);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView,cover;
        TextView textView;
        public MViewHolder(View itemView) {
            super(itemView);
            imageView= (RoundedImageView) itemView.findViewById(R.id.ori_image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            textView= (TextView) itemView.findViewById(R.id.ori_tectview);
            cover= (RoundedImageView) itemView.findViewById(R.id.cover);
            cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
