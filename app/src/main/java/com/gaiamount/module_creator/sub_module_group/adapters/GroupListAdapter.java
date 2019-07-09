package com.gaiamount.module_creator.sub_module_group.adapters;

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
import com.gaiamount.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by haiyang-lu on 16-6-13.
 * 创作者-小组的列表的适配器
 */
public class GroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private static final int TYPE_0 = 0;
//    private static final int TYPE_1 = 1;
    private  Context mContext;
    private  List<GroupInfo> mGroupInfoList;
    private  int mCoverHeight;

    public GroupListAdapter(Context context, List<GroupInfo> groupInfoList) {

        mContext = context;
        mGroupInfoList = groupInfoList;
        //动态计算图片的高度
        int width  = ScreenUtils.instance().getWidth();
        mCoverHeight = (int) (width*(9f/21f));
    }

    public void setGroupInfoList(List<GroupInfo> groupInfoList) {
        mGroupInfoList = groupInfoList;
    }


//    @Override
//    public int getItemViewType(int position) {
//        if (position==0) {
//            return TYPE_0;
//        }else {
//            return TYPE_1;
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType==TYPE_0) {
//            View view = View.inflate(mContext, R.layout.item_creator_group_type0, null);
//            MViewHolder2 holder = new MViewHolder2(view);
//            return holder;
//        }else if (viewType==TYPE_1) {
//            View view = View.inflate(mContext, R.layout.item_search_group, null);
//
//            MViewHolder holder = new MViewHolder(view);
//            return holder;
//        }
//        return null;

//        View view = View.inflate(mContext, R.layout.item_search_group, null);
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
//        if (position==0) {
//            MViewHolder2 holder2 = (MViewHolder2) holder;
//            holder2.addGroup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ActivityUtil.startAddGroupActivity(mContext);
//                }
//            });
//        }else {
//            final GroupInfo groupInfo = mGroupInfoList.get(position-1);
//            String background = groupInfo.getBackground();
//
//            MViewHolder mViewHolder = (MViewHolder) holder;
//            mViewHolder.groupTitle.setText(groupInfo.getName());
//            mViewHolder.groupSubTitleAuthor.setText(groupInfo.getNickName());
//            mViewHolder.groupSubTitleTime.setText("创立于"+StringUtil.getInstance().stringForDate(groupInfo.getCreateTime().getTime()));
//
//            if (background !=null) {
//                ImageUtils.getInstance(mContext).getCover(mViewHolder.groupCover, background);
//            }
//            mViewHolder.groupDesc.setText(groupInfo.getDescription());
//            mViewHolder.groupMemberCount.setText(String.valueOf(groupInfo.getMemberCount()));
//            mViewHolder.groupWorksCount.setText(String.valueOf(groupInfo.getCreationCount()));
//            //点击图片进入小组
//            mViewHolder.groupCover.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ActivityUtil.startGroupActivity(mContext,groupInfo.getId());
//                }
//            });
//
//            setImageHeight(mViewHolder.groupCover);
//        }

        final GroupInfo groupInfo = mGroupInfoList.get(position);
        String background = groupInfo.getBackground();

        MViewHolder mViewHolder = (MViewHolder) holder;
        mViewHolder.groupTitle.setText(groupInfo.getName());
        mViewHolder.groupSubTitleAuthor.setText(groupInfo.getNickName());
        mViewHolder.groupSubTitleTime.setText(getTime(groupInfo.getCreateTime().getTime())/*StringUtil.getInstance().stringForDate(groupInfo.getCreateTime().getTime())*/);

//        if (background !=null) {
//            ImageUtils.getInstance(mContext).getCover(mViewHolder.groupCover, background);
            Glide.with(mContext).load(Configs.COVER_PREFIX+background).placeholder(R.mipmap.group_top_bg).into(mViewHolder.groupCover);
//        }

        mViewHolder.groupDesc.setText(groupInfo.getDescription());
        mViewHolder.groupMemberCount.setText(String.valueOf(groupInfo.getCreationCount())+"创作·"+String.valueOf(groupInfo.getMemberCount())+"访问");
//        mViewHolder.groupMemberCount.setText(String.valueOf(groupInfo.getMemberCount()));
//        mViewHolder.groupWorksCount.setText(String.valueOf(groupInfo.getCreationCount()));
        //点击图片进入小组
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startGroupActivity(mContext,groupInfo.getId());
            }
        });

        setImageHeight(mViewHolder.groupCover);


    }

    @Override
    public int getItemCount() {
//        return mGroupInfoList.size()+1;
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

    class MViewHolder2 extends RecyclerView.ViewHolder{

        private LinearLayout addGroup;

        public MViewHolder2(View itemView) {
            super(itemView);
            addGroup = (LinearLayout) itemView.findViewById(R.id.add_group);
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
