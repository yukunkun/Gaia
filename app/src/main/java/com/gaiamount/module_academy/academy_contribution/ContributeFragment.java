package com.gaiamount.module_academy.academy_contribution;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.adapter.MixAdapter;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-22.
 */
public class ContributeFragment extends Fragment {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private int pi=1,s=0,c=-1,t=1,position=0;
    private long uid;
    private ArrayList<MixInfo> mixInfos=new ArrayList<>();
    private MixAdapter mixAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        c=arguments.getInt("c");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_lesson_fragment, null);
        init(view);
        uid= GaiaApp.getAppInstance().getUserInfo().id;
        getInfo(0,0);
        setAdapter();
        setListener();
        return view;
    }

    private void init(View inflate) {
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swiplayout);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_lesson);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    public void getInfo(int position,int clear) {
        if(clear==1){
            mixInfos.clear();
            pi=1;
        }
        this.position=position;
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ContributeFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
            }
        };
        AcademyApiHelper.getAllTouGou(s,position,pi,t,uid,c,getContext(),handler);
    }


    private void parasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        if(mixAdapter!=null&&a.length()>0){
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
            mixAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

    private void setAdapter() {
        mixAdapter = new MixAdapter(getContext(),mixInfos);
        recyclerView.setAdapter(mixAdapter);
        recyclerView.addItemDecoration(new MixLightDecoration(10,10,24,24));
    }

    private void setListener() {
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
                    pi++;
                    getInfo(position,0);
                    mixAdapter.notifyDataSetChanged();
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mixInfos.clear();
                pi=1;
                getInfo(position,0);
            }
        });
    }

}
