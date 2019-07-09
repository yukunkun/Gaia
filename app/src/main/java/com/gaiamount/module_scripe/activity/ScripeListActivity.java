package com.gaiamount.module_scripe.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_im.secret_chat.adapter.SecViewPagerAdapter;
import com.gaiamount.module_scripe.fragment.ScripeListFragment;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.widgets.custom.MFlowLayout;

import java.util.ArrayList;

public class ScripeListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageView;

    private String[] mFilterArray;
    private String[] mChooseArray;
    private String[] mBgArray;
    private String[] mSortArray;
    private int mFilterPos = 0 ;
    private int mSortBg = 0 ;
    private int mSortChoose = 0 ;
    private int mSortPos = 0 ;
    private PopupWindow mPopupWindow;
    private SecViewPagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragments=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripe_list);
        initString();
        init();
        initTolbar();
        setAdapter();
        setListener();
    }

    private void initString() {
        mFilterArray=getResources().getStringArray(R.array.scripe_list);
        mChooseArray=getResources().getStringArray(R.array.scripe_choose);
        mBgArray=getResources().getStringArray(R.array.scripe_bg);
        mSortArray=getResources().getStringArray(R.array.scripe_rato);
    }

    private void setAdapter() {
        pagerAdapter = new SecViewPagerAdapter(getSupportFragmentManager(),fragments,mFilterArray);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPopupWindow!=null&& mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }else {
                    showFilter(mFilterPos);
                }
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.setScrollPosition(position,0,false);
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.work_pool_tl);
        viewPager = (ViewPager) findViewById(R.id.work_pool_vp);
        imageView = (ImageView) findViewById(R.id.iv_filter_image);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tablayout设置值
        for (int i = 0; i < mFilterArray.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mFilterArray[i]));
        }

        for (int j = 0; j < mFilterArray.length; j++) {
            ScripeListFragment fragment=ScripeListFragment.newInstance(j);
           fragments.add(fragment);
        }

    }

    private void showFilter(int mFilterPos) {

        View view = View.inflate(ScripeListActivity.this,R.layout.item_scripe_filter,null);
        MFlowLayout fl_filter = (MFlowLayout) view.findViewById(R.id.fl_filter);
        MFlowLayout fl_bg = (MFlowLayout) view.findViewById(R.id.fl_bg);
        MFlowLayout fl_choose = (MFlowLayout) view.findViewById(R.id.fl_choose);
        MFlowLayout fl_sort = (MFlowLayout) view.findViewById(R.id.fl_sort);
        //第一个分类
        for (int i = 0; i < mFilterArray.length; i++) {
            TextView textView=(TextView) View.inflate(ScripeListActivity.this, R.layout.tv_work_filter, null);
            textView.setText(mFilterArray[i]);
            textView.requestFocus();

            textView.setOnClickListener(new MOnClick(i,textView));

            if(mFilterPos==i){
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_filter));
            }
            fl_filter.addView(textView);
        }
        //第二个分类
        for (int j = 0; j < mBgArray.length; j++) {
            TextView textView = (TextView) View.inflate(ScripeListActivity.this, R.layout.tv_work_sort, null);
            textView.setText(mBgArray[j]);
            textView.requestLayout();

            textView.setOnClickListener(new MOnClick2(j,textView));
            if (j==mSortBg) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            }

            fl_bg.addView(textView);
        }
        //第三个分类
        for (int k = 0; k < mChooseArray.length; k++) {
            TextView textView = (TextView) View.inflate(ScripeListActivity.this, R.layout.tv_work_sort, null);
            textView.setText(mChooseArray[k]);
            textView.requestLayout();

            textView.setOnClickListener(new MOnClick3(k,textView));
            if (k==mSortChoose) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            }

            fl_choose.addView(textView);
        }
        //第四个分类
        for (int l = 0; l < mSortArray.length; l++) {
            TextView textView = (TextView) View.inflate(ScripeListActivity.this, R.layout.tv_work_sort, null);
            textView.setText(mSortArray[l]);
            textView.requestLayout();

            textView.setOnClickListener(new MOnClick4(l,textView));
            if (l==mSortPos) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            }

            fl_sort.addView(textView);
        }



        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));

        if (mPopupWindow !=null&&!mPopupWindow.isShowing()) {
            int width = ScreenUtils.instance().getWidth();
            mPopupWindow.showAsDropDown(imageView,-width,0);
        }

        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));

    }


    private void initTolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class MOnClick implements View.OnClickListener {

        private TextView mTextView;
        private int postion;
        public MOnClick(int i, TextView textView) {
            postion=i;
            mTextView=textView;
        }

        @Override
        public void onClick(View v) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mTextView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_filter));
            mFilterPos = postion;
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            viewPager.setCurrentItem(mFilterPos);
        }

    }

    private class MOnClick2 implements View.OnClickListener {
        private int bgPostion;
        private TextView mTextView;
        public MOnClick2(int j, TextView textView) {
            bgPostion=j;
            mTextView=textView;
        }

        @Override
        public void onClick(View v) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mTextView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
             mSortBg= bgPostion;
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            ScripeListFragment videoMaterialFragment = (ScripeListFragment) pagerAdapter.getItem(viewPager.getCurrentItem());
            videoMaterialFragment.getInfo(mSortChoose, mSortBg, mSortPos);
        }
    }

    private class MOnClick3 implements View.OnClickListener {
        private int bgPostion;
        private TextView mTextView;
        public MOnClick3(int k, TextView textView) {
            bgPostion=k;
            mTextView=textView;
        }

        @Override
        public void onClick(View v) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mTextView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            mSortChoose = bgPostion;
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            ScripeListFragment videoMaterialFragment = (ScripeListFragment) pagerAdapter.getItem(viewPager.getCurrentItem());
            videoMaterialFragment.getInfo(mSortChoose, mSortBg, mSortPos);
        }
    }

    private class MOnClick4 implements View.OnClickListener {
        private int bgPostion;
        private TextView mTextView;
        public MOnClick4(int l, TextView textView) {
            bgPostion=l;
            mTextView=textView;
        }

        @Override
        public void onClick(View v) {

            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mTextView.setBackground(getResources().getDrawable(R.drawable.shape_tv_workpool_sort));
            mSortPos = bgPostion;
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }

            ScripeListFragment videoMaterialFragment = (ScripeListFragment) pagerAdapter.getItem(viewPager.getCurrentItem());

            videoMaterialFragment.getInfo(mSortChoose, mSortBg, mSortPos);
        }
    }
}
