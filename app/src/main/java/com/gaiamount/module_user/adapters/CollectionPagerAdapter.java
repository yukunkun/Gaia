package com.gaiamount.module_user.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gaiamount.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-5-4.
 */
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] mTitle;
    private List<Fragment> mCreatorWorkFragList = new ArrayList<>();

    public CollectionPagerAdapter(FragmentManager fm, Context context, List<Fragment> list, long uid) {
        super(fm);
        mTitle = context.getResources().getStringArray(R.array.creator_collection);
        mCreatorWorkFragList = list;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mCreatorWorkFragList.get(position);
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }
}
