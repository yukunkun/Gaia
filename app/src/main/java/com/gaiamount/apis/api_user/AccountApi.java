package com.gaiamount.apis.api_user;

import com.gaiamount.apis.Configs;

/**
 * Created by haiyang-lu on 16-3-31.
 * 账户相关接口
 */
public class AccountApi {
    public static final String URL_ACCOUNT = Configs.BASE_URL +"/account";
    /**
     * vef/验证邮箱
     */
    public static final int VEF_OPR_EMIAL = 1;
    /**
     * vef绑定邮箱
     */
    public static final int VEF_TYPE_BIND_EMAIL = 2;
    /**
     * 验证账号是否可用
     */
    public static final String AVB_URL = URL_ACCOUNT+"/avb";
    /**
     * 发送验证码
     */
    public static final String VEF_URL = URL_ACCOUNT+"/vef";
    /**
     * 注册账号
     */
    public static final String REG_URL = URL_ACCOUNT+"/reg";
    /**
     * 登陆账号
     */
    public static final String LOGIN_URL = URL_ACCOUNT+"/_login";
    /**
     * 注销账号
     */
    public static final String LOGOUT_URL = URL_ACCOUNT + "/logout";
    /**
     * 重置密码
     */
    public static final String FORGET_URL =URL_ACCOUNT +"/forget";
    /**
     * 获取用户信息
     */
    public static final String GU_URL = URL_ACCOUNT+"/gu";
    /**
     * 更新用户信息
     */
    public static final String UPDATE_URL = URL_ACCOUNT + "/update";

    public static final String BIND_URL = URL_ACCOUNT +"/bind";

    /**
     * 设置收款账户
     */
    public static final String BANK_ACCOUNT_URL = URL_ACCOUNT+"/bankAccount";

    public static final String FEED_BACK_URL = URL_ACCOUNT+"/feedback";

    //版本更新的uri
    public static final String VERSION = Configs.BASE_URL+"/version/andr_getVersion";

}
