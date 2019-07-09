package com.gaiamount.apis.api_im;

import android.content.Context;

import com.gaiamount.module_im.relate_me.RelateActivity;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yukun on 16-12-29.
 */
public class AynamicsHelper {
    /**
     * 获取与我相关的
     * @param uid
     * @param pi
     * @param context
     * @param handler
     */
    public static void DynamicRe(long uid, int pi, Context context, MJsonHttpResponseHandler handler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("ps",8);
            jsonObject.put("pi",pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(RelateActivity.class,jsonObject.toString());
        NetworkUtils.post(context,Aynamics.DYNAMICRELATE,jsonObject,handler);
    }

    /**
     * 我关注的
     * @param uid
     * @param pi
     * @param context
     * @param handler
     */
    public static void MyAttention(long uid, int pi, Context context, MJsonHttpResponseHandler handler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("ps",8);
            jsonObject.put("pi",pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(RelateActivity.class,jsonObject.toString());
        NetworkUtils.post(context,Aynamics.MYATTENTION,jsonObject,handler);
    }
    /**
     * 我关注的小组动态
     * @param uid
     * @param pi
     * @param context
     * @param handler
     */
    public static void GroupDynamic(long uid, int pi, Context context, MJsonHttpResponseHandler handler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("ps",8);
            jsonObject.put("pi",pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(RelateActivity.class,jsonObject.toString());
        NetworkUtils.post(context,Aynamics.GROUPDYNAMIC,jsonObject,handler);
    }
    /**
     * 我加入的小组
     * @param uid
     * @param pi
     * @param context
     * @param handler
     */
    public static void GroupJoin(long uid, int pi, Context context, MJsonHttpResponseHandler handler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("ps",8);
            jsonObject.put("pi",pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(RelateActivity.class,jsonObject.toString());
        NetworkUtils.post(context,Aynamics.GROUPJOIN,jsonObject,handler);
    }
}
