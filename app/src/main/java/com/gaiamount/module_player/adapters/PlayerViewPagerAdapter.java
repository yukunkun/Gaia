package com.gaiamount.module_player.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gaiamount.R;

import java.util.List;

/**
 * Created by haiyang-lu on 16-4-5.
 */
public class PlayerViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private Context mContext;
    private final String[] mTab_titles;

    public PlayerViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, Context context) {
        super(fm);
        this.fragmentList = fragmentList;
        mContext = context;
        mTab_titles = mContext.getResources().getStringArray(R.array.player_tabs);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTab_titles[position];
    }
}
