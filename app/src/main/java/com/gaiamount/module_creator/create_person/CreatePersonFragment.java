package com.gaiamount.module_creator.create_person;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.PersonApi;
import com.gaiamount.apis.api_creator.PersonApiHelper;
import com.gaiamount.module_creator.fragment.CreatorBaseFrag;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-7-27.
 */
public class CreatePersonFragment extends CreatorBaseFrag {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CreateAdapter adapter;
    private int s=0;
    private int opr=0;
    private int pi=1;//列表的页数
    private List<CreatePersonInfo> createPersonList=new ArrayList<>();
    private List<WorksInfo> worksList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.create_person_fragment, null);
        init(inflate);
        setAdapter();
        setListener();
        getInfo();
        return inflate;
    }
    public static CreatePersonFragment newInstance() {
        return new CreatePersonFragment();
    }

    @Override
    public String getFragmentTitle() {
        return "个人";
    }
    private void init(View inflate) {
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.create_person);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceReDecoration(30));
        refreshLayout= (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_fresh);
    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(CreatePersonFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
                adapter.notifyDataSetChanged();
                //刷新列表
                refreshLayout.setRefreshing(false);
            }
        };

        PersonApiHelper.createPersonList(s,opr,pi,getContext(),handler);
    }

    private void parasJson(JSONObject response) {

        JSONArray a = response.optJSONArray("a");
        for (int m = 0; m < a.length(); m++) {
            CreatePersonInfo personInfo=new CreatePersonInfo();
            JSONObject jsonObject = a.optJSONObject(m);
            personInfo.setAddress(jsonObject.optString("address"));
            personInfo.setSignature(jsonObject.optString("signature"));
            personInfo.setNickName(jsonObject.optString("nickName"));
            personInfo.setCreateCount(jsonObject.optInt("createCount"));
            personInfo.setBrowseCount(jsonObject.optInt("browseCount"));
            personInfo.setLikeCount(jsonObject.optInt("likeCount"));
            personInfo.setId(jsonObject.optInt("id"));
            personInfo.setAvatar(jsonObject.optString("avatar"));
            personInfo.setFocus(jsonObject.optInt("focus"));
            createPersonList.add(personInfo);
        }

        JSONObject o = response.optJSONObject("o");
        JSONArray works = o.optJSONArray("works");
        for (int i = 0; i < works.length(); i++) {
            WorksInfo worksInfo=new WorksInfo();
            JSONArray jsonArray = works.optJSONArray(i);
            ArrayList<CoverImgInfo> strings=new ArrayList<>();
            for (int j = 0; j < jsonArray.length(); j++) {
                CoverImgInfo imgInfo=new CoverImgInfo();
                JSONObject jsonObject = jsonArray.optJSONObject(j);
                imgInfo.setCover(jsonObject.optString("cover"));
                imgInfo.setScreenshot(jsonObject.optString("screenshot"));
                imgInfo.setId(jsonObject.optInt("id"));
                imgInfo.setType(jsonObject.optInt("type"));
                imgInfo.setFlag(jsonObject.optInt("flag"));
                strings.add(imgInfo);
            }
            worksInfo.setCoverImgInfos(strings);
            worksList.add(worksInfo);
        }

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
                    pi=pi+1;
                    getInfo();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        //刷新的监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pi=1;
                getInfo();
            }
        });
    }

    private void setAdapter() {
        adapter = new CreateAdapter(getContext(),createPersonList,worksList);
        mRecyclerView.setAdapter(adapter);
    }
}
