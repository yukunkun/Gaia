package com.gaiamount.module_material.adapters;

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
import com.gaiamount.module_academy.bean.AcademyInfo;
import com.gaiamount.module_academy.viewholder.NoScrollViewHolder;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-8-22.
 */
public class NoScrollViewMaterialAdapter extends BaseAdapter {
    private Context context;
    private List<MaterialInfo> academyInfos;

    public NoScrollViewMaterialAdapter(Context context, List<MaterialInfo> academyInfos) {
        this.context = context;
        this.academyInfos = academyInfos;
    }

    @Override
    public int getCount() {
        return academyInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return academyInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoScrollViewHolder holder=null;
        if(convertView==null){
            holder=new NoScrollViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.academy_gride_items,parent,false);
            holder.textViewIntroduce= (TextView) convertView.findViewById(R.id.grideview_name);
            holder.textViewPrice= (TextView) convertView.findViewById(R.id.grideview_price);
            holder.textViewLearn= (TextView) convertView.findViewById(R.id.grideview_num);
            holder.imageViewImg= (ImageView) convertView.findViewById(R.id.grideview_cover);
            convertView.setTag(holder);
        }else{
            holder= (NoScrollViewHolder) convertView.getTag();
        }
        holder.textViewIntroduce.setText(academyInfos.get(position).getName());
//        holder.textViewLearn.setText(academyInfos.get(position).getLearningCount()+"");
        //判断是否付费
        int allowFree = academyInfos.get(position).getAllowCharge();

        if(allowFree==0){
            holder.textViewPrice.setText(R.string.for_free);
            holder.textViewPrice.setTextColor(context.getResources().getColor(R.color.color_42bd56));
        }else if(allowFree==1){
            holder.textViewPrice.setText(R.string.not_free);
            holder.textViewPrice.setTextColor(context.getResources().getColor(R.color.color_ff5773));
        }

        ViewGroup.LayoutParams layoutParams = holder.imageViewImg.getLayoutParams();
        layoutParams.height = getHeight();
        (holder).imageViewImg.setLayoutParams(layoutParams);

        String screenshot = academyInfos.get(position).getScreenshot();
        //设置图片

        if(academyInfos.get(position).getCover()!=null&&academyInfos.get(position).getCover().length()!=0){
            Glide.with(context).load(Configs.COVER_PREFIX+academyInfos.get(position).getCover()).placeholder(R.mipmap.bg_general).into(holder.imageViewImg);
        }else if(screenshot!=null&&screenshot.length()!=0){
//            if(academyInfos.get(position).getFlag()==1){
                Glide.with(context).load(Configs.COVER_PREFIX+academyInfos.get(position).getScreenshot()+"_18.png").placeholder(R.mipmap.bg_general).into(holder.imageViewImg);
//            }else {
//                Glide.with(context).load(Configs.COVER_PREFIX+academyInfos.get(position).getScreenshot()).placeholder(R.mipmap.bg_general).into(holder.imageViewImg);
//
//            }
        }

        return convertView;
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width/2)*0.52);
        return itemHeight;
    }
}
