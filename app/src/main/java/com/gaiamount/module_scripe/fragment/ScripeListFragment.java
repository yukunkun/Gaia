package com.gaiamount.module_scripe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_scripe.adapter.ScripeListAdapter;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-10-21.
 */
public class ScripeListFragment extends Fragment {

    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ScripeListAdapter scripeListAdapter;

    private int sc=0;    //       (0全部 1爱情 …17其他)
    private int bg=0;    //背景 	0全部 1现代 …5其它
    private int se=0;    //筛选 	0全部 1已完稿 6推荐
    private int opr=0;  //排序 	0默认 1最新 … 4最多评论
    private int pi=1;

    private ArrayList<ScripeInfo> scripeInfos=new ArrayList<>();

    public static ScripeListFragment newInstance(int sc){
        ScripeListFragment scripeListFragment=new ScripeListFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("sc",sc);
        scripeListFragment.setArguments(bundle);
        return scripeListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sc = getArguments().getInt("sc");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.my_lesson_fragment,null);
        init(inflate);
        setAdapter();
        getInfo(se,bg,opr);
        setListener();
        return inflate;
    }

    private void init(View inflate) {
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swiplayout);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_lesson);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private boolean tag=false;
    private void setListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfo(se,bg,opr);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

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
                    tag = true;
                    pi++;
                    getInfo(se,bg,opr);
                    tag =false;
                }
            }
        });
    }

    private void setAdapter() {
        scripeListAdapter = new ScripeListAdapter(getContext(),scripeInfos);
        recyclerView.setAdapter(scripeListAdapter);
        recyclerView.addItemDecoration(new MixLightDecoration(20,20,40,40));
    }


    public void getInfo(int se_1, int bg_1, int opr_1){

        se=se_1;
        bg=bg_1;
        opr=opr_1;

        if(!tag&&scripeListAdapter!=null){
            scripeInfos.clear();
            pi=1;
            scripeListAdapter.notifyDataSetChanged();
        }


        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ScripeListFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parseJsons(response);
            }
        };

        ScriptApiHelper.getRec(sc, bg_1, se_1, opr_1,pi,10,getContext(),handler);
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
            scripeInfo.setCover(object.optString("cover"));
            //获取是时间
            JSONObject jsonObject = object.optJSONObject("time");
            scripeInfo.setTime(jsonObject.optLong("time"));
            scripeInfo.setCollectCount(object.optInt("collectCount"));
            scripeInfo.setCommentCount(object.optInt("commentCount"));
            scripeInfos.add(scripeInfo);
        }
        scripeListAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

}
