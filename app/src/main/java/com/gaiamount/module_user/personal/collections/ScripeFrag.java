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
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.module_user.personal.CollectionActivity;
import com.gaiamount.module_workpool.adapters.WorkPoolAdapter;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-9-29.
 */
public class ScripeFrag extends Fragment {
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<ScripeInfo> mLessonInfoList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CollectionActivity mConnActivity;
    private LinearLayout layout;
    private int pi=1;
    private ScripeCollAdapter adapter;
    private GridLayoutManager layoutManager;

    private long uid;

    public static ScripeFrag newInstance(long uid){
        ScripeFrag materialFrag=new ScripeFrag();
        Bundle bundle=new Bundle();
        bundle.putLong("uid",uid);
        materialFrag.setArguments(bundle);
        return materialFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getLong("uid");
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
        ScriptApiHelper.getScriptCollectList(uid,0,0,0,0,pi,8,getActivity(),handler);
    }
    private void paraJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        if(a.length()==0){
            return;
        }
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
            //获取时间
            JSONObject jsonObject = object.optJSONObject("time");

            if(jsonObject!=null){
                scripeInfo.setTime(jsonObject.optLong("time"));
            }

            scripeInfo.setCollectCount(object.optInt("collectCount"));
            scripeInfo.setCommentCount(object.optInt("commentCount"));

            mLessonInfoList.add(scripeInfo);
        }

        adapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    private void setadapter() {
        adapter = new ScripeCollAdapter(getContext(),mLessonInfoList);
        mRecyclerView.setAdapter(adapter);
        adapter.update(uid);
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
