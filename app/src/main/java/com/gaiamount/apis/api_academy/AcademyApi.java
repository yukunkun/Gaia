package com.gaiamount.apis.api_academy;

import com.gaiamount.apis.Configs;

/**
 * Created by yukun on 16-8-4.
 */
public class AcademyApi {
    public static final String ACADEMY = Configs.BASE_URL + "/creator/college/MiXingLight";
    //获取单栏
    public static final String ACADEMY_GETCOLLEGE = ACADEMY + "/getCollegeBanner";
    //获取列表
    public static final String ACADEMY_LIST = ACADEMY + "/getCourseList";
    //详情页
    public static final String ACADEMY_DETAIL = ACADEMY + "/details";
    //获取目录
    public static final String ACADEMY_CONTENT = ACADEMY + "/catalog";
    //视频播放
    public static final String ACADEMY_PLAY = ACADEMY + "/watch";
    //获取课表
    public static final String ACADEMY_LESSON = ACADEMY + "/getMySub";
    //开始学习
    public static final String ACADEMY_STARE_STUDY = ACADEMY + "/learn";
    //视屏进度
    public static final String ACADEMY_STARE_RECORD = ACADEMY + "/recordProgress";
    //收藏课程
    public static final String ACADEMY_COLLECT = ACADEMY + "/collect";
    //收藏列表
    public static final String ACADEMY_COLLECT_LIST = ACADEMY + "/getCollectCourse";
    //评价列表
    public static final String ACADEMY_COMMENT = ACADEMY + "/getCourseEva";
    //评价发布
    public static final String ACADEMY_SEND_COMMENT = ACADEMY + "/createEva";
    //添加到小组
    public static final String ACADEMY_ADD_GROUP = ACADEMY + "/addGroup";
    //学院主页数据
    public static final String ACADEMY_MAIN = ACADEMY + "/getCollegeSet";
    //判断教程是否存在
    public static final String ACADEMY_EXITS = ACADEMY + "/isExistsCourse";


}
