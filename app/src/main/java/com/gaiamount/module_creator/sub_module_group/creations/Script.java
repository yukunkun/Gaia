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
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_group.adapters.GroupScripeAdapter;
import com.gaiamount.module_creator.sub_module_group.beans.onEventMaterial;
import com.gaiamount.module_creator.sub_module_group.beans.onEventScript;
import com.gaiamount.module_creator.sub_module_group.fragment.AlbumMaterialDialog;
import com.gaiamount.module_creator.sub_module_group.fragment.AlbumScriptDialog;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
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
public class Script extends Fragment {


    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private Info info;
    private int pi=1;

    /**
     * 点击更多按钮应该显示的item文字列表，根据用户的权限动态生成，
     * memberType为3则有添加推荐视频的功能
     * 如果有视频管理权限，则有删除视频的功能
     * 如有专辑管理权限，则可以将一个视频添加到专辑中
     */
    private ArrayList<ScripeInfo> scripeInfos = new ArrayList<>();
    private GroupScripeAdapter adapter;

    public static Script newInstance(Info info) {
        Bundle args = new Bundle();
        args.putSerializable("info", info);
        Script fragment = new Script();
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
    public void onScriptEvent(onEventScript event) {
        int position = event.id;
        if(info.groupPower.allowManagerSpecial==1){
            AlbumScriptDialog albunCreateDialog=AlbumScriptDialog.newInstance(info.gid,scripeInfos.get(position),position);
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
        adapter = new GroupScripeAdapter(getActivity(),scripeInfos,info);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MixLightDecoration(10,10,24,24));
    }


    private void init(View view) {
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView) view.findViewById(R.id.album_recyclerview);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void getInfo() {
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(Script.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
            }
        };

        ScriptApiHelper.getGroupScript(info.gid,pi,8,getContext(),handler);
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
            scripeInfo.setGvid(object.optLong("gvid"));
            //获取时间
            JSONObject jsonObject = object.optJSONObject("time");


            if(jsonObject!=null){
                scripeInfo.setTime(jsonObject.optLong("time"));
            }

            scripeInfo.setCollectCount(object.optInt("collectCount"));
            scripeInfo.setCommentCount(object.optInt("commentCount"));

            scripeInfos.add(scripeInfo);
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
