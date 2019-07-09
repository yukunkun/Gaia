package com.gaiamount.module_material.viewholder;

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
    public LinearLayout mLinmaterial,mLinMoban,linclude;
    public ViewPagerHolder(View itemView) {
        super(itemView);
        mConvenientBanner= (ConvenientBanner) itemView.findViewById(R.id.academy_viewpager);
        mLinmaterial= (LinearLayout) itemView.findViewById(R.id.material_all);
        mLinMoban= (LinearLayout) itemView.findViewById(R.id.material_light);
        linclude= (LinearLayout) itemView.findViewById(R.id.lin);
    }
}
