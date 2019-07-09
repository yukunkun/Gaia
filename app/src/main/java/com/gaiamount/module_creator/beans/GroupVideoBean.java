package com.gaiamount.module_creator.beans;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-6-27.
 */
public class GroupVideoBean implements Serializable{

    /**
     * keywords : null
     * nickName : 柒月JULY
     * have1080 : 0
     * likeCount : 2
     * screenshot : screenshot/u4/v1886.png
     * avatar : avatar/u4.jpg
     * type : 12,
     * id : 4
     * commentCount : 2
     * playUrl : http://7xpmfz.com5.z0.glb.clouddn.com/playlist/u4/v1886_mp4.m3u8?e=1467616308&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:piCbwKxZ1gG-e512NYPOH-PEgpE=
     * cover : null
     * duration : 20
     * is4K : 1
     * playCount : 119
     * have720 : 1
     * grade : 6.2
     * name : Unleash 4K video recording with the all new XC10.mov
     * id : 8
     * inputKey : playlist/u4/v1886
     */

    private String keywords;
    private String nickName;
    private int have1080;
    private int likeCount;
    private String screenshot;
    private String avatar;
    private String type;
    private int userId;
    private int commentCount;
    private String playUrl;
    private String cover;
    private int duration;
    private int is4K;
    private int playCount;
    private int have720;
    private double grade;
    private String name;
    private int vid;
    private long gvid;
    private String inputKey;
    private long id;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public long getGvid() {
        return gvid;
    }

    public void setGvid(long gvid) {
        this.gvid = gvid;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getHave1080() {
        return have1080;
    }

    public void setHave1080(int have1080) {
        this.have1080 = have1080;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIs4K() {
        return is4K;
    }

    public void setIs4K(int is4K) {
        this.is4K = is4K;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getHave720() {
        return have720;
    }

    public void setHave720(int have720) {
        this.have720 = have720;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }
}
