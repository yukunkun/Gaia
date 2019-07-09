package com.gaiamount.module_creator.sub_module_group.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_group.beans.onEventMaterial;
import com.gaiamount.module_creator.sub_module_group.creations.Product;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_user.personal.collections.CollectionFragment;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-9-29.
 */
public class MaterialGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    Context mContext;
    ArrayList<MaterialInfo> mMaterialInfoList;
    private String m1080="";
    private final StringUtil mStringUtil;
    private Info info;

    public MaterialGroupAdapter(Context context, ArrayList<MaterialInfo> mMaterialInfoList, Info info) {
        this.mContext = context;
        this.mMaterialInfoList = mMaterialInfoList;
        this.info = info;
        //初始化inflater
        inflater = LayoutInflater.from(context);
        //初始化ImageLoader
        mStringUtil = StringUtil.getInstance();
        //获取屏幕的当前高宽
        getScreeWidth();
    }

    int sreenWidth;

    private void getScreeWidth() {
        sreenWidth = ScreenUtils.instance().getWidth();
    }

    private void setItemHeight(View itemView) {
        ImageView itemImageView = (ImageView) itemView.findViewById(R.id.work_cover);
        ViewGroup.LayoutParams layoutParams = itemImageView.getLayoutParams();
        layoutParams.height = (int) (((sreenWidth - 30) / 2) * 0.562);//16:9
        itemImageView.setLayoutParams(layoutParams);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_material_video, null);
        setItemHeight(itemView);
        WorkViewHolder holder = new WorkViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MaterialInfo videoInfo = mMaterialInfoList.get(position);

            //设置封面
            String cover = videoInfo.getCover();
            String screenshot = videoInfo.getScreenshot();

            if (cover != null && !cover.isEmpty()) {
//                ImageUtils.getInstance(mContext).getNetworkBitmap(holder.workCover, cover);
                cover = Configs.COVER_PREFIX + cover;
                Glide.with(mContext).load(cover).override(320,180).placeholder(R.mipmap.bg_general).into(((WorkViewHolder)holder).workCover);
            } else if (screenshot != null && !screenshot.isEmpty()) {
//                ImageUtils.getInstance(mContext).getNetworkBitmap(holder.workCover, screenshot);
                screenshot = Configs.COVER_PREFIX + screenshot+"_18.png";
                Glide.with(mContext).load(screenshot).override(320,180).placeholder(R.mipmap.bg_general).into(((WorkViewHolder)holder).workCover);
            }


        //是否显示官方授权标志
        if (videoInfo.getIsOfficial() == 1) {
            ((WorkViewHolder)holder).outh.setVisibility(View.VISIBLE);
        } else {
            ((WorkViewHolder)holder).outh.setVisibility(View.GONE);
        }
        //是否是4k
        if (videoInfo.getIs4K() == 1) {
            ((WorkViewHolder)holder).is4k.setVisibility(View.VISIBLE);
        } else {
            ((WorkViewHolder)holder).is4k.setVisibility(View.GONE);
        }

        m1080="";
        if(videoInfo.getWidth()==2048){
            m1080="2K";
        }else if(videoInfo.getWidth()==1280){
            m1080="720P";
        }else if(videoInfo.getWidth()==1920){
            m1080="1080P";
        }else if(videoInfo.getWidth()==3840){
            m1080="UHD";
        }else if(videoInfo.getWidth()==4096){
            m1080="4K";
        }

        String format = videoInfo.getFormat();
        String fps = videoInfo.getFps();
        if("null".equals(format)){
            format="";
        }
        if(fps==null||"null".equals(fps)){
            fps="";
        }

        ((WorkViewHolder)holder).workCount.setText(format+" "+fps+" "+m1080);

        //播放时长
        ((WorkViewHolder)holder).workDuration.setText(mStringUtil.stringForTime(videoInfo.getDuration() * 1000));
        //作品名称
        ((WorkViewHolder)holder).workName.setText(videoInfo.getName());
        int allowCharge = videoInfo.getAllowCharge();
        if(allowCharge==0){
            ((WorkViewHolder)holder).mTextViewPrice.setText("免费");
            ((WorkViewHolder)holder).mTextViewPrice.setTextColor(mContext.getResources().getColor(R.color.white));

        }else {
            ((WorkViewHolder)holder).mTextViewPrice.setText("付费");
            ((WorkViewHolder)holder).mTextViewPrice.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoInfo.getCategory()==1){ //视频素材
                    ActivityUtil.startMaterialPlayActivity(mContext,videoInfo.getId(),1);
                }else if(videoInfo.getCategory()==2){ //模板素材
                    ActivityUtil.startMaterialPlayActivity(mContext,videoInfo.getId(),0);
                }

            }
        });

        //显示删除按钮
        if(info.groupPower.allowCleanCreation==1){
            ((WorkViewHolder)holder).imageViewMore.setVisibility(View.VISIBLE);
        }

        ((WorkViewHolder) holder).imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopu(((WorkViewHolder)holder).imageViewMore,videoInfo.getGvid(),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMaterialInfoList.size();
    }
    public class WorkViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView workCover;
        private TextView workDuration;
        private ImageView outh;
        private ImageView is4k;
        private TextView workName;
        private TextView workCount;
        private TextView workGrade;
        private ImageView userAvatar;
        private TextView userName;
        private ImageView imageViewMore;
        private TextView mTextViewPrice;

        public WorkViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            workCover = (ImageView) itemView.findViewById(R.id.work_cover);
            workDuration = (TextView) itemView.findViewById(R.id.work_duration);
            outh = (ImageView) itemView.findViewById(R.id.outh);
            is4k = (ImageView) itemView.findViewById(R.id.quality4);
            workName = (TextView) itemView.findViewById(R.id.work_name);
            workCount = (TextView) itemView.findViewById(R.id.work_count);
            workGrade = (TextView) itemView.findViewById(R.id.work_grade);
            userAvatar = (ImageView) itemView.findViewById(R.id.work_avatar);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            imageViewMore= (ImageView) itemView.findViewById(R.id.image_more);
            mTextViewPrice= (TextView) itemView.findViewById(R.id.material_price);
            workName.setMaxLines(2);
            //设置标题的最大宽度
            workName.setMaxWidth((int) (((sreenWidth - 48) / 2) * 0.75));
        }


    }

    private PopupWindow popupWindow;
    private void showPopu(ImageView view, long cid, int position) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.academy_collection_shanchu, null);
        popupWindow=new PopupWindow(inflate,ScreenUtils.dp2Px(mContext, 120), LinearLayout.LayoutParams.WRAP_CONTENT);
        //移除专辑
        TextView textView= (TextView) inflate.findViewById(R.id.academy_collect_delete);
        TextView textView1= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
        //设置值
        textView1.setText("移入小组专辑");
        textView.setText("移出小组");
        textView.setOnClickListener(new Listener(cid,position));
        textView1.setOnClickListener(new Listener(cid,position));
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
        popupWindow.showAsDropDown(view,0-popupWidth+10,0-popupHeight-ScreenUtils.dp2Px(mContext, 25));
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
            int id = v.getId();
            switch (id){
                case R.id.academy_collect_delete:
                    if(info.groupPower.allowCleanCreation==1) {
                        selfDialoga(cid,position,mContext);
                        popupWindow.dismiss();
                    }else {
                        GaiaApp.showToast("对不起,你没有权限");
                        popupWindow.dismiss();
                    }
                    break;
                case R.id.academy_collect_deletes:
                    EventBus.getDefault().post(new onEventMaterial(position));
                    popupWindow.dismiss();
                    break;
            }
        }
    }
    //自定义的对话框
    private void selfDialoga(final long cid, final int position, final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);

        final View view = LayoutInflater.from(this.mContext).inflate(R.layout.group_works_dialog, null);

        builder.setTitle("确定要移除吗?");
        builder.setPositiveButton(this.mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                removeVideo(cid,position,context);
            }
        });

        builder.setNegativeButton(this.mContext.getResources().getString(R.string.give_up), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //移出小组的网络请求
    private void removeVideo(long cid, final int position, Context context) {
        long id1=cid;
        long gid = info.gid;
        Long[] ids = new Long[]{id1};

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(Product.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("操作成功");
                mMaterialInfoList.remove(position);
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
        GroupApiHelper.batchRemoveVideo(ids,1, gid, context, handler);

    }
}
