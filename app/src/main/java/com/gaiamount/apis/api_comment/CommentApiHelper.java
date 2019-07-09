package com.gaiamount.apis.api_comment;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-7-1.
 * 评论相关的接口帮助类
 */
public class CommentApiHelper {
    /**
     * 发送评论接口
     *
     * @param uid         用户id
     * @param content     评论内容
     * @param contentId   评论目标的id
     * @param contentType 目标类型 0作品     1素材(暂时)
     * @param isReply     是否是回复 0否1是
     * @param cid         回复的目标id   (isReply=0时cid=0)
     */
    public static void sendComment(long uid, String content, long contentId, int contentType, int isReply, long cid, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("uid", uid);
            jsonParams.put("content", content);
            jsonParams.put("contentId", contentId);
            jsonParams.put("contentType", contentType);
            jsonParams.put("isReply", isReply);
            jsonParams.put("cid", cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(CommentApiHelper.class, "sendComment",jsonParams.toString());

        NetworkUtils.post(context, CommentApi.SEND_COMMENT, jsonParams, jsonHttpResponseHandler);
    }

    /**
     * @param contentId               评论目标的id
     * @param contentType             目标类型 0作品  1素材(暂时)
     * @param pi                      数据的第几页 默认1
     * @param ps                      每一页多少条数据   默认 8
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getCommentData(long contentId, int contentType, int pi, int ps, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contentId", contentId);
            jsonObject.put("contentType", contentType);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(CommentApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, CommentApi.GET_COMMENT_LIST, jsonObject, jsonHttpResponseHandler);

    }

    /**
     * 删除评论
     *
     * @param id      评论id
     * @param context 上下文
     */
    public static void deleteComment(final long id, final AppCompatActivity context,MJsonHttpResponseHandler jsonHttpResponseHandler) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(CommentApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, CommentApi.DELETE_COMMENT, jsonObject, jsonHttpResponseHandler);

    }
}
