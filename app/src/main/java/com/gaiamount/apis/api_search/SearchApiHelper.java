package com.gaiamount.apis.api_search;

import android.content.Context;
import android.util.Log;

import com.gaiamount.apis.api_academy.AcademyApi;
import com.gaiamount.gaia_main.search.beans.OnEventPerson;
import com.gaiamount.gaia_main.search.beans.SearchGroupResult;
import com.gaiamount.gaia_main.search.beans.SearchPersonResult;
import com.gaiamount.gaia_main.search.beans.SearchWorksResult;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by haiyang-lu on 16-7-19.
 * 全局搜索的帮助类
 */
public class SearchApiHelper {
    /**
     * 搜索作品内容
     * wtype=作品类型筛选
     * 0 所有作品
     * 01 测试&样片
     * 02 剪辑&调色
     * 03 风景&旅行
     * 04 短片
     * 05 记录
     * 06 商业&广告
     * 07 MV
     * 08航拍
     * 09教程
     * 10 艺术与实验
     * 11 家庭与婚礼
     * 12 其他
     * ptype=作品属性筛选
     * 0 全部
     * 1 盖亚推荐
     * 2 4k视频
     * 3 可下载
     * 4 免费下载
     * 5 收费下载
     * stype=作品统计筛选
     * 0默认
     * 1 最新发布
     * 2 最多播放
     * 3 最多搜藏
     * 4 最多评论
     * 5 最多下载
     * key=关键字筛选
     * pi=数据的第几页
     * 默认1
     * ps=每一页多少条数据
     * 默认 8
     * 备注:可组合筛选
     */
    public static void searchWorks(String key, int stype, int pi, int ps, Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wtype", 0);//0代表所有
            jsonObject.put("ptype", 0);//0代表所有
            jsonObject.put("stype", stype);
            jsonObject.put("key", key);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(SearchApiHelper.class, "searchWorks", jsonObject.toString());

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(SearchApiHelper.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                List<SearchWorksResult> list = GsonUtil.getInstannce().getGson().fromJson(response.optJSONArray("a").toString(),
                        new TypeToken<List<SearchWorksResult>>() {
                        }.getType());
                EventBus.getDefault().post(list);
            }
        };

        NetworkUtils.post(context, SearchApi.SEARCH_WORKS, jsonObject, handler);
    }

    /**
     * 搜索小组
     * @param opr  0默认 1最多创作 2最多成员 3最新创建
     */
    public static void searchGroups(String key, int opr,int pi, int ps, Context context) {
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(SearchApiHelper.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                List<SearchGroupResult> list = GsonUtil.getInstannce().getGson().fromJson(response.optJSONArray("a").toString(),
                        new TypeToken<List<SearchGroupResult>>() {
                        }.getType());
                EventBus.getDefault().post(list);
            }
        };
        searchCreator(0,0,key,opr,pi,ps,context,handler);

    }

    /**
     * 搜索个人
     * @param opr 0默认 1最多创作 2最多粉丝 3最多浏览
     */
    public static void searchPersons(String name,int opr,Context context) {
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(SearchApiHelper.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                List<SearchPersonResult> list = GsonUtil.getInstannce().getGson().fromJson(response.optJSONArray("a").toString(),
                        new TypeToken<List<SearchPersonResult>>() {
                        }.getType());

                EventBus.getDefault().post(new OnEventPerson(list));

            }
        };
        searchCreator(0,1,name,opr,1,20,context,handler);

    }

    /**
     * @param s       t==1(0全部 1 导演 2编剧 3摄影师 4剪辑师 5调色师 6灯光师 7其他) t==0(s不用传) t==2暂定
     * @param t       0小组 1个人 2机构
     * @param name    任意字符串
     * @param opr     t==0的时候(0默认 1最多创作 2最多成员 3最新创建) t==1(0默认 1最多创作 2最多粉丝 3最多浏览)
     * @param pi      大于1的整数 默认为1
     * @param ps      大于1的整数 默认为8
     * @param context
     */
    public static void searchCreator(int s, int t, String name, int opr, int pi, int ps, Context context,MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("s", s);
            jsonObject.put("t", t);
            jsonObject.put("name", name);
            jsonObject.put("opr", opr);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(SearchApiHelper.class, "searchCreator", jsonObject.toString());

        NetworkUtils.post(context, SearchApi.SEARCH_CREATOR, jsonObject, handler);
    }

    public static void searchAcademy(int opr,int pi,int t,long uid,String key,Context context, JsonHttpResponseHandler jsonHttpResponseHandler){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("cid",0);
            jsonObject.put("s",0);
            jsonObject.put("opr",opr);
            jsonObject.put("pi",pi);
            jsonObject.put("t",t);
            jsonObject.put("uid",uid);
            jsonObject.put("key",key);
            Log.d("SearchAcademyFrag", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(context, AcademyApi.ACADEMY_LIST,jsonObject,jsonHttpResponseHandler);
    }
}
