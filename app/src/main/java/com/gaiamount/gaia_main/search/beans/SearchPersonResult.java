package com.gaiamount.gaia_main.search.beans;

/**
 * Created by haiyang-lu on 16-7-19.
 */
public class SearchPersonResult {

    /**
     * note : 0
     * address :
     * createCount : 1
     * signature : null
     * nickName : 18629553604
     * browseCount : 0
     * likeCount : 0
     * id : 1
     * avatar : avatar_default.jpg
     * job :
     */

    private int note;
    private String address;
    private int createCount;
    private String signature;
    private String nickName;
    private int browseCount;
    private int likeCount;
    private int id;
    private String avatar;
    private String job;

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCreateCount() {
        return createCount;
    }

    public void setCreateCount(int createCount) {
        this.createCount = createCount;
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
}
