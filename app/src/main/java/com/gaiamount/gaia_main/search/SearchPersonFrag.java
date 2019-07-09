package com.gaiamount.gaia_main.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.gaia_main.search.beans.OnEventMaterialKey;
import com.gaiamount.gaia_main.search.beans.OnEventPerson;
import com.gaiamount.gaia_main.search.beans.SearchPersonResult;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.image.ImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haiyang-lu on 16-7-19.
 * 搜索-个人片段
 */
public class SearchPersonFrag extends BaseFrag {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.relevancy)
    LinearLayout mRelevancy;
    @Bind(R.id.most_creations)
    LinearLayout mMostCreations;
    @Bind(R.id.most_fans)
    LinearLayout mMostFans;
    @Bind(R.id.empty_hint)
    TextView mEmptyHint;
    private List<SearchPersonResult> mSearchPersonResults = new ArrayList<>();
    private MAdapter mAdapter;


    public static SearchPersonFrag newInstance() {

        Bundle args = new Bundle();

        SearchPersonFrag fragment = new SearchPersonFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void update(List<?> list) {
        if (list.size() > 0 && list.get(0) instanceof SearchPersonResult && mAdapter != null) {
            mEmptyHint.setVisibility(View.GONE);
            mSearchPersonResults = (List<SearchPersonResult>) list;
            mAdapter.notifyDataSetChanged();
        }
        if (list.size() == 0) {
            mEmptyHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //传过来的值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMaterialKey(OnEventPerson event) {
        mSearchPersonResults.clear();
        mSearchPersonResults = event.list;
        if (mAdapter != null) {

            mEmptyHint.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_person, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //设置默认选项为相关度
        changeStyle(mRelevancy);

        setListener();

        return view;
    }

    private void setListener() {
        mRelevancy.setOnClickListener(onRelevancyClickListener);
        mMostCreations.setOnClickListener(onMostCreationsClickListener);
        mMostFans.setOnClickListener(onMostFansClickListener);
    }

    private View.OnClickListener onRelevancyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeStyle(mRelevancy);
            if (mOnPersonOptionClickListener != null) {
                mOnPersonOptionClickListener.onPersonOptionClickListener(GlobalSearchActivity.sPersonOpr[0]);
            }
        }
    };
    private View.OnClickListener onMostCreationsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeStyle(mMostCreations);
            if (mOnPersonOptionClickListener != null) {
                mOnPersonOptionClickListener.onPersonOptionClickListener(GlobalSearchActivity.sPersonOpr[1]);
            }
        }
    };
    private View.OnClickListener onMostFansClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeStyle(mMostFans);
            if (mOnPersonOptionClickListener != null) {
                mOnPersonOptionClickListener.onPersonOptionClickListener(GlobalSearchActivity.sPersonOpr[2]);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class MAdapter extends RecyclerView.Adapter<MViewHolder> {

        private final ImageUtils mImageUtils;

        public MAdapter() {
            mImageUtils = ImageUtils.getInstance(getActivity());
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getActivity(), R.layout.item_search_person, null);
            MViewHolder holder = new MViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final MViewHolder holder, int position) {
            final SearchPersonResult result = mSearchPersonResults.get(position);
            //头像
            if(result.getAvatar()!=null&&!result.getAvatar().equals("null")){
                if(!result.getAvatar().endsWith(".jpg")&&!result.getAvatar().endsWith(".png")&&!result.getAvatar().endsWith(".jpeg")){
                    Glide.with(getContext()).load(Configs.COVER_PREFIX+result.getAvatar()+".jpg").placeholder(R.mipmap.ic_avatar_default).into(holder.avatar);
                }else {
                    Glide.with(getContext()).load(Configs.COVER_PREFIX+result.getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(holder.avatar);
                }
            }else {
                Glide.with(getContext()).load(Configs.COVER_PREFIX+result.getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(holder.avatar);
            }

//            mImageUtils.getAvatar(holder.avatar, result.getAvatar());
            holder.itemView/*.avatar*/.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startPersonalActivity(getActivity(),result.getId(),holder.avatar,null);
                }
            });
            //昵称
            holder.nickName.setText(result.getNickName());
            //数据
            holder.creationFansCount.setText(result.getCreateCount() + "创作·" + result.getLikeCount() + "粉丝");
        }

        @Override
        public int getItemCount() {
            return mSearchPersonResults.size();
        }
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView avatar;
        private TextView nickName;
        private TextView creationFansCount;

        public MViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            nickName = (TextView) itemView.findViewById(R.id.nick_name);
            creationFansCount = (TextView) itemView.findViewById(R.id.creation_fans_count);
        }
    }

    private OnPersonOptionClickListener mOnPersonOptionClickListener;

    public void setOnPersonOptionClickListener(OnPersonOptionClickListener onPersonOptionClickListener) {
        mOnPersonOptionClickListener = onPersonOptionClickListener;
    }

    interface OnPersonOptionClickListener {
        void onPersonOptionClickListener(int opr);
    }
}
