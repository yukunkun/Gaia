package com.gaiamount.module_workpool;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_workpool.adapters.WorkPoolFragmentAdapter;
import com.gaiamount.module_workpool.fragment.WorkPoolFragment;
import com.gaiamount.receivers.StateBroadcastReceiver;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.widgets.custom.MFlowLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorkPoolActivity extends AppCompatActivity {

    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.work_pool_tl)
    TabLayout mWorkPoolTl;
    @Bind(R.id.iv_filter)
    ImageView mIV_Filter;
    @Bind(R.id.work_pool_vp)
    ViewPager mWorkPoolVp;

    private String[] mFilterArray;
    private String[] mSortArray;
    private int mFilterPos = 0 ;
    private int mSortPos = 0 ;
    private PopupWindow mPopupWindow;
    private WorkPoolFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_pool);
        ButterKnife.bind(this);

        //初始化filterArrary
        mFilterArray = getResources().getStringArray(R.array.work_pool);
        mSortArray = getResources().getStringArray(R.array.work_sort);

        mToolbar.setTitle("");
        mTitle.setText(getTitle());
        setSupportActionBar(mToolbar);

        //设置适配器，并与tablayout关联
        if (mWorkPoolVp != null && mWorkPoolTl != null) {
            //设置tab的模式
            mWorkPoolTl.setTabMode(TabLayout.MODE_SCROLLABLE);

            //适配器
            mAdapter = new WorkPoolFragmentAdapter(this, getSupportFragmentManager());
            mWorkPoolVp.setAdapter(mAdapter);
            //关联两者getVideoInfo
            mWorkPoolTl.setupWithViewPager(mWorkPoolVp);
            mWorkPoolVp.setOffscreenPageLimit(3);
        }

        mIV_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }else {
                    showFilter(mFilterPos);
                }
            }
        });
    }


    private void showFilter(int selectedPos) {
        //Inflate 视图
        View view = View.inflate(WorkPoolActivity.this,R.layout.item_workpool_filter,null);
        MFlowLayout fl_filter = (MFlowLayout) view.findViewById(R.id.fl_filter);
        MFlowLayout fl_sort = (MFlowLayout) view.findViewById(R.id.fl_sort);
//        TextView  textViews= (TextView) view.findViewById(R.id.textviews_alpha);
//        textViews.getBackground().setAlpha(50);
        //设置子view
        for (int i = 0; i< mFilterArray.length; i++) {

            TextView textView = (TextView) View.inflate(WorkPoolActivity.this, R.layout.tv_work_filter, null);
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
            TextView textView = (TextView) View.inflate(WorkPoolActivity.this, R.layout.tv_work_sort, null);
            textView.setText(mSortArray[i]);
            textView.requestLayout();

            textView.setOnClickListener(new MOnClick2(i,textView));
            if (i==mSortPos) {
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
            mPopupWindow.showAsDropDown(mIV_Filter,-width,0);
        }

        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
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

            mWorkPoolVp.setCurrentItem(postion);
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
            WorkPoolFragment workPoolFragment = (WorkPoolFragment) mAdapter.getItem(mWorkPoolVp.getCurrentItem());
            workPoolFragment.getVideoInfo(postion);

        }

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
//            mPopupWindow.dismiss();
//            return true;
//        }
//        return super.dispatchTouchEvent(ev);
//
//    }




    @Override
    public void onBackPressed() {
        if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_pool, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_search:
                ActivityUtil.startGlobalSearchActivity(this,null,1);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

/*-------------kun------------*/
    StateBroadcastReceiver receiver=new StateBroadcastReceiver();//实例化广播
    public String tag=null;
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter=new IntentFilter();//广播的注册
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    //广播的回传值
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tag=intent.getStringExtra("tag");
        mAdapter.notifyDataSetChanged();

    }
}
