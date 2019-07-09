package com.gaiamount.apis.api_user;

import android.content.Context;
import android.util.Log;

import com.gaiamount.module_user.notes.NoteCommentDialog;
import com.gaiamount.module_user.notes.NotesListActivity;
import com.gaiamount.module_user.task.LogUtils;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yukun on 16-12-28.
 */
public class NoteApiHelper {
    /**
     * 获取手记列表
     * @param uid
     * @param opr
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void noteList(long uid, int opr , int pi,Context context,JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("opr",opr );
            jsonObject.put("pi",pi);
            jsonObject.put("ps",8);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(NotesListActivity.class,jsonObject.toString());

        NetworkUtils.post(context, NoteApi.NOTELIST, jsonObject,jsonHttpResponseHandler);
    }
    public static void recomment(long uid,long id,int type,String content,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("id",id);
            jsonObject.put("type",type);
            jsonObject.put("content",content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(NoteCommentDialog.class,jsonObject.toString());
        NetworkUtils.post(context,NoteApi.NOTERECOMMENT,jsonObject,jsonHttpResponseHandler);
    }
    public static void rePrint(long uid,long nid,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("id",nid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(NoteCommentDialog.class,jsonObject.toString());
        NetworkUtils.post(context,NoteApi.NOTEREPRITE,jsonObject,jsonHttpResponseHandler);
    }
    public static void noteDetail(long uid,long nid,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("id",nid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(NoteCommentDialog.class,jsonObject.toString());
        NetworkUtils.post(context,NoteApi.NOTEDETAILS,jsonObject,jsonHttpResponseHandler);
    }
}
