package com.gaiamount.module_user.person_creater;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.module_im.secret_chat.adapter.SecViewPagerAdapter;
import com.gaiamount.module_user.person_creater.fragments.MaterialFrag;
import com.gaiamount.module_user.person_creater.fragments.ScriptFrag;
import com.gaiamount.module_user.person_creater.fragments.WorksFrag;
import com.gaiamount.module_user.person_creater.fragments.CreateAcademyFrag;

import java.util.ArrayList;

/*kun*/
public class PersonActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SecViewPagerAdapter secViewPagerAdapter;
    ArrayList<Fragment> fragments=new ArrayList<>();
    private String[] strings;
    private long mUid;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        mUid = getIntent().getLongExtra("uid", -1);
        position=getIntent().getIntExtra("position",0);
        init();
        initSet();
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        secViewPagerAdapter=new SecViewPagerAdapter(getSupportFragmentManager(),fragments,strings);
        mViewPager.setAdapter(secViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);
    }

    private void init() {
        mTabLayout= (TabLayout) findViewById(R.id.creater_tablayout);
        mViewPager= (ViewPager) findViewById(R.id.create_viewpager);
    }
    //初始化数据
    private void initSet() {
        strings=getApplicationContext().getResources().getStringArray(R.array.create_person);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < strings.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(strings[i]));
        }

        WorksFrag worksFrag=WorksFrag.getInstance(mUid);
        fragments.add(worksFrag);
        MaterialFrag materialFrag=MaterialFrag.getInstance(mUid);
        fragments.add(materialFrag);
        CreateAcademyFrag createAcademyFrag=CreateAcademyFrag.getInstance(mUid);
        fragments.add(createAcademyFrag);
//        ScriptFrag scriptFrag=ScriptFrag.getInstance(mUid);
//        fragments.add(scriptFrag);

    }

    //设置双监听
    private void setListener() {
        mViewPager.setCurrentItem(position);
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
    public void CreateBack(View view) {
        finish();
    }
}
