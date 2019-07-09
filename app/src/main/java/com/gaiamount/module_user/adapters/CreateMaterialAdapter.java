package com.gaiamount.module_user.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_user.person_creater.events.OnEventMaterial;
import com.gaiamount.module_user.person_creater.events.OnEventWorks;
import com.gaiamount.module_user.person_creater.fragments.MaterialFrag;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by haiyang-lu on 16-3-28.
 * 作品池列表适配器
 */
public class CreateMaterialAdapter extends RecyclerView.Adapter<CreateMaterialAdapter.WorkViewHolder> {
    private Context mContext;
    private List<MaterialInfo> materialInfos;
    private LayoutInflater inflater;
    private final ImageUtils instance;
    private final StringUtil mStringUtil;
    private long uid;
    private PopupWindow popupWindow;
    /**
     * 列表是否在滚动
     */
    private boolean isListFling;
    private String m1080="";

    public void setVideoInfoList(List<MaterialInfo> materialInfos) {
        this.materialInfos = materialInfos;
    }


    public CreateMaterialAdapter(Context context, List<MaterialInfo> materialInfos) {
        this.mContext = context;
        this.materialInfos = materialInfos;
        //初始化inflater
        inflater = LayoutInflater.from(context);
        //初始化ImageLoader
        instance = ImageUtils.getInstance(context);
        //获取Utils实例
        mStringUtil = StringUtil.getInstance();
        //获取屏幕的当前高宽
        getScreeWidth();
    }

    public void getUid(long uid){
        this.uid=uid;
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
    public CreateMaterialAdapter.WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_material_video, null);
        setItemHeight(itemView);
        WorkViewHolder holder = new WorkViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(final CreateMaterialAdapter.WorkViewHolder holder, final int position) {
        final MaterialInfo videoInfo = materialInfos.get(position);
//        if (!isListFling) {//如果列表在滑动的话，停止加载图片
            //设置封面
            String cover = videoInfo.getCover();
            String screenshot = videoInfo.getScreenshot();

        Log.i("---covershot",cover+"---"+screenshot);
            if (cover != null && !cover.isEmpty()) {
//                ImageUtils.getInstance(mContext).getNetworkBitmap(holder.workCover, cover);
                Glide.with(mContext).load(cover).override(320,180).placeholder(R.mipmap.bg_general).into(holder.workCover);
            } else if (screenshot != null && !screenshot.isEmpty()) {
//                ImageUtils.getInstance(mContext).getNetworkBitmap(holder.workCover, screenshot);
                screenshot = Configs.COVER_PREFIX + screenshot+("_18.png");
                Glide.with(mContext).load(screenshot).override(320,180).placeholder(R.mipmap.bg_general).into(holder.workCover);
            }
//        }

        //是否显示官方授权标志
        if (videoInfo.getIsOfficial() == 1) {
            holder.outh.setVisibility(View.VISIBLE);
        } else {
            holder.outh.setVisibility(View.GONE);
        }
        //是否是4k
        if (videoInfo.getIs4K() == 1) {
            holder.is4k.setVisibility(View.VISIBLE);
        } else {
            holder.is4k.setVisibility(View.GONE);
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

        holder.workCount.setText(videoInfo.getFormat()+" "+videoInfo.getFps()+" "+m1080);
        //播放时长
        holder.workDuration.setText(mStringUtil.stringForTime(videoInfo.getDuration() * 1000));
        //作品名称
        holder.workName.setText(videoInfo.getName());
        int allowCharge = videoInfo.getAllowCharge();
        if(allowCharge==0){
            holder.mTextViewPrice.setText("免费");
            holder.mTextViewPrice.setTextColor(mContext.getResources().getColor(R.color.white));

        }else {
            holder.mTextViewPrice.setText("付费");
            holder.mTextViewPrice.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startMaterialPlayActivity(mContext,videoInfo.getId(),0);
            }
        });
        if(uid== GaiaApp.getAppInstance().getUserInfo().id){
            holder.imageViewMore.setVisibility(View.VISIBLE);
        }

        (holder).imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopu(holder.imageViewMore,videoInfo.getId(),position);
            }

            private void showPopu(ImageView view,long id,int position) {
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.academy_collection_shanchu, null);
                popupWindow=new PopupWindow(inflate,ScreenUtils.dp2Px(mContext, 100), LinearLayout.LayoutParams.WRAP_CONTENT);
                //移除专辑
                TextView textView= (TextView) inflate.findViewById(R.id.academy_collect_delete);
                TextView textView1= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
                //设置值
                textView.setText("删除素材");
                textView.setOnClickListener(new Listener( id,position));
                textView1.setOnClickListener(new Listener( id,position));
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
                popupWindow.showAsDropDown(view,0-popupWidth,0-popupHeight-ScreenUtils.dp2Px(mContext, 25));
            }
        });


    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public int getItemCount() {
        return materialInfos.size();
    }


    public class WorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            itemView.setOnClickListener(this);
            //设置标题的最大宽度
            workName.setMaxWidth((int) (((sreenWidth - 48) / 2) * 0.75));
        }

        @Override
        public void onClick(View v) {
            //点击事件
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class Listener implements View.OnClickListener {
        long mid;
        int position;

        public Listener(long mid, int position) {
            this.mid = mid;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.academy_collect_delete){
                deleteMaterial(mid,position);
            }
            else if(v.getId()==R.id.academy_collect_deletes){
                EventBus.getDefault().post(new OnEventMaterial(position));
            }
            popupWindow.dismiss();
        }
    }

    private void deleteMaterial(long mid, final int position) {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(MaterialFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                materialInfos.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                GaiaApp.showToast("删除失败");
            }
        };
        MaterialApiHelper.getMaterialDet(mid,mContext,handler);

    }
}
