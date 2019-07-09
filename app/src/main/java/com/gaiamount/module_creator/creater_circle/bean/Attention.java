package com.gaiamount.module_creator.creater_circle.bean;

/**
 * Created by yukun on 16-7-25.
 */
public class Attention  {

    /**
     * creationCount : 2
     * address : 美国
     * signature : 看记录
     * nickName : gouxin123
     * browseCount : 17
     * likeCount : 2
     * id : 3094
     * avatar : avatar/u3094/1467019802933.png
     * job : 剪辑师
     */

    private int creationCount;
    private String address;
    private String signature;
    private String nickName;
    private int browseCount;
    private int likeCount;
    private int id;
    private String avatar;
    private String job;

    public int getCreationCount() {
        return creationCount;
    }

    public void setCreationCount(int creationCount) {
        this.creationCount = creationCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(int browseCount) {
        this.browseCount = browseCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Attention{" +
                "creationCount=" + creationCount +
                ", address='" + address + '\'' +
                ", signature='" + signature + '\'' +
                ", nickName='" + nickName + '\'' +
                ", browseCount=" + browseCount +
                ", likeCount=" + likeCount +
                ", id=" + id +
                ", avatar='" + avatar + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
