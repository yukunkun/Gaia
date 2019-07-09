package com.gaiamount.apis.api_im;

import com.gaiamount.apis.Configs;

/**
 * Created by yukun on 16-12-29.
 */
public class Aynamics {

    public static final String IM_URL = Configs.BASE_URL + "/im";
    /**
     * 与我相关
     */
    public static final String DYNAMICRELATE = IM_URL + "/dynamicRelate";
    /**
     * 我关注的
     */
    public static final String MYATTENTION = IM_URL + "/dynamicFocus";
    /**
     * 小组动态
     */
    public static final String GROUPDYNAMIC = IM_URL + "/getMyGroup";
    /**
     * 小组动态
     */
    public static final String GROUPJOIN = IM_URL + "/getJoinGroup";


}
