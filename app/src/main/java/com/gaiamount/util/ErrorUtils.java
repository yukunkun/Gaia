package com.gaiamount.util;

import android.util.Log;

import com.gaiamount.apis.api_works.WorksApi;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.apis.api_comment.CommentApi;

import org.json.JSONArray;

/**
 * Created by haiyang-lu on 16-3-7.
 * 错误码处理
 */
public class ErrorUtils {
    public static final String TAG = ErrorUtils.class.getClass().getSimpleName();
    private static boolean isDebug = true;

    public static void processError(JSONArray ec, int errorCode_i) {
        if(!isDebug) {
            return;
        }
        if (ec != null) {
            for (int i = 0; i < ec.length(); i++) {
                int anInt = ec.optInt(i);
                switch (anInt) {
                    case 401:
                        GaiaApp.showToast("账号不符合规范");
                        break;
                    case 402:
                        GaiaApp.showToast("新密码不能为空");
                        break;
                    case 403://密码不规范
                        GaiaApp.showToast("密码不规范");
                        break;
                    case 404:
                        GaiaApp.showToast("验证码不能为空");
                        break;
                    case 405:
                        GaiaApp.showToast("验证码长度为６");
                        break;
                    case 406://账号不能为空
                        GaiaApp.showToast("账号不能为空");
                        break;
                    case 407://账号不能为空字符串
                        GaiaApp.showToast("账号不能为空字符串");
                        break;
                    case 408://账号不能为空字符串
                        Log.e(TAG, "t为null");
                        break;
                    case 409:
                        Log.e(TAG, "t为空字符串");
                        break;
                    case 410:
                        Log.e(TAG,"默认语言非法");
                    default:
                        break;
                }
            }


        } else {
            Log.e(TAG, "ec为空");
        }


        switch ((errorCode_i)) {
            case 30001://未匹配上账号
                GaiaApp.showToast("未匹配上账号");
                break;
            case 30601:
                GaiaApp.showToast("验证码错误");
                break;
            case 30801:
                GaiaApp.showToast("验证码过期");
                break;
            case 31001:
                Log.e(TAG, "token无效");
                break;
            case 31601:
                GaiaApp.showToast("账号被禁用");
                break;
            case 31101://账号异常
                GaiaApp.showToast("账号异常");
                break;
            case 31301://密码错误
                GaiaApp.showToast("密码错误");
                break;
            case 32201://账号没有权限
                GaiaApp.showToast("账号没有权限");
                break;
            case 32401://账号不存在
                GaiaApp.showToast("账号不存在");
                break;
            case 32501://邮箱账号不存在
                GaiaApp.showToast("邮箱账号不存在");
                break;
            case 32601://手机账号不存在
                GaiaApp.showToast("手机账号不存在");
                break;
            case 50301://没有登陆的token
                Log.e(TAG, "没有登陆的token");
                break;
            case 50401://token失效
                Log.e(TAG,"token失效");
            case 50501://账号被禁用
                Log.e(TAG, "账号被禁用");
                break;
            case 50801://会话id不一致
                Log.e(TAG, "会话id不一致");
                break;
            default://其他错误
                GaiaApp.showToast("其他错误");
                break;
        }
    }

    public static int  processError(String tag, JSONArray ec, int i) {
        if(isDebug) {
            switch (tag) {
                case WorksApi.GRADE_URL:
                    processGradeError(ec, i);
                    break;
                case CommentApi.SEND_COMMENT:
                    processCommentListError(ec,i);
            }
        }

        return i;

    }

    private static void processCommentListError(JSONArray ec, int i) {
        if(ec!=null) {
            for (int j=0;j<ec.length();j++) {
                int i1 = ec.optInt(j);
                switch (i1) {
                    case 402:
                        LogUtil.e(ErrorUtils.class,"uid异常");
                        break;
                    case 403:
                        LogUtil.e(ErrorUtils.class,"wid异常");
                        break;
                    default:
                        LogUtil.e(ErrorUtils.class,"未被记录的错误码"+i1);
                        break;
                }
            }
        }

    }

    private static int processGradeError(JSONArray ec, int i) {
        if(ec!=null) {
            for (int j=0;j<ec.length();j++) {
                int i1 = ec.optInt(j);
                switch (i1) {
                    case 408:
                        LogUtil.e(ErrorUtils.class,"uid异常");
                        break;
                    case 414:
                        LogUtil.e(ErrorUtils.class,"wid异常");
                        break;
                    case 413:
                        LogUtil.e(ErrorUtils.class,"grade异常");
                        break;
                }
            }
        }
        switch (i) {
            case 30703:
                LogUtil.e(ErrorUtils.class,"用户不存在");
                break;
            case 30803:
                LogUtil.e(ErrorUtils.class,"y作品不存在");
                break;
            case 50301:
                LogUtil.e(ErrorUtils.class,"没有token");

                break;
            default:
                break;
        }

        return i;
    }


}
