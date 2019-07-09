package com.gaiamount.module_creator.sub_module_mygroup;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * lhy
 * 我的小组
 */
public class MyGroupActivity extends AppCompatActivity {

    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //标题
        mTitle.setText(R.string.activity_title_my_group);

        //菜单
        mToolbar.inflateMenu(R.menu.my_group);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId==R.id.action_create_group) {
                    ActivityUtil.startAddGroupActivity(MyGroupActivity.this);
                }
                return true;
            }
        });

        //联网获取数据
        getMyGroupData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getMyGroupData() {
        //获取数据
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(MyGroupFragment.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                Gson gson = GsonUtil.getInstannce().getGson();
                JSONObject jsonObject = response.optJSONObject("o");
                ArrayList<MyGroupInfo> myGroupList = gson.fromJson(jsonObject.optJSONArray("myGroups").toString(),
                        new TypeToken<List<MyGroupInfo>>() {
                        }.getType());
                ArrayList<MyGroupInfo> myControllGroupsList = gson.fromJson(jsonObject.optJSONArray("myControllGroups").toString(),
                        new TypeToken<List<MyGroupInfo>>() {
                        }.getType());
                ArrayList<MyGroupInfo> joinGroupsList = gson.fromJson(jsonObject.optJSONArray("joinGroups").toString(),
                        new TypeToken<List<MyGroupInfo>>() {
                        }.getType());
                Map<Integer, ArrayList<MyGroupInfo>> map = new HashMap<>();
                map.put(0, myGroupList);
                map.put(1, myControllGroupsList);
                map.put(2, joinGroupsList);
                onEventResponse(map);

            }
        };
        GroupApiHelper.getMyGroup(GaiaApp.getUserInfo().id, this, handler);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventResponse(Map<Integer, ArrayList<MyGroupInfo>> map) {
        //准备Fragment
        mFragmentList = new ArrayList<>();
        mFragmentList.add(MyGroupFragment.newInstance(map.get(0),0));
        mFragmentList.add(MyGroupFragment.newInstance(map.get(1),1));
        mFragmentList.add(MyGroupFragment.newInstance(map.get(2),2));

        //viewpager适配器
        mViewPager.setAdapter(new MAdapter(getSupportFragmentManager()));

        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    class MAdapter extends FragmentStatePagerAdapter {

        private final String[] mStrings;

        public MAdapter(FragmentManager fm) {
            super(fm);
            mStrings = getResources().getStringArray(R.array.my_group);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStrings[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mStrings.length;
        }
    }
}
