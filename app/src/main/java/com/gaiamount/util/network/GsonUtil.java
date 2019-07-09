package com.gaiamount.util.network;

import com.google.gson.Gson;

/**
 * Created by haiyang-lu on 16-4-29.
 */
public class GsonUtil {
    private static GsonUtil mGsonUtil;
    private Gson mGson;

    private GsonUtil () {
        mGson = new Gson();
    }

    public static GsonUtil getInstannce() {
        if(mGsonUtil==null) {
            mGsonUtil = new GsonUtil();
        }
        return mGsonUtil;
    }

    public Gson getGson() {
        return mGson;
    }
}
