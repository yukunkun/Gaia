package com.gaiamount.module_creator.creater_circle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.CircleApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.creater_circle.adapter.FansAdapter;
import com.gaiamount.module_creator.creater_circle.adapter.GroupAdapter;
import com.gaiamount.module_creator.creater_circle.bean.Attention;
import com.gaiamount.module_creator.creater_circle.bean.GroupMessage;
import com.gaiamount.module_creator.creater_circle.bean.GroupSpacesDecoration;
import com.gaiamount.module_workpool.adapters.SpacesItemDecoration;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-7-25.
 */
public class GroupFragment extends Fragment {
    RecyclerView mRecyclerView;
    List<GroupMessage> groupList=new ArrayList();
    private long uid;
    private LinearLayoutManager linearLayoutManager;
    private GroupAdapter groupAdapter;
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

    private void init(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.attentionRec);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        layout= (RelativeLayout) view.findViewById(R.id.attents_tishi);
    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(GroupFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                ParaseJson(response);
                groupAdapter.notifyDataSetChanged();
            }
        };
        CircleApiHelper.getGroupMessage(getContext(),uid,pi,handler);
    }

    private void ParaseJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            GroupMessage groupMessage=new GroupMessage();
            JSONObject jsonObject = a.optJSONObject(i);
            groupMessage.setBackground(jsonObject.optString("background"));
            groupMessage.setNickName(jsonObject.optString("nickName"));
            groupMessage.setName(jsonObject.optString("name"));
            groupMessage.setDescription(jsonObject.optString("description"));
            groupMessage.setId(jsonObject.optInt("id"));
            groupMessage.setUserId(jsonObject.optInt("userId"));
            groupMessage.setCreationCount(jsonObject.optInt("creationCount"));
            JSONObject jsonObject1 = jsonObject.optJSONObject("createTime");
            groupMessage.setSeconds(jsonObject1.optInt("seconds"));
            groupMessage.setTime(jsonObject1.optLong("time"));
            groupList.add(groupMessage);
            layout.setVisibility(View.GONE);
        }
        groupAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        groupAdapter=new GroupAdapter(getContext(),groupList);
        mRecyclerView.setAdapter(groupAdapter);
        mRecyclerView.addItemDecoration(new GroupSpacesDecoration(30));
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
                int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == linearLayoutManager.getItemCount()-1) {
                    //加载更多
                    pi++;
                    getInfo();
                    groupAdapter.notifyDataSetChanged();

                }
            }
        });
    }

}
