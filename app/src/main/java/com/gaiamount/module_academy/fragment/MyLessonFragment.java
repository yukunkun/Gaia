package com.gaiamount.module_academy.fragment;

import android.app.Activity;
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

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.module_academy.adapter.MyLessonAdapter;
import com.gaiamount.module_academy.bean.LessonInfo;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by yukun on 16-8-17.
 */
public class MyLessonFragment extends Fragment {
    private int t;
    private int pi=1;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<MixInfo> lessonInfos=new ArrayList<>();
    private MyLessonAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        t = arguments.getInt("t");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.my_lesson_fragment, null);
        init(inflate);
        getInfo();
        setAdapter();
        setListener();
        return inflate;
    }

    private void init(View inflate) {
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swiplayout);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_lesson);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(MyLessonFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
            }
        };

        AcademyApiHelper.getLesson(t,pi,getContext(),handler);
    }

    private void parasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");

        for (int i = 0; i < a.length(); i++) {
            MixInfo mixInfo=new MixInfo();
            JSONObject object = a.optJSONObject(i);
            mixInfo.setLearningCount(object.optInt("learningCount"));
            mixInfo.setHasLeanCount(object.optInt("hasLeanCount"));
            mixInfo.setAuthor(object.optString("auther"));
            mixInfo.setAvatar(object.optString("avatar"));
            mixInfo.setCover(object.optString("cover"));
            mixInfo.setAllowFree(object.optInt("allowFree"));
            mixInfo.setPrice(object.optLong("price"));
            mixInfo.setId(object.optInt("id"));
            mixInfo.setType(object.optInt("type"));//1,0,2,图文,视频,直播
            mixInfo.setName(object.optString("name"));
            mixInfo.setChaptCount(object.optInt("chaptCount"));
            mixInfo.setNickName(object.optString("nickName"));
            mixInfo.setPlayCount(object.optInt("playCount"));
            mixInfo.setBrowseCount(object.optInt("browseCount"));
            mixInfo.setGrade(object.optInt("grade"));
            mixInfo.setHourCount(object.optInt("hourCount"));
            mixInfo.setProprity(object.optInt("proprity"));

            mixInfo.setCid(object.optLong("cid"));
            lessonInfos.add(mixInfo);
        }
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    private void setAdapter() {
        adapter = new MyLessonAdapter(getContext(),lessonInfos);
        recyclerView.setAdapter(adapter);
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
                    getInfo();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lessonInfos.clear();
                pi=1;
                getInfo();
            }
        });
    }

}
