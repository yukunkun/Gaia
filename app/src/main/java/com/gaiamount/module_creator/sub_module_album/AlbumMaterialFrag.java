package com.gaiamount.module_creator.sub_module_album;

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
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_album.album_adapter.AlbunCollectionAdapter;
import com.gaiamount.module_creator.sub_module_album.album_adapter.MaterialAlbumAdapter;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by haiyang-lu on 16-6-30.
 */
public class AlbumMaterialFrag extends Fragment {
    public static final String AID = "aid";
    public static final String ALBUM_TYPE = "albumType";
    public static final String C = "c";
    private long mAid;
    private int mAlbumType;
    private int pi=1;
    private Info info;

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<MaterialInfo> mLessonInfoList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LinearLayout layout;
    private MaterialAlbumAdapter adapter;


    public static AlbumMaterialFrag newInstance(long aid, int t, Info info) {

        Bundle args = new Bundle();
        args.putLong(AID, aid);
        args.putInt(ALBUM_TYPE,t);
        args.putSerializable("info",info);
        AlbumMaterialFrag fragment = new AlbumMaterialFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mAid = args.getLong(AID);
        mAlbumType = args.getInt(ALBUM_TYPE);
        info = (Info) args.getSerializable("info");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_collection_all, container,false);
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
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AlbumCollageFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
                if(response.length()!=0){
                    layout.setVisibility(View.GONE);
                }
            }
        };
        AlbumApiHelper.getMaterialALbumCreateList(mAid,mAlbumType,pi,getContext(),handler);
    }

    //解析数据
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
            info.setGvid(jsonObject.optLong("avid")); //这里是avid,暂时用gvid来接收.
            info.setWidth(jsonObject.optInt("width"));
            info.setHeight(jsonObject.optInt("height"));
            info.setCategory(jsonObject.optInt("category"));
            info.setInputKey(jsonObject.optString("inputKey"));
            info.setAllowCharge(jsonObject.optInt("allowCharge")); //0 免费  1 付费
            mLessonInfoList.add(info);
        }

        adapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }
    private void setadapter() {
        adapter = new MaterialAlbumAdapter(getContext(), mLessonInfoList,info);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new MixLightDecoration(10,10,24,24));
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
