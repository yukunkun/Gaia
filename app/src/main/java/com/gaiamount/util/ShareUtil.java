package com.gaiamount.util;

import android.content.Context;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by haiyang-lu on 16-7-13.
 */
public class ShareUtil {
    private static ShareUtil mShareUtil;
    private  Context mContext;

    private ShareUtil(Context context) {

        mContext = context;
    }

    public static ShareUtil newInstance(Context context){
        if (mShareUtil==null) {
            mShareUtil = new ShareUtil(context);
        }
        return mShareUtil;
    }

    public  void startShare(String title,String titleUrl,String text,String imageUrl) {
        //分享
        ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
//            oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setTitleUrl(titleUrl);
        oks.setText(titleUrl);
        oks.setImageUrl(imageUrl);
        oks.setUrl(titleUrl);
        oks.setSiteUrl(titleUrl);
        oks.setSilent(true);
        //打开界面
        oks.show(mContext);
    }
    public  void share(String title,String titleUrl,String imageUrl,String siteUrl) {
        //分享
        ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
//            oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setTitleUrl(siteUrl);
        oks.setText(titleUrl);
        oks.setImageUrl(imageUrl);
        oks.setUrl(titleUrl);
        oks.setSiteUrl(siteUrl);
        oks.setSilent(true);
        //打开界面
        oks.show(mContext);
    }

}
