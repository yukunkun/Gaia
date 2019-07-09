package com.gaiamount.gaia_main.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.gaia_main.search.beans.OnEventKey;
import com.gaiamount.gaia_main.search.beans.SearchWorksResult;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-7-19.
 * 搜索-作品片段
 */
public class SearchWorksFrag extends BaseFrag {

    List<SearchWorksResult> mSearchWorksResultList = new ArrayList<>();
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.relevancy)
    LinearLayout mRelevancy;
    @Bind(R.id.most_playing)
    LinearLayout mMostPlaying;
    @Bind(R.id.most_collection)
    LinearLayout mMostCollection;
    @Bind(R.id.empty_hint)
    TextView mEmptyHint;
    private MAdapter mAdapter;
    private View.OnClickListener onRelevancyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeStyle(mRelevancy);
            if (mOnWorkOptionClickListener != null) {
                mOnWorkOptionClickListener.onWorkOptionClickListener(GlobalSearchActivity.sWorkSType[0]);
            }
        }
    };
    private View.OnClickListener onMostPlayingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeStyle(mMostPlaying);
            if (mOnWorkOptionClickListener != null) {
                mOnWorkOptionClickListener.onWorkOptionClickListener(GlobalSearchActivity.sWorkSType[1]);
            }
        }
    };
    private View.OnClickListener onMostCollectionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeStyle(mMostCollection);
            if (mOnWorkOptionClickListener != null) {
                mOnWorkOptionClickListener.onWorkOptionClickListener(GlobalSearchActivity.sWorkSType[2]);
            }
        }
    };

    public static SearchWorksFrag newInstance() {

        Bundle args = new Bundle();

        SearchWorksFrag fragment = new SearchWorksFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void update(List<?> list) {
        if (list.size() > 0&&mEmptyHint!=null) {
            mEmptyHint.setVisibility(View.GONE);
            if (list.get(0) instanceof SearchWorksResult && mAdapter != null) {
                mSearchWorksResultList = (List<SearchWorksResult>) list;
                mAdapter.notifyDataSetChanged();
            }
        } else {
//            mEmptyHint.setVisibility(View.VISIBLE);
        }

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventKey(OnEventKey event) {
//
//    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_works, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new MAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        //设置默认选中相关度
        changeStyle(mRelevancy);

        setListener();

        return view;
    }

    private void setListener() {
        mRelevancy.setOnClickListener(onRelevancyClickListener);
        mMostPlaying.setOnClickListener(onMostPlayingClickListener);
        mMostCollection.setOnClickListener(onMostCollectionClickListener);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class MAdapter extends RecyclerView.Adapter<MViewHolder> {

        ImageUtils mImageUtils;

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(getActivity(), R.layout.item_search_work, null);
            MViewHolder holder = new MViewHolder(itemView);
            mImageUtils = ImageUtils.getInstance(getActivity());
            return holder;
        }

        @Override
        public void onBindViewHolder(final MViewHolder holder, int position) {
            final SearchWorksResult searchWorksResult = mSearchWorksResultList.get(position);
            //封面
            String cover = searchWorksResult.getCover();
            String screenShot = searchWorksResult.getScreenshot();

            if(cover!=null&&!cover.isEmpty()&&!"null".equals(cover)) {
                cover = Configs.COVER_PREFIX + cover/*.replace(".", "_18.")*/;
                Glide.with(SearchWorksFrag.this).load(cover).placeholder(R.mipmap.bg_general).into(holder.workCover);
            } else if (screenShot!=null){
                if(searchWorksResult.getFlag()==0){
                    screenShot = Configs.COVER_PREFIX + screenShot.replace(".", "_18.");
                }else if(searchWorksResult.getFlag()==1){
                    screenShot=Configs.COVER_PREFIX+screenShot+"_18.png";
                }
                Glide.with(SearchWorksFrag.this).load(screenShot).placeholder(R.mipmap.bg_general).into(holder.workCover);
            }
            //标题
            holder.workTitle.setText(searchWorksResult.getName());
            //作者
            holder.workAuthor.setText("by" + searchWorksResult.getNickName());
            //播放量
            holder.workPlayCountColCount.setText(searchWorksResult.getPlayCount() + "播放·" + searchWorksResult.getLikeCount() + "收藏");
            //作品时长
            holder.workDuration.setText(StringUtil.getInstance().stringForTime(searchWorksResult.getDuration() * 1000));
            //创建时间
            holder.workCreateTime.setText(StringUtil.getInstance().stringForDate(searchWorksResult.getTime().getTime()));
            //点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startPlayerActivity(getActivity(),searchWorksResult.getId(), Configs.TYPE_WORK_POOL,holder.workCover);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSearchWorksResultList.size();
        }
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView workCover;
        private TextView workTitle;
        private TextView workAuthor;
        private TextView workPlayCountColCount;
        private TextView workDuration;
        private TextView workCreateTime;

        public MViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            workCover = (ImageView) itemView.findViewById(R.id.work_cover);
            workTitle = (TextView) itemView.findViewById(R.id.work_title);
            workAuthor = (TextView) itemView.findViewById(R.id.work_author);
            workPlayCountColCount = (TextView) itemView.findViewById(R.id.work_playcount_collcount);
            workDuration = (TextView) itemView.findViewById(R.id.work_duration);
            workCreateTime = (TextView) itemView.findViewById(R.id.work_create_time);
        }
    }

    private OnWorkOptionClickListener mOnWorkOptionClickListener;

    public void setOnWorkOptionClickListener(OnWorkOptionClickListener onWorkOptionClickListener) {
        mOnWorkOptionClickListener = onWorkOptionClickListener;
    }

    interface OnWorkOptionClickListener {
        void onWorkOptionClickListener(int stype);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
