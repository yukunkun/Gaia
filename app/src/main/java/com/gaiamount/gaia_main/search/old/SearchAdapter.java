package com.gaiamount.gaia_main.search.old;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_player.bean.VideoInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.StringUtil;

import java.util.List;

/**
 * Created by haiyang-lu on 16-4-8.
 * 展示搜索结果的列表适配器
 *
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<VideoInfo> videoInfoList;
    private final LayoutInflater inflater;
    private SearchActivity searchActivity;
    private final StringUtil mStringUtil;

    public SearchAdapter(SearchActivity searchActivity, List<VideoInfo> videoInfoList) {
        this.searchActivity = searchActivity;
        inflater = (LayoutInflater) searchActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.videoInfoList = videoInfoList;
        mStringUtil = StringUtil.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_search, null);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VideoInfo videoInfo = videoInfoList.get(position);
        ImageUtils.getInstance(searchActivity).getNetworkBitmap(holder.cover,videoInfo.getScreenshot());
        holder.duration.setText(mStringUtil.stringForTime(videoInfo.getDuration()*1000));
        holder.work_name.setText(videoInfo.getName());
        holder.author_name.setText(videoInfo.getNickName());
        holder.create_time.setText(mStringUtil.stringForDate(videoInfo.getTime().getTime()));
        holder.setOnItemClickListener(position);
    }

    @Override
    public int getItemCount() {
        return videoInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView cover;
        TextView duration;
        TextView work_name;
        TextView author_name;
        TextView create_time;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            cover = (ImageView) itemView.findViewById(R.id.work_cover);
            duration = (TextView) itemView.findViewById(R.id.work_duration);
            work_name = (TextView) itemView.findViewById(R.id.work_name);
            author_name = (TextView) itemView.findViewById(R.id.author_name);
            create_time = (TextView) itemView.findViewById(R.id.work_create_time);
        }

        public void setOnItemClickListener(final int position) {
            if (itemView != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //进入player界面
                        int id = videoInfoList.get(position).getId();
                        ActivityUtil.startPlayerActivity(searchActivity,id,0,cover);
                    }
                });
            }
        }
    }
}
