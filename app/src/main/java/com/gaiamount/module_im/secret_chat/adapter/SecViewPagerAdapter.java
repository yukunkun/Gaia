package com.gaiamount.module_im.secret_chat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by yukun on 16-7-14.
 */
public class SecViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;
    String[] strings;
    public SecViewPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragments,String[] strings) {
        super(fm);
        this.fragments=fragments;
        this.strings=strings;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return strings[position % strings.length];
    }
}
