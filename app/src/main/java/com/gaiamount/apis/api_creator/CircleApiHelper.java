package com.gaiamount.apis.api_creator;

import android.content.Context;

import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yukun on 16-7-25.
 */
public class CircleApiHelper {

    //获取关注的人
    public static void getAtention(Context context, long uid,int t, int opr, int pi, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", uid);
            jsonObject.put("p", 1);
            jsonObject.put("t", t);
            jsonObject.put("opr", opr);
            jsonObject.put("pi", pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "AttentionFragment:" + jsonObject.toString());

        NetworkUtils.post(context, CircleApi.circle_attention, jsonObject, jsonHttpResponseHandler);

    }
    //关注的粉丝
    public static void getFans(Context context, long uid,int pi, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", uid);
            jsonObject.put("p", 2);
            jsonObject.put("t", 0);
            jsonObject.put("opr", 0);
            jsonObject.put("pi", pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "AttentionFragment:" + jsonObject.toString());

        NetworkUtils.post(context, CircleApi.circle_attention, jsonObject, jsonHttpResponseHandler);
    }

    //小组列表
    public static void getGroupMessage(Context context, long uid,int pi, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", uid);
            jsonObject.put("p", 0);
            jsonObject.put("t", 0);
            jsonObject.put("opr", 0);
            jsonObject.put("pi", pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "AttentionFragment:" + jsonObject.toString());

        NetworkUtils.post(context, CircleApi.circle_attention, jsonObject, jsonHttpResponseHandler);

    }
}
