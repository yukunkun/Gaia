package com.gaiamount.apis.api_scripe;

import android.content.Context;
import android.util.Log;

import com.gaiamount.module_creator.sub_module_album.AlbumScriptFrag;
import com.gaiamount.module_creator.sub_module_group.creations.Script;
import com.gaiamount.module_scripe.ScripeActivity;
import com.gaiamount.module_user.personal.collections.ScripeFrag;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yukun on 16-10-25.
 */
public class ScriptApiHelper {
    //得到列表页
    public static void getRec(int sc, int bg, int se, int opr, int pi, int ps, Context context, MJsonHttpResponseHandler handler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("sc",sc);
            jsonObject.put("bg",bg);
            jsonObject.put("se",se);
            jsonObject.put("opr",opr);
            jsonObject.put("pi",pi);
            jsonObject.put("ps",ps);
            LogUtil.d(ScripeActivity.class,jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,ScriptApi.SCRIPTLIST,jsonObject,handler);
    }
    //我的创作
    public static void getMyScript(long uid,int pi, int ps, Context context, MJsonHttpResponseHandler handler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("sc",0);
            jsonObject.put("bg",0);
            jsonObject.put("se",0);
            jsonObject.put("opr",0);
            jsonObject.put("pi",pi);
            jsonObject.put("ps",ps);
            LogUtil.d(ScripeActivity.class,jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,ScriptApi.SCRIPTLIST,jsonObject,handler);
    }

    //我的小组剧本
    public static void getGroupScript(long gid,int pi, int ps, Context context, MJsonHttpResponseHandler handler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("gid",gid);
            jsonObject.put("sc",0);
            jsonObject.put("bg",0);
            jsonObject.put("se",0);
            jsonObject.put("opr",0);
            jsonObject.put("pi",pi);
            jsonObject.put("ps",ps);
            LogUtil.d(Script.class,jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,ScriptApi.SCRIPTLIST,jsonObject,handler);
    }
    //全局搜索页

    /**
     *
     * @param sc  (0全部 1爱情 …17其他)
     * @param bg 背景 	0全部 1现代 …5其它
     * @param se 筛选 	0全部 1已完稿 6推荐
     * @param opr 排序 	0默认 1最新 … 4最多评论
     * @param pi
     * @param ps
     * @param key
     * @param context
     * @param handler
     */
    public static void getGloableSearch(int sc, int bg, int se, int opr, int pi, int ps,String key, Context context, MJsonHttpResponseHandler handler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("sc",sc);
            jsonObject.put("bg",bg);
            jsonObject.put("se",se);
            jsonObject.put("opr",opr);
            jsonObject.put("pi",pi);
            jsonObject.put("ps",ps);
            jsonObject.put("key",key);
            LogUtil.d(ScripeActivity.class,jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,ScriptApi.SCRIPTLIST,jsonObject,handler);
    }

    //剧本的详细页
    public static void getScriptDetail(long sid,long uid,Context context,MJsonHttpResponseHandler handler){

        LogUtil.d(ScripeActivity.class,ScriptApi.SCRIPTDETAIL+sid+"/"+uid);

        NetworkUtils.get(ScriptApi.SCRIPTDETAIL+sid+"/"+uid,handler);
    }

    //剧本的目录
    public static void getScriptContent(long sid,long uid,Context context,MJsonHttpResponseHandler handler){

        LogUtil.d(ScripeActivity.class,ScriptApi.SCRIPTCONTENT+sid+"/"+uid);

        NetworkUtils.get(ScriptApi.SCRIPTCONTENT+sid+"/"+uid,handler);
    }

    //剧本的目录

    /**
     * @param t 1收藏 0取消
     * @param sid
     * @param uid
     *
     * @param context
     * @param handler
     */

    public static void getScriptCollect(long sid,long uid,int t,Context context,MJsonHttpResponseHandler handler){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("sid",sid);
            jsonObject.put("uid",uid);
            jsonObject.put("t",t);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(ScripeActivity.class,jsonObject.toString());
        NetworkUtils.post(context,ScriptApi.SCRIPTCOLLECT,jsonObject,handler);
    }
    // 权限
    public static void getScriptAgree(long sid,long uid,int t,MJsonHttpResponseHandler handler){

        LogUtil.d(ScripeActivity.class,ScriptApi.SCRIPTAGREE+uid+"/apply/"+sid+"/"+t);
        NetworkUtils.get(ScriptApi.SCRIPTAGREE+uid+"/apply/"+sid+"/"+t,handler);
    }

    // 删除
    public static void getScriptDel(long sid,MJsonHttpResponseHandler handler){

        LogUtil.d(ScripeActivity.class,ScriptApi.SCRIPTDET+sid);
        NetworkUtils.get(ScriptApi.SCRIPTDET+sid,handler);
    }
    /**
     *   收藏页
     * @param uid
     * @param sc  (0全部 1爱情 …17其他)
     * @param bg 背景 	0全部 1现代 …5其它
     * @param se 筛选 	0全部 1已完稿 6推荐
     * @param opr 排序 	0默认 1最新 … 4最多评论
     * @param pi
     * @param ps
     * @param context
     * @param handler
     */
    public static void getScriptCollectList(long uid,int sc, int bg, int se, int opr, int pi, int ps,Context context,MJsonHttpResponseHandler handler){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("sc",sc);
            jsonObject.put("bg",bg);
            jsonObject.put("se",se);
            jsonObject.put("opr",opr);
            jsonObject.put("pi",pi);
            jsonObject.put("ps",ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(ScripeFrag.class,"Scripe:"+jsonObject.toString());
        NetworkUtils.post(context,ScriptApi.SCRIPTCOLLLIST,jsonObject,handler);
    }

    //获取到剧本的banner条
    public static void getScripeBanner(MJsonHttpResponseHandler handler){

        JSONObject jsonObject=new JSONObject();

        LogUtil.d(ScripeActivity.class,"Scripe:"+jsonObject.toString());
        NetworkUtils.get(ScriptApi.SCRIPTBANNER,handler);
    }

    //获取到最佳编剧
    public static void getScripeEdits(long uid,Context context,MJsonHttpResponseHandler handler){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(ScripeActivity.class,"Scripe:"+jsonObject.toString());
        NetworkUtils.post(context,ScriptApi.SCRIPTEDITS,jsonObject,handler);
    }

    public static void getScripeAlbum(long aid,int pi, int ps,Context context,MJsonHttpResponseHandler handler) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("aid",aid);
            jsonObject.put("sc", 0);
            jsonObject.put("bg", 0);
            jsonObject.put("se", 0);
            jsonObject.put("opr", 0);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(AlbumScriptFrag.class, "Scripe:" + jsonObject.toString());
        NetworkUtils.post(context, ScriptApi.SCRIPTALBUM, jsonObject, handler);
    }

}
