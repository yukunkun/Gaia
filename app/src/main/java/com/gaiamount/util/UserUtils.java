package com.gaiamount.util;

import android.content.Context;
import android.util.Log;

import com.gaiamount.R;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.home.SplashActivity;
import com.gaiamount.module_user.user_edit.UserInfoActivity;
import com.gaiamount.module_user.user_edit.UserInfoEditActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.apis.api_user.AccountApi;
import com.gaiamount.module_im.official.Im;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by haiyang-lu on 16-3-7.
 * 加载用户信息，保存用户信息
 */
public class UserUtils {
    private static UserUtils instance;

    private UserUtils() {

    }

    public static UserUtils newInstance() {
        return new UserUtils();
    }

    /**
     * 将jsonObjec形式的用户信息转成UserInfo的bean
     */
    public static void switchJsonToUserInfoAndSet(JSONObject jsonObjectO) {
        UserInfo userInfo;
        JSONObject jsonObject = (JSONObject) jsonObjectO.opt("user");

        long id = jsonObject.optLong("id");
        int gender = jsonObject.optInt("gender", -1);
        int ulv = jsonObject.optInt("ulv", 0);
        int type = jsonObject.optInt("type", 0);
        int status = jsonObject.optInt("status", 0);
        int isVip = jsonObject.optInt("isVip", 0);
        int vipLevel = jsonObject.optInt("vipLevel", 0);
        int vlv = jsonObject.optInt("vlv", 0);
        int vtype = jsonObject.optInt("vtype", 0);
        int language = jsonObject.optInt("language", 0);
        int source = jsonObject.optInt("source", 0);
        int createCount = jsonObject.optInt("createCount",0);
        int college_collect = jsonObject.optInt("college_collect",0);

        String avatar = jsonObject.optString("avatar", "");
        String nickName = jsonObject.optString("nickName", "");
        String realName = jsonObject.optString("realName", "");
        String job = jsonObject.optString("job", "");
        String domain = jsonObject.optString("domain", "");
        String signature = jsonObject.optString("signature", "");
        String address = jsonObject.optString("address", "");
        String resume = jsonObject.optString("resume", "");
        String mobile = jsonObject.optString("mobile", "");
        String email = jsonObject.optString("email", "");

        String bankCard = jsonObject.optString("bankCard", "");
        String bankName = jsonObject.optString("bankName", "");
        String bankUserName = jsonObject.optString("bankUserName", "");
        String bankBranch = jsonObject.optString("bankBranch", "");
        String background = jsonObject.optString("background", "");
        String description = jsonObject.optString("description", "");
        int noteCount = jsonObject.optInt("noteCount", 0);
        int albumCount = jsonObject.optInt("albumCount", 0);
        int materialCount = jsonObject.optInt("materialCount", 0);
        int material_collect = jsonObject.optInt("material_collect", 0);
        int collegeCount=jsonObject.optInt("collegeCount",0);
        JSONObject imJsonObject = (JSONObject) jsonObjectO.opt("im");
        Im im=null;
        if(imJsonObject!=null){
             im= GsonUtil.getInstannce().getGson().fromJson(imJsonObject.toString(), Im.class);
        }else {


        }

        Log.d("UserUtils json的值:", jsonObject.toString());
        userInfo = new UserInfo(id, gender, ulv, type, status, isVip,vipLevel, vlv, vtype, language, source,
                avatar, nickName, realName, job, domain, signature, address, resume, mobile, email, bankCard, bankName, bankUserName
                , bankBranch,createCount,im,college_collect,background,noteCount,albumCount,materialCount,material_collect,collegeCount,description);
        Log.i("UserUtils.用户信息：", "userInfo:" + userInfo.toString());

        //更新到application中
        GaiaApp.getAppInstance().setUserInfo(userInfo);
        //更新用户登陆状态
        GaiaApp.loginStatus = true;

    }


