package com.gaiamount.module_creator.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.beans.GroupCommentBean;
import com.gaiamount.module_creator.sub_module_group.fragment.GroupMainPageFrag;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.StringUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by haiyang-lu on 16-6-15.
 */
public class GroupCommentListAdapter extends BaseAdapter {

    private final Context mContext;
    private GroupMainPageFrag mGroupMainPageFrag;
    private  List<GroupCommentBean> mGroupCommentBeanList;
    private ImageUtils mImageUtils;

    public GroupCommentListAdapter(Context context, GroupMainPageFrag groupMainPageFrag, List<GroupCommentBean> groupCommentBeanList) {

        mContext = context;
        mGroupMainPageFrag = groupMainPageFrag;
        mGroupCommentBeanList = groupCommentBeanList;
        mImageUtils = ImageUtils.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mGroupCommentBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_group_comment, null);
        ViewHolder holder = new ViewHolder(view);
        holder.setValue(position);
        return view;
    }

    public void setData(List<GroupCommentBean> list) {
        mGroupCommentBeanList = list;
    }

    class ViewHolder{
        ImageView avatar;
        TextView commenter;
        TextView commentTime;
        TextView commentContent;
        TextView commentCallback;
        ImageView callBackAvatar;
        ImageView commentIcon;
        TextView deleteComment;

        ViewHolder (View view) {
            avatar = (ImageView) view.findViewById(R.id.comment_avatar);
            commenter = (TextView) view.findViewById(R.id.commenter);
            commentTime = (TextView) view.findViewById(R.id.comment_time);
            commentContent = (TextView) view.findViewById(R.id.comment_content);
            commentCallback = (TextView) view.findViewById(R.id.comment_callback_content);
            callBackAvatar = (ImageView) view.findViewById(R.id.comment_callback_avatar);
            commentIcon = (ImageView) view.findViewById(R.id.comment_icon);
            deleteComment = (TextView) view.findViewById(R.id.delete_comment);
        }

        public void setValue(final int position) {
            final GroupCommentBean playerCommentInfo = mGroupCommentBeanList.get(position);
            //头像
            String avatar = playerCommentInfo.getAvatar();
            mImageUtils.getAvatar(this.avatar, avatar);
            //评论者
            commenter.setText(playerCommentInfo.getNickName());
            //评论时间
            commentTime.setText(StringUtil.getInstance().stringForDate(playerCommentInfo.getCreateTime().getTime()));
            //评论内容
            commentContent.setText(playerCommentInfo.getContent());
            //回复内容
            String originContent = playerCommentInfo.getOriginContent();
            if(originContent !=null && !originContent.equals("null")&& !TextUtils.isEmpty(originContent)) {
                SpannableStringBuilder commentCallbackStr = new SpannableStringBuilder(playerCommentInfo.getOriginNickName()+":");
                commentCallbackStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_333)),0,commentCallbackStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                commentCallbackStr.append(originContent);
                commentCallback.setText(commentCallbackStr);
                //回复者头像
                String originAvatar = playerCommentInfo.getOriginAvatar();
                mImageUtils.getAvatar(callBackAvatar, originAvatar);

                callBackAvatar.setVisibility(View.VISIBLE);
                commentCallback.setVisibility(View.VISIBLE);
            }
            else {
                callBackAvatar.setVisibility(View.GONE);
                commentCallback.setVisibility(View.GONE);
            }

            //commentIcon点击事件
            commentIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开回复
                    mGroupMainPageFrag.replyToggle(true,position);
                }
            });

            //删除评论
            deleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder unLoginDialog = new AlertDialog.Builder(mContext);

                    unLoginDialog.setMessage("请确认您的操作")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupCommentListAdapter.class){
                                        @Override
                                        public void onGoodResponse(JSONObject response) {
                                            super.onGoodResponse(response);
                                            GaiaApp.showToast("删除成功");
                                            mGroupMainPageFrag.getComment();
                                        }
                                    };
                                    GroupApiHelper.deleteGroupOneComment(playerCommentInfo.getId(),mContext,jsonHttpResponseHandler);
;
                                }
                            })
                            .setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                            .create();

                    unLoginDialog.show();

                }
            });

        }
    }
}
