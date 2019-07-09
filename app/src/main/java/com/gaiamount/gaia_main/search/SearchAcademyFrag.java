package com.gaiamount.gaia_main.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_search.SearchApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.search.adapter.SearchAcademyAdapter;
import com.gaiamount.gaia_main.search.beans.OnEventKey;
import com.gaiamount.module_academy.adapter.MixAdapter;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.util.network.MJsonHttpResponseHandler;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-8-23.
 */
public class SearchAcademyFrag extends BaseFrag implements View.OnClickListener {


    private LinearLayout mRelevancy;
    private LinearLayout mLayoutMosrPer;
    private LinearLayout mLayoutMosrCol;
    private RecyclerView mRecyclerView;
    private TextView textViewEmpty;
    private int opr=0,pi=1,t=1;//opr 的筛选０默认　１最新发布　２最多浏览　３最多学员　４最多收藏　５　最多评论
    private long uid;
    private  String key="";
    private ArrayList<MixInfo> mixInfos=new ArrayList<>();
    private SearchAcademyAdapter searchAdapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void update(List<?> list) {

    }

    public static SearchAcademyFrag newInstance() {
        Bundle args = new Bundle();
        SearchAcademyFrag fragment = new SearchAcademyFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventKey(OnEventKey event) {
        key=event.key;
        pi=1;
        opr=0;
        mixInfos.clear();
        changeStyle(mRelevancy);
        getInfo();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.search_academy_fragment, null);
        init(inflate);
        uid= GaiaApp.getAppInstance().getUserInfo().id;
        setListener();
        getInfo();
        setAdapter();
        return inflate;
    }

    private void init(View inflate) {
        gridLayoutManager = new GridLayoutManager(getContext(),1);
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRelevancy = (LinearLayout) inflate.findViewById(R.id.relevancy);
        mLayoutMosrPer = (LinearLayout) inflate.findViewById(R.id.most_person);
        mLayoutMosrCol = (LinearLayout) inflate.findViewById(R.id.most_collection);
        textViewEmpty= (TextView) inflate.findViewById(R.id.empty_hint);
        changeStyle(mRelevancy);

    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(SearchAcademyFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
                if(textViewEmpty.getVisibility()==View.VISIBLE){
                    textViewEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                textViewEmpty.setVisibility(View.VISIBLE);
            }
        };

        SearchApiHelper.searchAcademy(opr,pi,t,uid,key,getContext(),handler);
    }

    private void parasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            MixInfo mixInfo=new MixInfo();
            JSONObject object = a.optJSONObject(i);
            mixInfo.setLearningCount(object.optInt("learningCount"));
            mixInfo.setAuthor(object.optString("auther"));
            mixInfo.setAvatar(object.optString("avatar"));
            mixInfo.setCover(object.optString("cover"));
            mixInfo.setAllowFree(object.optInt("allowFree"));
            mixInfo.setPrice(object.optLong("price"));
            mixInfo.setId(object.optInt("id"));
            mixInfo.setType(object.optInt("type"));//1,0,2,图文,视频,直播
            mixInfo.setName(object.optString("name"));
            mixInfo.setChaptCount(object.optInt("chapterCount"));
            mixInfo.setNickName(object.optString("nickName"));
            mixInfo.setPlayCount(object.optInt("playCount"));
            mixInfo.setBrowseCount(object.optInt("browseCount"));
            mixInfo.setGrade(object.optInt("grade"));
            mixInfo.setHourCount(object.optInt("hourCount"));
            mixInfo.setProprity(object.optInt("proprity"));
            JSONObject jsonObject = object.optJSONObject("createTime");
            Long time = jsonObject.optLong("time");
            mixInfo.setTime(time);
            mixInfos.add(mixInfo);
        }
        searchAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        searchAdapter = new SearchAcademyAdapter(getContext(),mixInfos);
        mRecyclerView.setAdapter(searchAdapter);
    }

    private void setListener() {
        mRelevancy.setOnClickListener(this);
        mLayoutMosrPer.setOnClickListener(this);
        mLayoutMosrCol.setOnClickListener(this);

        //加载更多
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == gridLayoutManager.getItemCount() - 1) {
                    //加载更多
                    pi++;
                    getInfo();
                    searchAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //搜索选项
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relevancy:
                changeStyle(mRelevancy);
                mixInfos.clear();
                opr=0;
                pi=1;
                getInfo();

                break;
            case R.id.most_person:
                changeStyle(mLayoutMosrPer);
                mixInfos.clear();
                opr=3;
                pi=1;
                getInfo();

                break;
            case R.id.most_collection:
                changeStyle(mLayoutMosrCol);
                mixInfos.clear();
                opr=4;
                pi=1;
                getInfo();

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       EventBus.getDefault().unregister(this);
    }
}
