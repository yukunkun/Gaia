package com.gaiamount.module_creator.sub_module_group.beans;

import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-6-23.
 */
public class GroupMemberBean {

    private long memberId;

    private String memberAvatar;

    private String memberNickName;

    private String memberJob;

    private String memberAddress;

    private int createCount;

    private int browseCount;

    private long userId;

    private int type;
    private long mid;

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public GroupMemberBean() {
    }


    public GroupMemberBean(JSONObject response) {
        memberId = response.optLong("id");
        mid= response.optLong("mid");
        memberAvatar = response.optString("avatar");
        memberNickName = response.optString("nickName");
        memberJob = response.optString("job");
        memberAddress = response.optString("address");

    }

    public int getCreateCount() {
        return createCount;
    }

    public void setCreateCount(int createCount) {
        this.createCount = createCount;
    }

    public int getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(int browseCount) {
        this.browseCount = browseCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public String getMemberJob() {
        return memberJob;
    }

    public void setMemberJob(String memberJob) {
        this.memberJob = memberJob;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }
}
