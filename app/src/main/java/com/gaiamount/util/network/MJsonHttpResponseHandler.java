package com.gaiamount.util.network;

import android.app.ProgressDialog;
import android.content.Context;

import com.gaiamount.util.LogUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by haiyang-lu on 16-4-28.
 * 自定义的对网络返回的数据处理类
 * @see NetworkUtils#post(Context, String, JSONObject, AsyncHttpResponseHandler)
 */
public class MJsonHttpResponseHandler extends JsonHttpResponseHandler {
    private ProgressDialog mProgressDialog;
    private Class<?> mClazz;

    public MJsonHttpResponseHandler(Class<?> clazz) {
        mClazz = clazz;
    }

    public MJsonHttpResponseHandler(Class<?> jsonHttpResponseHandler,ProgressDialog progressDialog) {
        this(jsonHttpResponseHandler);
        mProgressDialog = progressDialog;
    }



    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        LogUtil.i(mClazz,"联网请求成功"+response.toString());
        if(response.optInt("b")==1) {
            onGoodResponse(response);
        } else {
            onBadResponse(response);
        }
    }


    /**
     * response中的b=0的调用该方法
     * @param response
     */
    public void onBadResponse(JSONObject response) {
        //根据错误值打印log
        JSONArray ec = response.optJSONArray("ec");
        int i = response.optInt("i");
        if (ec!=null) {
            LogUtil.e(mClazz,"ec:"+ec.toString()+"i:"+i);
        }
    }

    /**
     * respons的b==1的时候调用这方法
     * @param response
     */
    public void onGoodResponse(JSONObject response) {
//        LogUtil.d(mClazz,response.toString());
        //解析json
        parseJson(response);
    }

    public void parseJson(JSONObject response) {

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        if(errorResponse!=null) {
            LogUtil.e(mClazz,"联网请求失败"+"statusCode:"+statusCode+"throwable:"+throwable.toString()+"errorResponse:"+
                    errorResponse.toString());
        }

        onFailureResponse(statusCode,throwable);

    }

    public void onFailureResponse(int statusCode,Throwable throwable) {
        if (statusCode==500) {
            //测试时打开
//            GaiaApp.showToast("500：服务器错误");
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        LogUtil.e(mClazz,"联网请求失败"+"statusCode:"+statusCode+"throwable:"+throwable.toString()+"responseString:"+
                responseString.toString());
        onFailureResponse(statusCode,throwable);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        LogUtil.e(mClazz,"联网请求失败"+"statusCode:"+statusCode+"throwable:"+throwable.toString()+"errorResponse:"+
                errorResponse.toString());
        onFailureResponse(statusCode,throwable);

    }

    @Override
    public void onFinish() {
        super.onFinish();
        if(mProgressDialog!=null) {
            mProgressDialog.dismiss();
        }
    }
}
