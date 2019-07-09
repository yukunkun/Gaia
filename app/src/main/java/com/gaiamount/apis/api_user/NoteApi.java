package com.gaiamount.apis.api_user;

import com.gaiamount.apis.Configs;

/**
 * Created by yukun on 16-12-28.
 */
public class NoteApi {

    public static final String NOTE = Configs.BASE_URL + "/note";
    /**
     * 手记列表
     */
    public static final String NOTELIST = NOTE + "/getList";
    /**
     * 手记详情
     */
    public static final String NOTEDETAILS=NOTE+"/details";
     /**
      * 手记评论
      */
    public static final String NOTERECOMMENT=NOTE+"/recommend";
    /**
     * 转载手记
     */
    public static final String NOTEREPRITE=NOTE+"/reprint";
    /**
     * 删除手记
     */
    public static final String NOTEDELETE=NOTE+"/delete";




}
