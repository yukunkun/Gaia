package com.gaiamount.module_creator.creater_circle.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.module_creator.creater_circle.fragment.AttentionFragment;
import com.gaiamount.module_creator.creater_circle.fragment.FansFragment;
import com.gaiamount.module_creator.creater_circle.fragment.GroupFragment;
import com.gaiamount.module_im.secret_chat.adapter.SecViewPagerAdapter;

import java.util.ArrayList;

/**
 *  Created by yukun on 16-7-27.
 */
public class CircleActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SecViewPagerAdapter secViewPagerAdapter;
    ArrayList<Fragment> fragments;
    private String[] strings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        init();
        initSet();
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        //设置适配器
        secViewPagerAdapter=new SecViewPagerAdapter(getSupportFragmentManager(),fragments,strings);
        mViewPager.setAdapter(secViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
    }

    private void init() {
        mTabLayout= (TabLayout) findViewById(R.id.circle_tablayout);
        mViewPager= (ViewPager) findViewById(R.id.circle_viewpager);
    }
    //初始化数据
    private void initSet() {
        strings=getApplicationContext().getResources().getStringArray(R.array.circle);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < 3; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(strings[i]));
        }
//        添加viewpager的fragment
        fragments=new ArrayList<>();
        AttentionFragment attentionFragment=new AttentionFragment();
        fragments.add(attentionFragment);
        FansFragment fansFragment=new FansFragment();
        fragments.add(fansFragment);
        GroupFragment groupFragment=new GroupFragment();
        fragments.add(groupFragment);
    }

    //设置双监听
    private void setListener() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTabLayout.setScrollPosition(position,0,false);
            }
        });
    }

    public void Circleback(View view) {
        finish();
    }
}
