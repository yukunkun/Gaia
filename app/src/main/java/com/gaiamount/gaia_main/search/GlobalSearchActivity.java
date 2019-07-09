package com.gaiamount.gaia_main.search;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_search.SearchApiHelper;
import com.gaiamount.gaia_main.search.beans.OnEventKey;
import com.gaiamount.gaia_main.search.beans.OnEventMaterialKey;
import com.gaiamount.gaia_main.search.beans.OnEventScripeKey;
import com.gaiamount.gaia_main.search.beans.SearchGroupResult;
import com.gaiamount.gaia_main.search.beans.SearchPersonResult;
import com.gaiamount.gaia_main.search.beans.SearchWorksResult;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GlobalSearchActivity extends AppCompatActivity
        implements SearchWorksFrag.OnWorkOptionClickListener,
        SearchGroupFrag.OnGroupOptionClickListener, SearchPersonFrag.OnPersonOptionClickListener {

    public static final int[] sPersonOpr = new int[]{0, 1, 2};
    public static final int[] sGroupOpr = new int[]{0, 1, 2};
    public static final int[] sWorkSType = new int[]{0, 2, 3};

    public static final String WORDS = "words";
    public static final String KIND = "kind";
    private static final int KIND_WORKS = 1;
    private static final int KIND_GROUP = 2;
    private static final int KIND_PERSONAL = 3;
    private static final int KIND_ACADEMY = 4;
    private static final int KIND_MMATERIAL = 0;
    private static final int KIND_SCRIPE = 5;
    public static final String KEY_WORDS = "KEY_WORDS";
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.btn_cancel)
    Button mBtnCancel;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private int mCurrentKind;
    private String mWords;
    private List<BaseFrag> mFragmentList;
    private String key_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_search);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        //获取传入的数据
        mCurrentKind = getIntent().getIntExtra(KIND, KIND_WORKS);
        mWords = getIntent().getStringExtra(WORDS);

        key_word = getIntent().getStringExtra(KEY_WORDS);
        if (mWords != null && !mWords.isEmpty()) {
            doSearch(mWords, mCurrentKind);
        }

        if(key_word!=null&&key_word.length()!=0){
            mEtSearch.setText(key_word);
        }

        //设置tablayout的模式
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //准备fragment
        mFragmentList = new ArrayList<>();
        SearchMaterialFragment searchMaterialFragment=SearchMaterialFragment.newInstance();
        mFragmentList.add(searchMaterialFragment);

        SearchWorksFrag searchWorksFrag = SearchWorksFrag.newInstance();
        mFragmentList.add(searchWorksFrag);
        SearchGroupFrag searchGroupFrag = SearchGroupFrag.newInstance();
        mFragmentList.add(searchGroupFrag);
        SearchPersonFrag searchPersonFrag = SearchPersonFrag.newInstance();
        mFragmentList.add(searchPersonFrag);
        //添加学院搜索  kun
        SearchAcademyFrag searchAcademyFrag=SearchAcademyFrag.newInstance();
        mFragmentList.add(searchAcademyFrag);

        //添加剧本搜索  kun
