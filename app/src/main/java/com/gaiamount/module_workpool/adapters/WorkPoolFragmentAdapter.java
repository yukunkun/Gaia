package com.gaiamount.module_workpool.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gaiamount.R;
import com.gaiamount.module_workpool.fragment.WorkPoolFragment;
import com.gaiamount.module_workpool.WorkPoolActivity;

/**
 * Created by haiyang-lu on 16-4-18.
 * 作品池的片段适配器
 */
public class WorkPoolFragmentAdapter extends FragmentStatePagerAdapter {
    private WorkPoolActivity mActivity;
    private WorkPoolFragment[] mWorkPoolFragments;
    private String[] strings;

    public WorkPoolFragmentAdapter(WorkPoolActivity activity, FragmentManager fm) {
        super(fm);
        mActivity = activity;
        strings = mActivity.getResources().getStringArray(R.array.work_pool);
        mWorkPoolFragments = new WorkPoolFragment[strings.length];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings[position];
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (mWorkPoolFragments[position]==null) {
            mWorkPoolFragments[position] = WorkPoolFragment.newInstance(position);
        }
        return mWorkPoolFragments[position];
    }

}
