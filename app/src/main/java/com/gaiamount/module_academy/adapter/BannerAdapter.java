package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.util.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-2.
 */
public class BannerAdapter extends PagerAdapter {

    private ArrayList<String> covers;
    Context context;
    public BannerAdapter(Context context,ArrayList<String> covers){
        this.covers=covers;
        this.context=context;
    }
    @Override
    public int getCount() {
        return covers.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View inflate = LayoutInflater.from(container.getContext()).inflate(R.layout.bannerlayout, null);
        ImageView imageView= (ImageView) inflate.findViewById(R.id.banner_img);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = getHeight();
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.mipmap.personal_bg);
        //加载图片
        Glide.with(context).load(Configs.COVER_PREFIX+covers.get(position)).placeholder(R.mipmap.personal_bg).into(imageView);
        container.addView(inflate);
        return inflate;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width)*0.3);
        return itemHeight;
    }
}