//        SearchScripeFrag searchScripeFrag=SearchScripeFrag.newInstance();
//        mFragmentList.add(searchScripeFrag);
        //设置监听fragment
        searchWorksFrag.setOnWorkOptionClickListener(this);
        searchGroupFrag.setOnGroupOptionClickListener(this);
        searchPersonFrag.setOnPersonOptionClickListener(this);
        //设置viewpager适配器
        mViewPager.setAdapter(new MAdapter(getSupportFragmentManager(), mFragmentList));

        //关联
        mTabLayout.setupWithViewPager(mViewPager);

        setListener();

        mEtSearch.requestFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void doSearch(String key, int kind) {
        if (key == null || TextUtils.isEmpty(key)) {
            return;
        }

        switch (kind) {
            case KIND_WORKS:
                SearchApiHelper.searchWorks(key, work_stype, 1, 50, this);//0代表默认
                break;
            case KIND_GROUP:
                SearchApiHelper.searchGroups(key, group_opr, 1, 50, this);
                break;
            case KIND_PERSONAL:
                SearchApiHelper.searchPersons(key, person_opr, this);//0代表默认
                break;
            case KIND_ACADEMY:
                //传值给searchAcademtFrag
                EventBus.getDefault().post(new OnEventKey(key));
                break;
            case KIND_MMATERIAL:
                //传值给searchMaterialFrag
                EventBus.getDefault().post(new OnEventMaterialKey(key));
                break;
            case KIND_SCRIPE:
                //传值给searchScripeFrag
                EventBus.getDefault().post(new OnEventScripeKey(key));
                break;
        }
    }

    /**
     * 订阅
     *
     * @param list
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSearchWorks(List<SearchWorksResult> list) {
        mFragmentList.get(0).update(list);
    }

    /**
     * 订阅
     *
     * @param list
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSearchGroup(List<SearchGroupResult> list) {
        mFragmentList.get(1).update(list);
    }

    /**
     * 订阅
     *
     * @param list
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSearchPerson(List<SearchPersonResult> list) {
        mFragmentList.get(2).update(list);
    }

    private void setListener() {
        mBtnCancel.setOnClickListener(cancelSearchListener);
        mViewPager.setCurrentItem(mCurrentKind);
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        mEtSearch.setOnEditorActionListener(mOnEditorActionListener);
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //执行搜索
                String key = v.getText().toString().trim();
                doSearch(key, mCurrentKind);

                UIUtils.hideSoftInputMethod(GlobalSearchActivity.this, mEtSearch);

                //显示下部分内容
                mTabLayout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);

                return true;
            }
            return false;
        }

    };

    private View.OnClickListener cancelSearchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case KIND_WORKS:
                    mCurrentKind = KIND_WORKS;
                    break;
                case KIND_GROUP:
                    mCurrentKind = KIND_GROUP;
                    break;
                case KIND_PERSONAL:
                    mCurrentKind = KIND_PERSONAL;
                    break;
                case KIND_ACADEMY:
                    mCurrentKind = KIND_ACADEMY;
                    break;
                case KIND_MMATERIAL:
                    mCurrentKind = KIND_MMATERIAL;
                    break;
                case KIND_SCRIPE:
                    mCurrentKind = KIND_SCRIPE;
                    break;
                default:
                    break;
            }
            String key = mEtSearch.getText().toString().trim();
            if (key != null && !TextUtils.isEmpty(key)) {
                doSearch(key, mCurrentKind);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //------作品类目的筛选///
    /**
     * 作品类的筛选类型：
     * 0默认 2 最多播放 3 最多收藏
     */
    private int work_stype = 0;

    @Override
    public void onWorkOptionClickListener(int stype) {
        mCurrentKind = KIND_WORKS;
        work_stype = stype;
        doSearch(mEtSearch.getText().toString().trim(), mCurrentKind);
    }

    //------------//

    //-------小组的筛选------//
    /**
     * 小组筛选选项，
     * 0默认 1最多创作 2最多成员 3最新创建
     */
    private int group_opr = 0;

    @Override
    public void onGroupOptionClickListener(int opr) {
        mCurrentKind = KIND_GROUP;
        group_opr = opr;
        doSearch(mEtSearch.getText().toString().trim(), mCurrentKind);
    }

    //---------个人筛选项-----
    private int person_opr = 0;

    @Override
    public void onPersonOptionClickListener(int opr) {
        mCurrentKind = KIND_PERSONAL;
        person_opr = opr;

        doSearch(mEtSearch.getText().toString().trim(), mCurrentKind);
    }

    //--------//

    class MAdapter extends FragmentStatePagerAdapter {

        private List<BaseFrag> mFragmentList;

        public MAdapter(FragmentManager fm, List<BaseFrag> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.search)[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
