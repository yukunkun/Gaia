package com.gaiamount.apis.api_im;

import com.gaiamount.apis.Configs;

/**
 * Created by yukun on 16-7-14.
 */
public class ImApi {

    public static final String IM_URL = Configs.BASE_URL + "/im";
    //环信token
    public static final String TOKEN_URL = IM_URL + "/getToken";
    //联系人
    public static final String CONTACT_URL = IM_URL + "/getContacts";
    //聊天小组
    public static final String GROUP_URL = IM_URL + "/getMyChatGroups";
    //拉黑uri
    public static final String IM_KILL = Configs.BASE_URL+"/creator/person/shielding";
    //拉黑列表
    public static final String IM_KILL_INFO = Configs.BASE_URL+"/creator/person/getShieldList";
    //取消拉黑
    public static final String RERET_FRI =  Configs.BASE_URL+ "/creator/person/cancleShielding";

}
