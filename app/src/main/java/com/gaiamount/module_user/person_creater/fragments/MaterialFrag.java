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
import com.gaiamount.apis.api_creation.CreationApiHelper;
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_user.adapters.CreateCollectionAdapter;
import com.gaiamount.module_user.adapters.CreateMaterialAdapter;
import com.gaiamount.module_user.person_creater.dialogs.AlbumCreateDialog;
import com.gaiamount.module_user.person_creater.events.OnEventMaterial;
import com.gaiamount.module_user.person_creater.events.OnEventWorks;
import com.gaiamount.module_user.personal.CollectionActivity;
import com.gaiamount.module_workpool.adapters.WorkPoolAdapter;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-9-7.
 */
public class MaterialFrag extends Fragment {
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CollectionActivity mConnActivity;
    private LinearLayout layout;
    private int t=1;
    private int s=0;
    private int pi=1;
    private int opr=0;
    private CreateMaterialAdapter adapter;
    private GridLayoutManager layoutManager;
    private long uid;
    private ArrayList<MaterialInfo> materialInfos=new ArrayList<>();

    public static MaterialFrag getInstance(long mUid) {
        MaterialFrag materialFrag = new MaterialFrag();
        Bundle bundle = new Bundle();
        bundle.putLong("mUid", mUid);
        materialFrag.setArguments(bundle);
        return materialFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getLong("mUid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_collection_all, null);
        EventBus.getDefault().register(this);
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
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(CreateAcademyFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
//                Log.i("material",response.toString());
                paraJson(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                layout.setVisibility(View.GONE);
            }
        };
        try {
            MaterialApiHelper.getMaterialCreateList(uid,pi,getContext(),handler);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void paraJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            MaterialInfo info = new MaterialInfo();
            info.setNickName(jsonObject.optString("nickName"));
            info.setHave1080(jsonObject.optInt("have1080"));
            info.setFormat(jsonObject.optString("format"));
            info.setLikeCount(jsonObject.optInt("likeCount"));
            info.setScreenshot(jsonObject.optString("screenshot"));
            info.setAvatar(jsonObject.optString("avatar"));
            info.setType(jsonObject.optString("type"));
            info.setUserId(jsonObject.optLong("userId"));
            info.setIsVip(jsonObject.optInt("isVip"));
            info.setIsOfficial(jsonObject.optInt("isOfficial"));
            info.setCommentCount(jsonObject.optInt("commentCount"));
            info.setCover(jsonObject.optString("cover"));
            info.setIs4K(jsonObject.optInt("is4K"));
            info.setDuration(jsonObject.optInt("duration"));
            info.setVipLevel(jsonObject.optInt("vipLevel"));
            info.setPlayCount(jsonObject.optInt("playCount"));
            info.setHave720(jsonObject.optInt("have720"));
            info.setGrade(jsonObject.optDouble("grade"));
            info.setName(jsonObject.optString("name"));
            info.setId(jsonObject.optLong("id"));
            info.setWidth(jsonObject.optInt("width"));
            info.setHeight(jsonObject.optInt("height"));
            info.setInputKey(jsonObject.optString("inputKey"));
            info.setAllowCharge(jsonObject.optInt("allowCharge")); //0 免费  1 付费
            materialInfos.add(info);
        }
        adapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }
    private void setadapter() {
        adapter = new CreateMaterialAdapter(getContext(),materialInfos);
        mRecyclerView.setAdapter(adapter);
        adapter.getUid(uid);
        mRecyclerView.addItemDecoration(new MixLightDecoration(10,10,24,24));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnEventMaterial event) {
        AlbumCreateDialog alertDialog= AlbumCreateDialog.newInstance(materialInfos.get(event.position).getId(),event.position,1);
        alertDialog.show(getFragmentManager(),"personAlbumActivity");
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
                materialInfos.clear();
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
