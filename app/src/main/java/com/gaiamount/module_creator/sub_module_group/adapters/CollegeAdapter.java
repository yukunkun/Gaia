package com.gaiamount.module_creator.sub_module_group.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.OnEventLearn;
import com.gaiamount.module_creator.beans.GroupVideoBean;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_group.creations.Product;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.media.GMediaController;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-8-4.
 */
public class CollegeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<MixInfo> mixInfos;
    private MixViewHolder holder;
    private PopupWindow popupWindow;
    private Info mInfo;

    public CollegeAdapter(Context context, ArrayList<MixInfo> mixInfos, Info mInfo){
        this.context=context;
        this.mixInfos=mixInfos;
        this.mInfo=mInfo;
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

        //显示删除按钮
        if(mInfo.groupPower.allowCleanCreation==1){
            ((MixViewHolder)holder).imageViewMore.setVisibility(View.VISIBLE);
        }
        ((MixViewHolder)holder).imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopu(((MixViewHolder)holder).imageViewMore,mixInfo,context,position);
            }

            private void showPopu(ImageView view, MixInfo mixInfo, Context context, int position) {
                CardView inflate = (CardView) LayoutInflater.from(CollegeAdapter.this.context).inflate(R.layout.academy_collection_shanchu, null);
                popupWindow=new PopupWindow(inflate,ScreenUtils.dp2Px(context, 130), LinearLayout.LayoutParams.WRAP_CONTENT);
                //移除专辑
                TextView textViewDelete= (TextView) inflate.findViewById(R.id.academy_collect_delete);
                textViewDelete.setOnClickListener(new Listener(mixInfo,context,position));
                textViewDelete.setText("移出小组");

                TextView textViewGroupIn= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
                textViewGroupIn.setOnClickListener(new Listener(mixInfo,context,position));
                textViewGroupIn.setText("移入小组专辑");
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
//                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,
//                        (location[0] + view.getWidth() / 2) - popupWidth*2, location[1] - popupHeight*4);
                popupWindow.showAsDropDown(view,0-popupWidth+ScreenUtils.dp2Px(context, 10),0-popupHeight-ScreenUtils.dp2Px(context, 25));

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
        private Context context;
        private int position;
        private MixInfo mixInfo;
        public Listener(MixInfo mixInfo, Context context, int position) {
            this.context=context;
            this.mixInfo=mixInfo;
            this.position=position;
        }
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.academy_collect_delete:
                    if(mInfo.groupPower.allowCleanCreation==1) {
                        selfDialoga(mixInfo,position,context);
                        popupWindow.dismiss();
                    }else {
                        GaiaApp.showToast("对不起,你没有权限");
                        popupWindow.dismiss();
                    }
                    break;
                case R.id.academy_collect_deletes:
                    EventBus.getDefault().post(new OnEventLearn(position));
                    popupWindow.dismiss();
                    break;
            }
        }
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width/2)*0.52);
        return itemHeight;
    }

    //自定义的对话框
    private void selfDialoga(final MixInfo albumWorks, final int position, final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        final View view = LayoutInflater.from(this.context).inflate(R.layout.group_works_dialog, null);

        builder.setTitle("确定要移除吗?");
      //  builder.setView(view);
        builder.setPositiveButton(this.context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                removeVideo(albumWorks,position,context);
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

    //删除作品
    private void removeVideo(final MixInfo albumWorks, final int evenPos, Context context) {

        long id1 = mixInfos.get(evenPos).getGcid();
        long gid = mInfo.gid;
        Long[] ids = new Long[]{id1};

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
        GroupApiHelper.batchRemoveVideo(ids,2, gid, context, handler);

    }
}
