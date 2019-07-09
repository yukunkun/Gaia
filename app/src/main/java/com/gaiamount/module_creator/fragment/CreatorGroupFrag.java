package com.gaiamount.module_creator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.beans.GroupInfo;
import com.gaiamount.module_creator.sub_module_group.adapters.GroupListAdapter;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.recyc.RecyclerItemDecoration;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-6-6.
 * 创作者-小组
 */
public class CreatorGroupFrag extends CreatorBaseFrag {
    /**
     * 小组列表控件
     */
    private RecyclerView mRecyclerView;
    /**
     * 列表数据
     */
    private List<GroupInfo> mGroupInfoList;
    /**
     * 列表适配器
     */
    private GroupListAdapter mAdapter;
    /**
     * 下拉刷新控件
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int mPi = 1;
    private ProgressBar mProgressBar;

    private int mSortType = GroupApiHelper.GROUP_SORT_TYPE_RECOMMEND;

    @Override
    public String getFragmentTitle() {
        return "小组";
    }

    public static CreatorGroupFrag newInstance() {
        return new CreatorGroupFrag();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.creator_recycler);
        //下拉刷新设置
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(3);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        //设置适配器
        mGroupInfoList = new ArrayList<>();
        final LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layout);
        mAdapter = new GroupListAdapter(getActivity(), mGroupInfoList);
        mRecyclerView.setAdapter(mAdapter);
        //设置间距
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(ScreenUtils.dp2Px(getActivity(), 10)));

        //上拉加载更多
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layout.findLastCompletelyVisibleItemPosition();
                if (layout.getItemCount() - 1 == lastVisibleItemPosition) {
                    //加载下一页
                    mPi = 2;
                    getData(mSortType);
                    mPi++;
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData(GroupApiHelper.GROUP_SORT_TYPE_RECOMMEND);

    }

    /**
     * 刷新数据
     *
     * @param sortType 排序 0推荐度 1最多成员 2作品量 3 创建时间
     */
    public void getData(int sortType) {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(CreatorGroupFrag.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray a = response.optJSONArray("a");
                List<GroupInfo> groupInfos = GsonUtil.getInstannce().getGson().fromJson(a.toString(), new TypeToken<List<GroupInfo>>() {
                }.getType());

                //如果没有数据，提示没有更多了
                if (mPi>1&&groupInfos.size()==0) {
                    GaiaApp.showToast("没有更多了");
                }

                mGroupInfoList.addAll(groupInfos);
                mAdapter.setGroupInfoList(mGroupInfoList);
                mAdapter.notifyDataSetChanged();

                mProgressBar.setVisibility(View.INVISIBLE);

            }
        };
        GroupApiHelper.getGroupList(sortType, mPi, 20, getActivity(), jsonHttpResponseHandler);
    }

    public void updateData(int sortType) {
        mSortType = sortType;
        mGroupInfoList.clear();
        getData(sortType);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //重置
        mPi = 1;
    }

}
