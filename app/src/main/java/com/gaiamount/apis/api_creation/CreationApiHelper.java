package com.gaiamount.apis.api_creation;

import android.content.Context;
import android.util.Log;

import com.gaiamount.apis.api_academy.AcademyApi;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yukun on 16-8-25.
 */
public class CreationApiHelper {
    //学习进度
    public static void creationAcademy(long cid, int s,int pi,long uid,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",cid);
            jsonObject.put("pi",pi);
            jsonObject.put("s",s);
            jsonObject.put("opr",0);
            jsonObject.put("uid", uid);
            Log.d("CreateAcademyFrag", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context, CreationApi.CREATION_ACADEMY,jsonObject,jsonHttpResponseHandler);
    }
}
