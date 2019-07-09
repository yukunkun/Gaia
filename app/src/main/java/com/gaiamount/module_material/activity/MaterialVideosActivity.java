package com.gaiamount.module_material.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.search.beans.OnEventMaterialKey;
import com.gaiamount.module_academy.academy_contribution.ContributeFragment;
import com.gaiamount.module_im.secret_chat.adapter.SecViewPagerAdapter;
import com.gaiamount.module_material.adapters.VideoMaterialAdapter;
import com.gaiamount.module_material.bean.EventPosition;
import com.gaiamount.module_material.fragment.VideoMaterialFragment;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.widgets.custom.MFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MaterialVideosActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private String[] strings;
    private ImageView imageViewChoice;
    private int mFilterPos=0;
    private int mSortPos = 0 ;
    private int mPricePos=0;
    private int mFormatPos=0;
    private int mRatioPos=0;
    private PopupWindow mPopupWindow;
    private String[] mFilterArray;
    private String[] mSortArray;
    private TextView mTextView;
    private  SecViewPagerAdapter pagerAdapter;
    private String[] mFilterPrice;
    private String[] mFilterFormat;
    private String[] mFilterRatio;
    private int positions=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_all);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            positions= intent.getExtras().getInt("position");
        }
        init();
        getInfo();
        setListener();
        setAdapter();
        if(positions !=0){
            mViewPager.setCurrentItem(positions);
        }
    }

    /*初始化*/
    private void init() {
        mTextView = (TextView) findViewById(R.id.material_title);
        mFilterArray = getResources().getStringArray(R.array.material_video);
        mSortArray = getResources().getStringArray(R.array.material_more);
        mFilterPrice = getResources().getStringArray(R.array.material_price);
        mFilterFormat = getResources().getStringArray(R.array.material_format);
        mFilterRatio = getResources().getStringArray(R.array.material_ratio);
        mTabLayout = (TabLayout) findViewById(R.id.material_all_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.material_viewpager);
        imageViewChoice= (ImageView) findViewById(R.id.image_choice);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        imageViewChoice.setOnClickListener(this);
        mTextView.setText("视频素材");
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
        View view = View.inflate(MaterialVideosActivity.this,R.layout.item_material_filter,null);
        MFlowLayout fl_filter = (MFlowLayout) view.findViewById(R.id.fl_filter);
        MFlowLayout fl_sort = (MFlowLayout) view.findViewById(R.id.fl_sort);
        MFlowLayout fl_price = (MFlowLayout) view.findViewById(R.id.fl_price);
        MFlowLayout fl_format = (MFlowLayout) view.findViewById(R.id.fl_format);
        MFlowLayout fl_ratio = (MFlowLayout) view.findViewById(R.id.fl_ratio);

        //设置子view
        for (int i = 0; i< mFilterArray.length; i++) {

            TextView textView = (TextView) View.inflate(MaterialVideosActivity.this, R.layout.tv_work_filter, null);
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
            TextView textView = (TextView) View.inflate(MaterialVideosActivity.this, R.layout.tv_work_sort, null);
            textView.setText(mSortArray[i]);
            textView.requestLayout();

            textView.setOnClickListener(new MOnClick2(i,textView));
            if (i==mSortPos) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            }

            fl_sort.addView(textView);
        }
        //设置子view
        for (int i=0;i<mFilterPrice.length;i++) {
            TextView textView = (TextView) View.inflate(MaterialVideosActivity.this, R.layout.tv_work_sort, null);
            textView.setText(mFilterPrice[i]);
            textView.requestLayout();

            textView.setOnClickListener(new MOnClick3(i,textView));
            if (i==mPricePos) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            }

            fl_price.addView(textView);
        }
        //设置子view
        for (int i=0;i<mFilterFormat.length;i++) {
            TextView textView = (TextView) View.inflate(MaterialVideosActivity.this, R.layout.tv_work_sort, null);
            textView.setText(mFilterFormat[i]);
            textView.requestLayout();

            textView.setOnClickListener(new MOnClick4(i,textView));
            if (i==mFormatPos) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            }

            fl_format.addView(textView);
        }
        //设置子view
        for (int i=0;i<mFilterRatio.length;i++) {
            TextView textView = (TextView) View.inflate(MaterialVideosActivity.this, R.layout.tv_work_sort, null);
            textView.setText(mFilterRatio[i]);
            textView.requestLayout();

            textView.setOnClickListener(new MOnClick5(i,textView));
            if (i==mRatioPos) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            }

            fl_ratio.addView(textView);
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
            VideoMaterialFragment videoMaterialFragment = (VideoMaterialFragment) pagerAdapter.getItem(mViewPager.getCurrentItem());
            videoMaterialFragment.getInfo(mFilterPos,mSortPos,mPricePos,mFormatPos,mRatioPos);


        }

    }
    class MOnClick3 implements View.OnClickListener{

        private int postion;
        private TextView mTextView;

        public MOnClick3(int pos,TextView textView) {

            postion = pos;
            mTextView = textView;
        }

        @Override
        public void onClick(View v) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mTextView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            mPricePos = postion;
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            VideoMaterialFragment videoMaterialFragment = (VideoMaterialFragment) pagerAdapter.getItem(mViewPager.getCurrentItem());
            videoMaterialFragment.getInfo(mFilterPos,mSortPos,mPricePos,mFormatPos,mRatioPos);

        }

    }
    class MOnClick4 implements View.OnClickListener{

        private int postion;
        private TextView mTextView;

        public MOnClick4(int pos,TextView textView) {

            postion = pos;
            mTextView = textView;
        }

        @Override
        public void onClick(View v) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mTextView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            mFormatPos = postion;
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            VideoMaterialFragment videoMaterialFragment = (VideoMaterialFragment) pagerAdapter.getItem(mViewPager.getCurrentItem());
            videoMaterialFragment.getInfo(mFilterPos,mSortPos,mPricePos,mFormatPos,mRatioPos);

        }

    }
    class MOnClick5 implements View.OnClickListener{

        private int postion;
        private TextView mTextView;

        public MOnClick5(int pos,TextView textView) {

            postion = pos;
            mTextView = textView;
        }

        @Override
        public void onClick(View v) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mTextView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            mRatioPos = postion;
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            VideoMaterialFragment videoMaterialFragment = (VideoMaterialFragment) pagerAdapter.getItem(mViewPager.getCurrentItem());
            videoMaterialFragment.getInfo(mFilterPos,mSortPos,mPricePos,mFormatPos,mRatioPos);
        }

    }

    private void getInfo() {
        fragments = new ArrayList<>();

        strings = getApplicationContext().getResources().getStringArray(R.array.material_video);
        for (int i = 0; i < strings.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(strings[i]));
        }

        for (int i = 0; i < strings.length; i++) {
            VideoMaterialFragment contributeFragment=VideoMaterialFragment.getInstance(i);
            fragments.add(contributeFragment);
        }

        //判断跳转的是那个fragment

    }

    private void setAdapter() {
        pagerAdapter = new SecViewPagerAdapter(getSupportFragmentManager(),fragments,strings);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
//        mViewPager.setOffscreenPageLimit(strings.length);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void MaterialAllBack(View view) {
        finish();
    }
}
