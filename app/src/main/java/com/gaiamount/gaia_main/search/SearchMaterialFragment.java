package com.gaiamount.gaia_main.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.apis.api_search.SearchApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.search.adapter.SearchAcademyAdapter;
import com.gaiamount.gaia_main.search.adapter.SearchMaterialAdapter;
import com.gaiamount.gaia_main.search.beans.OnEventKey;
import com.gaiamount.gaia_main.search.beans.OnEventMaterialKey;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-9-29.
 */
public class SearchMaterialFragment extends BaseFrag implements View.OnClickListener {
    private LinearLayout mRelevancy;
    private LinearLayout mLayoutMosrPer;
    private LinearLayout mLayoutMosrCol;
    private RecyclerView mRecyclerView;
    private TextView textViewEmpty;
    private int opr=0,pi=1,t=1;//opr 的筛选０默认　１最新发布　２最多浏览　３最多学员　４最多收藏　５　最多评论
    private long uid;
    private  String key="";
    private ArrayList<MaterialInfo> materialInfos=new ArrayList<>();

    private GridLayoutManager gridLayoutManager;
    private SearchMaterialAdapter searchAdapter;
    private TextView textViewLoad;

    public static SearchMaterialFragment newInstance() {
        Bundle args = new Bundle();
        SearchMaterialFragment fragment = new SearchMaterialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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

    //传过来的值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMaterialKey(OnEventMaterialKey event) {
        key=event.key;
        pi=1;
        opr=0;
        materialInfos.clear();
        changeStyle(mRelevancy);
        getInfo();


    }
    private void init(View inflate) {
        gridLayoutManager = new GridLayoutManager(getContext(),1);
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        textViewLoad = (TextView) inflate.findViewById(R.id.more_load);
        textViewLoad.setText("最多下载");

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


        try {
            MaterialApiHelper.getMaterialSearchList(0,0,opr,pi,10,3,key,getContext(),handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void parasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            MaterialInfo info = new MaterialInfo();
            info.setNickName(jsonObject.optString("nickName"));
            info.setHave1080(jsonObject.optInt("have1080"));
            info.setFormat(jsonObject.optString("format"));
            info.setLikeCount(jsonObject.optInt("likeCount"));
            info.setScreenshot(jsonObject.optString("screenshot"));
            info.setAvatar(jsonObject.optString("avatar"));
            info.setType(jsonObject.optString("type"));
            info.setUserId(jsonObject.optLong("userId"));
            info.setIsVip(jsonObject.optInt("isVip"));
            info.setIsOfficial(jsonObject.optInt("isOfficial"));
            info.setCommentCount(jsonObject.optInt("commentCount"));
            info.setCover(jsonObject.optString("cover"));
            info.setIs4K(jsonObject.optInt("is4K"));
            info.setDuration(jsonObject.optInt("duration"));
            info.setVipLevel(jsonObject.optInt("vipLevel"));
            info.setPlayCount(jsonObject.optInt("playCount"));
            info.setHave720(jsonObject.optInt("have720"));
            info.setGrade(jsonObject.optDouble("grade"));
            info.setName(jsonObject.optString("name"));
            info.setId(jsonObject.optLong("id"));
            info.setWidth(jsonObject.optInt("width"));
            info.setFlag(jsonObject.optInt("flag"));
            info.setHeight(jsonObject.optInt("height"));
            info.setCategory(jsonObject.optInt("category"));
            info.setDownloadCount(jsonObject.optInt("downloadCount"));
            info.setInputKey(jsonObject.optString("inputKey"));
            info.setAllowCharge(jsonObject.optInt("allowCharge")); //0 免费  1 付费
            materialInfos.add(info);
        }
        searchAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        searchAdapter = new SearchMaterialAdapter(getContext(),materialInfos);
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
    @Override
    public void update(List<?> list) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relevancy:
                changeStyle(mRelevancy);
                materialInfos.clear();
                opr=0;
                pi=1;
                getInfo();

                break;
            case R.id.most_person:
                changeStyle(mLayoutMosrPer);
                materialInfos.clear();
                opr=4;
                pi=1;
                getInfo();

                break;
            case R.id.most_collection:
                changeStyle(mLayoutMosrCol);
                materialInfos.clear();
                opr=5;
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
