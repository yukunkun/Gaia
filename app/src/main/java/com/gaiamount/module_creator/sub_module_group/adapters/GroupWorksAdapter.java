package com.gaiamount.module_creator.sub_module_group.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.OnEventLearn;
import com.gaiamount.module_creator.beans.GroupVideoBean;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_album.AlbumWork;
import com.gaiamount.module_creator.sub_module_group.beans.OnEventGroup;
import com.gaiamount.module_creator.sub_module_group.constant.CreationType;
import com.gaiamount.module_creator.sub_module_group.creations.Product;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yukun on 16-8-4.
 */
public class GroupWorksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    List<GroupVideoBean> albumWorks;

    private MixViewHolder holder;
    private PopupWindow popupWindow;
    private Info mInfo;

    public GroupWorksAdapter(Context context, List<GroupVideoBean> albumWorks, Info mInfo){
        this.context=context;
        this.albumWorks=albumWorks;
        this.mInfo=mInfo;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_work_pool, null);
        holder = new MixViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GroupVideoBean groupVideoBean = albumWorks.get(position);

//        if (groupVideoBean.getIsOfficial() == 1) {
//            ((MixViewHolder)holder).outh.setVisibility(View.VISIBLE);
//        } else {
//            ((MixViewHolder)holder).outh.setVisibility(View.GONE);
//        }
        //是否是4k
        if (groupVideoBean.getIs4K() == 1) {
            ((MixViewHolder)holder).is4k.setVisibility(View.VISIBLE);
        } else {
            ((MixViewHolder)holder).is4k.setVisibility(View.GONE);
        }
        //播放时长
        ((MixViewHolder)holder).workDuration.setText(StringUtil.getInstance().stringForTime(groupVideoBean.getDuration() * 1000));
        //作品名称
        ((MixViewHolder)holder).workName.setText(groupVideoBean.getName());

        //播放次数
        ((MixViewHolder)holder).workCount.setText(StringUtil.getInstance().setNum(groupVideoBean.getPlayCount()));
        //评分
        ((MixViewHolder)holder).workGrade.setText(String.valueOf(groupVideoBean.getGrade()));

        //用户名
        ((MixViewHolder)holder).userName.setText(groupVideoBean.getNickName());

        String cover = groupVideoBean.getCover();
        String screenshot = groupVideoBean.getScreenshot();
        //动态设置宽高
        ViewGroup.LayoutParams layoutParams = ((MixViewHolder)holder).workCover.getLayoutParams();//设置宽高
        layoutParams.height=getHeight();
        ((MixViewHolder)holder).workCover.setLayoutParams(layoutParams);


        if (cover != null && !cover.isEmpty()) {

            cover = Configs.COVER_PREFIX + cover/*.replace(".", "_18.")*/;
            Glide.with(context).load(cover).override(320,180).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).workCover);
        } else if (screenshot != null && !screenshot.isEmpty()) {
            if(groupVideoBean.getFlag()==0){
                screenshot = Configs.COVER_PREFIX + screenshot.replace(".", "_18.");
            }else if(groupVideoBean.getFlag()==1){
                screenshot = Configs.COVER_PREFIX + screenshot+"_18.";
            }
            Glide.with(context).load(screenshot).override(320,180).placeholder(R.mipmap.bg_general).into(((MixViewHolder)holder).workCover);
        }

        //用户头像
        if (groupVideoBean.getAvatar() != null && !groupVideoBean.getAvatar().equals("null")) {
            Glide.with(context).load(Configs.COVER_PREFIX+groupVideoBean.getAvatar()).override(320,180).placeholder(R.mipmap.ic_avatar_default).into(((MixViewHolder)holder).userAvatar);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ActivityUtil.startPlayerActivity(context, (int) groupVideoBean.getId(), CreationType.TYPE_WORK);        }
        });

        if(mInfo.groupPower.allowManagerSpecial==1||mInfo.groupPower.allowCleanCreation==1){
            ((MixViewHolder)holder).imageMore.setVisibility(View.VISIBLE);
        }

        ((MixViewHolder)holder).imageMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopu(((MixViewHolder)holder).imageMore,albumWorks,position,context);

            }

            private void showPopu(ImageView view, List<GroupVideoBean> albumWorks, int position, Context context) {
                View inflate = LayoutInflater.from(GroupWorksAdapter.this.context).inflate(R.layout.group_shanchu, null);
                popupWindow=new PopupWindow(inflate,ScreenUtils.dp2Px(context, 120), LinearLayout.LayoutParams.WRAP_CONTENT);
                //移除专辑
                TextView textViewOut= (TextView) inflate.findViewById(R.id.academy_collect_delete);
                textViewOut.setOnClickListener(new Listener(albumWorks,position,context));

                TextView textViewGroupIn= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
                textViewGroupIn.setOnClickListener(new Listener(albumWorks,position,context));
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
                popupWindow.showAsDropDown(view,0-popupWidth+ScreenUtils.dp2Px(context, 1),0-popupHeight-ScreenUtils.dp2Px(context, 25));
//                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,
//                        (location[0] + view.getWidth() / 2) - popupWidth*2, location[1] - popupHeight*4);
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
        List<GroupVideoBean> albumWorks;
        int position;
        Context context;
        public Listener(List<GroupVideoBean> albumWorks, int position,Context context) {
            this.albumWorks=albumWorks;
            this.position=position;
            this.context=context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.academy_collect_delete:
                    if(mInfo.groupPower.allowCleanCreation==1) {
                        selfDialoga(albumWorks, position, context);
                        popupWindow.dismiss();
                    }else {
                        GaiaApp.showToast("对不起,你没有权限");
                        popupWindow.dismiss();
                    }
                    break;
                case R.id.academy_collect_deletes:
                    EventBus.getDefault().post(new OnEventGroup(position));
                    popupWindow.dismiss();
                    break;
            }
        }
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width/2-30)*0.62);
        return itemHeight;
    }
    //自定义的对话框
    private void selfDialoga(final List<GroupVideoBean> albumWorks, final int position, final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        final View view = LayoutInflater.from(this.context).inflate(R.layout.group_works_dialog, null);

        builder.setTitle("确定要移除吗?");
//        builder.setView(view);
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
    private void removeVideo(final List<GroupVideoBean> albumWorks, final int evenPos, Context context) {

        final GroupVideoBean groupVideoBean = albumWorks.get(evenPos);
        long id = groupVideoBean.getGvid();
        long gid = mInfo.gid;
        Long[] ids = new Long[]{id};

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
        GroupApiHelper.batchRemoveVideo(ids, 0,gid,context, handler);

    }

}
