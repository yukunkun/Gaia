package com.gaiamount.gaia_main.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.gaia_main.search.adapter.SearchScripeAdapter;
import com.gaiamount.gaia_main.search.beans.OnEventKey;
import com.gaiamount.gaia_main.search.beans.OnEventScripeKey;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-10-24.
 */
public class SearchScripeFrag extends BaseFrag implements View.OnClickListener {

    private String key;
    private RecyclerView mRecyclerView;
    private LinearLayout mLayoutMosrPer;
    private LinearLayout mLayoutMosrCol;
    private TextView textViewEmpty;
    private LinearLayout mRelevancy;
    private int sc=0;    //       (0全部 1爱情 …17其他)
    private int bg=0;    //背景 	0全部 1现代 …5其它
    private int se=0;    //筛选 	0全部 1已完稿 6推荐
    private int opr=0;  //排序 	0默认 1最新 … 4最多评论
    private int pi=1;

    private ArrayList<ScripeInfo> scripeInfos=new ArrayList<>();
    private SearchScripeAdapter adapter;

    @Override
    public void update(List<?> list) {

    }

    public static SearchScripeFrag newInstance() {
        Bundle args = new Bundle();
        SearchScripeFrag fragment = new SearchScripeFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventKey(OnEventScripeKey event) {
        key=event.key;
        pi=1;
        opr=0;
        scripeInfos.clear();
        changeStyle(mRelevancy);
        getInfo();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.search_scripe_item, null);
        init(inflate);
        getInfo();
        setAdapter();
        setListener();
        return inflate;
    }



    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(SearchScripeFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parseJsons(response);
            }
        };

        ScriptApiHelper.getGloableSearch(sc,bg,se,opr,pi,8,key,getContext(),handler);
    }

    private void parseJsons(JSONObject response) {

        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject object = a.optJSONObject(i);
            ScripeInfo scripeInfo=new ScripeInfo();
            scripeInfo.setNickName(object.optString("nickName"));
            scripeInfo.setIntroduce(object.optString("introduce"));
            scripeInfo.setAvatar(object.optString("avatar"));
            scripeInfo.setType(object.optString("type"));
            scripeInfo.setTitle(object.optString("title"));
            scripeInfo.setSpace(object.optInt("space"));  // 1 长篇 0 短篇
            scripeInfo.setBrowserCount(object.optInt("browserCount"));
            scripeInfo.setSid(object.optInt("sid"));
            scripeInfo.setOutline(object.optString("outline"));
            scripeInfo.setIsFree(object.optInt("isFree"));
            scripeInfo.setPrice(object.optInt("price"));
            scripeInfo.setState(object.optInt("state"));  // 1 完结 0 连载
            scripeInfo.setId(object.optLong("id"));
            scripeInfo.setCategory(object.optInt("category"));
            scripeInfo.setCover(object.optString("cover"));
            scripeInfo.setCollectCount(object.optInt("collectCount"));
            scripeInfo.setCommentCount(object.optInt("commentCount"));
//            JSONObject jsonObject = object.optJSONObject("time");
//            scripeInfo.setUpDateTime(jsonObject.optString("time"));
            scripeInfos.add(scripeInfo);
        }
        adapter.notifyDataSetChanged();
    }


    private void init(View inflate) {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRelevancy = (LinearLayout) inflate.findViewById(R.id.relevancy);
        mLayoutMosrPer = (LinearLayout) inflate.findViewById(R.id.most_person);
        mLayoutMosrCol = (LinearLayout) inflate.findViewById(R.id.most_collection);
        textViewEmpty = (TextView) inflate.findViewById(R.id.empty_hint);

        changeStyle(mRelevancy);

    }
    private void setAdapter() {
        adapter = new SearchScripeAdapter(getContext(),scripeInfos);
        mRecyclerView.setAdapter(adapter);
    }
    private void setListener() {
        mRelevancy.setOnClickListener(this);
        mLayoutMosrPer.setOnClickListener(this);
        mLayoutMosrCol.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relevancy:
                changeStyle(mRelevancy);
                scripeInfos.clear();
                opr=0;
                pi=1;
                getInfo();

                break;
            case R.id.most_person:
                changeStyle(mLayoutMosrPer);
                scripeInfos.clear();
                opr=3;
                pi=1;
                getInfo();

                break;
            case R.id.most_collection:
                changeStyle(mLayoutMosrCol);
                scripeInfos.clear();
                opr=4;
                pi=1;
                getInfo();

                break;
        }

    }
}
