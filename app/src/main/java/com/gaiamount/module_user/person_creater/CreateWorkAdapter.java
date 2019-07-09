package com.gaiamount.module_user.person_creater;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.gaiamount.module_creator.create_person.rec_viewholder.CreateViewHolder3;
import com.gaiamount.module_user.person_creater.events.OnEventWorks;
import com.gaiamount.module_user.personal.PersonalWorks;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by yukun on 16-8-24.
 */
public class CreateWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private ArrayList<PersonalWorks> worksArrayList;
    private final int mScreenWidth;
    private final int mItemHeight;
    private PopupWindow popupWindow;
    private long uid;

    public CreateWorkAdapter(Context context, ArrayList<PersonalWorks> worksArrayList, long uid) {
        this.context = context;
        this.worksArrayList = worksArrayList;
        this.uid = uid;
        //屏幕宽度
        mScreenWidth = ScreenUtils.instance().getWidth();
        int itemWidth = (mScreenWidth - (3 * ScreenUtils.dp2Px(context, 8))) / 2;
        mItemHeight = (int) (itemWidth * 0.562);//16:9
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.list_item_personal_create, null);
        CreateViewHolder createViewHolder = new CreateViewHolder(inflate);
        return createViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final PersonalWorks personalWorks = worksArrayList.get(position);
        String cover = personalWorks.getCover();
        String screenshot = personalWorks.getScreenshot();
        //作品封面
        if(!TextUtils.isEmpty(cover)&&cover.length()!=0&&!"null".equals(cover)){
            Glide.with(context).load(Configs.COVER_PREFIX+cover).placeholder(R.mipmap.bg_general).into(((CreateViewHolder) holder).workCover);
        }else if (!TextUtils.isEmpty(screenshot)&& personalWorks.getScreenshot().length()!=0){
            
            if(personalWorks.getFlag()==1){
                screenshot=screenshot+"_18.png";
            }else if(personalWorks.getFlag()==0){
                screenshot=screenshot.replace(".","_18.");
            }
            Glide.with(context).load(Configs.COVER_PREFIX+screenshot).placeholder(R.mipmap.bg_general).into(((CreateViewHolder) holder).workCover);
        }

//        Glide.with(context).load(Configs.COVER_PREFIX + cover).placeholder(R.mipmap.bg_general).into(((CreateViewHolder) holder).workCover);
        //作品时长
        ((CreateViewHolder) holder).workDuration.setText(StringUtil.getInstance().stringForTime(personalWorks.getDuration() * 1000));
        //是否官方
        ((CreateViewHolder) holder).outh.setVisibility(1 != personalWorks.getIsOfficial() ? View.GONE : View.VISIBLE);
        //是否4k
        ((CreateViewHolder) holder).is4k.setVisibility(personalWorks.getIs4K() == 1 ? View.VISIBLE : View.GONE);
        //作品名称
        ((CreateViewHolder) holder).workName.setText(personalWorks.getName());
        //作品播放数量
        ((CreateViewHolder) holder).workCount.setText(StringUtil.getInstance().setNum(personalWorks.getPlayCount()));
        //作品评分
        ((CreateViewHolder) holder).workGrade.setText(Double.toString(personalWorks.getGrade()));
        //作品收藏
        ((CreateViewHolder) holder).workLike.setText(StringUtil.getInstance().setNum(personalWorks.getLikeCount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startPlayerActivity(context, worksArrayList.get(position).getId(), 0);

            }
        });
        if (uid == GaiaApp.getAppInstance().getUserInfo().id) {
            ((CreateViewHolder) holder).imageViewMore.setVisibility(View.VISIBLE);
        }

        ((CreateViewHolder) holder).imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopu(((CreateViewHolder) holder).imageViewMore, personalWorks.getId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return worksArrayList.size();
    }

    class CreateViewHolder extends RecyclerView.ViewHolder {
        private ImageView workCover;
        private TextView workDuration;
        private ImageView outh;
        private ImageView is4k;
        private TextView workName, workLike;
        private TextView workCount;
        private TextView workGrade;
        private ImageView imageViewMore;

        public CreateViewHolder(View itemView) {
            super(itemView);
            workCover = (ImageView) itemView.findViewById(R.id.work_cover);
            workDuration = (TextView) itemView.findViewById(R.id.work_duration);
            outh = (ImageView) itemView.findViewById(R.id.outh);
            is4k = (ImageView) itemView.findViewById(R.id.quality4);
            workName = (TextView) itemView.findViewById(R.id.work_name);
            workCount = (TextView) itemView.findViewById(R.id.work_count);
            workGrade = (TextView) itemView.findViewById(R.id.work_grade);
            workLike = (TextView) itemView.findViewById(R.id.work_like);
            imageViewMore = (ImageView) itemView.findViewById(R.id.image_more);
            workName.setMaxLines(2);
            workCover.getLayoutParams().height = mItemHeight;
        }
    }

    private void showPopu(ImageView view, long cid, int position) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.academy_collection_shanchu, null);
        popupWindow = new PopupWindow(inflate, ScreenUtils.dp2Px(context, 100), LinearLayout.LayoutParams.WRAP_CONTENT);
        //移除专辑
        TextView textView = (TextView) inflate.findViewById(R.id.academy_collect_delete);
        TextView textView1 = (TextView) inflate.findViewById(R.id.academy_collect_deletes);
        //设置值
        textView.setText("删除作品");
        textView.setOnClickListener(new Listener(cid, position));
        textView1.setOnClickListener(new Listener(cid, position));

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
        popupWindow.showAsDropDown(view, 0 - popupWidth/* + ScreenUtils.dp2Px(context, 15)*/, 0 - popupHeight - ScreenUtils.dp2Px(context, 20));
    }
    class Listener implements View.OnClickListener {
        private long wid;
        private int position;

        public Listener(long cid, int position) {
            this.wid = cid;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.academy_collect_delete){
                deleteWorks(wid,position);
            }else if(v.getId()==R.id.academy_collect_deletes){
                EventBus.getDefault().post(new OnEventWorks(position));
            }
            popupWindow.dismiss();
        }
    }

    private void deleteWorks(long wid, final int position) {

        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(CreateWorkAdapter.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                worksArrayList.remove(position);
                notifyItemRemoved(position);
            }
        };
        WorksApiHelper.deleteWorks(uid,wid,context,handler);
    }


}
