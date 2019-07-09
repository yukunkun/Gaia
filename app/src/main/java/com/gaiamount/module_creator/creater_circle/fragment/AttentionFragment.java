package com.gaiamount.module_creator.creater_circle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.CircleApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.creater_circle.adapter.AttentionAdapter;
import com.gaiamount.module_creator.creater_circle.bean.Attention;
import com.gaiamount.module_creator.creater_circle.bean.SpacesDecoration;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-7-25.
 */
public class AttentionFragment extends Fragment {
    RecyclerView mRecyclerView;
    List<Attention> attentList=new ArrayList();
    private long uid;
    private GridLayoutManager gridLayoutManager;
    private AttentionAdapter attentionAdapter;
    private RelativeLayout layout;
    int pi=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attention_fragment, null);
        init(view);
        uid= GaiaApp.getAppInstance().getUserInfo().id;
        getInfo();
        setAdapter();
        setListener();
        return view;
    }

    private void setAdapter() {
        attentionAdapter=new AttentionAdapter(getContext(),attentList);
        mRecyclerView.setAdapter(attentionAdapter);
        mRecyclerView.addItemDecoration(new SpacesDecoration(20));
    }

    //初始化数据
    private void init(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.attentionRec);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesDecoration(20));
        layout= (RelativeLayout) view.findViewById(R.id.attents_tishi);
    }

    //获取数据
    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AttentionFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
            }
        };
        CircleApiHelper.getAtention(getContext(),uid,0,0,pi,handler);
    }

    private void paraJson(JSONObject response) {
        JSONObject o = response.optJSONObject("o");
        JSONArray user = o.optJSONArray("user");
        for (int i = 0; i < user.length(); i++) {
            Attention attention=new Attention();
            JSONObject jsonObject = user.optJSONObject(i);
            attention.setNickName(jsonObject.optString("nickName"));
            attention.setAvatar(jsonObject.optString("avatar"));
            attention.setBrowseCount(jsonObject.optInt("browseCount"));
            attention.setLikeCount(jsonObject.optInt("likeCount"));
            attention.setId(jsonObject.optInt("id"));
            attention.setCreationCount(jsonObject.optInt("creationCount"));
            attentList.add(attention);
            layout.setVisibility(View.GONE);
        }
        attentionAdapter.notifyDataSetChanged();
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

                int lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == gridLayoutManager.getItemCount()-1) {
                    //加载更多
                    pi++;
                    getInfo();
                    attentionAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
