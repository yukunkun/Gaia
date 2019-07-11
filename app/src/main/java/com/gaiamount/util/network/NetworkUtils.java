package com.gaiamount.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.LogUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by haiyang-lu on 16-3-4.
 * 联网工具类
 */
public class NetworkUtils {

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NO_NETWORK = 0;
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;

    public static AsyncHttpClient aHttpClient = new AsyncHttpClient();

    /**
     * 使用get联网
     *
     * @param url             地址
     * @param requestParams   请求参数
     * @param responseHandler 处理
     */
    public static void get(String url, RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        aHttpClient.get(url, requestParams, responseHandler);

    }

    /**
     *
     * @param url
     * @param jsonObject
     * @param context
     * @param responseHandler
     * 传参数的get
     */
    public static void get(String url,JSONObject jsonObject, Context context,AsyncHttpResponseHandler responseHandler) {

        String jsonString = jsonObject.toString();
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonString.getBytes("utf-8"));
            //设置超时时间
            aHttpClient.setTimeout(5);
            aHttpClient.get(context, url, entity, "application/json", responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用get联网
     *
     * @param url             地址
     * @param responseHandler 处理
     */
    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        aHttpClient.get(url,responseHandler);
//        aHttpClient.post(url,responseHandler);
    }

//    /**
//     * 使用post联网
//     *
//     * @param url             地址
//     * @param responseHandler 处理
//     */
//    public static void post(Context context,String url, AsyncHttpResponseHandler responseHandler) {
//        aHttpClient.setMaxRetriesAndTimeout(1,5000);
//        aHttpClient.post(url,responseHandler);
//    }
    /**
     * 使用post联网
     *
     * @param context         上下文
     * @param url             地址
     * @param jsonObject      要上传的json对象
     * @param responseHandler 处理
     */
    public static void post(Context context, String url, JSONObject jsonObject, AsyncHttpResponseHandler responseHandler) {
        aHttpClient.setMaxRetriesAndTimeout(1,5000);
        if (!GaiaApp.getToken().equals("")) {//如果有cookie，则带上
            aHttpClient.addHeader("Set-Cookie", GaiaApp.getToken());
            Log.i("=====",GaiaApp.getToken());
        }
        try {
            String jsonString = jsonObject.toString();
            ByteArrayEntity entity = new ByteArrayEntity(jsonString.getBytes("utf-8"));
            //设置超时时间
            aHttpClient.setTimeout(5);
            aHttpClient.post(context, url, entity, "application/json", responseHandler);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查网络连接
     * @return
     */
    public static int checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) GaiaApp.getAppInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null && activeNetworkInfo.isConnected()) {
            wifiConnected = activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeNetworkInfo.getType()== ConnectivityManager.TYPE_MOBILE;
            if(wifiConnected){
                return TYPE_WIFI;
            } else if(mobileConnected) {
                GaiaApp.showToast(GaiaApp.getAppInstance().getResources().getString(R.string.mobile_network_hint));
                return TYPE_MOBILE;
            } else {
                GaiaApp.showToast(GaiaApp.getAppInstance().getResources().getString(R.string.no_network_hint));
                return TYPE_NO_NETWORK;
            }
         }else {
            GaiaApp.showToast(GaiaApp.getAppInstance().getResources().getString(R.string.no_network_hint));
            return TYPE_NO_NETWORK;
        }
    }

}

