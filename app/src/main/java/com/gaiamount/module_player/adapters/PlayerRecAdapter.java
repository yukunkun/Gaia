package com.gaiamount.module_player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_player.bean.PlayerRec;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;

import java.util.List;

/**
 * Created by haiyang-lu on 16-4-29.
 */
public class PlayerRecAdapter extends BaseAdapter {
    private Context mContext;
    private List<PlayerRec> mPlayerRecList;
    private LayoutInflater mInflater;

    public PlayerRecAdapter(Context context, List<PlayerRec> playerRecList) {
        mContext = context;
        mPlayerRecList = playerRecList;
        if (mContext!=null) {
            mInflater = LayoutInflater.from(mContext);
        }
    }

    @Override
    public int getCount() {
        return mPlayerRecList.size();
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
        if (convertView == null&&mInflater!=null) {
            convertView = mInflater.inflate(R.layout.item_frag_player_rec, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setValue(position);
        return convertView;
    }

    class ViewHolder {
        ImageView cover_;
        ImageView isOfficial_;
        ImageView is4k_;
        TextView duration;
        TextView workTitle;
        TextView workAuthor;
        TextView workPublishTime;

        public ViewHolder(View view) {
            cover_ = (ImageView) view.findViewById(R.id.player_rec_cover);
            isOfficial_ = (ImageView) view.findViewById(R.id.player_rec_isOfficial);
            is4k_ = (ImageView) view.findViewById(R.id.player_rec_is4k);
            duration = (TextView) view.findViewById(R.id.player_rec_duration);
            workTitle = (TextView) view.findViewById(R.id.player_rec_title);
            workAuthor = (TextView) view.findViewById(R.id.player_rec_author);
            workPublishTime = (TextView) view.findViewById(R.id.player_rec_publish_time);
        }

        public void setValue(int position) {
            PlayerRec playerRec = mPlayerRecList.get(position);
            //封面
            String cover = playerRec.getCover();
            String screenShot = playerRec.getScreenshot();
            if(cover!=null&&!cover.isEmpty()&&!"null".equals(cover)) {
                cover = Configs.COVER_PREFIX + cover/*.replace(".", "_18.")*/;
                Glide.with(mContext).load(cover).placeholder(R.mipmap.bg_general).into(cover_);
            } else if (screenShot!=null){
                screenShot = Configs.COVER_PREFIX + screenShot.replace(".", "_18.");
                Glide.with(mContext).load(screenShot).placeholder(R.mipmap.bg_general).into(cover_);
            }

            //是否官方
            int isOfficial = playerRec.getIsOfficial();
            isOfficial_.setVisibility(isOfficial == 1 ? View.VISIBLE : View.GONE);
            //是否4k
            int is4k = playerRec.getIs4K();
            is4k_.setVisibility(is4k == 1 ? View.VISIBLE : View.GONE);
            //时长
            int duration = playerRec.getDuration();
            this.duration.setText(StringUtil.getInstance().stringForTime(duration * 1000));
            //标题
            String name = playerRec.getName();
            workTitle.setText(name);
            //作者
            String nickName = playerRec.getNickName();
            workAuthor.setText(nickName);
            //发布时间
            long time = playerRec.getTime().getTime();
            workPublishTime.setText(StringUtil.getInstance().stringForDate(time));
        }
    }
}
