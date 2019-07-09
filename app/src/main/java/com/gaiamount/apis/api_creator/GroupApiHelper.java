package com.gaiamount.apis.api_creator;

import android.content.Context;

import com.gaiamount.module_creator.beans.GroupDetailInfo;
import com.gaiamount.module_creator.sub_module_group.activities.GroupExamineActivity;
import com.gaiamount.module_creator.sub_module_group.beans.GroupAdmin;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by haiyang-lu on 16-6-13.
 * 创作者-小组相关的接口帮助类
 */
public class GroupApiHelper {

    /**
     * 创建小组
     *
     * @param name
     * @param keywords                小组关键字
     * @param isExamine               是否需要审核--0不需要1需要
     * @param description             小组描述
     * @param backgrounds             背景封面
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void createGroup(String name, String keywords, int isExamine, String description, String backgrounds, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", name);
            jsonObject.put("keywords", keywords);
            jsonObject.put("isExamine", isExamine);
            jsonObject.put("description", description);
            jsonObject.put("background", backgrounds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "createGroup:" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GROUP_CREATE_URL, jsonObject, jsonHttpResponseHandler);

    }

    public static final int GROUP_SORT_TYPE_RECOMMEND = 0;
    public static final int GROUP_SORT_TYPE_MEMBER = 1;
    public static final int GROUP_SORT_TYPE_CREATIONS = 2;
    public static final int GROUP_SORT_TYPE_CREATE_TIME = 3;
    /**
     * 获取小组列表
     *
     * @param type type(int) 排序方式{0 推荐度 1 成员量 2 作品量 3 创建时间}
     *             pi(int) 当前页{默认为1}
     *             ps 每页显示的数据{默认为8}
     *             key 按照关键字搜索
     * @param context
     * @param jsonHttpResponseHandler
     * @return 网络链接情况
     * @see NetworkUtils#TYPE_NO_NETWORK
     */
    public static int getGroupList(int type, int pi, int ps, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        int networkConnection = NetworkUtils.checkNetworkConnection();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type", type);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "getGroupList:" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.SEARCH_URL, jsonObject, jsonHttpResponseHandler);


        return networkConnection;
    }

