package com.gaiamount.gaia_main.search.old;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SearchView;

import com.gaiamount.R;
import com.gaiamount.apis.api_works.WorksApi;
import com.gaiamount.module_player.bean.VideoInfo;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.UIUtils;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.NetworkUtils;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by LHY
 * 搜索界面
 */
public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<VideoInfo> videoInfoList = new ArrayList<>();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolBar();

        initViews();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d(SearchActivity.class,"Event:"+event.getAction());
        return super.onTouchEvent(event);

    }

    private void initViews() {
//        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        viewPager = (ViewPager) findViewById(R.id.view_pager);
        recyclerView = (RecyclerView) findViewById(R.id.search_list);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsRelative(0,0);
        setSupportActionBar(toolbar);

        searchView =  (SearchView) findViewById(R.id.search_works);
        if(searchView!=null) {
            searchView.setIconifiedByDefault(false);//自动打开searchView
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //联网请求数据
                    getData(SearchActivity.this,query,0,0,0,1,100);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            //开启输入法
            UIUtils.showSoftInputMethod(this);
        }


    }

    public void getData(final Context context, String key, int wtype, int ptyep, int stype, int pi, int ps) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wtype",wtype);//所有作品
            jsonObject.put("ptype",ptyep);//作品属性筛选
            jsonObject.put("stype",stype);//作品统计筛选
            jsonObject.put("key",key);
            jsonObject.put("pi",pi);
            jsonObject.put("ps",ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //显示加载动画
        UIUtils.displayCustomProgressDialog(context,getString(R.string.searching),true);
        NetworkUtils.post(context, WorksApi.SEARCH_URL,jsonObject,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("SearchActivity","搜索作品的联网请求成功"+response.toString());
                if(response.optInt("b")==1) {
                    videoInfoList = GsonUtil.getInstannce().getGson().fromJson(response.optJSONArray("a").toString(),new TypeToken<List<VideoInfo>>(){}.getType());
                    //布局管理器
                    LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.addItemDecoration(new RecyclerViewDivider(SearchActivity.this,LinearLayoutManager.HORIZONTAL,8,R.color.gray_line));
                    //设置适配器
                    recyclerView.setAdapter(new SearchAdapter(SearchActivity.this,videoInfoList));
//                    if(viewPager !=null&& tabLayout !=null) {
//
//                        viewPager.setAdapter(new SearchAdapter(getSupportFragmentManager(),videoInfoList));
//                        tabLayout.setupWithViewPager(viewPager);
//                    }
                }

                //隐藏输入法
                UIUtils.hideSoftInputMethod(SearchActivity.this,searchView);
                //隐藏加载动画
                UIUtils.displayCustomProgressDialog(context,getString(R.string.searching),false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("SearchActivity","搜索作品的联网请求失败"+statusCode+"--"+throwable.toString());
                UIUtils.hideSoftInputMethod(SearchActivity.this,searchView);
                //隐藏加载动画
                UIUtils.displayCustomProgressDialog(context,getString(R.string.searching),false);
            }
        });
    }

}
