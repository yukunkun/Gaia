package com.gaiamount.module_creator.sub_module_album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_album.album_adapter.AlbunCollectionAdapter;
import com.gaiamount.module_user.personal.collections.CollectionAdapter;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by haiyang-lu on 16-6-30.
 */
public class AlbumCollageFrag extends Fragment {
    public static final String AID = "aid";
    public static final String ALBUM_TYPE = "albumType";
    public static final String C = "c";
    public static final String MGid = "mGid";
    private long mAid;
    private long mGid;
    private int mAlbumType;
    private int mC;
    private int pi=1;
    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<MixInfo> mLessonInfoList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LinearLayout layout;
    private AlbunCollectionAdapter adapter;
    private Info info;

    public static AlbumCollageFrag newInstance(long aid, int t, Info info, int c) {

        Bundle args = new Bundle();
        args.putLong(AID, aid);
        args.putInt(ALBUM_TYPE, t);
        args.putInt(C, c);
        args.putSerializable("info",info);
        AlbumCollageFrag fragment = new AlbumCollageFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mAid = args.getLong(AID);
        mAlbumType = args.getInt(ALBUM_TYPE);
        mC = args.getInt(C);
        info= (Info) args.getSerializable("info");
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
        AlbumApiHelper.getAlbumAcademyList(mAid,0,GaiaApp.getAppInstance().getUserInfo().id,pi,getContext(),handler);
    }

    private void paraJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            MixInfo mixInfo=new MixInfo();
            JSONObject object = a.optJSONObject(i);
            mixInfo.setLearningCount(object.optInt("learningCount"));
            mixInfo.setAuthor(object.optString("auther"));
            mixInfo.setAvatar(object.optString("avatar"));
            mixInfo.setCover(object.optString("cover"));
            mixInfo.setAllowFree(object.optInt("allowFree"));
            mixInfo.setPrice(object.optLong("price"));
            mixInfo.setId(object.optInt("id"));
            mixInfo.setType(object.optInt("type"));//1,0,2,图文,视频,直播
            mixInfo.setName(object.optString("name"));
            mixInfo.setChaptCount(object.optInt("chapterCount"));
            mixInfo.setNickName(object.optString("nickName"));
            mixInfo.setPlayCount(object.optInt("playCount"));
            mixInfo.setBrowseCount(object.optInt("browseCount"));
            mixInfo.setGrade(object.optInt("grade"));
            mixInfo.setHourCount(object.optInt("hourCount"));
            mixInfo.setGcid(object.optInt("acid")); //专辑学院是acid ,小组的创作学院是gcid,不能混淆,暂时用mixif的gcid接收
            mixInfo.setProprity(object.optInt("proprity"));
            JSONObject jsonObject = object.optJSONObject("createTime");
            Long time = jsonObject.optLong("time");
            mixInfo.setTime(time);
            mLessonInfoList.add(mixInfo);
        }
        adapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    private void setadapter() {
        adapter = new AlbunCollectionAdapter(getContext(), mLessonInfoList,info);
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
