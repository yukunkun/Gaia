package com.gaiamount.module_user.personal.collections;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-4.
 */
public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<MixInfo> mixInfos;
    private MixViewHolder holder;
    private PopupWindow popupWindow;
    private long uid;

    public CollectionAdapter(Context context, ArrayList<MixInfo> mixInfos,long uid){
        this.context=context;
        this.mixInfos=mixInfos;
        this.uid=uid;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.academy_collection_item, null);
        holder = new MixViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MixInfo mixInfo = mixInfos.get(position);
        ((MixViewHolder)holder).textViewLearn.setText(StringUtil.getInstance().stringForLeare(mixInfo.getLearningCount()));
        ((MixViewHolder)holder).textViewIntroduce.setText(mixInfo.getName());
        ((MixViewHolder)holder).textViewChapter.setText(mixInfo.getChaptCount()+context.getString(R.string.academy_chapter)+mixInfo.getHourCount()+context.getString(R.string.academy_jie));
        ((MixViewHolder)holder).textViewScan.setText(mixInfo.getPlayCount()+"");
        ((MixViewHolder)holder).textViewLike.setText(mixInfo.getBrowseCount()+"");
        ((MixViewHolder)holder).textViewGrade.setText(mixInfo.getGrade()+"");
        ((MixViewHolder)holder).textViewNickname.setText(mixInfo.getNickName());

        ViewGroup.LayoutParams layoutParams = ((MixViewHolder)holder).imageViewImg.getLayoutParams();
        layoutParams.height = getHeight();
        ((MixViewHolder)holder).imageViewImg.setLayoutParams(layoutParams);

        Glide.with(context).load(Configs.COVER_PREFIX +mixInfo.getCover()).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).imageViewImg);
        Glide.with(context).load(Configs.COVER_PREFIX+mixInfo.getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(((MixViewHolder)holder).imageViewAvator);
        int type = mixInfo.getType();
        //判断视频类型
        if(type==0){
            ((MixViewHolder)holder).imageViewDevide.setImageResource(R.mipmap.ic_video);
        }else if(type==1){
            ((MixViewHolder)holder).imageViewDevide.setImageResource(R.mipmap.ic_tw);
        }else if(type==2){
            ((MixViewHolder)holder).imageViewDevide.setImageResource(R.mipmap.ic_live);
        }
        int proprity = mixInfo.getProprity();
        if(proprity==1){
            if((((MixViewHolder)holder).imageViewRecommend).getVisibility()==View.GONE)
            ((MixViewHolder)holder).imageViewRecommend.setVisibility(View.VISIBLE);
        }else if(proprity==0){
            ((MixViewHolder)holder).imageViewRecommend.setVisibility(View.GONE);
        }

        if(mixInfo.getAllowFree()==1){
            ((MixViewHolder)holder).textViewPrice.setText(context.getResources().getString(R.string.for_free));
            ((MixViewHolder)holder).textViewPrice.setTextColor(context.getResources().getColor(R.color.color_009944));
        }else {
            ((MixViewHolder)holder).textViewPrice.setText(mixInfo.getPrice()+"");
            ((MixViewHolder)holder).textViewPrice.setTextColor(context.getResources().getColor(R.color.color_ff5773));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startAcademyDetailActivity(Long.valueOf(mixInfo.getId()),context);
            }
        });

        if(uid==GaiaApp.getUserInfo().id){
            ((MixViewHolder)holder).imageViewMore.setVisibility(View.VISIBLE);
        }

        ((MixViewHolder)holder).imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopu(((MixViewHolder)holder).imageViewMore,mixInfo.getId(),position);
            }

            private void showPopu(ImageView view, long cid, int position) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.academy_collection_shanchu, null);
                popupWindow=new PopupWindow(inflate,200, LinearLayout.LayoutParams.WRAP_CONTENT);
                //移除专辑
                TextView textView= (TextView) inflate.findViewById(R.id.academy_collect_delete);
                TextView textView1= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
                textView1.setVisibility(View.GONE);
                //设置值
                textView.setText("取消收藏");
                textView.setOnClickListener(new Listener(cid,position));
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
                popupWindow.showAsDropDown(view,0-popupWidth+ScreenUtils.dp2Px(context, 15),0-popupHeight-ScreenUtils.dp2Px(context, 25));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mixInfos.size();
    }

    class MixViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewIntroduce,textViewPrice,textViewLearn,textViewChapter,textViewScan,textViewLike,textViewGrade,textViewNickname;
        public ImageView imageViewImg, imageViewDevide,imageViewRecommend,imageViewAvator,imageViewMore;
        public MixViewHolder(View itemView) {
            super(itemView);
            textViewIntroduce= (TextView) itemView.findViewById(R.id.academy_mix_lesson_name);
            textViewPrice= (TextView) itemView.findViewById(R.id.academy_mix_price);
            textViewLearn= (TextView) itemView.findViewById(R.id.academy_mix_learn_num);
            textViewChapter=(TextView) itemView.findViewById(R.id.academy_textview_learn);
            textViewScan= (TextView) itemView.findViewById(R.id.academy_mix_watchs);
            textViewLike=(TextView) itemView.findViewById(R.id.academy_mix_likes);
            textViewGrade=(TextView) itemView.findViewById(R.id.academy_mix_stars);
            textViewNickname=(TextView) itemView.findViewById(R.id.academy_mix_username);
            imageViewImg= (ImageView) itemView.findViewById(R.id.academy_img_map);
            imageViewDevide= (ImageView) itemView.findViewById(R.id.academy_devide);
            imageViewAvator=(ImageView)itemView.findViewById(R.id.academy_mix_head);
            imageViewRecommend= (ImageView) itemView.findViewById(R.id.academy_recomend);
            imageViewMore= (ImageView) itemView.findViewById(R.id.academy_more);
            //点击事件
        }
    }
    class Listener implements View.OnClickListener {
        private long cid;
        private int position;

        public Listener(long cid, int position) {
            this.cid = cid;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
//            addColect(cid,position);
            cancelCollection(cid,position);
            popupWindow.dismiss();
        }
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width/2)*0.52);
        return itemHeight;
    }

    private void addColect(long cid, final int position) {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(CollectionFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                long o = response.optLong("o");
                cancelCollection(o,position);
            }
        };
        AcademyApiHelper.getCollectIs(cid,handler);
    }
    private void cancelCollection(long o, final int position) {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(CollectionFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                mixInfos.remove(position);
                notifyItemRemoved(position);
            }
        };
        AcademyApiHelper.collectLesson(o,context,0,handler);

    }
}
