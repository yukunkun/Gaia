package com.gaiamount.apis.api_creator;

import android.content.Context;

import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-7-22.
 * 创作者个人相关的联网帮助类
 */
public class PersonApiHelper {

    /**
     * 添加取消关注
     *
     * @param oid     被关注人的id  	大于1的整数
     * @param state   状态 0取消关注 1关注
     * @param context
     */
    public static void addAttention(long oid, final int state, Context context) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oid", oid);
            jsonObject.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(PersonApiHelper.class,jsonObject.toString());

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(PersonApiHelper.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                EventAddAttention eventAddAttention = GsonUtil.getInstannce().getGson().fromJson(response.toString(), EventAddAttention.class);
                EventBus.getDefault().post(eventAddAttention);

            }
        };
        NetworkUtils.post(context, PersonApi.ADD_ATTENTION, jsonObject, handler);
    }

    public static void isAttentioned(long targetUserId, Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oid", targetUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(PersonApiHelper.class,jsonObject.toString());

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(PersonApiHelper.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                EventIsAttention eventIsAttention = GsonUtil.getInstannce().getGson().fromJson(response.toString(),EventIsAttention.class);
                EventBus.getDefault().post(eventIsAttention);
            }
        };

        NetworkUtils.post(context,PersonApi.IS_ATTENTIONED,jsonObject,handler);

    }

    public class EventAddAttention {
        private int b;

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    /**
     * {
     "a": 0,（当前登陆用户没有关注你查看的用户）
     "b": 1（本次请求成功）
     }
     */
    public class EventIsAttention{
        private int a;
        private int b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    //获取到创作者个人列表
    public static void createPersonList(int s,int opr,int pi ,Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("s", s);
            jsonObject.put("t", 1);
            jsonObject.put("opr", opr);
            jsonObject.put("ps", 10);
            jsonObject.put("pi", pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "createGroup:" + jsonObject.toString());

        NetworkUtils.post(context, PersonApi.CREATE_PERSON, jsonObject, jsonHttpResponseHandler);

    }

    //获取到创作者个人列表
    public static void createOrngizationList(int pi ,String name,Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("t", 2);
            jsonObject.put("name",name);
            jsonObject.put("opr", 0);
            jsonObject.put("ps", 10);
            jsonObject.put("pi", pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "ori:" + jsonObject.toString());

        NetworkUtils.post(context, PersonApi.CREATE_PERSON, jsonObject, jsonHttpResponseHandler);

    }

}
