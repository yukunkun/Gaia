package com.gaiamount.apis.api_academy;

import android.content.Context;
import android.util.Log;

import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yukun on 16-8-4.
 */
public class AcademyApiHelper {

    //获取学院首页
    public static void getCollege(JsonHttpResponseHandler jsonHttpResponseHandler){
        Log.d("AcademyActivity", "getcollege:");
        NetworkUtils.get(AcademyApi.ACADEMY_GETCOLLEGE,jsonHttpResponseHandler);
    }
    //mix列表
    public static void getMixList(long cid, int s,int opr,int pi,int t,long uid,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",cid);
            jsonObject.put("s",s);
            jsonObject.put("opr",opr);
            jsonObject.put("t",t);
            jsonObject.put("pi",pi);
            jsonObject.put("uid",uid);
//            jsonObject.put("channelId",channelId);
            Log.d("MixingLightActivity", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_LIST,jsonObject,jsonHttpResponseHandler);
    }
    //学院详情列表uu

    public static void getDetail(long uid, long cid,int t,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("cid",cid);
            jsonObject.put("t",t);
            Log.d("AcademyDetailActivity", "getDetail"+jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_DETAIL,jsonObject,jsonHttpResponseHandler);
    }

    //获取目录
    public static void getContent(long cid,int t,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",cid);
            jsonObject.put("t",t);
            Log.d("ContentFragment", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_CONTENT,jsonObject,jsonHttpResponseHandler);
    }

    //获取视频
    public static void getVideo(long cid,long chapterId,long hid,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",cid);
            jsonObject.put("chapterId",chapterId);
            jsonObject.put("hid",hid);
            Log.d("AcademyPlayActivity", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_PLAY,jsonObject,jsonHttpResponseHandler);
    }

    //获取视频
    public static void getTuWen(long cid,long chapterId,long hid,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",cid);
            jsonObject.put("chapterId",chapterId);
            jsonObject.put("hid",hid);
            Log.d("TuWenDetailActivity", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_PLAY,jsonObject,jsonHttpResponseHandler);
    }

    //获取课表
    public static void getLesson(int  t,int pi,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("t",t);
            jsonObject.put("pi",pi);
            Log.d("MyLessonActivity", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_LESSON,jsonObject,jsonHttpResponseHandler);
    }

    //开始学习
    public static void stareStudy(long cid,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",cid);
            Log.d("AcademyDetailActivity", "startstudy"+jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_STARE_STUDY,jsonObject,jsonHttpResponseHandler);
    }
    //学习进度
    public static void stareStudyRecord(long id,double watchLen,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("watchLen",watchLen);
            Log.d("AcademyPlayActivity", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_STARE_RECORD,jsonObject,jsonHttpResponseHandler);
    }

    public static void stareTuWenRecord(long id,double watchLen,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("watchLen",watchLen);
            Log.d("TuWenDetailActivity", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_STARE_RECORD,jsonObject,jsonHttpResponseHandler);
    }
    //学习进度
    public static void collectLesson(long cid,Context context,int t,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",cid);
            jsonObject.put("t",t);
            Log.d("AcademyDetailActivity", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_COLLECT,jsonObject,jsonHttpResponseHandler);
    }
    //收藏列表
    public static void collectList(long uid,int s,int opr,int pi,int t,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("s",s);
            jsonObject.put("opr",opr);
            jsonObject.put("pi",pi);
            jsonObject.put("t",t);
            Log.d("CollectionFragment", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_COLLECT_LIST,jsonObject,jsonHttpResponseHandler);
    }
    //收藏列表
    public static void commentList(long cid,int belong,int pi,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",cid);
            jsonObject.put("belong",belong);
            jsonObject.put("ps",8);
            jsonObject.put("pi",pi);
            Log.d("CommentFragment", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_COMMENT,jsonObject,jsonHttpResponseHandler);
    }
    //发布评论
    public static void sendComment(long cid,long uid,int start,String content,float grade,Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",cid);
            jsonObject.put("uid",uid);
            jsonObject.put("start",start);
            jsonObject.put("content",content);
            jsonObject.put("grade",grade);
            Log.d("CommentFragment", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_SEND_COMMENT,jsonObject,jsonHttpResponseHandler);
    }
    //添加到小组
    public static void addToGroup(long uid, long []gids, long[] cs, int [] bs, Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            JSONArray csArray = new JSONArray();
            csArray.put(cs[0]); //只有单个添加
            JSONArray gidArray=new JSONArray();
            gidArray.put(gids[0]);
            JSONArray bsArray=new JSONArray();
            bsArray.put(bs[0]);//固定0和1(中文英文教程)
            jsonObject.put("uid",uid);
            jsonObject.put("gids",gidArray);
            jsonObject.put("cs",csArray);
            jsonObject.put("bs",bsArray);
            Log.d("addGroupDialog", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkUtils.post(context,AcademyApi.ACADEMY_ADD_GROUP,jsonObject,jsonHttpResponseHandler);
    }


    public static void getAcademyMain(Context context,JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid", GaiaApp.getAppInstance().getUserInfo().id);
            Log.d("AcademyActivity", "mainInfo:");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.get(AcademyApi.ACADEMY_MAIN+"?uid="+GaiaApp.getAppInstance().getUserInfo().id,jsonHttpResponseHandler);

    }
    //全部投稿列表
    public static void getAllTouGou(int s,int opr,int pi,int t,long uid,int c,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",1);
            jsonObject.put("s",s);
            jsonObject.put("opr",opr);
            jsonObject.put("t",t);
            jsonObject.put("pi",pi);
            jsonObject.put("uid",GaiaApp.getAppInstance().getUserInfo().id);
            jsonObject.put("c",c);
//            jsonObject.put("channelId",channelId);
            Log.d("ContributeFragment", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context,AcademyApi.ACADEMY_LIST,jsonObject,jsonHttpResponseHandler);
    }


    //获取Tolen值
    public static void getCollectIs(long cid,JsonHttpResponseHandler jsonHttpResponseHandler){
        Log.d("AcademyDetailActivity", "getCollectIs:");
        NetworkUtils.get(AcademyApi.ACADEMY_EXITS+"?cid="+cid,jsonHttpResponseHandler);
    }
}
