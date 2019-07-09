package com.gaiamount.module_academy.academy_contribution;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_im.secret_chat.adapter.SecViewPagerAdapter;
import com.gaiamount.module_workpool.fragment.WorkPoolFragment;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.widgets.custom.MFlowLayout;

import java.util.ArrayList;

/*kun*/
public class ContributionActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private String[] strings;
    private ImageView imageViewChoice;
    private int mFilterPos=0;
    private int mSortPos = 0 ;
    private PopupWindow mPopupWindow;
    private SecViewPagerAdapter pagerAdapter;
    private String[] mFilterArray;
    private String[] mSortArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution);
        init();
        getInfo();
        setListener();
        setAdapter();

    }
    /*初始化*/
    private void init() {
        mFilterArray = getResources().getStringArray(R.array.academy_contribute);
        mSortArray = getResources().getStringArray(R.array.work_sort);
        mTabLayout = (TabLayout) findViewById(R.id.contribute_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.contribution_viewpager);
        imageViewChoice= (ImageView) findViewById(R.id.image_choice);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        imageViewChoice.setOnClickListener(this);
    }
    //展开popu的imageview
    @Override
    public void onClick(View v) {

        if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }else {
            showFilter(mFilterPos);
        }
    }

    private void showFilter(int selectedPos) {
        //Inflate 视图
        View view = View.inflate(ContributionActivity.this,R.layout.item_workpool_filter,null);
        MFlowLayout fl_filter = (MFlowLayout) view.findViewById(R.id.fl_filter);
        MFlowLayout fl_sort = (MFlowLayout) view.findViewById(R.id.fl_sort);

        //设置子view
        for (int i = 0; i< mFilterArray.length; i++) {

            TextView textView = (TextView) View.inflate(ContributionActivity.this, R.layout.tv_work_filter, null);
            textView.setText(mFilterArray[i]);
            textView.requestLayout();
            //设置点击事件
            textView.setOnClickListener(new MOnClick(i,textView));

            if (i==selectedPos) {
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_filter));
            }


            fl_filter.addView(textView);
        }

        //设置子view
        for (int i=0;i<mSortArray.length;i++) {
            TextView textView = (TextView) View.inflate(ContributionActivity.this, R.layout.tv_work_sort, null);
            textView.setText(mSortArray[i]);
            textView.requestLayout();

            textView.setOnClickListener(new MOnClick2(i,textView));
            if (i==mSortPos) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            }

            fl_sort.addView(textView);
        }
        //popuwindow显示
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));

        if (mPopupWindow !=null&&!mPopupWindow.isShowing()) {
            int width = ScreenUtils.instance().getWidth();
            mPopupWindow.showAsDropDown(imageViewChoice,-width,0);
        }

    }




    class MOnClick implements View.OnClickListener{

        private int postion;
        private TextView mTextView;

        public MOnClick(int pos,TextView textView) {

            postion = pos;
            mTextView = textView;
        }

        @Override
        public void onClick(View v) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mTextView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_filter));
            mFilterPos = postion;
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }

            mViewPager.setCurrentItem(postion);
        }
    }

    class MOnClick2 implements View.OnClickListener{

        private int postion;
        private TextView mTextView;

        public MOnClick2(int pos,TextView textView) {

            postion = pos;
            mTextView = textView;
        }

        @Override
        public void onClick(View v) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mTextView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            mSortPos = postion;
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            ContributeFragment contributeFragment = (ContributeFragment) pagerAdapter.getItem(mViewPager.getCurrentItem());
            contributeFragment.getInfo(postion,1);

        }

    }

    private void getInfo() {
        fragments = new ArrayList<>();

        strings = getApplicationContext().getResources().getStringArray(R.array.academy_contribute);
        for (int i = 0; i < strings.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(strings[i]));
        }

        for (int i = 0; i < strings.length; i++) {
            ContributeFragment contributeFragment=new ContributeFragment();
            Bundle bundle=new Bundle();
            bundle.putInt("c",i-1);
            contributeFragment.setArguments(bundle);
            fragments.add(contributeFragment);
        }

    }

    private void setAdapter() {
        pagerAdapter = new SecViewPagerAdapter(getSupportFragmentManager(),fragments,strings);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(12);
    }

    /*联动的监听*/
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


    //返回
    public void ContributeBack(View view) {
        finish();
    }
    //全局搜索
//    public void SearchGloab(View view) {
//        ActivityUtil.startGlobalSearchActivity(ContributionActivity.this,"",3);
//    }
}