    /**
     * 将UserInfo转成JsonObject
     *
     * @param userInfo 用户信息对象
     * @return jsonObject对象
     */
    public static JSONObject switchUserInfoToJson(UserInfo userInfo) {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("uid", userInfo.id);
            jobj.put("type", userInfo.type);
            jobj.put("status", userInfo.status);
            jobj.put("isVip", userInfo.isVip);
            jobj.put("vlv", userInfo.vlv);
            jobj.put("avatar", userInfo.avatar);
            jobj.put("nickName", userInfo.nickName);
            jobj.put("realName", userInfo.realName);
            jobj.put("ulv", userInfo.ulv);
            jobj.put("vType", userInfo.vType);
            jobj.put("job", userInfo.job);
            jobj.put("domain", userInfo.domain);
            jobj.put("language", userInfo.language);
            jobj.put("signature", userInfo.signature);
            jobj.put("address", userInfo.address);
            jobj.put("resume", userInfo.resume);
            jobj.put("mobile", userInfo.mobile);
            jobj.put("gender", userInfo.gender);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jobj;

    }

    public static boolean isLogin() {
        return GaiaApp.loginStatus;
    }

    public static void autoLogin(Context context,JsonHttpResponseHandler handler) {
        AccountApiHelper.getUserInfoData(GaiaApp.getAppInstance().uId,context,handler);
    }


    /**
     * 从网上获取最新用户信息,在应用首次进入后执行该方法
     *
     * @see SplashActivity#attemptAutoLogin()
     */
    public static void getUserInfoFromNet(final Context context) {
//        long id = GaiaApp.getAppInstance().uId;
        long id = GaiaApp.getAppInstance().getUserInfo().id;

        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(UserUtils.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONObject o = response.optJSONObject("o");
                switchJsonToUserInfoAndSet(o);

                //通知activity更新
                BroadcastUtils.sendUserInfoChangedBroadcast();

                //登陆Im
                ImUtil.loginEase();
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                int i = response.optInt("i");
                if (i == 50401) {
                    GaiaApp.showToast("用户信息过期，请重新登陆");
                    ActivityUtil.startLoginActivity(context);
                }
            }
        };
        AccountApiHelper.getUserInfoData(id, context, jsonHttpResponseHandler);
    }

    /**
     * 请求服务器修改用户数据
     *
     * @param userInfo 待上传的数据
     * @param context  所在页面的activity实例
     */
    public static void updateUserInfo(UserInfo userInfo, final Context context) {
        //最先显示加载动画
        UIUtils.displayCustomProgressDialog(context, context.getString(R.string.requesting_changed), true);
        //转化为json对象
        JSONObject jobj = switchUserInfoToJson(userInfo);

        if (jobj == null) {
            Log.e("NetWorkUtils", "传入的json为null");
            return;
        }
        Log.d("更改用户名：json", jobj.toString());

        //联网请求更改
        NetworkUtils.post(context, AccountApi.UPDATE_URL, jobj, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //隐藏加载动画
                UIUtils.displayCustomProgressDialog(context, false);
                Log.e("NetWorkUtils", "修改用户个性签名的网络请求成功" + statusCode + response.toString());
                if (response.optInt("b") == 1) {
                    GaiaApp.showToast("修改数据成功");
                    //获取用户最新信息
                    getUserInfoFromNet(context);
                    //更新用户编辑界面
                    if (context instanceof UserInfoEditActivity) {
                        UserInfoEditActivity activity = (UserInfoEditActivity) context;
                        activity.updateUserInfo();
                    } else if (context instanceof UserInfoActivity) {
                        UserInfoActivity activity = (UserInfoActivity) context;
                        activity.updateUserInfo();
                    }

                    //发送广播，更新其他页面
                    BroadcastUtils.sendUserInfoChangedBroadcast();

                } else {
                    GaiaApp.showToast("修改数据失败");
                    JSONArray ec = response.optJSONArray("ec");
                    int i = response.optInt("i");
                    ErrorUtils.processError(ec, i);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("NetWorkUtils", "修改用户个性签名的网络请求失败" + statusCode + throwable.toString());
                //隐藏加载动画
                UIUtils.displayCustomProgressDialog(context, false);
            }
        });
    }
}
