package com.gaiamount.module_creator.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
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
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_album.AlbumWork;
import com.gaiamount.module_creator.sub_module_group.constant.CreationType;
import com.gaiamount.module_creator.sub_module_group.creations.Product;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-8-4.
 */
public class AlbumWorksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    List<AlbumWork> albumWorks;

    private MixViewHolder holder;
    private PopupWindow popupWindow;
    private Info info;

    public AlbumWorksAdapter(Context context, List<AlbumWork> albumWorks, Info info){
        this.context=context;
        this.albumWorks=albumWorks;
        this.info=info;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_work_pool, null);
        holder = new MixViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final AlbumWork albumWork = albumWorks.get(position);

        if (albumWork.getIsOfficial() == 1) {
            ((MixViewHolder)holder).outh.setVisibility(View.VISIBLE);
        } else {
            ((MixViewHolder)holder).outh.setVisibility(View.GONE);
        }
        //是否是4k
        if (albumWork.getIs4K() == 1) {
            ((MixViewHolder)holder).is4k.setVisibility(View.VISIBLE);
        } else {
            ((MixViewHolder)holder).is4k.setVisibility(View.GONE);
        }
        //播放时长
        ((MixViewHolder)holder).workDuration.setText(StringUtil.getInstance().stringForTime(albumWork.getDuration() * 1000));
        //作品名称
        ((MixViewHolder)holder).workName.setText(albumWork.getName());

        //播放次数
        ((MixViewHolder)holder).workCount.setText(StringUtil.getInstance().setNum(albumWork.getPlayCount()));
        //评分
        ((MixViewHolder)holder).workGrade.setText(String.valueOf(albumWork.getGrade()));

        //用户名
        ((MixViewHolder)holder).userName.setText(albumWork.getNickName());

        String cover = albumWork.getCover();
        String screenshot = albumWork.getScreenshot();

        ViewGroup.LayoutParams layoutParams = ((MixViewHolder)holder).workCover.getLayoutParams();//设置宽高
        layoutParams.height=getHeight();
        ((MixViewHolder)holder).workCover.setLayoutParams(layoutParams);

        if (cover != null && !cover.isEmpty()&&!"".equals(cover)) {
//                ImageUtils.getInstance(mContext).getNetworkBitmap(holder.workCover, cover);
            cover = Configs.COVER_PREFIX + cover/*.replace(".", "_18.")*/;
            Glide.with(context).load(cover).override(320,180).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).workCover);
        } else if (screenshot != null && !screenshot.isEmpty()) {
            if(screenshot.endsWith(".png")){
                screenshot = Configs.COVER_PREFIX + screenshot.replace(".png","_18.png");
            }  else {
                screenshot = Configs.COVER_PREFIX + screenshot+"_18.png";
            }
            Glide.with(context).load(screenshot).override(320,180).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).workCover);
        }

        //用户头像
        if (albumWork.getAvatar() != null && !albumWork.getAvatar().equals("null")) {
            Glide.with(context).load(Configs.COVER_PREFIX+albumWork.getAvatar()).override(320,180).placeholder(R.mipmap.ic_avatar_default).into(((MixViewHolder)holder).userAvatar);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startPlayerActivity(context, albumWork.getId(), CreationType.TYPE_WORK);            }
        });

//        Log.i("---info_work",info.groupPower.allowCleanCreation+"+"+info.groupPower.allowManagerSpecial);
        if(info.groupPower.allowCleanCreation==1){
            ((MixViewHolder)holder).imageMore.setVisibility(View.VISIBLE);
        }

        ((MixViewHolder)holder).imageMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopu(((MixViewHolder)holder).imageMore,albumWork,position);
            }

            private void showPopu(ImageView view, AlbumWork albumWork, int position) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.album_shanchu, null);
                popupWindow=new PopupWindow(inflate,200, LinearLayout.LayoutParams.WRAP_CONTENT);
                //移除专辑
                TextView textView= (TextView) inflate.findViewById(R.id.academy_collect_delete);
                textView.setOnClickListener(new Listener(albumWork,position));
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
                popupWindow.showAsDropDown(view,0-popupWidth-ScreenUtils.dp2Px(context, 50),0-popupHeight-ScreenUtils.dp2Px(context, 55));

            }
        });

    }

    @Override
    public int getItemCount() {
        return albumWorks.size();
    }

    class MixViewHolder extends RecyclerView.ViewHolder{

        private View itemView;
        private ImageView workCover;
        private TextView workDuration;
        private ImageView outh;
        private ImageView is4k,imageMore;
        private TextView workName;
        private TextView workCount;
        private TextView workGrade;
        private ImageView userAvatar;
        private TextView userName;

        public MixViewHolder(View itemView) {
            super(itemView);
            workCover = (ImageView) itemView.findViewById(R.id.work_cover);
            workDuration = (TextView) itemView.findViewById(R.id.work_duration);
            outh = (ImageView) itemView.findViewById(R.id.outh);
            is4k = (ImageView) itemView.findViewById(R.id.quality4);
            workName = (TextView) itemView.findViewById(R.id.work_name);
            workCount = (TextView) itemView.findViewById(R.id.work_count);
            workGrade = (TextView) itemView.findViewById(R.id.work_grade);
            userAvatar = (ImageView) itemView.findViewById(R.id.work_avatar);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            imageMore= (ImageView) itemView.findViewById(R.id.image_more);
            //点击事件
        }
    }
    class Listener implements View.OnClickListener {
        private AlbumWork albumWork;
        private int position;

        public Listener(AlbumWork albumWork, int position) {
            this.albumWork = albumWork;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            selfDialoga(albumWork,position);
            popupWindow.dismiss();
        }
    }
    private void selfDialoga(final AlbumWork albumWork, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        builder.setTitle("确定要移除吗?");
        builder.setPositiveButton(this.context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                removeVideo(albumWork,position,context);
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
    private void removeVideo(final AlbumWork albumWork, final int evenPos, Context context) {

        long id = albumWork.getAvid();

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(Product.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("操作成功");
                albumWorks.remove(evenPos);
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
        AlbumApiHelper.removeVideoFromAlbum(id, info.gid,0,context, handler);
    }


    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width/2)*0.62);
        return itemHeight;
    }

}
