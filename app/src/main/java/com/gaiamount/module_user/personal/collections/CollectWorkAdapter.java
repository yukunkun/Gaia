package com.gaiamount.module_user.personal.collections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.fragment.CreatorWorkFrag;
import com.gaiamount.module_player.bean.VideoInfo;
import com.gaiamount.module_player.fragments.PlayerColFrag;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by haiyang-lu on 16-3-28.
 * 作品池列表适配器
 */
public class CollectWorkAdapter extends RecyclerView.Adapter<CollectWorkAdapter.WorkViewHolder> {
    private Context mContext;
    private List<VideoInfo> videoInfoList;
    private LayoutInflater inflater;
    private final ImageUtils instance;
    private final StringUtil mStringUtil;
    private long uid;
    private PopupWindow popupWindow;

    /**
     * 列表是否在滚动
     */
    private boolean isListFling;

    public void setListFling(boolean listFling) {
        isListFling = listFling;
    }

    public void setVideoInfoList(List<VideoInfo> videoInfoList) {
        this.videoInfoList = videoInfoList;
    }


    public CollectWorkAdapter(Context context, List<VideoInfo> videoInfoList, long uid) {
        this.mContext = context;
        this.videoInfoList = videoInfoList;
        this.uid=uid;
        //初始化inflater
        inflater = LayoutInflater.from(context);
        //初始化ImageLoader
        instance = ImageUtils.getInstance(context);
        //获取Utils实例
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
    public CollectWorkAdapter.WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_work_pool, null);
        setItemHeight(itemView);
        WorkViewHolder holder = new WorkViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(final CollectWorkAdapter.WorkViewHolder holder, final int position) {
        final VideoInfo videoInfo = videoInfoList.get(position);
        if (!isListFling) {//如果列表在滑动的话，停止加载图片
            //设置封面
            String cover = videoInfo.getCover();
            String screenshot = videoInfo.getScreenshot();

            if (cover != null && !cover.isEmpty()&&!cover.equals("null")) {
//                ImageUtils.getInstance(mContext).getNetworkBitmap(holder.workCover, cover);
                cover = Configs.COVER_PREFIX + cover/*.replace(".", "_18.")*/;
                Glide.with(mContext).load(cover).override(320,180).placeholder(R.mipmap.bg_general).into(holder.workCover);
            } else if (screenshot != null && !screenshot.isEmpty()) {
                if(screenshot.endsWith(".png")){
                    screenshot = Configs.COVER_PREFIX + screenshot.replace(".png","_18.png");
                }  else {
                    screenshot = Configs.COVER_PREFIX + screenshot+"_18.png";
                }
                Glide.with(mContext).load(screenshot).override(320,180).placeholder(R.mipmap.bg_general).into(holder.workCover);
            }

            //用户头像
            if (videoInfo.getAvatar() != null && !videoInfo.getAvatar().equals("null")) {
                instance.getAvatar(holder.userAvatar, videoInfo.getAvatar());
            }
        }


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
        //播放时长
        holder.workDuration.setText(mStringUtil.stringForTime(videoInfo.getDuration() * 1000));
        //作品名称
        holder.workName.setText(videoInfo.getName());

        //播放次数
        holder.workCount.setText(String.valueOf(videoInfo.getPlayCount()));
        //评分
        holder.workGrade.setText(String.valueOf(videoInfo.getGrade()));

        //头像点击事件
        holder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long uid = videoInfo.getUserId();
                ActivityUtil.startPersonalActivity(mContext, uid);
            }
        });

        //用户名
        holder.userName.setText(videoInfo.getNickName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startPlayerActivity((Activity) mContext,videoInfo.getId(), Configs.TYPE_WORK_POOL,holder.workCover);
            }
        });

        if(uid==GaiaApp.getAppInstance().getUserInfo().id){
            holder.imageViewMore.setVisibility(View.VISIBLE);
        }

        holder.imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopu(holder.imageViewMore, videoInfo.getId(), videoInfo.getType(),position);
            }
        });

    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public int getItemCount() {
        return videoInfoList.size();
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
            itemView.setOnClickListener(this);
            workName.setMaxLines(2);
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

    private void showPopu(ImageView view, long id, String type, int position) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.academy_collection_shanchu, null);
        popupWindow=new PopupWindow(inflate,200, LinearLayout.LayoutParams.WRAP_CONTENT);
        //移除专辑
        TextView textView= (TextView) inflate.findViewById(R.id.academy_collect_delete);
        TextView textView1= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
        textView1.setVisibility(View.GONE);
        //设置值
        textView.setText("取消收藏");
        textView.setOnClickListener(new Listener(id,type,position));
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
        popupWindow.showAsDropDown(view,0-popupWidth/*+ScreenUtils.dp2Px(mContext, 15)*/,0-popupHeight-ScreenUtils.dp2Px(mContext, 20));
    }

    class Listener implements View.OnClickListener {
        private long wid;
        private int position;
        private String type;

        public Listener(long cid, String type, int position) {
            this.wid = cid;
            this.position = position;
            this.type=type;
        }

        @Override
        public void onClick(View v) {
            collectionWorksToggle(wid,type,position);
            popupWindow.dismiss();
        }
    }

    private void collectionWorksToggle(long wid, String type, final int position) {
        //作品类型
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(CreatorWorkFrag.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                videoInfoList.remove(position);
                notifyItemRemoved(position);
                GaiaApp.showToast(mContext.getString(R.string.cancel_collection_success));
            }
        };

        WorksApiHelper.collect(wid, 0, 0, mContext, jsonHttpResponseHandler);
    }

}
