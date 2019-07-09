package com.gaiamount.module_user.personal.collections;

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
import android.widget.LinearLayout;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_user.bean.MAterialColl;
import com.gaiamount.module_user.personal.CollectionActivity;
import com.gaiamount.module_workpool.adapters.WorkPoolAdapter;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-18.
 */
public class CollectionFragment extends Fragment {
    private WorkPoolAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<MixInfo> mLessonInfoList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CollectionActivity mConnActivity;
    private LinearLayout layout;
    private int t=1;
    private int s=0;
    private int pi=1;
    private int opr=0;
    private long uid;
    private GridLayoutManager layoutManager;
    private CollectionAdapter adapter;


    public static CollectionFragment newInstance(long uid) {
        CollectionFragment collectionFragment = new CollectionFragment();
        Bundle bundle=new Bundle();
        bundle.putLong("uid",uid);
        collectionFragment.setArguments(bundle);
        return collectionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid=getArguments().getLong("uid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_collection_all, null);
        init(inflate);
        getInfo();
        setadapter();
        setListener();
        return inflate;
    }

    private void init(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        layout = (LinearLayout) view.findViewById(R.id.work_fragment_linear);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.work_list);
        layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(CollectionFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                layout.setVisibility(View.GONE);
            }
        };
        AcademyApiHelper.collectList(uid,s,opr,pi,t,getContext(),handler);
    }

    private void paraJson(JSONObject response) {
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
            mLessonInfoList.add(mixInfo);
        }
        adapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    private void setadapter() {
        adapter = new CollectionAdapter(getContext(),mLessonInfoList,uid);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new MixLightDecoration(10,10,20,24));
    }

    private void setListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    //加载更多
                    pi++;
                    getInfo();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pi=1;
                mLessonInfoList.clear();
                getInfo();
            }
        });
    }


}
