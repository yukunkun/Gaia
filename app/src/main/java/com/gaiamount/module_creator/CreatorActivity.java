package com.gaiamount.module_creator;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.dialogs.GroupSortDialogFrag;
import com.gaiamount.module_creator.create_person.CreatePersonFragment;
import com.gaiamount.module_creator.fragment.CreatorBaseFrag;
import com.gaiamount.module_creator.fragment.CreatorGroupFrag;
import com.gaiamount.module_creator.fragment.OrganizationCreatorFrag;
import com.gaiamount.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *Created by LHY
 * 创作者界面，包含三个分页：个人，小组，机构（目前只有小组）
 */
public class CreatorActivity extends AppCompatActivity {

   /* @Bind(R.id.creator_filter)
    ImageView mCreatorFilter;*/
    private MAdapter mAdapter;
    private ViewPager mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        ButterKnife.bind(this);

        initToolbar();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.creator_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.creator_viewPager);
        mAdapter = new MAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        /*mCreatorFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });*/

    }

    @Override
    public void recreate() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment:mAdapter.mFragList) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
        super.recreate();
    }

    private void initToolbar() {
        tvTitle.setText(R.string.creator);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_creator);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_creator_search) {
                    ActivityUtil.startGlobalSearchActivity(CreatorActivity.this,null,2);
                }
                return false;
            }
        });
    }

    /**
     * 显示创作者筛选和排序
     */
    private void showFilter() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem==1) {
            GroupSortDialogFrag.newInstance(currentItem).show(getSupportFragmentManager(),"");
        }
    }

    /**
     * 执行排序
     * @param sortType 按照什么排序
     */
    public void doFilter(int sortType) {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem==1) {
            CreatorGroupFrag creatorGroupFrag = (CreatorGroupFrag) mAdapter.mFragList.get(currentItem);
            creatorGroupFrag.updateData(sortType);
        }
    }

    private class MAdapter extends FragmentStatePagerAdapter {
        public List<CreatorBaseFrag> mFragList;


        public MAdapter(FragmentManager fm) {
            super(fm);
            mFragList = new ArrayList<>();
            mFragList.add(CreatorGroupFrag.newInstance());
//            mFragList.add(PersonalCreatorFrag.newInstance());
            mFragList.add(CreatePersonFragment.newInstance());
            mFragList.add(OrganizationCreatorFrag.newInstance());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragList.get(position).getFragmentTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragList.get(position);
        }

        @Override
        public int getCount() {
            return mFragList.size();
        }
    }
}
