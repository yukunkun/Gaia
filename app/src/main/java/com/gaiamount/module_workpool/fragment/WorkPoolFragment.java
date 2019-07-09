package com.gaiamount.module_workpool.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.module_creator.fragment.present.OrgBean;
import com.gaiamount.module_player.bean.VideoInfo;
import com.gaiamount.module_workpool.WorkPoolActivity;
import com.gaiamount.module_workpool.adapters.SpacesItemDecoration;
import com.gaiamount.module_workpool.adapters.WorkPoolAdapter;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.recyc.RecyclerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-3-29.
 * 作品池——所有
 */
public class WorkPoolFragment extends Fragment {

    private RecyclerView mWorkList;
    private WorkPoolActivity mActivity;
    private LinearLayout linearLayout;

    /**
     * 页数，表示请求第几页，默认为第0页
     */
    private int page = 1;
    /**
     * viewpager的位置
     */
    private int mPosition;
    private WorkPoolAdapter mWorkPoolAdapter;
    private SwipeRefreshLayout mSrl;
    List<VideoInfo> videoInfoList = new ArrayList<>();

    public static WorkPoolFragment newInstance(int position) {
        WorkPoolFragment workPoolFragment = new WorkPoolFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        workPoolFragment.setArguments(bundle);
        return workPoolFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mPosition = arguments.getInt("position");
        videoInfoList.clear();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (WorkPoolActivity) getActivity();
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.fragment_work_pool_all, container, false);
            mSrl = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
            //设置颜色
            mSrl.setColorSchemeResources(R.color.colorAccent);
            linearLayout= (LinearLayout) view.findViewById(R.id.work_fragment_linear);
            mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    videoInfoList.clear();
                    mIsRefreshing=true;
                    page=1;
                    getVideoInfo(1);
                }
            });
            initContentView(view);
        }
        setAdapter();
        getVideoInfo(1);
        return view;
    }


    private void initContentView(View view) {
        mWorkList = (RecyclerView) view.findViewById(R.id.work_list);
    }

    int lastVisibleItemPosition;

    public void getVideoInfo(int stype) {
//        videoInfoList.clear();
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(WorkPoolFragment.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
//                JSONArray jsonArray = response.optJSONArray("a");
//                VideoInfo videoInfo;
                Gson gson = new Gson();
                List<VideoInfo> stringList = gson.fromJson(response.optJSONArray("a")+"", new TypeToken<List<VideoInfo>>() {}.getType());
                videoInfoList.addAll(stringList);
                mWorkPoolAdapter.notifyDataSetChanged();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    videoInfo = gson.fromJson(jsonArray.optJSONObject(i).toString(), VideoInfo.class);
//                    if(videoInfo!=null){
//                        videoInfoList.add(videoInfo);
//                    }
//                }
//                if (mWorkPoolAdapter == null&&mActivity!=null&&videoInfoList.size()>0) {
//                    setAdapter();
//                } else {
//                    if(mWorkPoolAdapter!=null&&videoInfoList!=null){
//                        mWorkPoolAdapter.setVideoInfoList(videoInfoList);
//                        mWorkPoolAdapter.notifyDataSetChanged();
//                    }
//                }
                linearLayout.setVisibility(View.GONE);
                Log.d("WorkPoolFragment", videoInfoList.toString());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if(mSrl!=null) {
                    mSrl.setRefreshing(false);
                    mIsRefreshing=false;
                }
                linearLayout.setVisibility(View.GONE);
            }
        };
        WorksApiHelper.getWorkPoolList(mPosition, 0, stype, page,10, mActivity, jsonHttpResponseHandler);

    }

    private void setAdapter() {
        mWorkPoolAdapter = new WorkPoolAdapter(getContext(), videoInfoList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
        mWorkList.setLayoutManager(gridLayoutManager);
        mWorkList.addItemDecoration(new RecyclerItemDecoration(/*ScreenUtils.dp2Px(getContext(),10)*/20));
        int height = ScreenUtils.instance().getHeight();
        int width = ScreenUtils.instance().getWidth();
        if (width < height) {//手机
            mWorkList.addItemDecoration(new SpacesItemDecoration(16));
        } else {//平板
            mWorkList.addItemDecoration(new SpacesItemDecoration(16));//16px
        }

        mWorkList.setAdapter(mWorkPoolAdapter);

        //设置item的点击事件
        mWorkPoolAdapter.setOnItemClickListener(new WorkPoolAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //启动播放器
                ActivityUtil.startPlayerActivity(mActivity, videoInfoList.get(position).getId(), Configs.TYPE_WORK_POOL);
            }
        });

        //设置scroll的监听
        mWorkList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState==RecyclerView.SCROLL_STATE_DRAGGING) {
                    mWorkPoolAdapter.setListFling(true);
                }else {
                    mWorkPoolAdapter.setListFling(false);
                }
            }
        });

        setLoadMoreListener();
    }

    RecyclerView.OnScrollListener onScrollListener;
    boolean mIsRefreshing=true;
    private void setLoadMoreListener() {
        final GridLayoutManager layoutManager = (GridLayoutManager) mWorkList.getLayoutManager();
        //设置滑动监听，当滑动到最后一个时，加载更多
        if (onScrollListener == null) {
            onScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition == layoutManager.getItemCount() - 1) {
                        //加载更多
                        page++;
                        getVideoInfo(0);

                    }
                }
            };
            mWorkList.setOnScrollListener(onScrollListener);
        }

        mWorkList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mIsRefreshing){
                    return true;
                }else {
                    return  false;
                }
            }
        });




    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        WorkPoolActivity poolActivity=((WorkPoolActivity)activity);
        if(poolActivity.tag!=null){
            getVideoInfo(0);
        }
    }

}


