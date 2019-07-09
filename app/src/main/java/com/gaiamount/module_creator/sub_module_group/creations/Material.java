package com.gaiamount.module_creator.sub_module_group.creations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_academy.bean.OnEventLearn;
import com.gaiamount.module_creator.beans.GroupVideoBean;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_group.adapters.MaterialGroupAdapter;
import com.gaiamount.module_creator.sub_module_group.beans.onEventMaterial;
import com.gaiamount.module_creator.sub_module_group.constant.CreationType;
import com.gaiamount.module_creator.sub_module_group.fragment.AlbumMaterialDialog;
import com.gaiamount.module_creator.sub_module_group.fragment.AlbunCollegeDialog;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-6-27.
 */
public class Material extends Fragment {
    private long mGid;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private Info info;
    private List<String> listMore = new ArrayList<>();
    private ArrayList<MaterialInfo> materialInfos=new ArrayList<>();
    private MaterialGroupAdapter adapter;

    public static Material newInstance(Info info) {

        Bundle args = new Bundle();
        args.putSerializable("info",info);
        Material fragment = new Material();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        info = (Info) getArguments().getSerializable("info");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMaterialEvent(onEventMaterial event) {
        int position = event.id;
        if(info.groupPower.allowManagerSpecial==1){
            AlbumMaterialDialog albunCreateDialog=AlbumMaterialDialog.newInstance(info.gid,materialInfos.get(position),position);
            albunCreateDialog.show(getChildFragmentManager(),"Product");
        }else {
            GaiaApp.showToast("对不起,你没有权限");
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment_detail, null);

        init(view);
        //获取数据
        setAdapter();
        getInfo();
        setListener();
        return view;
    }

    private void setAdapter() {
        adapter = new MaterialGroupAdapter(getContext(),materialInfos,info);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MixLightDecoration(10,10,24,24));
    }

    private int pi=1;

    private void init(View view) {
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView) view.findViewById(R.id.album_recyclerview);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void getInfo() {
        JsonHttpResponseHandler handler = new MJsonHttpResponseHandler(Product.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
//                Log.i("-----material",response.toString());
            }
        };
        try {
            MaterialApiHelper.getMaterialGroupCreateList(info.gid,pi,getContext(),handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void paraJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            MaterialInfo info=new MaterialInfo();
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
            info.setGvid(jsonObject.optLong("gvid"));
            info.setWidth(jsonObject.optInt("width"));
            info.setHeight(jsonObject.optInt("height"));
            info.setCategory(jsonObject.optInt("category"));
            info.setInputKey(jsonObject.optString("inputKey"));
            info.setAllowCharge(jsonObject.optInt("allowCharge")); //0 免费  1 付费
            materialInfos.add(info);
        }
        adapter.notifyDataSetChanged();
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

                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    //加载更多
                    pi++;
                    getInfo();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
