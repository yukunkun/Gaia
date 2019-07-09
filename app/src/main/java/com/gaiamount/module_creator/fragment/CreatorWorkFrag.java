package com.gaiamount.module_creator.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_player.bean.VideoInfo;
import com.gaiamount.module_user.personal.CollectionActivity;
import com.gaiamount.module_user.personal.collections.CollectWorkAdapter;
import com.gaiamount.module_workpool.adapters.SpacesItemDecoration;
import com.gaiamount.module_workpool.adapters.WorkPoolAdapter;
import com.gaiamount.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-5-4.
 * 收藏-作品
 */
public class CreatorWorkFrag extends Fragment {

    private CollectWorkAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private List<VideoInfo> mVideoInfoList = new ArrayList<>();
    private RecyclerView mWorkList;
    private CollectionActivity mConnActivity;
    private ProgressBar mProgressBar;
    private TextView mEmptyHint;
    private long uid;

    public static CreatorWorkFrag newInstance(long uid) {
        CreatorWorkFrag creatorWorkFrag = new CreatorWorkFrag();
        Bundle bundle=new Bundle();
        bundle.putLong("uid",uid);
        creatorWorkFrag.setArguments(bundle);
        return creatorWorkFrag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid=getArguments().getLong("uid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_pool_all, container, false);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mEmptyHint = (TextView) view.findViewById(R.id.empty_hint);
        mWorkList = (RecyclerView) view.findViewById(R.id.work_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mWorkList.setLayoutManager(layoutManager);


        //获取列表
        mAdapter = new CollectWorkAdapter(getActivity(), mVideoInfoList,uid);
        mWorkList.addItemDecoration(new MixLightDecoration(10,10,20,20));
        mWorkList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CollectWorkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ActivityUtil.startPlayerActivity(getActivity(), mVideoInfoList.get(position).getId(), 0);
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mConnActivity != null) {
                    mConnActivity.getCollects(CreatorWorkFrag.this);
                }
            }
        });

        Activity activity = getActivity();
        if (activity instanceof CollectionActivity) {
            mConnActivity = (CollectionActivity) activity;
            mConnActivity.getCollects(this);
        }

        return view;
    }

    /**
     * 在CollectionActivity的getCollects调用后，如果成功获取到数据，此方法被调用
     *
     * @param videoInfoList
     */
    public void setVideoInfoList(List<VideoInfo> videoInfoList) {
        mVideoInfoList = videoInfoList;
        onNetFinished();

        mAdapter.setVideoInfoList(mVideoInfoList);
        mAdapter.notifyDataSetChanged();
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 联网请求之后
     */
    public void onNetFinished() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mVideoInfoList.size()==0&&mEmptyHint!=null) {
            mEmptyHint.setText(R.string.no_collects);
            mProgressBar.setVisibility(View.GONE);
        }else if (mVideoInfoList.size()>0&&mEmptyHint!=null){
            mEmptyHint.setVisibility(View.GONE);
        }
    }


}
