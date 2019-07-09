package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gaiamount.R;
import com.gaiamount.module_academy.activity.MixingLightActivity;
import com.gaiamount.module_academy.activity.MyLessonActivity;
import com.gaiamount.util.ScreenUtils;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by yukun on 16-8-2.
 */
public class DevideViewPagerAdapter extends PagerAdapter {
    private Context context;
    private int id;
    public DevideViewPagerAdapter(Context context, int id){
        this.context=context;
        this.id=id;
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View inflate = LayoutInflater.from(container.getContext()).inflate(R.layout.devide_viewpager, null);
        RoundedImageView imageView1= (RoundedImageView) inflate.findViewById(R.id.img_mix_light);
        RoundedImageView imageView2= (RoundedImageView) inflate.findViewById(R.id.img_my_desk);
        //设置高度
        ViewGroup.LayoutParams layoutParams = imageView1.getLayoutParams();
        layoutParams.height = getHeight();
        imageView1.setLayoutParams(layoutParams);
        //设置高度
        ViewGroup.LayoutParams layoutParams2 = imageView2.getLayoutParams();
        layoutParams2.height = getHeight();
        imageView2.setLayoutParams(layoutParams2);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MixingLightActivity.class);
                intent.putExtra("id",id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MyLessonActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

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
        itemHeight = (int) ((width/2-16)*0.4);
        return itemHeight;
    }
//    public int getHeight(){//设置宽高比例
//        int itemHeight;
//        int width = ScreenUtils.instance().getWidth();
//        itemHeight = (int) ((width/2-16)*0.3);
//        return itemHeight;
//    }
}
