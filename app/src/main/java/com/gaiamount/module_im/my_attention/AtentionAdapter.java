package com.gaiamount.module_im.my_attention;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_scripe.adapter.ScripeAdapter;
import com.gaiamount.util.ActivityUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by yukun on 16-10-19.
 */
public class AtentionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int TYPE_1=0;
    private int TYPE_2=1;
    private Context context;
    private ArrayList<AttentInfo> attentInfos;

    public AtentionAdapter(Context context, ArrayList<AttentInfo> attentInfos) {
        this.context = context;
        this.attentInfos = attentInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
//        if(viewType==TYPE_1){
//            view= LayoutInflater.from(context).inflate(R.layout.attention_holder_1,null);
//            ViewHolder_1 holder_1=new ViewHolder_1(view);
//            return holder_1;
//        }else if(viewType==TYPE_2){
//            view= LayoutInflater.from(context).inflate(R.layout.attention_holder_2,null);
//            ViewHolder_2 holder_2=new ViewHolder_2(view);
//            return holder_2;
//        }
          view= LayoutInflater.from(context).inflate(R.layout.my_attent_item,null);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            //公共的view展示
            final AttentInfo attentInfo = attentInfos.get(position);
            Glide.with(context).load(Configs.COVER_PREFIX+attentInfos.get(position).getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(((MyViewHolder) holder).imageViewHead);
            ((MyViewHolder) holder).textViewName.setText(attentInfo.getNickName());
            ((MyViewHolder) holder).layout.setVisibility(View.GONE);
            long time = attentInfo.getCreateTime().getTime();
            ((MyViewHolder) holder).textViewTime.setText(getTime(time));
            ((MyViewHolder) holder).textViewLike.setText(attentInfo.getLikeCount()+"");
            ((MyViewHolder) holder).textViewComment.setText(attentInfo.getBrowserCount()+"");

            String cover = attentInfo.getCover();
            String shotCover = attentInfo.getScreenshot();
//            Log.i("--cover1",cover+"");
//            Log.i("--shotCover",shotCover+"");
            if(cover!=null&&cover!=""&&cover.length()!=0){
                Glide.with(context).load(Configs.COVER_PREFIX + cover).placeholder(R.mipmap.ic_avatar_default).into(((MyViewHolder) holder).imageViewCover);
            }else if(shotCover!=null){
                if(!shotCover.endsWith(".jpg")&&!shotCover.endsWith(".png")&&!shotCover.endsWith(".jpeg")){
                    shotCover=shotCover+"_18.png";
                    Glide.with(context).load(Configs.COVER_PREFIX+shotCover).placeholder(R.mipmap.ic_avatar_default).into(((MyViewHolder) holder).imageViewCover);
                }else {
                    //加载正确的avatar
                    Glide.with(context).load(Configs.COVER_PREFIX + shotCover).placeholder(R.mipmap.ic_avatar_default).into(((MyViewHolder) holder).imageViewCover);
                }
            }
            //分类判断
            final int types = attentInfo.getTypes();//(0发布  1收藏 2创建 3关注 4升级 5转载 6购买)
            final int contentType = attentInfo.getContentType();//(0作品 1素材 2剧本 3学院 4手记 )
            //作品
            if(contentType==0&&(types==0||types==1||types==6)){
                if(types==0){
                    ((MyViewHolder) holder).textViewAction.setText("发布作品");
                }if(types==1){
                    ((MyViewHolder) holder).textViewAction.setText("收藏作品");
                }if(types==6){
                    ((MyViewHolder) holder).textViewAction.setText("购买作品");
                }
                ((MyViewHolder) holder).textViewContent.setText(attentInfo.getContentName());
                ((MyViewHolder) holder).textViewSign.setText(attentInfo.getDescription());
            }
            if(types==2){
                ((MyViewHolder) holder).textViewAction.setText(attentInfo.getMessage());
                ((MyViewHolder) holder).textViewContent.setText(attentInfo.getContentName());
                ((MyViewHolder) holder).textViewSign.setText(attentInfo.getDescription());
            }
            //关注
            if(types==3){
                ((MyViewHolder) holder).textViewAction.setText(attentInfo.getMessage());
                ((MyViewHolder) holder).textViewContent.setText(attentInfo.getFocusName());
                if(!attentInfo.getDescription().equals("null"))
                ((MyViewHolder) holder).textViewSign.setText(attentInfo.getDescription());
            }
            //素材&发布 收藏 购买
            if(contentType==1&&(types==0||types==1||types==6)){
                if(types==0){
                    ((MyViewHolder) holder).textViewAction.setText("发布素材");
                }if(types==1){
                    ((MyViewHolder) holder).textViewAction.setText("收藏素材");
                }if(types==6){
                    ((MyViewHolder) holder).textViewAction.setText("购买素材");
                }
                ((MyViewHolder) holder).textViewContent.setText(attentInfo.getContentName());
                ((MyViewHolder) holder).textViewSign.setText(attentInfo.getDescription());
            }
            //剧本&发布 收藏 购买
            if(contentType==2&&(types==0||types==1||types==6)){
                if(types==0){
                    ((MyViewHolder) holder).textViewAction.setText("发布剧本");
                }if(types==1){
                    ((MyViewHolder) holder).textViewAction.setText("收藏剧本");
                }if(types==6){
                    ((MyViewHolder) holder).textViewAction.setText("购买剧本");
                }
                ((MyViewHolder) holder).textViewContent.setText(attentInfo.getContentName());
                ((MyViewHolder) holder).textViewSign.setText(attentInfo.getDescription());
            }
            //学院&发布 收藏 购买
            if(contentType==3&&(types==0||types==1||types==6)){
                if(types==0){
                    ((MyViewHolder) holder).textViewAction.setText("发布学院");
                }if(types==1){
                    ((MyViewHolder) holder).textViewAction.setText("收藏学院");
                }if(types==6){
                    ((MyViewHolder) holder).textViewAction.setText("购买学院");
                }
                ((MyViewHolder) holder).textViewContent.setText(attentInfo.getContentName());
                ((MyViewHolder) holder).textViewSign.setText(attentInfo.getDescription());
            }
            //手记&发布 转载
            if(contentType==4&&(types==0||types==5)){
                if(contentType==0){
                    ((MyViewHolder) holder).textViewAction.setText("发布手记");
                }if(contentType==5){
                    ((MyViewHolder) holder).textViewAction.setText("转载手记");
                }
                ((MyViewHolder) holder).textViewContent.setText(attentInfo.getContentName());
                ((MyViewHolder) holder).textViewSign.setText(attentInfo.getDescription());
            }
            //升级会员
            if(types==4){
                if(contentType==2){
                    ((MyViewHolder) holder).textViewAction.setText("Gaiamount PRO");
                }if(contentType==4){
                    ((MyViewHolder) holder).textViewAction.setText("Gaiamount BNS");
                }
                ((MyViewHolder) holder).textViewContent.setText(attentInfo.getContentName());
                ((MyViewHolder) holder).textViewSign.setText("Gaiamount 为会员用户提供了更加专业和更高质量 的转码选项；其中专业（PRO）会员与高级（ADV） 会员可以选择 YUV422 10bit 的高质量转码，商业 （BNS）会员更是可以选择YUV444 10bit CRF17 的母带级别转码质量！");
            }
            //头像的点击
            ((MyViewHolder) holder).imageViewHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startPersonalActivity(context,attentInfo.getOtherId());
                }
            });
            //item的点击
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //作品
                    if(contentType==0&&(types==0||types==1||types==6)){
                        ActivityUtil.startPlayerActivity(context,attentInfo.getContentId(),0);
                    }
                    //剧本
                    if(contentType==2&&(types==0||types==1||types==6)){
                        ActivityUtil.startScripeDetailActivity(context,attentInfo.getContentId());
                    }
                    //学院
                    if(contentType==3&&(types==0||types==1||types==6)){
                        ActivityUtil.startAcademyDetailActivity(attentInfo.getContentId(),context);
                    }
                    //素材
                    if(contentType==1&&(types==0||types==1||types==6)){
                        ActivityUtil.startMaterialPlayActivity(context,attentInfo.getContentId(),1); // 1 表示是视频素材,0表示模板素材
                    }
                    //手记
                    if(contentType==4&&(types==0||types==5)){
                        ActivityUtil.startNOtesDetailActivity(context,attentInfo.getUserId(),attentInfo.getContentId());
                    }
                    //关注
                    if(types==3){
                        ActivityUtil.startPersonalActivity(context,attentInfo.getContentId());
                    }
                    //升级
                    if(types==4){
                        ActivityUtil.startPersonalActivity(context,attentInfo.getUserId());
                    }
                    //小组
                    if(types==2){
                        ActivityUtil.startGroupActivity(context,attentInfo.getContentId());
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return attentInfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageViewHead;
        TextView textViewName,textViewContent,textViewTime,textViewText,textViewAction,textViewSign;
        TextView textViewLike,textViewComment,textViewShare;
        ImageView imageViewCover;
        RelativeLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageViewHead= (CircleImageView) itemView.findViewById(R.id.message_head);
            textViewName= (TextView) itemView.findViewById(R.id.message_name);
            textViewContent= (TextView) itemView.findViewById(R.id.message_content);
            textViewTime= (TextView) itemView.findViewById(R.id.message_time);
            textViewText= (TextView) itemView.findViewById(R.id.message_text);
            textViewSign= (TextView) itemView.findViewById(R.id.message_sign);
            textViewAction= (TextView) itemView.findViewById(R.id.message_action);
            textViewLike= (TextView) itemView.findViewById(R.id.message_like);
            textViewComment= (TextView) itemView.findViewById(R.id.message_comment);
            textViewShare= (TextView) itemView.findViewById(R.id.message_share);
            imageViewCover= (ImageView) itemView.findViewById(R.id.message_cover);
            layout= (RelativeLayout) itemView.findViewById(R.id.message_layout_like);
        }
    }

    private String getTime(Long msgTime) {
        long l = System.currentTimeMillis();
        if(l-msgTime<3600000){
            SimpleDateFormat formatter = new SimpleDateFormat("mm");
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
            SimpleDateFormat formatter = new SimpleDateFormat("hh");
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
}
