package com.gaiamount.apis.api_creator;

import com.gaiamount.apis.Configs;

/**
 * Created by haiyang-lu on 16-7-22.
 * 创作者个人相关
 */
public class PersonApi {
    public static final String PERSON_URL = Configs.BASE_URL + "/creator/person";

    public static final String ADD_ATTENTION = PERSON_URL + "/focus";

    public static final String IS_ATTENTIONED = PERSON_URL + "/state";

    //获取到创作者个人列表  kun
    public static final String CREATE_PERSON = PERSON_URL + "/getList";
}
