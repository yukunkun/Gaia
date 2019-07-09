package com.gaiamount.apis.api_material;

import com.gaiamount.apis.Configs;

/**
 * Created by yukun on 16-9-28.
 */
public class Material_Api {

    public static final String MATERIAL = Configs.BASE_URL+"/material";

    public static final String MATERIAL_BANNER = MATERIAL+"/getMaterialPhoto";

    public static final String MATERIAL_LIST = MATERIAL+"/getList";
    public static final String MATERIAL_GET_BANNER = MATERIAL+"/getBanner";
    //推荐
    public static final String MATERIAL_RECOMEND = MATERIAL+"/recommend";
    //详情
    public static final String MATERIAL_DETAIL = MATERIAL+"/details";
    //收藏list
    public static final String MATERIAL_COLL = MATERIAL+"/getCollectList";
    //收藏
    public static final String MATERIAL_ADD_COLL = MATERIAL+"/collect";
    //删除素材
    public static final String MATERIAL_DET = MATERIAL+"/delete";

}
