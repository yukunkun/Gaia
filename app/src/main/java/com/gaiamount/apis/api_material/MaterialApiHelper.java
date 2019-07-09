package com.gaiamount.apis.api_material;

import android.content.Context;
import android.util.Log;

import com.gaiamount.apis.api_academy.AcademyApi;
import com.gaiamount.gaia_main.search.SearchMaterialFragment;
import com.gaiamount.module_creator.sub_module_group.adapters.MaterialGroupAdapter;
import com.gaiamount.module_creator.sub_module_group.creations.Material;
import com.gaiamount.module_material.MaterialMainActivity;
import com.gaiamount.module_user.person_creater.fragments.MaterialFrag;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by yukun on 16-9-28.
 */
public class MaterialApiHelper {


    public static void materialBanner(Context context,MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        LogUtil.d(MaterialMainActivity.class, "+"+jsonObject.toString());
        NetworkUtils.get(Material_Api.MATERIAL_BANNER,handler);

    }
    public static void getMaterialBanner(Context context,MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        LogUtil.d(MaterialMainActivity.class, "++"+jsonObject.toString());
        NetworkUtils.get(Material_Api.MATERIAL_GET_BANNER,handler);

    }

    /**
     *
     * @param t
     * @param s 推荐
     * @param opr
     * @param pi
     * @param ps
     * @param type
     * @param context
     * @param handler
     * @throws JSONException
     */
    public static void getMaterialList(int t,int s,int opr,int pi,int ps,int type,Context context,JsonHttpResponseHandler handler) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("t", t);
            jsonObject.put("s", s);
            jsonObject.put("opr", opr);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
            jsonObject.put("type", type);
        }catch (Exception e){
            e.printStackTrace();
        }
        LogUtil.d(MaterialMainActivity.class, jsonObject.toString());
        NetworkUtils.post(context,Material_Api.MATERIAL_LIST,jsonObject,handler);

    }

    /**
     * 自己创作的素材
     * @param uid
     * @param pi
     * @param context
     * @param handler
     * @throws JSONException
     */
    public static void getMaterialCreateList(long uid,int pi,Context context,JsonHttpResponseHandler handler) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("t", 0);
            jsonObject.put("s", 0);
            jsonObject.put("opr", 0);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", 10);
            jsonObject.put("type", 3);
        }catch (Exception e){
            e.printStackTrace();
        }
        LogUtil.d(MaterialFrag.class, "material:"+jsonObject.toString());
        NetworkUtils.post(context,Material_Api.MATERIAL_LIST,jsonObject,handler);

    }

    /**
     * 创作者小组素材列表
     * @param gid
     * @param pi
     * @param context
     * @param handler
     * @throws JSONException
     */
    public static void getMaterialGroupCreateList(long gid,int pi,Context context,JsonHttpResponseHandler handler) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gid",gid);
            jsonObject.put("t", 0);
            jsonObject.put("s", 0);
            jsonObject.put("opr", 0);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", 10);
            jsonObject.put("type", 3);
        }catch (Exception e){
            e.printStackTrace();
        }
        LogUtil.d(Material.class, jsonObject.toString());
        NetworkUtils.post(context,Material_Api.MATERIAL_LIST,jsonObject,handler);
    }

    public static void getMaterialSearchList(int t,int s,int opr,int pi,int ps,int type,String keywords,Context context,JsonHttpResponseHandler handler) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("t", t);
            jsonObject.put("s", s);
            jsonObject.put("opr", opr);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
            jsonObject.put("type", type);
            jsonObject.put("keywords",keywords);
        }catch (Exception e){
            e.printStackTrace();
        }
        LogUtil.d(SearchMaterialFragment.class, jsonObject.toString());
        NetworkUtils.post(context,Material_Api.MATERIAL_LIST,jsonObject,handler);

    }

    /**
     *
     * @param t
     * @param s
     * @param opr
     * @param pi
     * @param ps
     * @param type
     * @param format
     * @param width
     * @param height
     * @param context
     * @param handler
     * @throws JSONException
     */
    public static void getMaterialLists(int t,int s,int opr,int pi,int ps,int type,String format,
                                        int width,int height,Context context,JsonHttpResponseHandler handler) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("t", t);
            jsonObject.put("s", s);
            jsonObject.put("opr", opr);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
            jsonObject.put("type", type);
            jsonObject.put("format", format);
            jsonObject.put("width", width);
            jsonObject.put("height", height);
        }catch (Exception e){
            e.printStackTrace();
        }
        LogUtil.d(MaterialMainActivity.class, jsonObject.toString());
        NetworkUtils.post(context,Material_Api.MATERIAL_LIST,jsonObject,handler);

    }

    public static void getMaterialRec(long mid, int o,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("mid",mid);
            jsonObject.put("o",0);
            jsonObject.put("ps",20);
            Log.d("MaterialRecomend", "getDetail"+jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context, Material_Api.MATERIAL_RECOMEND,jsonObject,jsonHttpResponseHandler);
    }

    /**
     *  素材详情页
     * @param uid
     * @param mid
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getMaterialDetail(long uid, long mid,Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("mid", mid);
            Log.d("MaterialDetail", "getDetail" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context, Material_Api.MATERIAL_DETAIL,jsonObject,jsonHttpResponseHandler);

    }

    /**
     *  收藏列表
     * @param uid
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getMaterialColl(long uid,int pi,Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("t",0);
            jsonObject.put("s",0);
            jsonObject.put("pi",pi);
            jsonObject.put("opr",0);
            Log.d("MaterialFrag", "getDetail" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context, Material_Api.MATERIAL_COLL,jsonObject,jsonHttpResponseHandler);

    }
    /**  o  0取消收藏 1收藏
     *  收藏列表
     * @param uid
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getMaterialAddColl(long uid,long mid,int o,Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("mid",mid);
            jsonObject.put("o",o);
            Log.d("MaterialPlayActivity", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context, Material_Api.MATERIAL_ADD_COLL,jsonObject,jsonHttpResponseHandler);

    }
    public static void getMaterialDet(long mid,Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid",mid);
            Log.d("MaterialFrag", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context, Material_Api.MATERIAL_DET,jsonObject,jsonHttpResponseHandler);

    }
}