package com.gaiamount.module_player;

import android.app.Activity;
import android.util.Log;

import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_player.bean.VideoDetailInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-7-12.
 * 视频相关工具类
 */
public class VideoUtil {

    public void getVideoInfo(Activity activity, final int workId) {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(VideoUtil.class) {
            @Override
            public void parseJson(JSONObject response) {
                super.parseJson(response);
                VideoDetailInfo detailInfo = new Gson().fromJson(response.optJSONObject("o").toString(), VideoDetailInfo.class);
                EventBus.getDefault().post(new OnVideoInfoGettedEvent(detailInfo));
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
        WorksApiHelper.getVideoDetail(GaiaApp.getUserInfo().id,workId, activity, jsonHttpResponseHandler);
    }

    public class OnVideoInfoGettedEvent {
        private String defaultPath;
        private VideoDetailInfo mDetailInfo;

        public OnVideoInfoGettedEvent(VideoDetailInfo detailInfo) {
            mDetailInfo = detailInfo;
            //根据情况判断使用哪个作为默认的uri，如果都为0，使用MP4
            defaultPath = selectDefaultUri(detailInfo);
        }

        public String getDefaultPath() {
            return defaultPath;
        }

        public VideoDetailInfo getDetailInfo() {
            return mDetailInfo;
        }
    }

    private String selectDefaultUri(VideoDetailInfo detailInfo) {
        String defaultPath = null;
        int have4k = detailInfo.getWorks().getHave4K();
        int have2k = detailInfo.getWorks().getHave2K();
        int have1080 = detailInfo.getWorks().getHave1080();
        int have720 = detailInfo.getWorks().getHave720();
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
