package com.gaiamount.apis.api_im;

import android.content.Context;
import android.util.Log;

import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yukun on 16-7-14.
 */
public class ImApiHelper {
    //获取Tolen值
    public static void getToken(JsonHttpResponseHandler jsonHttpResponseHandler){
        Log.d("MySecretActivity", "getToken:");
        NetworkUtils.get(ImApi.TOKEN_URL,jsonHttpResponseHandler);
    }

    //获取联系人列表
    public static void getContacts(long uid,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            Log.d("ContentPerson", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,ImApi.CONTACT_URL,jsonObject,jsonHttpResponseHandler);
    }

    //获取小组的信息
    public static void getGroup(long uid,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            Log.d("GroupFragment",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,ImApi.GROUP_URL,jsonObject,jsonHttpResponseHandler);
    }
    //拉黑
    public static void killFri(long uid,long oid,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("oid",oid);
            Log.d("ContactChatActivity",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,ImApi.IM_KILL,jsonObject,jsonHttpResponseHandler);
    }

    //获取拉黑列表
    public static void getKillList(long uid,int pi,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            Log.d("KillFriActivity",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,ImApi.IM_KILL_INFO,jsonObject,jsonHttpResponseHandler);
    }
    //取消拉黑
    public static void ResetFri(long fid,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("fid",fid);
            Log.d("KillFriActivity",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,ImApi.RERET_FRI,jsonObject,jsonHttpResponseHandler);
    }
}