    /**
     * @param uid
     * @param gid
     * @param domain
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getGroupDetail(long uid, long gid, String domain, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("gid", gid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(GroupApiHelper.class, "getGroupDetail:" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GROUP_DETAIL_URL, jsonObject, jsonHttpResponseHandler);
    }

    public static void getGroupComment(long gid, int pi, int ps, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gid", gid);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(GroupApiHelper.class, "getGroupComment:" + jsonObject.toString());
        NetworkUtils.post(context, GroupApi.GROUP_COMMENT_URL, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 删除小组里的某条评论
     *
     * @param id      评论的id
     * @param context
     */
    public static void deleteGroupOneComment(long id, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gcid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GROUP_DETAIL_COMMENT_URL, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 小组留言，回复
     *
     * @param t                      固定值为0
     * @param uId                    当前评论人的id
     * @param gid                    小组id
     * @param content                评论内容
     * @param isReply                是否是回复
     * @param gcid                   被回复评论的id
     * @param commentResponseHandler
     */
    public static void sendComment(int t, long uId, long gid, String content, int isReply, Context context, long gcid, MJsonHttpResponseHandler commentResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("t", t);
            jsonObject.put("uid", uId);
            jsonObject.put("gid", gid);
            jsonObject.put("content", content);
            if (isReply == 1) {
                jsonObject.put("t", 1);
                jsonObject.put("gcid", gcid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(GroupApiHelper.class, "sendComment" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GROUP_DO_COMMENT, jsonObject, commentResponseHandler);
    }

    /**
     * 加入小组（不需要审核）
     *
     * @param gids
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void joinGroup(List<Long> gids, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(gids.get(0));
            jsonObject.put("gids", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(GroupApiHelper.class, "joinGroup" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GROUP_JOIN_URL, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 退出小组
     *
     * @param gid
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void exitGroup(long gid, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gid", gid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "exitGroup" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GROUP_EXITGROUP, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 判断用户是否属于该用户
     *
     * @param gid                     小组id
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void isGroupMember(long gid, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gid", gid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "isGroupMember" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GROUP_ISGROUP_MEMBER, jsonObject, jsonHttpResponseHandler);

    }

    /**
     * * 更新小组信息
     *
     * @param groupDetailInfo
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void updateGroupInfo(GroupDetailInfo groupDetailInfo, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        GroupDetailInfo.OBean.GroupBean group = groupDetailInfo.getO().getGroup();
        long[] gids = new long[]{group.getId()};
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(gids[0]);

            jsonObject.put("name", group.getName());
            jsonObject.put("gids", jsonArray);
            jsonObject.put("keywords", group.getKeywords());
            jsonObject.put("background", group.getBackground());
            jsonObject.put("isExamine", group.getIsExamine());
            jsonObject.put("description", group.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(GroupApiHelper.class, "updateGroupInfo" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GROUP_UPDATE_URL, jsonObject, jsonHttpResponseHandler);

    }

    /**
     * 设置小组管理员
     *
     * @param groupAdmin              管理员对象
     * @param type                    1设置管理员 0取消管理员
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void setGroupAdmin(GroupAdmin groupAdmin, int type, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("gid", groupAdmin.getGid());
            jsonObject.put("mid", groupAdmin.getMid());
            jsonObject.put("type", type);
            jsonObject.put("exa", groupAdmin.getExa());
            jsonObject.put("ccre", groupAdmin.getCcre());
            jsonObject.put("mb", groupAdmin.getMb());
            jsonObject.put("ms", groupAdmin.getMs());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "setGroupAdmin" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.SET_GROUP_ADMIN, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 获取小组成员列表
     *
     * @param gid                     小组id
     * @param sType                   筛选条件
     * @param opr                     排序方式
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getGroupUser(long gid, int sType, int opr, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("gid", gid);
            jsonObject.put("sType", sType);
            jsonObject.put("opr", opr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(GroupApiHelper.class, "getGroupUser" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GET_GROUP_USER, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 获取小组管理员列表
     *
     * @param gid
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getAdmin(long gid, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("gid", gid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "getAdmin" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GET_ADMIN, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * @param gid
     * @param context
     * @param jsonHttpResponseHandler
     * @deprecated 获取小组推荐视频列表
     */
    public static void getRecommend(long gid, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("gid", gid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "getRecommend" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.GET_RECOMMEND, jsonObject, jsonHttpResponseHandler);

    }

    /**
     * 获取小组创作列表
     *
     * @param gid                     小组id
     * @param vType                   视频类型(0 作品 1 剧本 2 学院)
     * @param pi                      当前页(默认为1)
     * @param ps                      每页显示的数据（默认为8）
     * @param context
     * @param jsonHttpResponseHandler
     */
    public static void getGroupVideo(long gid, int vType, int pi, int ps, String keywords, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("gid", gid);
            jsonObject.put("vType", vType);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", ps);
            jsonObject.put("keywords", keywords);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "getGroupVideo" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.VIDEOS, jsonObject, jsonHttpResponseHandler);
    }

    public static void setGroupRecommend(long gid, long vid, int position, Context context, JsonHttpResponseHandler jsonHttpResponseHandler) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("gid", gid);
            jsonObject.put("vid", vid);
            jsonObject.put("position", position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "setGroupRecommend" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.SET_RECOMMEND, jsonObject, jsonHttpResponseHandler);
    }

    /**
     * 批量删除小组成员
     *
     * @param gid
     * @param uids
     * @param context
     * @param handler
     */
    public static void batchCleanMember(long gid, List<Long> uids, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < uids.size(); i++) {
            jsonArray.put(uids.get(i));
        }
        try {
            jsonObject.put("gid", gid);
            jsonObject.put("uids", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "batchCleanMember" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.BATCH_CLEAN_MEMBER, jsonObject, handler);
    }

    /**
     * @param context
     * @param handler
     */
    public static void myGroup(Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        LogUtil.d(GroupApiHelper.class, "myGroup" + "不需要参数");

        NetworkUtils.post(context, GroupApi.MY, jsonObject, handler);
    }

    /**
     * 为小组添加视频
     *
     * @param gid     小组id
     * @param vid     视频id
     * @param vType   视频类型{0 作品 1 素材 2 剧本}
     * @param context
     * @param handler
     */
    public static void addVideoToGroup(long gid, long vid, int vType, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gid", gid);
            jsonObject.put("vid", vid);
            jsonObject.put("vType", vType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(GroupApiHelper.class, "addVideoToGroup" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.ADD_VIDEO, jsonObject, handler);
    }

    /**
     * 批量移除小组视频
     *
     * @param ids     小组视频id数组
     * @param gid     小组id
     * @param context type 0 作品 ,1素材 ,2学院 ,3剧本
     * @param handler
     */
    public static void batchRemoveVideo(Long[] ids, int type,long gid, Context context, MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("gid", gid);
            jsonObject.put("type", type);
            jsonArray.put(ids[0]);
            jsonObject.put("gvids", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.d(GroupApiHelper.class, "batchRemoveVideo" + jsonObject.toString());

        NetworkUtils.post(context, GroupApi.BATCH_REMOVE_VIDEO, jsonObject, handler);
    }

    public static void getMyGroup(long uid,Context context,MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "batchRemoveVideo" + jsonObject.toString());
        NetworkUtils.post(context,GroupApi.MY,jsonObject,handler);
    }
    //小组的学院
    public static void getMyGroupCOLLEGE(long uid,long gid,int pi,Context context,MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("gid", gid);
            jsonObject.put("s", 0);
            jsonObject.put("opr", 0);
            jsonObject.put("pi", pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupApiHelper.class, "College" + jsonObject.toString());
        NetworkUtils.post(context,GroupApi.GROUP_COLLEGE,jsonObject,handler);
    }

    //小组审核
    public static void getGroupExamine(long gid,int pi,Context context,MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gid", gid);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", 8);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupExamineActivity.class, "" + jsonObject.toString());
        NetworkUtils.post(context,GroupApi.GROUP_EXAMINE,jsonObject,handler);
    }
    //小组审核同意
    public static void getExamineOk(long mid,int result,Context context,MJsonHttpResponseHandler handler) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", mid);
            jsonObject.put("result", result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(GroupExamineActivity.class, "" + jsonObject.toString());
        NetworkUtils.post(context,GroupApi.GROUP_OK_OR_NO,jsonObject,handler);
    }
}
