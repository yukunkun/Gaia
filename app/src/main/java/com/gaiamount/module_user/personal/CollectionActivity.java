package com.gaiamount.module_user.personal;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.DrawerBaseActivity;
import com.gaiamount.module_user.personal.collections.CollectionFragment;
import com.gaiamount.module_creator.fragment.CreatorWorkFrag;
import com.gaiamount.module_player.bean.VideoInfo;
import com.gaiamount.module_user.adapters.CollectionPagerAdapter;
import com.gaiamount.module_user.personal.collections.MaterialFrag;
import com.gaiamount.module_user.personal.collections.ScripeFrag;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * lhy 收藏界面
 */
public class CollectionActivity extends DrawerBaseActivity {

    public static final String UID = "uid";

    @Bind(R.id.creator_coll_tab)
    TabLayout mCreatorCollTab;
    @Bind(R.id.creator_coll_vp)
    ViewPager mCreatorCollVp;
    @Bind(R.id.title)
    TextView mTitle;

    //筛选的变量
    private int filterOption = 0;
    //排序的变量 默认为最新发布
    private int sortOption = 1;

    private FragmentStatePagerAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private long uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        parseIntent();

        //父类方法
        initHeaderView();
        //父类方法
        updateUserData();
        //父类方法
        initToolbar();

        //设置标题
        mTitle.setText(R.string.collection);

        //准备fragment
        mFragmentList.add(CreatorWorkFrag.newInstance(uid));
        mFragmentList.add(MaterialFrag.newInstance(uid));//素材收藏
        mFragmentList.add(CollectionFragment.newInstance(uid));//学院收藏
//        mFragmentList.add(ScripeFrag.newInstance(uid));

        mAdapter = new CollectionPagerAdapter(getSupportFragmentManager(), this,mFragmentList,uid);
        mCreatorCollVp.setAdapter(mAdapter);
        mCreatorCollVp.setOffscreenPageLimit(4);

        //关联
        mCreatorCollTab.setupWithViewPager(mCreatorCollVp);
        if (ScreenUtils.instance().isPanel()) {
            mCreatorCollTab.setTabMode(TabLayout.MODE_FIXED);
        } else {
            mCreatorCollTab.setTabMode(TabLayout.MODE_FIXED);
        }

    }

    public void parseIntent() {
        uid = getIntent().getLongExtra(UID, -1);
    }


    /**
     * 获取根据筛选项和排序项返回的数据
     * @param frag
     */
    public void getCollects(final CreatorWorkFrag frag) {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(CollectionActivity.class) {
            @Override
            public void parseJson(JSONObject response) {
                super.parseJson(response);
                JSONArray a = response.optJSONArray("a");
                List<VideoInfo> videoInfoList = GsonUtil.getInstannce().getGson().fromJson(a.toString(), new TypeToken<List<VideoInfo>>() {
                }.getType());
                //将数据传递给fragment
                frag.setVideoInfoList(videoInfoList);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                frag.onNetFinished();
            }
        };
        AccountApiHelper.getCollects(uid, filterOption, sortOption, 1, 100, CollectionActivity.this, jsonHttpResponseHandler);
    }
}
