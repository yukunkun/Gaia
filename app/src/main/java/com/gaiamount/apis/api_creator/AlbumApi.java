package com.gaiamount.apis.api_creator;

import com.gaiamount.apis.Configs;

/**
 * Created by haiyang-lu on 16-6-30.
 * 专辑相关的接口
 */
public class AlbumApi {
    public static final String ALBUM = Configs.BASE_URL + "/creator/album";
    /**
     * 获取专辑中的作品
     */
    public static final String SEARCH_ALBUM_WORKS = ALBUM + "/seachAlbumWorks";
    /**
     * 获取专辑列表
     */
    public static final String GET_ALBUM_LIST = GroupApi.GROUP_URL + "/getAlbumList";
    /**
     * 创建专辑
     */
    public static final String CREATE_ALBUM = ALBUM + "/create";

    /**
     * 更新专辑
     */
    public static final String CREATE_UPDATE = ALBUM + "/update";

    /**
     * 获取专辑详情
     */
    public static final String DETAILS = ALBUM + "/details";
    /**
     * 删除专辑
     */
    public static final String DELETE = ALBUM + "/delete";
    /**
     * 添加创作到专辑
     */
    public static final String ADD = ALBUM + "/add";
    /**
     * 从专辑中移除创作
     */
    public static final String REMOVE_VIDEO_FROM_ALBUM = ALBUM + "/removeAlbum";
    //专辑学院教程
    //获得小组教程
    public static final String ACADEMY_GROUP_LIST = ALBUM + "/getAblumCourse";

    //专辑列表
    public static final String ACADEMY_ALBUM_SEARCH = ALBUM + "/seach";

    //专辑material列表
    public static final String MATERIAL_ALBUM_LIST = ALBUM + "/getAlbumMaterialList";

}
