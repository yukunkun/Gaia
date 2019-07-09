package com.gaiamount.apis.api_user;

import com.gaiamount.apis.Configs;

/**
 * Created by haiyang-lu on 16-5-20.
 * 支付接口（待定）
 */
public class ChargeApi {
    public static final String URL_CHARGE = Configs.BASE_URL + "/charge";

    public static final String URL_GET_SPENDING = URL_CHARGE + "/getSpending";

    public static final String URL_GETDAYPROFIT = URL_CHARGE + "/getDayProfit";

    public static final String URL_GETPROFIT = URL_CHARGE + "/getProfit";

}
