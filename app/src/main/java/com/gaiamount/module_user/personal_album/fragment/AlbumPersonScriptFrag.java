package com.gaiamount.module_user.personal_album.fragment;

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
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_album.album_adapter.AlbumScripeAdapter;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.module_user.personal_album.adapter.AlbumPersonScripeAdapter;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by haiyang-lu on 16-6-30.
 */
public class AlbumPersonScriptFrag extends Fragment {
    private int mC;
    public static final String AID = "aid";
    public static final String ALBUM_TYPE = "albumType";
    public static final String C = "c";
    private long mAid;
    private int mAlbumType;
    private int pi=1;
    private Info info;

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<ScripeInfo> mLessonInfoList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LinearLayout layout;
    private AlbumPersonScripeAdapter adapter;

    public static AlbumPersonScriptFrag newInstance(long aid, int t, Info info) {

        Bundle args = new Bundle();
        args.putLong(AID, aid);
        args.putInt(ALBUM_TYPE,t);
        args.putSerializable("info",info);
        AlbumPersonScriptFrag fragment = new AlbumPersonScriptFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mAid = args.getLong(AID);
        mAlbumType = args.getInt(ALBUM_TYPE);
        info=(Info)args.getSerializable("info");
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
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AlbumPersonScriptFrag.class){
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
                if(response.length()!=0){
                    layout.setVisibility(View.GONE);
                }
            }
        };

        ScriptApiHelper.getScripeAlbum(mAid,pi,8,getContext(),handler);
    }

    //解析数据
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
            scripeInfo.setAvid(object.optLong("avid"));

            //获取时间
            JSONObject jsonObject = object.optJSONObject("time");

            if(jsonObject!=null){
                scripeInfo.setTime(jsonObject.optLong("time"));
            }

            scripeInfo.setCollectCount(object.optInt("collectCount"));
            scripeInfo.setCommentCount(object.optInt("commentCount"));
            mLessonInfoList.add(scripeInfo);
        }

        adapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    private void setadapter() {
        adapter = new AlbumPersonScripeAdapter(getContext(),mLessonInfoList,info);
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
