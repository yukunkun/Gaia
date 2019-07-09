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

import com.gaiamount.R;
import com.gaiamount.gaia_main.search.beans.SearchGroupResult;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.widgets.recyc.RecyclerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-7-19.
 * 搜索 小组片段
 */
public class SearchGroupFrag extends BaseFrag {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.relevancy)
    LinearLayout mRelevancy;
    @Bind(R.id.most_creations)
    LinearLayout mMostCreations;
    @Bind(R.id.most_members)
    LinearLayout mMostMembers;
    @Bind(R.id.empty_hint)
    TextView mEmptyHint;
    private List<SearchGroupResult> mSearchGroupResultList = new ArrayList<>();
    private MAdapter mAdapter;
    private View.OnClickListener onRelevancyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeStyle(mRelevancy);
            if (mOnGroupOptionClickListener != null) {
                mOnGroupOptionClickListener.onGroupOptionClickListener(GlobalSearchActivity.sGroupOpr[0]);
            }
        }
    };
    private View.OnClickListener onMostCreationsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeStyle(mMostCreations);
            if (mOnGroupOptionClickListener != null) {
                mOnGroupOptionClickListener.onGroupOptionClickListener(GlobalSearchActivity.sGroupOpr[1]);
            }
        }
    };
    private View.OnClickListener onMostMembersClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeStyle(mMostMembers);
            if (mOnGroupOptionClickListener != null) {
                mOnGroupOptionClickListener.onGroupOptionClickListener(GlobalSearchActivity.sGroupOpr[2]);
            }
        }
    };

    public static SearchGroupFrag newInstance() {

        Bundle args = new Bundle();

        SearchGroupFrag fragment = new SearchGroupFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void update(List<?> list) {
        if (list.size() > 0) {
            mEmptyHint.setVisibility(View.GONE);
            if (list.get(0) instanceof SearchGroupResult && mAdapter != null) {
                mSearchGroupResultList = (List<SearchGroupResult>) list;
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mEmptyHint.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_group, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(ScreenUtils.dp2Px(getActivity(), 10)));

        //设置默认选中相关度
        changeStyle(mRelevancy);

        setListener();

        return view;
    }

    private void setListener() {
        mRelevancy.setOnClickListener(onRelevancyClickListener);
        mMostCreations.setOnClickListener(onMostCreationsClickListener);
        mMostMembers.setOnClickListener(onMostMembersClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class MAdapter extends RecyclerView.Adapter<MViewHolder> {
        private int mCoverHeight;

        public MAdapter() {
            //动态计算图片的高度
            int width = ScreenUtils.instance().getWidth();
            mCoverHeight = (int) (width * (9f / 21f));
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getActivity(), R.layout.item_search_group, null);
            MViewHolder holder = new MViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            final SearchGroupResult groupInfo = mSearchGroupResultList.get(position);
            String background = groupInfo.getBackground();

            MViewHolder mViewHolder = holder;
            mViewHolder.groupTitle.setText(groupInfo.getName());
            mViewHolder.groupSubTitleAuthor.setText(groupInfo.getNickName());
            mViewHolder.groupSubTitleTime.setText(StringUtil.getInstance().stringForDate(groupInfo.getCreateTime().getTime()));

            if (background != null) {
                ImageUtils.getInstance(getActivity()).getCover(mViewHolder.groupCover, background);
            }
            mViewHolder.groupDesc.setText(groupInfo.getDescription());
            mViewHolder.groupMemberCount.setText(String.valueOf(groupInfo.getMemberCount()));
            mViewHolder.groupWorksCount.setText(String.valueOf(groupInfo.getCreationCount()));
            //点击图片进入小组
            mViewHolder.groupCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startGroupActivity(getActivity(), groupInfo.getId());
                }
            });

            setImageHeight(mViewHolder.groupCover);
        }

        private void setImageHeight(View itemView) {
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = mCoverHeight;
            itemView.setLayoutParams(layoutParams);
        }


        @Override
        public int getItemCount() {
            return mSearchGroupResultList.size();
        }
    }


    class MViewHolder extends RecyclerView.ViewHolder {
        private TextView groupTitle;
        private TextView groupSubTitleAuthor;
        private TextView groupSubTitleTime;
        private TextView groupDesc;
        private TextView groupMemberCount;
        private TextView groupWorksCount;
        private ImageView groupCover;

        public MViewHolder(View itemView) {
            super(itemView);
            groupTitle = (TextView) itemView.findViewById(R.id.group_title);
            groupSubTitleAuthor = (TextView) itemView.findViewById(R.id.group_sub_title_author);
            groupSubTitleTime = (TextView) itemView.findViewById(R.id.group_sub_title_time);
            groupDesc = (TextView) itemView.findViewById(R.id.group_desc);
            groupMemberCount = (TextView) itemView.findViewById(R.id.group_member_count);
            groupWorksCount = (TextView) itemView.findViewById(R.id.group_video_count);
            groupCover = (ImageView) itemView.findViewById(R.id.group_cover);
        }
    }

    private OnGroupOptionClickListener mOnGroupOptionClickListener;

    public void setOnGroupOptionClickListener(OnGroupOptionClickListener onGroupOptionClickListener) {
        mOnGroupOptionClickListener = onGroupOptionClickListener;
    }

    interface OnGroupOptionClickListener {
        void onGroupOptionClickListener(int opr);
    }
}
