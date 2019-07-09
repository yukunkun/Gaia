package com.gaiamount.module_material.activity.video;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_material.bean.MaterialDetailInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;

/**
 * Created by haiyang-lu on 16-7-12.
 * 视频相关工具类
 */
public class MaterialVideoUtil {
    String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/gaia/test.m3u8";
    private  Uri uri;

    public void getVideoInfo(Activity activity, final long mid) {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(MaterialVideoUtil.class) {
            @Override
            public void parseJson(JSONObject response) {
                super.parseJson(response);
                MaterialDetailInfo detailInfo = new Gson().fromJson(response.optJSONObject("o").toString(), MaterialDetailInfo.class);
                EventBus.getDefault().post(new OnVideoInfoGettedEvent(detailInfo));
                JSONObject jsonObject = response.optJSONObject("o").optJSONObject("material");
                JSONObject jsonObject1 = response.optJSONObject("o").optJSONObject("video");
                JSONObject jsonObject2 = response.optJSONObject("o").optJSONObject("audio");
                Log.i("-----",response.toString());
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                int i = response.optInt("i");
                if (i == 30803) {
                    GaiaApp.showToast("视频不存在");
                }
            }
        };
        MaterialApiHelper.getMaterialDetail(GaiaApp.getUserInfo().id,mid, activity, jsonHttpResponseHandler);
    }

    public class OnVideoInfoGettedEvent {
        private String defaultPath;
        private MaterialDetailInfo mDetailInfo;

        public OnVideoInfoGettedEvent(MaterialDetailInfo detailInfo) {
            mDetailInfo = detailInfo;
//            String uri="http://qv.gaiamount.com/playlist/u1638/v17474_fhd_src.m3u8?e=1480507318&amp;token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:emAAbGKplNXZluuypR2X29guOhg=";
//            String uri2="http://qv.gaiamount.com/playlist/u3320/v3310_mp4.m3u8?e=1470708869&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:aCqTqhZoFBa6OuWJDXEFjaH9Zig=";
//            String uri="http://qv.gaiamount.com/playlist/u1638/v17474_fhd_src.m3u8?e=1480507318&amp;token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:emAAbGKplNXZluuypR2X29guOhg=";

//            defaultPath=uri;
            defaultPath=path;

//            根据情况判断使用哪个作为默认的uri，如果都为0，使用MP4
            defaultPath = selectDefaultUri(detailInfo);
        }

        public String getDefaultPath() {
            return defaultPath;
        }

        public MaterialDetailInfo getDetailInfo() {
            return mDetailInfo;
        }

    }

    private String selectDefaultUri(MaterialDetailInfo detailInfo) {
        String defaultPath = null;
        int have4k = detailInfo.getMaterial().getHave4K();
        int have2k = detailInfo.getMaterial().getHave2K();
        int have1080 = detailInfo.getMaterial().getHave1080();
        int have720 = detailInfo.getMaterial().getHave720();
        if (have720 == 1) {
            defaultPath = detailInfo.getResource().getP720();
        } else if (have1080 == 1) {
            defaultPath = detailInfo.getResource().getP1080();
        } else if (have2k == 1) {
            defaultPath = detailInfo.getResource().getK2();
        } else if (have4k == 1) {
            defaultPath = detailInfo.getResource().getK4();
        }
        if (defaultPath == null) {
            defaultPath = detailInfo.getResource().getMp4();
        } else {
            return defaultPath;
        }
        return defaultPath;
    }
}
