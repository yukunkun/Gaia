package com.gaiamount.module_creator.fragment.present;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_creator.beans.GroupInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by haiyang-lu on 16-6-13.
 * 创作者-小组的列表的适配器
 */
public class OrginAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  Context mContext;
    private  List<OrgBean> mGroupInfoList;
    private  int mCoverHeight;

    public OrginAdapter(Context context, List<OrgBean> groupInfoList) {

        mContext = context;
        mGroupInfoList = groupInfoList;
        //动态计算图片的高度
        int width  = ScreenUtils.instance().getWidth();
        mCoverHeight = (int) (width*(9f/21f));
    }

    public void setGroupInfoList(List<OrgBean> groupInfoList) {
        mGroupInfoList = groupInfoList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.creater_group_layout, null);
        MViewHolder holder = new MViewHolder(view);
        return holder;
    }

    private void setImageHeight(View itemView) {
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height = mCoverHeight;
        itemView.setLayoutParams(layoutParams);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final OrgBean groupInfo = mGroupInfoList.get(position);

        MViewHolder mViewHolder = (MViewHolder) holder;
        mViewHolder.groupTitle.setText(groupInfo.getName());
        mViewHolder.groupSubTitleAuthor.setText(groupInfo.getNickName());
//        mViewHolder.groupSubTitleTime.setText(getTime(groupInfo.getCreateTime().getTime())/*StringUtil.getInstance().stringForDate(groupInfo.getCreateTime().getTime())*/);
//
        Glide.with(mContext).load(Configs.COVER_PREFIX+groupInfo.getBackground()).placeholder(R.mipmap.group_top_bg).into(mViewHolder.groupCover);
//
        mViewHolder.groupDesc.setText(groupInfo.getResume());
        mViewHolder.groupMemberCount.setText(String.valueOf(groupInfo.getCreateCount())+"创作·"+String.valueOf(groupInfo.getLikeCount())+"粉丝·"+groupInfo.getBrowseCount()+"浏览量");
//        //点击图片进入小组
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startPersonalActivity(mContext,groupInfo.getId());
            }
        });
        setImageHeight(mViewHolder.groupCover);
    }

    @Override
    public int getItemCount() {
        return mGroupInfoList.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder{
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

    private String getTime(Long msgTime) {
        long l = System.currentTimeMillis();
        if(l-msgTime<3600000){
            SimpleDateFormat formatter = new SimpleDateFormat    ("mm");
            Date curDate = new Date(msgTime);//获取时间
            String str1 = formatter.format(curDate);
            Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate2);
            int i = Integer.valueOf(str) - Integer.valueOf(str1);
            if(i>=0){
                return i+mContext.getString(R.string.time_minute_ago);
            }else {
                return 60+i+mContext.getString(R.string.time_minute_ago);
            }
        }
        if(3600000<l-msgTime&l-msgTime<86400000){
            SimpleDateFormat formatter = new SimpleDateFormat    ("hh");
            Date curDate = new Date(msgTime);//获取时间
            String str1 = formatter.format(curDate);
            Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate2);
            int i = Integer.valueOf(str) - Integer.valueOf(str1);
            if(i>=0){
                return i+mContext.getString(R.string.time_hours_ago);
            }else {
                return 24+i+mContext.getString(R.string.time_hours_ago);
            }
        }else if(l-msgTime>86400000 & l-msgTime<=86400000*2){
            return mContext.getString(R.string.one_day_ago);
        }else if(l-msgTime>86400000*2 & l-msgTime<=86400000*3){
            return mContext.getString(R.string.two_day_ago);
        }else if(l-msgTime>86400000*3 & l-msgTime<=86400000*4) {
            return mContext.getString(R.string.three_day_ago);
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat    ("yyyy/MM/dd");
            Date curDate = new Date(msgTime);//获取时间
            return formatter.format(curDate);
        }
    }
}
