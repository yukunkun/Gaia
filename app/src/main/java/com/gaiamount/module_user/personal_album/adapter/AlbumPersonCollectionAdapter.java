package com.gaiamount.module_user.personal_album.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_group.creations.Product;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-4.
 */
public class AlbumPersonCollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<MixInfo> mixInfos;
    private MixViewHolder holder;
    private PopupWindow popupWindow;
    private Info info;

    public AlbumPersonCollectionAdapter(Context context, ArrayList<MixInfo> mixInfos, Info info){
        this.context=context;
        this.mixInfos=mixInfos;
        this.info=info;
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
        ((MixViewHolder)holder).textViewScan.setText(StringUtil.getInstance().setNum(mixInfo.getPlayCount()));
        ((MixViewHolder)holder).textViewLike.setText(StringUtil.getInstance().setNum(mixInfo.getBrowseCount()));
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

        if(info.gid==GaiaApp.getAppInstance().getUserInfo().id){ // 判断是否有删除的权限
            ((MixViewHolder)holder).imageViewMore.setVisibility(View.VISIBLE);
        }

        ((MixViewHolder)holder).imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopu(((MixViewHolder)holder).imageViewMore,mixInfo,position);
            }

            private void showPopu(ImageView view, MixInfo mixinfo,int position) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.academy_collection_shanchu, null);
                popupWindow=new PopupWindow(inflate,200, LinearLayout.LayoutParams.WRAP_CONTENT);
                //移除专辑
                TextView textView= (TextView) inflate.findViewById(R.id.academy_collect_delete);
                textView.setText("移出专辑");
                textView.setOnClickListener(new Listener(mixinfo,position));
                CardView cardView= (CardView) inflate.findViewById(R.id.cardview);
                TextView textViews= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
                textViews.setVisibility(View.GONE);
                //獲取焦點
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                //測量控件寬高
                inflate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int popupWidth = view.getMeasuredWidth();
                int popupHeight = view.getMeasuredHeight();
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                //在控件上方显示
//                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,
//                        (location[0] + view.getWidth() / 2) - popupWidth*2, location[1] - popupHeight*2);
                popupWindow.showAsDropDown(view,0-popupWidth-ScreenUtils.dp2Px(context, 20),0-popupHeight-ScreenUtils.dp2Px(context, 40));
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
        private MixInfo mixInfo;
        private int position;

        public Listener(MixInfo mixInfo, int position) {
            this.mixInfo = mixInfo;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            selfDialoga(mixInfo,position,context);
            popupWindow.dismiss();
        }
    }
    private void selfDialoga(final MixInfo mixInfo, final int position, final Context context) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        builder.setTitle("确定要移除吗?");
        builder.setPositiveButton(this.context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                removeVideo(mixInfo,position,context);
            }
        });

        builder.setNegativeButton(this.context.getResources().getString(R.string.give_up), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void removeVideo(final MixInfo mixInfo, final int evenPos, Context context) {

        long id = mixInfo.getGcid();

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(Product.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("操作成功");
                mixInfos.remove(evenPos);
                notifyDataSetChanged();
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                GaiaApp.showToast("操作失败");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        };
        AlbumApiHelper.removeVideoFromAlbum(id,0,3,context, handler);
    }


    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width/2)*0.52);
        return itemHeight;
    }


}
