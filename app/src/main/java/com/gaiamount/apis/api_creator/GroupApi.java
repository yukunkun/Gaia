package com.gaiamount.apis.api_creator;

import com.gaiamount.apis.Configs;

/**
 * Created by haiyang-lu on 16-6-13.
 * 创作者-小组相关的接口
 */
public class GroupApi {

    public static final String GROUP_URL = Configs.BASE_URL + "/group";

    public static final String SEARCH_URL = GROUP_URL + "/search";

    public static final String GROUP_DETAIL_URL = GROUP_URL + "/details";

    public static final String GROUP_COMMENT_URL = GROUP_URL + "/getGroupComment";

    public static final String GROUP_DETAIL_COMMENT_URL = GROUP_URL + "/deleteComment";

    public static final String GROUP_DO_COMMENT = GROUP_URL + "/doComment";

    public static final String GROUP_CREATE_URL = GROUP_URL + "/create";

    public static final String GROUP_JOIN_URL = GROUP_URL + "/join";

    public static final String GROUP_ISGROUP_MEMBER = GROUP_URL + "/isGroupMember";

    public static final String GROUP_EXITGROUP = GROUP_URL + "/exitGroup";

    public static final String GROUP_UPDATE_URL = GROUP_URL + "/update";

    public static final String SET_GROUP_ADMIN = GROUP_URL + "/setGroupAdmin";

    public static final String GET_GROUP_USER = GROUP_URL + "/groupUser";

    public static final String GET_ADMIN = GROUP_URL + "/getAdmin";

    public static final String GET_RECOMMEND = GROUP_URL + "/getRecommend";

    public static final String VIDEOS = GROUP_URL + "/videos";

    public static final String SET_RECOMMEND = GROUP_URL + "/setRecommend";

    public static final String BATCH_CLEAN_MEMBER = GROUP_URL + "/batchCleanMember";

    public static final String MY = GROUP_URL + "/my";

    public static final String ADD_VIDEO = GROUP_URL + "/addVideo";

    public static final String BATCH_REMOVE_VIDEO = GROUP_URL + "/batchRemoveVideo";

    public static final String GROUP_COLLEGE = GROUP_URL + "/getGroupCourse";
    //小组审核
    public static final String GROUP_EXAMINE = GROUP_URL + "/getExamineList";
    //小组审核
    public static final String GROUP_OK_OR_NO = GROUP_URL + "/examine";

}
