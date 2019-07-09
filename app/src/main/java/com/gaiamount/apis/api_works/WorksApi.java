package com.gaiamount.apis.api_works;

import com.gaiamount.apis.Configs;

/**
 * Created by haiyang-lu on 16-4-18.
 * 作品池相关接口
 */
public class WorksApi {

    public static final String WORK_URL = Configs.BASE_URL + "/works";
    /**
     * 收藏作品
     */
    public static final String COLLECT_URL = WORK_URL +"/collect";
    /**
     * 获取推荐作品
     */
    public static final String REC_URL = WORK_URL + "/recommendedWorks";
    /**
     * 为作品打分
     */
    public static final String GRADE_URL = WORK_URL +"/grade";

    /**
     * 作品相关接口
     */
    public static final String RECOMMEND_URL = WORK_URL + "/recommend";

    public static final String SEARCH_URL = WORK_URL + "/search";

    public static final String DETAILS_URL = WORK_URL + "/details";


    /**
     * 上传的相关接口
     */
    public static final String UPLOAD_URL = WORK_URL + "/upload";

    public static final String UPLOAD_LIST_URL = WORK_URL + "/uploadList";


    public static final String UPLOAD_SUSPEND_URL = WORK_URL + "/suspend";

    public static final String MYWORKS_URL = WORK_URL + "/myWorks";

    /**
     * 收藏列表展示
     */
    public static final String GET_COLLECTS_URL = WORK_URL + "/getCollects";

    /**
     * 是否被收藏
     */
    public static final String IS_COLLECTED_URL = WORK_URL + "/isCollected";
    /**
     * 举报作品
     */
    public static final String TIP_OFF_URL = WORK_URL + "/report";

    /**
     * 下载作品接口
     */
    public static final String DOWNLOAD_URL = WORK_URL + "/download";

    /**
     * 删除作品
     */
    public static final String DELETE_VIDEO_URL = WORK_URL + "/deleteVideo";

    /**
     * 更新作品信息
     */
    public static final String UPDATE_WORKS = WORK_URL + "/updateWorks";

    /**
     * 获取作品上传的信息
     */
    public static final String GET_UPDATE_WORKS_URL = WORK_URL + "/getUpdateWorks";

    /**
     * 删除自己的作品
     */
    public static final String DELETE_WORKS_URL = WORK_URL + "/batchDeleteWorks";

    /**
     * 删除自己的作品
     */
    public static final String UPLOAD_INFO = WORK_URL + "/addInfo";
    /**
     * 删除自己的作品
     */
    public static final String ADDTOALBUM = WORK_URL + "/batchUpdate";




}
