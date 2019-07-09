package com.gaiamount.apis.file_api;

import android.content.Context;

import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_works.WorksApi;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload_manage.UploadedWorks;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by haiyang-lu on 16-6-16.
 * 作品上传/下载相关接口
 */
public class FileApiHelper {


    /**
     * 图片上传
     *
     * @param uid                     用户id
     * @param type                    type(int) 图片类型{0 头像 1 封面  2 水印 3小组}
     * @param format                  format(String) 图片后缀{像jpg、png、jpeg等等}
     * @param wid                     作品id{如果上传作品的封面这个参数不能缺省}
     * @param gid                     小组id{如果上传小组的背景这个参数不能缺省}
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void uploadImg(long uid, long wid, long gid, int type, String format, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("type", type);
            jsonObject.put("format", format);
            if (type == 1) {
                jsonObject.put("wid", wid);
            } else if (type == 3) {
                jsonObject.put("gid", gid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(FileApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, FileApi.UPLOAD_IMG_URL, jsonObject, jsonHttpResponseHandler);

    }

    public static void uploadUserAvatar(long uid, String format, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        uploadImg(uid, 0, 0, 0, format, context, jsonHttpResponseHandler);
    }

    public static void uploadWorkCover(long uid, String format, long wid, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        uploadImg(uid, wid, 0, 1, format, context, jsonHttpResponseHandler);

    }

    public static void uploadGroupCover(long uid, String format, long gid, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        uploadImg(uid, 0, gid, 3, format, context, jsonHttpResponseHandler);
    }

    public static void uploadAlbumCover(long uid, String format, Context context, final String imageAbsolutePath, final UpCompletionHandler upCompletionHandler, final UpProgressHandler upProgressHandler) {
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(FileApiHelper.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                String inputKey = response.optJSONObject("o").optString("inputKey");
                String token = response.optJSONObject("o").optString("token");

                UploadManager uploadManager = new UploadManager();

                uploadManager.put(imageAbsolutePath, inputKey, token, upCompletionHandler, new UploadOptions(null, null, false, upProgressHandler, null));
            }
        };
        uploadImg(uid, 0, 0, 4, format, context, handler);
    }

    /**
     * 下载作品接口
     *
     * @param uid                     用户id
     * @param wid                     作品id
     * @param downloadType            下载类型 0=源文件  1=4k  2=2k    3=HD    4=720
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void downloadVideo(long uid, long wid, int downloadType, int e_, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("wid", wid);
            jsonObject.put("t", downloadType);
            jsonObject.put("e", e_);
            jsonObject.put("type", e_);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(FileApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.DOWNLOAD_URL, jsonObject, jsonHttpResponseHandler);
    }

    public static void uploadVideo(JSONObject jobj, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        LogUtil.d(FileApiHelper.class, jobj.toString());

        NetworkUtils.post(context, WorksApi.UPLOAD_URL, jobj, jsonHttpResponseHandler);
    }

    public static void getUpdateVideoInfo(int workId, int contentType, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", workId);
            jsonObject.put("t", contentType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(FileApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.GET_UPDATE_WORKS_URL, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 用户作品上传列表
     *
     * @param uid     用户id
     * @param type    类型 0 未上传完成 4 上传完成
     * @param client  上传客户端 0=web 1=app
     * @param context
     * @see UploadedWorks
     */
    public static void getUploadList(long uid, int type, int client, int pi, int ps, Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("type", type);
            jsonObject.put("client", client);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(FileApiHelper.class, jsonObject.toString());

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(FileApiHelper.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                List<UploadedWorks> list = GsonUtil.getInstannce().getGson().fromJson(response.optJSONArray("a").toString(), new TypeToken<List<UploadedWorks>>() {
                }.getType());
                EventBus.getDefault().post(list);
            }
        };

        NetworkUtils.post(context, WorksApi.UPLOAD_LIST_URL, jsonObject, handler);
    }

    public static void getUploadList(Context context) {
        getUploadList(GaiaApp.getUserInfo().id, FileApi.UPLOAD_LIST_TYPE_FINISH, Configs.CLIENT_APP, 1, 20, context);
    }

    /**
     * 作品上传暂停/取消/报错
     * (suspend)
     * fid=作品id
     * schedule=上传进度
     * message=上传信息(用于上传报错,保存报错信息,无错传空字符串)
     * opr=操作
     * opr=1暂停
     * opr=2 取消
     * opr=3 上传报错
     */
    public static void cancelUpload(long fid, int schedule, String message, int opr , final Context context,MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fid", fid);
            jsonObject.put("schedule", schedule);
            jsonObject.put("message", message);
            jsonObject.put("opr", opr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(FileApiHelper.class, jsonObject.toString());



        NetworkUtils.post(context, FileApi.SUSPEND, jsonObject, handler);
    }



    public static void getUpdate(long size, String format , JsonHttpResponseHandler jsonHttpResponseHandler) {

        LogUtil.d(FileApiHelper.class, FileApi.UPLOAD_URI+"size="+size+"&"+"format="+format+"&type=0");

        NetworkUtils.get(FileApi.UPLOAD_URI+"size="+size+"&"+"format="+format+"&type=0",  jsonHttpResponseHandler);
    }
}
