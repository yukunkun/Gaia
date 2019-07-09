package com.gaiamount.module_academy.viewholder;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.gaiamount.R;

/**
 * Created by yukun on 16-8-2.
 */
public class ViewPagerHolder extends RecyclerView.ViewHolder {
    public ConvenientBanner mConvenientBanner;
    public LinearLayout mLinAll,mLinMix,mLinLesson;
    public ViewPagerHolder(View itemView) {
        super(itemView);
        mConvenientBanner= (ConvenientBanner) itemView.findViewById(R.id.academy_viewpager);
        mLinAll= (LinearLayout) itemView.findViewById(R.id.academy_all);
        mLinMix= (LinearLayout) itemView.findViewById(R.id.academy_mix_light);
        mLinLesson= (LinearLayout) itemView.findViewById(R.id.academy_lesson);
    }
}
