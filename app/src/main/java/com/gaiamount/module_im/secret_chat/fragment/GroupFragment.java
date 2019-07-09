package com.gaiamount.module_im.secret_chat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaiamount.R;
import com.gaiamount.apis.api_im.ImApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.secret_chat.adapter.GroupRecyAdapter;
import com.gaiamount.module_im.secret_chat.bean.GroupBean;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-7-15.
 */
public class GroupFragment extends Fragment{
    RecyclerView mRecyclerView;
    List<GroupBean> groupList=new ArrayList<>();
    GroupRecyAdapter groupAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.group_fragment,null);
        init(view);
        getInfo();
        setAdapter();
        return view;
    }


    private void init(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.group_recy);
        LinearLayoutManager linear=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linear);
    }

    private void setAdapter() {
        groupAdapter=new GroupRecyAdapter(getContext(),groupList);
        mRecyclerView.setAdapter(groupAdapter);
    }

    private void getInfo() {
        JsonHttpResponseHandler handler=new MJsonHttpResponseHandler(GroupFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraseJson(response);
            }
        };
        ImApiHelper.getGroup(GaiaApp.getAppInstance().getUserInfo().id,getContext(),handler);

    }


    private void paraseJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            GroupBean groupBean=new GroupBean();
            groupBean.setBackground(jsonObject.optString("background"));
            groupBean.setCreateTime(jsonObject.optString("createTime"));
            groupBean.setCreationCount(jsonObject.optInt("creationCount"));
            groupBean.setDescription(jsonObject.optString("description"));
            groupBean.setId(jsonObject.optString("id"));
            groupBean.setDescription(jsonObject.optString("description"));
            groupBean.setKeywords(jsonObject.optString("keywords"));
            groupBean.setName(jsonObject.optString("name"));

            groupBean.setUserId(jsonObject.optString("userId"));

            groupBean.setImId(jsonObject.optString("imId"));
            groupBean.setVisitCount(jsonObject.optInt("visitCount"));
            groupList.add(groupBean);
        }
        groupAdapter.notifyDataSetChanged();
    }

}
