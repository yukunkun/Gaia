package com.gaiamount.gaia_main.history;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;

import java.util.List;

/**
 * Created by haiyang-lu on 16-7-26.
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MViewHolder> {

    private  Activity activity;
    private  List<OnEventHistory> mList;
    private  ImageUtils mImageUtils;

    public HistoryListAdapter(Activity activity, List<OnEventHistory> list) {

        this.activity = activity;
        mList = list;
        mImageUtils = ImageUtils.getInstance(activity);

    }

    @Override
    public HistoryListAdapter.MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_history, null);
        MViewHolder holder = new MViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final HistoryListAdapter.MViewHolder holder, int position) {
        final OnEventHistory history = mList.get(position);
        //图片
        mImageUtils.showImage(holder.ivCover, history.getCover(), history.getScreenShot());
        //名称
        holder.tvName.setText(history.getName());
        holder.tvTimeStamp.setText(StringUtil.getInstance().stringForDate(history.getTimeStamp()));
        //点击进入播放页面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startPlayerActivity(activity,history.getId(),history.getContentType(),holder.ivCover);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView ivCover;
        TextView tvName;
        TextView tvTimeStamp;

        public MViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivCover = (ImageView) itemView.findViewById(R.id.cover);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
