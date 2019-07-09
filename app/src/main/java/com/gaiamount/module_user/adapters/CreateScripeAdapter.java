package com.gaiamount.module_user.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.module_user.person_creater.events.OnEventMaterial;
import com.gaiamount.module_user.person_creater.events.OnEventScript;
import com.gaiamount.module_user.person_creater.fragments.ScriptFrag;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yukun on 16-10-21.
 */
public class CreateScripeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList <ScripeInfo> scripeInfos;
    private long uid;

    public CreateScripeAdapter(Context context, ArrayList<ScripeInfo> scripeInfos) {
        this.context = context;
        this.scripeInfos = scripeInfos;
    }

    public  void update(long uid){
        this.uid=uid;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(context).inflate(R.layout.scripe_list_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyViewHolder){
            
            ((MyViewHolder) holder).textViewTitle.setText(scripeInfos.get(position).getTitle());
            ((MyViewHolder) holder).textViewContent.setText(scripeInfos.get(position).getOutline());
            ((MyViewHolder) holder).textViewLook.setText(scripeInfos.get(position).getBrowserCount()+"");
            ((MyViewHolder) holder).textViewLike.setText(scripeInfos.get(position).getCollectCount()+"");
            int isFree = scripeInfos.get(position).getIsFree();

            if(isFree==1){
                ((MyViewHolder) holder).textViewShow.setText("仅展示");
                ((MyViewHolder) holder).textViewShow.setTextColor(context.getResources().getColor(R.color.color_33bbff));

            }else {
                ((MyViewHolder) holder).textViewShow.setText(scripeInfos.get(position).getPrice()+"");
            }
            int state = scripeInfos.get(position).getState();
            if(state==1){
                ((MyViewHolder) holder).textViewTag.setText("已完结");
                ((MyViewHolder) holder).textViewTag.setBackgroundResource(R.drawable.shape_scripe_tag);

            }else {

                ((MyViewHolder) holder).textViewTag.setText("连载中");
                ((MyViewHolder) holder).textViewTag.setBackgroundResource(R.drawable.shape_scripe_tag_over);
            }
            ((MyViewHolder) holder).textViewTime.setText(getTime(scripeInfos.get(position).getTime()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startScripeDetailActivity(context,scripeInfos.get(position).getSid());
                }
            });
            if(uid== GaiaApp.getAppInstance().getUserInfo().id){
                ((MyViewHolder) holder).imageViewMore.setVisibility(View.VISIBLE);
            }

            ((MyViewHolder) holder).imageViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopu(((MyViewHolder) holder).imageViewMore, scripeInfos.get(position).getSid(), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return scripeInfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  textViewTag,textViewTitle,textViewContent,textViewShow,textViewTime,
        textViewLook,textViewLike;
        ImageView imageViewMore;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewTag=(TextView) itemView.findViewById(R.id.scripe_tag);
            textViewTitle=(TextView) itemView.findViewById(R.id.scripe_title);
            textViewContent=(TextView) itemView.findViewById(R.id.scripe_content);
            textViewShow=(TextView) itemView.findViewById(R.id.scripe_show);
            textViewTime=(TextView) itemView.findViewById(R.id.scripe_time);
            textViewLook=(TextView) itemView.findViewById(R.id.scripe_look);
            textViewLike=(TextView) itemView.findViewById(R.id.scripe_like);
            imageViewMore= (ImageView) itemView.findViewById(R.id.scripe_more);

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
                return 24+i+context.getString(R.string.time_hours_ago);
            }
        }else if(l-msgTime>86400000 & l-msgTime<=86400000*2){
            return context.getString(R.string.one_day_ago);
        }else if(l-msgTime>86400000*2 & l-msgTime<=86400000*3){
            return context.getString(R.string.two_day_ago);
        }else if(l-msgTime>86400000*3 & l-msgTime<=86400000*4) {
            return context.getString(R.string.three_day_ago);
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date curDate = new Date(msgTime);//获取时间
            return formatter.format(curDate);
        }
    }


    private PopupWindow popupWindow;
    private void showPopu(ImageView view, long sid, int position) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.academy_collection_shanchu, null);
        popupWindow=new PopupWindow(inflate,ScreenUtils.dp2Px(context, 100), LinearLayout.LayoutParams.WRAP_CONTENT);
        //移除专辑
        TextView textView= (TextView) inflate.findViewById(R.id.academy_collect_delete);
        TextView textView1= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
        //设置值
        textView.setText("删除素材");
        textView.setOnClickListener(new Listener(sid,position));
        textView1.setOnClickListener(new Listener(sid,position));

        //獲取焦點
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //測量控件寬高
        inflate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = inflate.getMeasuredWidth();
        int popupHeight = inflate.getMeasuredHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        //在控件上方显示
        popupWindow.showAsDropDown(view,0-popupWidth+ScreenUtils.dp2Px(context, 25),0-popupHeight- ScreenUtils.dp2Px(context, 25));
    }

    private class Listener implements View.OnClickListener {
        long sid;
        int position;
        public Listener(long sid,int position) {
            this.sid=sid;
            this.position=position;
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.academy_collect_delete){
                collectNet(sid,position);
            }
            else if(v.getId()==R.id.academy_collect_deletes){
                EventBus.getDefault().post(new OnEventScript(position));
            }
            popupWindow.dismiss();
        }
    }
    private void collectNet(final long sid, final int position) {

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(ScriptFrag.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                    scripeInfos.remove(position);
                    notifyItemRemoved(position);
                    GaiaApp.showToast("删除成功");
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                GaiaApp.showToast("删除失败");
            }
        };
        ScriptApiHelper.getScriptDel(sid,handler);
    }
}
