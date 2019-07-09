package com.gaiamount.apis.api_creator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gaiamount.apis.api_academy.AcademyApi;
import com.gaiamount.apis.api_material.Material_Api;
import com.gaiamount.apis.api_works.WorksApi;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.sub_module_group.creations.Material;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.encrypt.SHA256;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-6-30.
 * 专辑相关的接口帮助类
 */
public class AlbumApiHelper {
    /**
     * 创建专辑,如果t=1 gid必填；如果pub==0 pwd必填
     *
     * @param name    专辑名称
     * @param bg      封面
     * @param content 专辑简介
     * @param t       专辑类型（0个人1小组)
     * @param gid     如果t为1，此参数不为空
     * @param pub     是否公开
     * @param pwd     密码
     */
    public static void createAlbum(String name, String bg, String content, int t, @Nullable long gid, int pub, String pwd, Context context, MJsonHttpResponseHandler handler) {
        if (name.isEmpty()) {
            GaiaApp.showToast("名称不能为空");
            return;
        }
        if (content.isEmpty()) {
            GaiaApp.showToast("描述不能为空");
            return;
        }
        if (bg.isEmpty()) {
            GaiaApp.showToast("请上传专辑图片");
            return;
        }
        if (pub == 0 && pwd.isEmpty()) {
            GaiaApp.showToast("请输入密码");
            return;
        }
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", name);
            jsonObject.put("bg", bg);
            jsonObject.put("t", t);
            jsonObject.put("content", content);
            if (t == 1) {
                jsonObject.put("gid", gid);
            }
            jsonObject.put("pub", pub);
            if (pub == 0 && pwd != null) {
                jsonObject.put("pwd", SHA256.bin2hex(pwd));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "createAlbum" + jsonObject.toString());

        NetworkUtils.post(context, AlbumApi.CREATE_ALBUM, jsonObject, handler);
    }

    public static void updateAlbum(long aid,String name, String bg, String content, int t, @Nullable long gid, int pub, String pwd, Context context, MJsonHttpResponseHandler handler) {
        if (name.isEmpty()) {
            GaiaApp.showToast("名称不能为空");
            return;
        }
        if (content.isEmpty()) {
            GaiaApp.showToast("描述不能为空");
            return;
        }
//        if (bg.isEmpty()) {
//            GaiaApp.showToast("请上传专辑图片");
//            return;
//        }
        if (pub == 0 && pwd.isEmpty()) {
            GaiaApp.showToast("请输入密码");
            return;
        }
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", name);
            if(bg!="null"||!bg.equals(null)){
                jsonObject.put("bg", bg);
            }

            jsonObject.put("aid", aid);
            jsonObject.put("t", t);
            jsonObject.put("content", content);
            if (t == 1) {
                jsonObject.put("gid", gid);
            }
            jsonObject.put("pub", pub);
            if (pub == 0 && pwd != null) {
                jsonObject.put("pwd", SHA256.bin2hex(pwd));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "createAlbum" + jsonObject.toString());

        NetworkUtils.post(context, AlbumApi.CREATE_UPDATE, jsonObject, handler);
    }

    /**
     * 获取专辑创作的作品
     *
     * @param aid     专辑id
     * @param t       小组类型（0个人 1小组)
     * @param c       查询的类别（0 作品 1素材 2剧本 3学院)
     * @param opr     排序类型（0默认 1最新发布 2最多播放 3最多收藏 4最多
     * @param s       查询条件（0全部 1推荐 2 4K 3 可下载 4 免费下载 5 付费下载)
     * @param context
     * @param handler
     */
    public static void searchAlbumWorks(long aid, int t, int c, int opr, int s, int pi, int ps, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("aid", aid);
            jsonObject.put("t", t);
            jsonObject.put("c", c);
            jsonObject.put("opr", opr);
            jsonObject.put("s", s);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("AlbumWorkFrag", jsonObject.toString());

        NetworkUtils.post(context, AlbumApi.SEARCH_ALBUM_WORKS, jsonObject, handler);
    }

    /**
     * 得到专辑列表
     *对应的javabean为{@link com.gaiamount.module_creator.sub_module_album.AlbumBean}
     * @param gid     小组id
     * @param type    小组类型0 个人 1小组
     * @param opr     排序方式 0默认 1 最新创建 2 最多创作 3 最多浏览
     * @param context
     * @param handler
     */
    public static void getAlbumList(long gid, int type, int opr, int pi,Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("gid", gid);
            jsonObject.put("type", type);
            jsonObject.put("opr", opr);
            jsonObject.put("ps",50);
            jsonObject.put("pi", pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "getAlbumList" + jsonObject.toString());

        NetworkUtils.post(context, AlbumApi.GET_ALBUM_LIST, jsonObject, handler);
    }

