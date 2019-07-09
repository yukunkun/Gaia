package com.gaiamount.apis.api_works;

import android.content.Context;
import android.util.Log;

import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.module_user.person_creater.CreateWorkAdapter;
import com.gaiamount.module_user.person_creater.fragments.WorksFrag;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-7-1.
 * 作品池先关接口帮助类
 */
public class WorksApiHelper {
    public static void getMyWorks(long uid, int pi,int ptype, int stype, Context context, MJsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("pi",pi);
            jsonObject.put("ptype",ptype);
            jsonObject.put("stype",stype);
            jsonObject.put("status",7); //
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksFrag.class,jsonObject.toString());
        NetworkUtils.post(context, WorksApi.MYWORKS_URL,jsonObject,jsonHttpResponseHandler);
    }


    /**
     * 获取作品池列表
     * @param position
     * @param ptype  0 全部
                    1 盖亚推荐
                    2 4k视频
                    3 可下载
                    4 免费下载
                    5 收费下载
     * @param stype
     * 0默认
                    1 最新发布
                    2 最多播放
                    3 最多搜藏
                    4 最多评论
                    5 最多下载
     * @param pi
     * @param ps
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getWorkPoolList(int position, int ptype, int stype, int pi, int ps, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wtype",position);//作品类型筛选
            jsonObject.put("ptype",ptype);//作品属性筛选
            jsonObject.put("stype",stype);//作品统计筛选 默认最新发布
            jsonObject.put("pi",pi);
            jsonObject.put("ps", ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksApiHelper.class,jsonObject.toString());

        NetworkUtils.post(context, WorksApi.SEARCH_URL,jsonObject,jsonHttpResponseHandler);
    }

    /**
     * 删除作品
     * @param workId (int)   0 作品 1 素材 2 剧本 3 学院
     * @param contentType id(long)  作品id
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void deleteWork(int workId, int contentType, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",workId);
            jsonObject.put("t",contentType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksApiHelper.class,jsonObject.toString());

        NetworkUtils.post(context, WorksApi.DELETE_VIDEO_URL,jsonObject,jsonHttpResponseHandler);
    }

    /**
     * 更新作品信息
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void updateWorks(UpdateWorksBean updateWorks, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            UpdateWorksBean.ABean a = updateWorks.getA();
            jsonObject.put("id", a.getId());
            jsonObject.put("name",a.getName());
            jsonObject.put("requirePassword",a.getRequirePassword());
            jsonObject.put("allowDownload",a.getAllowDownload());
            jsonObject.put("allowCharge",a.getAllowCharge());
            jsonObject.put("allowEmbed",0);
            jsonObject.put("colorist",a.getColorist());
            jsonObject.put("address",a.getAddress());
            jsonObject.put("keywords",a.getKeywords());
            jsonObject.put("director",a.getDirector());
            jsonObject.put("cutter",a.getCutter());
            jsonObject.put("description",a.getDescription());
            jsonObject.put("lens",a.getLens());
            jsonObject.put("type",a.getType());
            jsonObject.put("machine",a.getMachine());
            jsonObject.put("photographer",a.getPhotographer());
            jsonObject.put("cover",a.getCover());
            jsonObject.put("password",a.getPassword());
            jsonObject.put("priceOriginal",a.getPriceOriginal());
            jsonObject.put("price720",a.getPrice720());
            jsonObject.put("price1080",a.getPrice1080());
            jsonObject.put("price2K",a.getPrice2K());
            jsonObject.put("price4K",a.getPrice4K());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(WorksApiHelper.class,jsonObject.toString());

        NetworkUtils.post(context, WorksApi.UPDATE_WORKS,jsonObject,jsonHttpResponseHandler);
    }

    /**
     * 获取主页的推荐作品池列表
     *
     * @param num   数量
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getRecommend(int num, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("num", num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.RECOMMEND_URL, jsonObject, jsonHttpResponseHandler);
    }

    public static void setGrade(long uid, long wid, double grade, Context context, JsonHttpResponseHandler handler) {
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("uid", uid);
            jobj.put("wid",wid);
            jobj.put("grade",grade);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksApiHelper.class,"setGrade:"+jobj);
        NetworkUtils.post(context, WorksApi.GRADE_URL,jobj,handler);
    }

    public static void getDetailWorkInfo(long wid, Context context, JsonHttpResponseHandler handler) {
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("wid", wid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksApiHelper.class,"getDetailWorkInfo:"+jobj);

        NetworkUtils.post(context, WorksApi.DETAILS_URL,jobj,handler);
    }

    public static void getVideoDetail(long uid,int id, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("wid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksApiHelper.class,jsonObject.toString());

        NetworkUtils.post(context, WorksApi.DETAILS_URL,jsonObject,jsonHttpResponseHandler);
    }

    /**
     * 获取视频相关推荐列表
     *
     * @param t   作品的类型
     * @param key 作品的关键字
     * @param pi  分页参数 页码
     * @param ps  分页参数 每页显示数据的条数
     */
    public static void getRecWorks(String t, String key, int pi, int ps, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("t", t);
            if (key != null) {
                jsonObject.put("key", key);
            }
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.REC_URL, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 收藏/取消收藏作品
     *
     * @param wid 作品id
     * @param t   作品类型 0:作品 1素材
     * @param s   收藏状态 0:取消 1 收藏
     */
    public static void collect(long wid, long t, int s, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wid", wid);
            jsonObject.put("t", t);
            jsonObject.put("s", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.COLLECT_URL, jsonObject, jsonHttpResponseHandler);
    }

    public static void sendTipOff(long wid, StringBuilder content, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wid", wid);
            jsonObject.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(WorksApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.TIP_OFF_URL, jsonObject, jsonHttpResponseHandler);
    }

    public static void isCollected(int workId, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wid", workId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(WorksApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.IS_COLLECTED_URL, jsonObject, handler);
    }

    /**
     * 删除作品
     * @param uid
     * @param wid
     * @param context
     * @param handler
     */
    public static void deleteWorks(long uid, long wid,Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            JSONArray wids=new JSONArray();
            wids.put(wid);
            jsonObject.put("wids",wids);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(CreateWorkAdapter.class, jsonObject.toString());

        NetworkUtils.post(context, WorksApi.DELETE_WORKS_URL, jsonObject, handler);
    }

    /**
     *   上传页的数据
     * @param context
     * @param jsonObject
     * @param handler
     */
    public static void uploadAddInfo(Context context,JSONObject jsonObject, MJsonHttpResponseHandler handler) {

        LogUtil.d(CreateWorkAdapter.class, "addInfo"+jsonObject.toString());

        NetworkUtils.post(context, WorksApi.UPLOAD_INFO, jsonObject, handler);
    }

}
