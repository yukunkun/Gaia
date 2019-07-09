package com.gaiamount.module_user.personal_album.adapter;

import android.app.Activity;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.fragment.AlbumDialog;
import com.gaiamount.module_creator.sub_module_album.AlbumBean;
import com.gaiamount.module_creator.sub_module_album.AlbumDetail;
import com.gaiamount.module_creator.sub_module_album.AlbumDetailActivity;
import com.gaiamount.module_user.personal_album.PersonalAlbumActivity;
import com.gaiamount.module_user.personal_album.activity.PersonAlbumDetailActivity;
import com.gaiamount.module_user.personal_album.bean.OnEventPersonAlbum;
import com.gaiamount.module_user.personal_album.bean.PersonAlbum;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by yukun on 17-1-4.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PersonAlbum> mList;
    private final ImageUtils mImageUtils;
    private Context context;
    private long uid;
    public MyAdapter(Context context, ArrayList<PersonAlbum> mList, long uid) {
        this.mList = mList;
        this.context= context;
        this.uid=uid;
        mImageUtils = ImageUtils.getInstance(context);
    }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(context, R.layout.item_album_list, null);
            MViewHolder holder = new MViewHolder(itemView);
            return holder;
        }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MViewHolder) {
            final PersonAlbum albumBean = mList.get(position);
            //专辑封面
            Glide.with(context).load(Configs.COVER_PREFIX + albumBean.getBgImg()).placeholder(R.mipmap.bg_general).into(((MViewHolder)holder).albumCover);
//            //专辑信息
//            more = "作品 " + albumBean.getWorksCount() + "|" +
//                    "素材 " + albumBean.getMaterialCount() + "|" +
//                    "剧本 " + albumBean.getScriptCount() + "|" +
//                    "学院 " + albumBean.getCourseCount();

            //专辑标题
            ((MViewHolder)holder).albumTitle.setText(albumBean.getName());

//            ViewGroup.LayoutParams layoutParams = (holder.albumCover).getLayoutParams();
//            layoutParams.height = getHeight();
//            (holder.albumCover).setLayoutParams(layoutParams);
            (((MViewHolder)holder).albumCover).getLayoutParams().height = getHeight();

            //判断是否公开
            if (albumBean.getIsPublic() == 0) { //1为公开
                ((MViewHolder)holder).albumSec.setVisibility(View.VISIBLE);
            } else {
                ((MViewHolder)holder).albumSec.setVisibility(View.GONE);
            }
            //点击封面跳转到专辑详情
            ((MViewHolder)holder).albumCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(albumBean.getIsPublic()==0){  //ispublic 0表示已经加密
                        selfDialoga(albumBean.getId(),albumBean);
                    }else /*if(albumBean.getIsPublic()==1)*/{
                        Intent intent = new Intent(context, PersonAlbumDetailActivity.class);
                        intent.putExtra(AlbumDetailActivity.AID, albumBean.getId());
                        intent.putExtra(AlbumDetailActivity.ALBUM_TYPE, 0);
                        intent.putExtra(AlbumDetailActivity.IS_PUBLIC, albumBean.getIsPublic());
                        intent.putExtra("gid",uid);
                        context.startActivity(intent);
                    }
                }
            });

            if((albumBean.getUserId()==GaiaApp.getAppInstance().getUserInfo().id)){  //有权限
                ((MViewHolder)holder).albumMore.setVisibility(View.VISIBLE);
                ((MViewHolder)holder).albumMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopu(((MViewHolder)holder).albumMore,albumBean.getId(),position);
                    }
                });
            }else {
                ((MViewHolder)holder).albumMore.setVisibility(View.GONE);
            }
        }
    }

        @Override
        public int getItemCount() {
            return mList.size();
        }

    class MViewHolder extends RecyclerView.ViewHolder {
        private TextView albumTitle,albumSec;
        private ImageView albumCover,albumMore;

        public MViewHolder(View itemView) {
            super(itemView);
            albumCover = (ImageView) itemView.findViewById(R.id.album_cover);
            albumTitle = (TextView) itemView.findViewById(R.id.album_title);
            albumMore= (ImageView) itemView.findViewById(R.id.album_more);
            albumSec= (TextView) itemView.findViewById(R.id.album_secret);
        }
    }

        int mScreenWidth;
        int mItemHeight;
        private int getHeight(){
            mScreenWidth = ScreenUtils.instance().getWidth();
            int itemWidth = (mScreenWidth -(3* ScreenUtils.dp2Px(context,8)))/2;
            mItemHeight = (int) (itemWidth *0.562);
            return mItemHeight;
        }

    private PopupWindow popupWindow;
    private void showPopu(ImageView view, long aid, int position) {
        CardView inflate = (CardView) LayoutInflater.from(context).inflate(R.layout.academy_collection_shanchu, null);
        popupWindow=new PopupWindow(inflate,ScreenUtils.dp2Px(context, 80), LinearLayout.LayoutParams.WRAP_CONTENT);
        //移除专辑
        TextView textViewDelete= (TextView) inflate.findViewById(R.id.academy_collect_delete);
        TextView textViewSeeting= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
        textViewSeeting.setVisibility(View.GONE);

        textViewDelete.setOnClickListener(new Listener(aid,position));
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
        popupWindow.showAsDropDown(view,0-popupWidth,0-popupHeight- ScreenUtils.dp2Px(context, 25));
    }
    class  Listener implements View.OnClickListener{
        long aid;
        int position;
        public Listener(long aid, int position) {
            this.aid=aid;
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new OnEventPersonAlbum(position,aid));
            popupWindow.dismiss();

        }
    }

    //自定义的对话框
    private void selfDialoga(final long aid, final PersonAlbum albumBean) {
         AlertDialog.Builder builder = new AlertDialog.Builder(context);
         LayoutInflater inflater =LayoutInflater.from(context);
         final View view = inflater.inflate(R.layout.my_dialog, null);

        builder.setTitle("请输入密码");
        builder.setView(view);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                EditText editText= (EditText) view.findViewById(R.id.my_dialog_edit);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                sendSecret(aid,editText.getText().toString(),albumBean);
            }
        });

        builder.setNegativeButton(context.getResources().getString(R.string.give_up), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
// 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
    private void sendSecret(final long aid, final String pwd, final PersonAlbum albumBean) {
        //验证密码是否正确，同时如果正确，返回专辑详情
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbumDetailActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONObject a = response.optJSONObject("a");
                Intent intent = new Intent(context, PersonAlbumDetailActivity.class);
                intent.putExtra(AlbumDetailActivity.AID, aid);
                intent.putExtra(AlbumDetailActivity.ALBUM_TYPE, 0);
                intent.putExtra("pwd", pwd);
                intent.putExtra("gid",uid);
                intent.putExtra(AlbumDetailActivity.IS_PUBLIC, albumBean.getIsPublic());
                context.startActivity(intent);
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                GaiaApp.showToast("密码错误");
            }
        };
        AlbumApiHelper.getAlbumDetail(aid, pwd, context, handler);
    }

}
