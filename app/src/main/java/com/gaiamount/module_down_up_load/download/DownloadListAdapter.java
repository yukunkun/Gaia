package com.gaiamount.module_down_up_load.download;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.BroadcastUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;

import java.util.List;

/**
 * Created by haiyang-lu on 16-5-17.
 * 下载列表适配器
 */
public class DownloadListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private Context mContext;
    private List<DownloadItem> mCheckedLists;

    public DownloadListAdapter(Context context, List<DownloadItem> list) {
        mContext = context;
        mCheckedLists = list;
        mInflater = LayoutInflater.from(context);

    }

    public void setCheckedLists(List<DownloadItem> checkedLists) {
        mCheckedLists = checkedLists;
    }

    @Override
    public int getCount() {
        return mCheckedLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_download, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setValue(position);
        return convertView;
    }

    class ViewHolder {
        CheckBox mCheckBox;
        ImageView cover;
        TextView videoName;
        TextView progressTxt;
        ProgressBar mProgressBar;
        Button status;
        private long mTaskProgress;
        private long mTaskTotalSize;
        private DownloadItem mDownloadItem;
        private DownloadUtil mDownloadUtil;
        private StringUtil mStringUtil;
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Uri uri = Uri.parse(mDownloadItem.url);

                mTaskProgress = mDownloadUtil.getTaskProgress(uri);
                mTaskTotalSize = mDownloadUtil.getTaskTotalSize(uri);
                mProgressBar.setMax((int) mTaskTotalSize);

                progressTxt.setText(mStringUtil.stringForSize(mTaskProgress) + "/" + mStringUtil.stringForSize(mTaskTotalSize));
                mProgressBar.setProgress((int) mTaskProgress);
                handler.sendEmptyMessageDelayed(0, 1000);

                //当相同时，移除handler，更新界面
                if (mTaskProgress == mTaskTotalSize) {
                    handler.removeMessages(0);

                }
            }
        };

        ViewHolder(View view) {
            mCheckBox = (CheckBox) view.findViewById(R.id.download_item_check);
            cover = (ImageView) view.findViewById(R.id.download_cover);
            videoName = (TextView) view.findViewById(R.id.download_item_name);
            progressTxt = (TextView) view.findViewById(R.id.download_item_progress_desc);
            mProgressBar = (ProgressBar) view.findViewById(R.id.download_item_progress);
            status = (Button) view.findViewById(R.id.download_pause_start_play);

            mDownloadUtil = DownloadUtil.getInstance(mContext);
            mStringUtil = StringUtil.getInstance();
        }

        public void setValue(final int position) {
            mDownloadItem = mCheckedLists.get(position);
            //checkBox
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCheckedLists.get(position).isChecked = isChecked;
                }
            });
            if (mDownloadItem.isShow) {
                mCheckBox.setVisibility(View.VISIBLE);
                if (mDownloadItem.isChecked) {
                    mCheckBox.setChecked(true);
                } else {
                    mCheckBox.setChecked(false);
                }
            } else {
                mCheckBox.setVisibility(View.GONE);
                mCheckBox.setChecked(false);
            }

            //名称
            if (mDownloadItem.videoName != null) {
                videoName.setText(mDownloadItem.videoName);
            } else {
                videoName.setText(R.string.unkown);
            }

            //进度
            long videoSize = mDownloadItem.videoSize;
            long downloadedSize = mDownloadItem.downloadedSize;

            if (videoSize == downloadedSize && videoSize != 0) {
                mProgressBar.setVisibility(View.INVISIBLE);
                //进度描述
                progressTxt.setText(mStringUtil.stringForSize(videoSize) + "/" + mContext.getString(R.string.finished));
                //播放
                status.setText(R.string.play);
                status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDownloadItem.id != 0) {
                            ActivityUtil.startPlayerActivity((Activity) mContext, (int) mDownloadItem.id, mDownloadItem.contentType,cover);
                        } else {
                            ActivityUtil.startSystemPlayer(mContext, mDownloadItem.data);

                        }
                    }
                });

            } else {
                handler.sendEmptyMessage(0);

                status.setText(R.string.cancel_download);
                status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDownloadUtil.deleteTask(Uri.parse(mDownloadItem.url));
                        //更新界面
                        mCheckedLists.remove(mDownloadItem);
                        notifyDataSetChanged();
                        //提示
                        GaiaApp.showToast(mContext.getString(R.string.deleted));
                    }
                });

                BroadcastUtils.registerDownloadComplete(mContext, new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        //更新界面
                        mProgressBar.setVisibility(View.INVISIBLE);
                        progressTxt.setText(mContext.getString(R.string.finished) + "/" + mTaskTotalSize);
                        status.setText(R.string.play);
                        status.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityUtil.startSystemPlayer(mContext, mDownloadItem.data);
                            }
                        });
                    }
                });
            }
            //封面
            cover.setImageBitmap(ImageUtils.getVideoThumbnail(mDownloadItem.data));


        }


    }

}
