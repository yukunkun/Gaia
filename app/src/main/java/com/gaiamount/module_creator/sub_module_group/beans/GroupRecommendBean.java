package com.gaiamount.module_creator.sub_module_group.beans;

/**
 * Created by haiyang-lu on 16-6-27.
 */
public class GroupRecommendBean {

    /**
     * createTime : 2016-06-24 17:55:58
     * groupId : 62
     * id : 48
     * isDelete : 0
     * isRecommend : 1
     * id : 3101
     * videoId : 8
     * videoType : 0
     */

    private String createTime;
    private long groupId;
    private long id;
    private int isDelete;
    private int isRecommend;
    private long userId;
    private long videoId;
    private int videoType;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }
}
