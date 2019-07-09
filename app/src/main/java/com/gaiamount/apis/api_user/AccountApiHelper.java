package com.gaiamount.apis.api_user;

import android.content.Context;
import android.util.Log;

import com.gaiamount.apis.api_works.WorksApi;
import com.gaiamount.module_user.personal.PersonalActivity;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.gaia_main.signin_signup.LoginActivity;
import com.gaiamount.util.DataProgressor;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-7-1.
 * 账户相关接口帮助类
 */
public class AccountApiHelper {

    /**
     * 登陆
     *
     * @param account                 帐号
     * @param password                明文密码
     * @param type                    登陆类型
     * @param loginActivity           上下文
     * @param jsonHttpResponseHandler
     */
    public static void login(String account, String password, int type, LoginActivity loginActivity, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("c", account.trim());
            jsonObject.put("p", DataProgressor.encyptPwd(account, password));//AES加密
            jsonObject.put("opr", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AccountApiHelper.class, "login:"+jsonObject.toString());

        NetworkUtils.post(loginActivity, AccountApi.LOGIN_URL, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 用户注册接口
     *
     * @param phone_number            手机账号
     * @param vertify_code
     * @param pwd_encrypt             密码(6-20位)  AES加密密的key和iv:
     *                                1.单个账号注册则就使用单个账号的SHA 256,前32位为key,后32位为iv
     *                                2.两个账号同时注册,key和iv取手机号码的SHA256,前32位为key,后32位为iv
     * @param nickName                用户昵称
     * @param job                     用户职业 验证码
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void register(String phone_number, String vertify_code, String pwd_encrypt, String nickName, String job, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("m", phone_number);
            jsonObject.put("name", nickName);
            jsonObject.put("job", job);
            jsonObject.put("p", pwd_encrypt);
            jsonObject.put("v", vertify_code);
            LogUtil.d(AccountApiHelper.class, "m=" + phone_number + ",name=" + nickName + ",job=" + job + ",p=" + pwd_encrypt + ",v=" + vertify_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AccountApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, AccountApi.REG_URL, jsonObject, jsonHttpResponseHandler);

    }

    /**
     * 发送验证码
     *
     * @param opr                     1 发送到邮箱 2 发送到手机
     * @param e                       邮箱账号 传入e邮箱时
     *                                type=验证码类型
     *                                1=找回密码
     *                                2=绑定邮箱
     *                                type=2 时需要传入
     *                                uid=用户id
     * @param m                       手机账号 需传入区号
     * @param type
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void sendVerifyCode(int opr, String e, String m, int type, int uid, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (opr == 1) {
                jsonObject.put("opr", opr);
                jsonObject.put("e", e);
            } else if (opr == 2) {
                jsonObject.put("opr", opr);
                jsonObject.put("m", m);
            }
            jsonObject.put("type", type);
            if (e != null && type == 2) {
                jsonObject.put("uid", uid);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        LogUtil.d(AccountApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, AccountApi.VEF_URL, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 获取用户信息数据
     *
     * @param uid                     用户的id
     * @param context
     */
    public static void getUserInfoData(long uid, Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonHttpResponseHandler handler = new MJsonHttpResponseHandler(PersonalActivity.class) {
            @Override
            public void parseJson(JSONObject response) {
                super.parseJson(response);
                JSONObject o = response.optJSONObject("o").optJSONObject("user");
                UserInfo userInfo = new Gson().fromJson(o.toString(), UserInfo.class);
                EventBus.getDefault().post(userInfo);
            }
        };

        LogUtil.d(AccountApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, AccountApi.GU_URL, jsonObject, handler);
    }

    /**
     * 兼容
     * @param uid
     * @param context
     * @param handler
     */
    public static void getUserInfoData(long uid, Context context,JsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(AccountApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, AccountApi.GU_URL, jsonObject, handler);
    }

    public static void resetPwd(String account, String encyptPwd, String vertifyCode, int f, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("c", account);
            jsonObject.put("o", vertifyCode);
            jsonObject.put("p", encyptPwd);
            jsonObject.put("f",f);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(AccountApiHelper.class,jsonObject.toString());

        NetworkUtils.post(context, AccountApi.FORGET_URL,jsonObject,jsonHttpResponseHandler);
    }

    /**
     * 设置收款账户
     *
     * @param values                  1：默认收款方式，3收款方银行，4：银行卡号，5：收款人姓名，其他为“”
     * @param context                 上下文
     * @param jsonHttpResponseHandler 回调对象
     */
    public static void updateBankAccount(String[] values, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            /**
             * bn(String)=银行名称
             bc(String)=银行卡号
             bb(String)=开户行支行
             un(String)=开户人姓名
             */
            jsonObject.put("bn", values[3]);
            jsonObject.put("bc", values[4]);
            jsonObject.put("bb", values[6]);
            jsonObject.put("un", values[5]);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AccountApiHelper.class, jsonObject.toString());


        NetworkUtils.post(context, AccountApi.BANK_ACCOUNT_URL, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 分页参数
     * pi(int)=当前页
     * ps(int)=每页显示的数据
     * t(int)默认为0=类型
     * (
     * 0 所有类型
     * 1 素材支出
     * 2 作品支出
     * 3 教程支出
     * 4 会员升级
     * )
     * d(int)默认为0=时间
     * (
     * 0 全部时间
     * 1 最近一个月
     * 2 最近3个月
     * 3 最近一年
     * 4 自定义
     */
    public static void getSpending(int pi, int ps, int type, int d, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
            jsonObject.put("t", type);
            jsonObject.put("d", d);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkUtils.post(context, ChargeApi.URL_GET_SPENDING, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 分页参数
     * pi(int)=当前页
     * ps(int)=每页显示的数据
     * 条件查询参数
     * t(int)默认为0=类型
     * (
     * 0 所有类型
     * 1 素材支出
     * 2 作品支出
     * 3 教程支出
     * 4 会员升级
     * )
     * d(int)默认为0=时间
     * (
     * 0 全部时间
     * 1 最近一个月
     * 2 最近3个月
     * 3 最近一年
     * 4 自定义
     * 如果d=5后面在加两个参数(
     * before 开始时间
     * 和
     * after 结束时间
     * )
     * )
     *
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getProfit(int pi, int ps, int type, int d, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
            jsonObject.put("t", type);
            jsonObject.put("d", d);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkUtils.post(context, ChargeApi.URL_GETPROFIT, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * @param date
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getDayProfit(String date, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkUtils.post(context, ChargeApi.URL_GETDAYPROFIT, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 获取用户收藏列表
     *
     * @param s                       作品查询的条件
     *                                0  全部
     *                                1  按推荐度
     *                                2  4k
     *                                3 可下载
     *                                4  免费下载
     *                                5  付费下载）
     * @param o                       作品排序的条件
     *                                0  默认
     *                                1  最新发布
     *                                2   最多播放
     *                                3  最多收藏
     *                                4  最多评论
     *                                5  最多下载)
     * @param pi                      分页参数 当前页码
     * @param ps                      分页参数 每页有多少数据
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getCollects(long uid,int s, int o, int pi, int ps, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",uid);
            jsonObject.put("s", s);
            jsonObject.put("o", o);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AccountApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.GET_COLLECTS_URL, jsonObject, jsonHttpResponseHandler);
    }

    public static void isCollected(long wid, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wid", wid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AccountApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.IS_COLLECTED_URL, jsonObject, jsonHttpResponseHandler);

    }

    //yukun---
    public static void sendFeedBack(String name, String number, String email, String content, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject1=new JSONObject();
        try {
            jsonObject1.put("name",name);
            jsonObject1.put("phone",number);
            jsonObject1.put("email",email);
            jsonObject1.put("content",content);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AccountApiHelper.class, jsonObject1.toString());

        NetworkUtils.post(context,AccountApi.FEED_BACK_URL,jsonObject1,handler);

    }
    //----

    /**
     * 注销账号
     * @param tokenValue
     * @param context
     * @param handler
     */
    public static void logout(String tokenValue, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            Log.d("tokenValue的值", tokenValue);
            jsonObject.put("t", tokenValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(AccountApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context,AccountApi.LOGOUT_URL,jsonObject,handler);
    }

    public static void getVersion(JsonHttpResponseHandler jsonHttpResponseHandler){
        Log.d("HomeFrag:","getVersion");
        NetworkUtils.get(AccountApi.VERSION,jsonHttpResponseHandler);
    }
}
