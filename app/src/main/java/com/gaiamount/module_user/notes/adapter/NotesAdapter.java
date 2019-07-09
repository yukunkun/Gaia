package com.gaiamount.module_user.notes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_user.notes.bean.NoteInfo;
import com.gaiamount.util.ActivityUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yukun on 16-11-4.
 */
public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<NoteInfo> noteInfos;
    private long uid;

    public NotesAdapter(Context context, ArrayList<NoteInfo> noteInfos,long uid) {
        this.context=context;
        this.noteInfos=noteInfos;
        this.uid=uid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.notes_item_rec, null);
        return new NotesViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof NotesViewHolder){
            ((NotesViewHolder) holder).textViewTitle.setText(noteInfos.get(position).getTitle());
            ((NotesViewHolder) holder).textViewName.setText(noteInfos.get(position).getNickName());
            ((NotesViewHolder) holder).textViewCont.setText(noteInfos.get(position).getText());
            ((NotesViewHolder) holder).textViewName.setText(noteInfos.get(position).getNickName());
            Glide.with(context).load(Configs.COVER_PREFIX+noteInfos.get(position).getCover()).into(((NotesViewHolder) holder).imageViewHead);
            int reprintId = noteInfos.get(position).getReprintId();
            if(reprintId==0){
                ((NotesViewHolder) holder).textViewTag.setText("原创");
                ((NotesViewHolder) holder).textViewTag.setBackgroundResource((R.color.color_ff5773));
            }else if(reprintId>0){
                ((NotesViewHolder) holder).textViewTag.setText("转载");
                ((NotesViewHolder) holder).textViewTag.setBackgroundResource((R.color.color_007aff));
            }
            long time = noteInfos.get(position).getTime();
            String time1 = getTime(time);
            ((NotesViewHolder) holder).textViewTime.setText(time1);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reprintId = noteInfos.get(position).getReprintId();
                if(reprintId==0){
                    ActivityUtil.startNOtesDetailActivity(context,uid,noteInfos.get(position).getId());
                }else {
                    ActivityUtil.startNOtesDetailActivity(context,uid,noteInfos.get(position).getReprintId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteInfos.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTag,textViewTitle,textViewCont,textViewName,textViewTime;
        ImageView imageViewHead;
        public NotesViewHolder(View itemView) {
            super(itemView);
            textViewTag= (TextView) itemView.findViewById(R.id.notes_list_tag);
            textViewTitle= (TextView) itemView.findViewById(R.id.notes_list_title);
            textViewCont= (TextView) itemView.findViewById(R.id.notes_list_cont);
            textViewName= (TextView) itemView.findViewById(R.id.notes_list_name);
            textViewTime= (TextView) itemView.findViewById(R.id.notes_list_time);
            imageViewHead= (ImageView) itemView.findViewById(R.id.notes_list_head);
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
                return i+context.getString(R.string.time_minute_ago);
            }else {
                return 60+i+context.getString(R.string.time_minute_ago);
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
                return i+context.getString(R.string.time_hours_ago);
            }else {
                return 12+i+context.getString(R.string.time_hours_ago);
            }
        }else if(l-msgTime>86400000 & l-msgTime<=86400000*2){
            return context.getString(R.string.one_day_ago);
        }else if(l-msgTime>86400000*2 & l-msgTime<=86400000*3){
            return context.getString(R.string.two_day_ago);
        }else if(l-msgTime>86400000*3 & l-msgTime<=86400000*4) {
            return context.getString(R.string.three_day_ago);
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat    ("yyyy/MM/dd");
            Date curDate = new Date(msgTime);//获取时间
            return formatter.format(curDate);
        }
    }
}
