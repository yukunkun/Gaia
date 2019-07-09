package com.gaiamount.module_player.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_comment.CommentApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_player.bean.PlayerCommentInfo;
import com.gaiamount.module_player.fragments.PlayerCommentFrag;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by haiyang-lu on 16-4-27.
 */
public class PlayerCommentListAdapter extends BaseAdapter {
    private AppCompatActivity mContext;
    private PlayerCommentFrag mPlayerCommentFrag;
    private List<PlayerCommentInfo> mPlayerCommentInfos;
    private final LayoutInflater mInflater;
    private ImageUtils mImageUtils;

    public void setPlayerCommentInfos(List<PlayerCommentInfo> playerCommentInfos) {
        mPlayerCommentInfos = playerCommentInfos;
    }

    public PlayerCommentListAdapter(AppCompatActivity context, PlayerCommentFrag playerCommentFrag,
                                    List<PlayerCommentInfo> playerCommentInfos/*, ApiHelper apiHelper*/) {

        mContext = context;
        mPlayerCommentFrag = playerCommentFrag;
        mPlayerCommentInfos = playerCommentInfos;
        mInflater = LayoutInflater.from(mContext);
        mImageUtils = ImageUtils.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mPlayerCommentInfos.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_player_comment, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setValue(position);

        return convertView;
    }

    class ViewHolder {
        ImageView avatar;
        TextView commenter;
        TextView commentTime;
        TextView commentContent;
        TextView commentCallback;
        ImageView callBackAvatar;
        ImageView commentIcon;
        TextView deleteComment;

        ViewHolder(View view) {
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
            PlayerCommentInfo playerCommentInfo = mPlayerCommentInfos.get(position);
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
            if (originContent != null && !originContent.equals("null") && !TextUtils.isEmpty(originContent)) {
                SpannableStringBuilder commentCallbackStr = new SpannableStringBuilder(playerCommentInfo.getOriginNickName() + ":");
                commentCallbackStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_333)), 0, commentCallbackStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                commentCallbackStr.append(originContent);
                commentCallback.setText(commentCallbackStr);
                //回复者头像
                String originAvatar = playerCommentInfo.getOriginAvatar();
                mImageUtils.getAvatar(callBackAvatar, originAvatar);

                callBackAvatar.setVisibility(View.VISIBLE);
                commentCallback.setVisibility(View.VISIBLE);
            } else {
                callBackAvatar.setVisibility(View.GONE);
                commentCallback.setVisibility(View.GONE);
            }

            //commentIcon点击事件
            commentIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开回复
                    mPlayerCommentFrag.replyToggle(true, position);
                }
            });
            //是否可以删除评论（只有与自己相关的可以删除）
            if (GaiaApp.getUserInfo().id==playerCommentInfo.getUserId()) {
                deleteComment.setVisibility(View.VISIBLE);
            }else {
                deleteComment.setVisibility(View.GONE);
            }
            //删除评论
            deleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener onPositiveClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final PlayerCommentInfo playerCommentInfo1 = mPlayerCommentInfos.get(position);
                            long id = playerCommentInfo1.getId();
                            ProgressDialog progressDialog = ProgressDialog.show(mContext, null, "正在删除");
                            MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(PlayerCommentListAdapter.class, progressDialog) {
                                @Override
                                public void parseJson(JSONObject response) {
                                    super.parseJson(response);
                                    GaiaApp.showToast("删除评论成功");
                                    mPlayerCommentInfos.remove(playerCommentInfo1);
                                    notifyDataSetChanged();
                                }
                            };
                            CommentApiHelper.deleteComment(id,mContext,handler);
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder
                            .setMessage("请确认您的操作")
                            .setPositiveButton("删除", onPositiveClickListener)
                            .setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                            .create();

                    builder.create();

                    builder.show();
                }
            });

        }
    }
}
