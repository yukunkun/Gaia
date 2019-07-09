package com.gaiamount.module_user.person_creater.fragments;

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
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_creator.create_person.WorksInfo;
import com.gaiamount.module_creator.fragment.AlbumDialog;
import com.gaiamount.module_user.person_creater.CreateWorkAdapter;
import com.gaiamount.module_user.person_creater.dialogs.AlbumCreateDialog;
import com.gaiamount.module_user.person_creater.events.OnEventWorks;
import com.gaiamount.module_user.personal.CollectionActivity;
import com.gaiamount.module_user.personal.PersonalWorks;
import com.gaiamount.module_user.personal.PersonalWorksActivity;
import com.gaiamount.module_user.personal_album.bean.OnEventPersonAlbum;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-8-23.
 */
public class WorksFrag extends Fragment {
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout layout;
    private GridLayoutManager layoutManager;
    private ArrayList<PersonalWorks> worksArrayList = new ArrayList<>();
    private int pi=1;
    private long mUid;
    private CreateWorkAdapter createWorkAdapter;

    public static WorksFrag getInstance(long mUid){
        WorksFrag worksFrag=new WorksFrag();
        Bundle bundle=new Bundle();
        bundle.putLong("mUid",mUid);
        worksFrag.setArguments(bundle);
        return worksFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mUid=arguments.getLong("mUid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.create_works_person, null);
        EventBus.getDefault().register(this);
        init(inflate);
        getInfo();
        setListener();
        setAdapter();
        return inflate;
    }

    private void getInfo() {
        MJsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(PersonalWorksActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray a = response.optJSONArray("a");
                if(a.length()!=0){
                    layout.setVisibility(View.GONE);
                }
                parasJson(response);
            }
        };
        WorksApiHelper.getMyWorks(mUid, pi,0, 0, getContext(), jsonHttpResponseHandler);
    }

    private void parasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            PersonalWorks personalWorks=new PersonalWorks();
            personalWorks.setName(jsonObject.optString("nickName"));
            personalWorks.setLikeCount(jsonObject.optInt("likeCount"));
            personalWorks.setAvatar(jsonObject.optString("avatar"));
            personalWorks.setType(jsonObject.optInt("type")+"");
            personalWorks.setIsOfficial(jsonObject.optInt("isOfficial"));
            personalWorks.setCommentCount(jsonObject.optInt("commentCount"));
            personalWorks.setCover(jsonObject.optString("cover"));
            personalWorks.setScreenshot(jsonObject.optString("screenshot"));
            personalWorks.setDuration(jsonObject.optInt("duration"));
            personalWorks.setIs4K(jsonObject.optInt("is4K"));
            personalWorks.setPlayCount(jsonObject.optInt("playCount"));
            personalWorks.setGrade(jsonObject.optInt("grade"));
            personalWorks.setName(jsonObject.optString("name"));
            personalWorks.setId(jsonObject.optInt("id"));
            personalWorks.setFlag(jsonObject.optInt("flag"));
            worksArrayList.add(personalWorks);
        }
        createWorkAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    private void init(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.create_swiprefresh);
        layout = (LinearLayout) view.findViewById(R.id.create_bg);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.create_recyclerview);
        layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnEventWorks event) {
            AlbumCreateDialog alertDialog= AlbumCreateDialog.newInstance(worksArrayList.get(event.position).getId(),event.position,0);
            alertDialog.show(getFragmentManager(),"personAlbumActivity");
    }

    private void setAdapter() {
        createWorkAdapter = new CreateWorkAdapter(getContext(),worksArrayList,mUid);
        mRecyclerView.setAdapter(createWorkAdapter);
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
                    createWorkAdapter.notifyDataSetChanged();
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pi=1;
                worksArrayList.clear();
                getInfo();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
