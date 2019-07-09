package com.gaiamount.module_scripe.bean;

/**
 * Created by yukun on 16-11-1.
 */
public class EdidsInfo {

    /**
     * uid : 3108
     * avatar : avatar/u3108/1477639620783
     * job : 导演
     * signature : 来来来
     * nickName : Mrflea
     */

    private long uid;
    private String avatar;
    private String job;
    private String signature;
    private String nickName;
    private int isFocus;

    public int getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
