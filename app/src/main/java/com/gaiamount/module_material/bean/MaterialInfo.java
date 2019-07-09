package com.gaiamount.module_material.bean;

import java.io.Serializable;

/**
 * Created by yukun on 16-9-28.
 */
public class MaterialInfo implements Serializable{

    /**
     * keywords : null
     * nickName : 你好世界
     * have1080 : 1
     * fps : 25/1
     * format : mp4
     * likeCount : 0
     * screenshot : screenshot/u49/v1315.png
     * avatar : avatar/u1688/1466747698611.png
     * type : 12,
     * userId : 1688
     * isVip : 1
     * isOfficial : 0
     * commentCount : 0
     * cover : null
     * duration : 93
     * is4K : 0
     * vipLevel : 4
     * playCount : 113
     * have720 : 1
     * grade : 0
     * name : 桃花盛开.mov
     * id : 21
     * inputKey : playlist/u49/v1315
     * allowCharge : 0
     */

    private String keywords;
    private String nickName;
    private int have1080;
    private String fps;
    private String format;
    private int likeCount;
    private String screenshot;
    private String avatar;
    private String type;
    private long userId;
    private int isVip;
    private int isOfficial;
    private int commentCount;
    private String cover;
    private int duration;
    private int is4K;
    private int vipLevel;
    private int playCount;
    private int have720;
    private double grade;
    private String name;
    private long id;
    private String inputKey;
    private int allowCharge;
    private int width;
    private int downloadCount;
    private int category;
    private int flag;

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    private int types;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    private long gvid;

    public long getGvid() {
        return gvid;
    }

    public void setGvid(long gvid) {
        this.gvid = gvid;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private int height;

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

    public String getFps() {
        return fps;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(int isOfficial) {
        this.isOfficial = isOfficial;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
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

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public int getAllowCharge() {
        return allowCharge;
    }

    public void setAllowCharge(int allowCharge) {
        this.allowCharge = allowCharge;
    }
}
