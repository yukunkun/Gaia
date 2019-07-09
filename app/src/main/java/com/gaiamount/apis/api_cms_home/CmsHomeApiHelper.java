package com.gaiamount.apis.api_cms_home;

import android.support.v7.app.AppCompatActivity;

import com.gaiamount.apis.api_comment.CommentApi;
import com.gaiamount.gaia_main.home.HomeFrag;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;



/**
 * Created by yukun on 16-10-31.
 */
public class CmsHomeApiHelper {

    /**
     * 获取到Banner条
     * @param jsonHttpResponseHandler
     */
    public static void homeBanner(MJsonHttpResponseHandler jsonHttpResponseHandler) {


        LogUtil.d(HomeFrag.class, CmsHomeApi.HOMEBANNER);

        NetworkUtils.get(CmsHomeApi.HOMEBANNER,jsonHttpResponseHandler);

    }

    /**
     *  主页推荐
     * @param jsonHttpResponseHandler
     */
    public static void homeRecomend(MJsonHttpResponseHandler jsonHttpResponseHandler) {

        LogUtil.d(HomeFrag.class, CmsHomeApi.HOMEREC);

        NetworkUtils.get(CmsHomeApi.HOMEREC,jsonHttpResponseHandler);

    }
}
