package com.gaiamount.module_material.fragment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_material.fragment.RecomendFragment;
import com.gaiamount.util.StringUtil;

import java.util.List;

/**
 * Created by yukun on 16-9-29.
 */
public class MaterialListviewAdapter extends BaseAdapter {
    List<RecomendFragment.RecomendInfo> recomendInfos;
    Context mContext;

    public MaterialListviewAdapter(List<RecomendFragment.RecomendInfo> recomendInfos, Context context) {
        this.recomendInfos = recomendInfos;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return recomendInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return recomendInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MViewHolder holder=null;
        if(view==null){
            holder=new MViewHolder();
            view= LayoutInflater.from(mContext).inflate(R.layout.item_frag_player_rec,null);
            holder.cover_ = (ImageView) view.findViewById(R.id.player_rec_cover);
            holder.isOfficial_ = (ImageView) view.findViewById(R.id.player_rec_isOfficial);
            holder.is4k_ = (ImageView) view.findViewById(R.id.player_rec_is4k);
            holder.duration = (TextView) view.findViewById(R.id.player_rec_duration);
            holder.workTitle = (TextView) view.findViewById(R.id.player_rec_title);
            holder.workAuthor = (TextView) view.findViewById(R.id.player_rec_author);
            holder.workPublishTime = (TextView) view.findViewById(R.id.player_rec_publish_time);
            view.setTag(holder);
        }else {
            holder= (MViewHolder) view.getTag();
        }

        RecomendFragment.RecomendInfo playerRec = recomendInfos.get(position);
        //封面
        String screenshot = playerRec.getScreenshot();

        String cover = playerRec.getCover();
        if(cover!=null&&!cover.isEmpty()&&!"null".equals(cover)) {
            Glide.with(mContext).load(Configs.COVER_PREFIX+cover).placeholder(R.mipmap.bg_general).into(holder.cover_);
        }else if(playerRec.getScreenshot()!=null&&!playerRec.getScreenshot().isEmpty()){
            screenshot = Configs.COVER_PREFIX +screenshot+"_18.png";
            Glide.with(mContext).load(screenshot).placeholder(R.mipmap.bg_general).into(holder.cover_);
        }

        //时长
        long duration = playerRec.getDuration();
        holder.duration.setText(StringUtil.getInstance().stringForTime(((int)(duration/1000/60))));
        //标题
        String name = playerRec.getName();
        holder.workTitle.setText(name);
        //作者
        String nickName = playerRec.getNickName();
        holder.workAuthor.setText(nickName);

        return view;
    }
    class MViewHolder {
        ImageView cover_;
        ImageView isOfficial_;
        ImageView is4k_;
        TextView duration;
        TextView workTitle;
        TextView workAuthor;
        TextView workPublishTime;
    }
}
