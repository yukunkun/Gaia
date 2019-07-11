package com.gaiamount.module_material.fragment;

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

import com.gaiamount.R;
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.module_academy.adapter.MixAdapter;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_material.adapters.VideoMaterialAdapter;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-9-28.
 */
public class VideoMaterialFragment extends Fragment {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private int pi=1,s=0,opr=0,t=0,ps=10;
    int formats=0;
    private ArrayList<MaterialInfo> materialInfos=new ArrayList<>();
    private VideoMaterialAdapter adapter;
    int width=0;
    int height=0;
    int ratios=0;
    private boolean tag=false;
    private String[] RatioString=new String[]{"所有","4K","UHD","2K","1080","720"};
    private String[] formatString=new String[]{"","MOV","MP4","MXF","MTS","R3D"};
    public static VideoMaterialFragment getInstance(int t){
        VideoMaterialFragment fragment=new VideoMaterialFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("t",t);           //0全部 ~ 18其他
//        bundle.putInt("s",s);           //0全部 1免费 2付费 3推荐
//        bundle.putInt("opr",opr);       //0默认 1最新 2价格由高到低 3价格由低到高 4最多下载 5最多收藏
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        t=bundle.getInt("t");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.my_lesson_fragment,null);
        init(inflate);
        setAdapter();
        getInfo(t,s,opr,formats,ratios);
        setListener();
        return inflate;

    }

    public void getInfo(int t, int s,int opr,int formats,int ratios) {
        this.t=t;
        this.s=s;
        this.opr=opr;
        this.formats=formats;
        this.ratios=ratios;
        if(!tag&&adapter!=null){
            materialInfos.clear();
            pi=1;
            adapter.notifyDataSetChanged();
        }
        String forMat=formatString[0];
        MJsonHttpResponseHandler handler3=new MJsonHttpResponseHandler(VideoMaterialFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJsonModulVideo(response);
            }
        };

        if(formats==0){
            forMat=formatString[0];
        }else if(formats==1){
            forMat=formatString[1];
        }else if(formats==2){
            forMat=formatString[2];
        }else if(formats==3){
            forMat=formatString[3];
        }else if(formats==4){
            forMat=formatString[4];
        }else if(formats==5){
            forMat=formatString[5];
        }
        if(ratios==0){
            width=0;
            height=0;
        }else if(ratios==1){
            width=4096;
            height=2160;
        }else if(ratios==2){
            width=3840;
            height=2160;
        }else if(ratios==3){
            width=2048;
            height=1080;
        }else if(ratios==4){
            width=1920;
            height=1080;
        }else if(ratios==5){
            width=1280;
            height=720;
        }

        try {
            MaterialApiHelper.getMaterialLists(t,s,opr,pi,ps,1,forMat,width,height,getContext(),handler3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void parasJsonModulVideo(JSONObject response) {
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
            info.setFlag(jsonObject.optInt("flag"));
            info.setWidth(jsonObject.optInt("width"));
            info.setHeight(jsonObject.optInt("height"));
            info.setInputKey(jsonObject.optString("inputKey"));
            info.setAllowCharge(jsonObject.optInt("allowCharge")); //0 免费  1 付费
            materialInfos.add(info);
        }
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        adapter.devideMaterial(1);// 1 表示是视频素材,0表示模板素材
    }


    private void init(View inflate) {
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swiplayout);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_lesson);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
    private void setAdapter() {
        adapter = new VideoMaterialAdapter(getContext(),materialInfos);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MixLightDecoration(10,10,24,24));
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

                int lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == gridLayoutManager.getItemCount() - 1) {
                    //加载更多
                    tag = true;
                    pi++;
                    getInfo(t,s,opr,formats,ratios);
                    tag=false;
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                materialInfos.clear();
                pi=1;
                getInfo(t,0,0,0,0);
            }
        });
    }
}
