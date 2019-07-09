package com.gaiamount.apis.api_cms_home;

import com.gaiamount.apis.Configs;

/**
 * Created by yukun on 16-10-31.
 */
public class CmsHomeApi {

    public static String CMS= Configs.BASE_URL+"/cms/home";
    //主页banner
    public static String HOMEBANNER= CMS+"/getShuffling";
    //推荐列表
    public  static String HOMEREC=CMS+"/recommend";
}