    /**
     * 获取专辑详情
     * @param aid
     * @param pwd
     * @param context
     * @param handler
     */
    public static void getAlbumDetail(long aid, String pwd, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("aid", aid);
            if (pwd!=null&&!pwd.equals("")) {
                jsonObject.put("pwd", SHA256.bin2hex(pwd));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "getAlbumDetail" + jsonObject.toString());

        NetworkUtils.post(context, AlbumApi.DETAILS, jsonObject, handler);
    }

    /**
     * 删除专辑
     * @param aid 专辑id
     * @param context
     * @param handler
     */
    public static void deleteAlbum(long aid, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("aid",aid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "deleteAlbum" + jsonObject.toString());

        NetworkUtils.post(context,AlbumApi.DELETE,jsonObject,handler);
    }

    /**
     * 批量添加创作到一个专辑中
     * @param wids 创作的id数组
     * @param ctype 添加到专辑的视频类型（0作品 1素材 2剧本 3学院)
     * @param aid  专辑id
     * @param context
     * @param
     */
    public static void addVideoToAlbum(Long[] wids, long ctype, Long aid, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(wids[0]);//目前只做单个添加
            jsonObject.put("wids",jsonArray);
            jsonObject.put("c",ctype);
            JSONArray aids = new JSONArray();
            aids.put(aid);
            jsonObject.put("aids",aids);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "addVideoToAlbum" + jsonObject.toString());

        NetworkUtils.post(context,AlbumApi.ADD,jsonObject,handler);
    }

    /**
     *
     * @param wids
     * @param t
     * @param albumId
     * @param context
     * @param handler
     */
    public static void addWorksToAlbum(Long[] wids, long t, Long[] albumId, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(wids[0]);//目前只做单个添加
            jsonObject.put("wids",jsonArray);
            jsonObject.put("t",t);
            JSONArray aids = new JSONArray();
            aids.put(albumId[0]);
            jsonObject.put("albumId",aids);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "addWorksToAlbum" + jsonObject.toString());

        NetworkUtils.post(context, WorksApi.ADDTOALBUM,jsonObject,handler);
    }

    /**
     * 移除专辑中的某个视频
     * @param id_
     * @param gid
     * @param type
     * @param context
     * @param handler
     */
    public static void removeVideoFromAlbum(long id_, long gid, int type, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id_);
            if(gid!=0){
                jsonObject.put("gid",gid);
            }
            jsonObject.put("t",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "removeVideoFromAlbum" + jsonObject.toString());

        NetworkUtils.post(context,AlbumApi.REMOVE_VIDEO_FROM_ALBUM,jsonObject,handler);
    }

    public static void removePersonAlbum(long t,int id,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("t",t);
            Log.d("AlbumPersonCollageFrag", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context, AlbumApi.ACADEMY_GROUP_LIST,jsonObject,jsonHttpResponseHandler);
    }

    /**
     *
     * @param aid
     * @param cid
     * @param uid
     * @param pi
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getAlbumAcademyList(long aid,long cid,long uid,int pi,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("aid",aid);
            jsonObject.put("cid",cid);
            jsonObject.put("uid",uid);
            jsonObject.put("pi",pi);
            jsonObject.put("s",0);
            jsonObject.put("opr",0);
            Log.d("AlbumCollageFrag", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context, AlbumApi.ACADEMY_GROUP_LIST,jsonObject,jsonHttpResponseHandler);
    }

    public static void getAlbumName(Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("t",1);
            jsonObject.put("opr",0);
            jsonObject.put("pi",1);
            jsonObject.put("ps",100);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "Product" + jsonObject.toString());

        NetworkUtils.post(context,AlbumApi.ACADEMY_ALBUM_SEARCH,jsonObject,handler);
    }

    public static void getPersonAlbumList(long uid,Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",uid);
            jsonObject.put("t",0);
            jsonObject.put("opr",0);
            jsonObject.put("pi",1);
            jsonObject.put("ps",100);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumApiHelper.class, "Product" + jsonObject.toString());

        NetworkUtils.post(context,AlbumApi.ACADEMY_ALBUM_SEARCH,jsonObject,handler);
    }
    /**
     * 专辑素材列表
     * @param aid
     * @param pi
     * @param context
     * @param handler
     * @throws JSONException
     */
    public static void getMaterialALbumCreateList(long aid,int t,int pi,Context context,JsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("aid",aid);
            jsonObject.put("t", t);
            jsonObject.put("s", 0);
            jsonObject.put("opr", 0);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", 10);
            jsonObject.put("type", 3);
        }catch (Exception e){
            e.printStackTrace();
        }
        LogUtil.d(Material.class, jsonObject.toString());
        NetworkUtils.post(context, AlbumApi.MATERIAL_ALBUM_LIST,jsonObject,handler);
    }

}
