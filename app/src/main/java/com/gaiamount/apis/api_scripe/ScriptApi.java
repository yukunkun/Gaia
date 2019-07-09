package com.gaiamount.apis.api_scripe;

import com.gaiamount.apis.Configs;

/**
 * Created by yukun on 16-10-25.
 */
public class ScriptApi {
    public static final String SCRIPT = Configs.BASE_URL + "/script";
    //列表
    public static final String SCRIPTLIST=SCRIPT+"/getList";
    //详情页
    public static final String SCRIPTDETAIL=SCRIPT+"/details/";//get
    //目录页
    public static final String SCRIPTCONTENT=SCRIPT+"/catalog/";//get
    //收藏
    public static final String SCRIPTCOLLECT=SCRIPT+"/collect";
    //评论列表
    public static final String SCRIPTCOLLLIST=SCRIPT+"/getCollectList";
    //权限
    public static final String SCRIPTAGREE=SCRIPT+"/applyAuthor/";//get
    //删除
    public static final String SCRIPTDET=SCRIPT+"/delete/";//get
    //banner
    public static final String SCRIPTBANNER=SCRIPT+"/getBanner";
    //最佳编剧
    public static final String SCRIPTEDITS=SCRIPT+"/getWriters";
    //剧本专辑
    public static final String SCRIPTALBUM=SCRIPT+"/getAlbumList";


}
