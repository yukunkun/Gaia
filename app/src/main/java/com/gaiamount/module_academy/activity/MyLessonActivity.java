package com.gaiamount.module_academy.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gaiamount.R;

import com.gaiamount.module_academy.fragment.MyLessonFragment;
import com.gaiamount.module_im.secret_chat.adapter.SecViewPagerAdapter;

import java.util.ArrayList;

/**
 * kun 我的课表
 */
public class MyLessonActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SecViewPagerAdapter secViewPagerAdapter;
    ArrayList<Fragment> fragments=new ArrayList<>();
    private String[] strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lesson);
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
        mTabLayout= (TabLayout) findViewById(R.id.academy_tablayout);
        mViewPager= (ViewPager) findViewById(R.id.academy_viewpager);
    }
    //初始化数据
    private void initSet() {
        strings=getApplicationContext().getResources().getStringArray(R.array.academy_lesson);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < 2; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(strings[i]));
        }
        //传递t值,网络请求
        MyLessonFragment lessonFragment=new MyLessonFragment();
        fragments.add(lessonFragment);
        Bundle bundle=new Bundle();
        bundle.putInt("t",1);
        lessonFragment.setArguments(bundle);
        MyLessonFragment lessonFragment1=new MyLessonFragment();
        fragments.add(lessonFragment1);
        Bundle bundle1=new Bundle();
        bundle1.putInt("t",2);
        lessonFragment1.setArguments(bundle1);
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

    public void MyLessonBack(View view) {
        finish();
    }
}
