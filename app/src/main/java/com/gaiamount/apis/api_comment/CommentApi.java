package com.gaiamount.apis.api_comment;

import com.gaiamount.apis.Configs;

/**
 * Created by haiyang-lu on 16-4-27.
 * 评论相关的Api接口
 */
public class CommentApi {

    public static final String URL_COMMENT = Configs.BASE_URL + "/comment";

    /**
     * 获取评论列表
     */
    public static final String GET_COMMENT_LIST = URL_COMMENT +"/list";

    /**
     * 发送评论
     */
    public static final String SEND_COMMENT = URL_COMMENT +"/do";

    /**
     * 删除评论
     */
    public static final String DELETE_COMMENT = URL_COMMENT+"/deleteComment";
}
