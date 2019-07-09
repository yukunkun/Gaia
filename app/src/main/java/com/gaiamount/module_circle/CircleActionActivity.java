package com.gaiamount.module_circle;

import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.module_circle.fragment.AttentionFragment;
import com.gaiamount.module_circle.fragment.GroupsFragment;
import com.gaiamount.module_im.secret_chat.adapter.SecViewPagerAdapter;

import java.util.ArrayList;

public class CircleActionActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] mFilterArray;
    private SecViewPagerAdapter viewPagerAdapter;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle2);
        init();
        setInfo();
        setListener();
    }

    private void init() {
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    }

    private void setListener() {
        //设置联动
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.setScrollPosition(position,0,true);
            }
        });
    }

    private void setInfo() {
        mFilterArray = new String[]{getString(R.string.attent),getString(R.string.hot)};
        fragments = new ArrayList<>();
        //tablayout设置值
        for (int i = 0; i < mFilterArray.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mFilterArray[i]));
        }

        //添加fragment实例
        AttentionFragment attentionFragment=new AttentionFragment();
        fragments.add(attentionFragment);
        GroupsFragment groupsFragment=new GroupsFragment();
        fragments.add(groupsFragment);
        //设置viewpager的fragment数据
        viewPagerAdapter=new SecViewPagerAdapter(getSupportFragmentManager(),fragments,mFilterArray);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void Back(View view) {
        finish();
    }
}
