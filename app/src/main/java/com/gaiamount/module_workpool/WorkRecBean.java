package com.gaiamount.module_workpool;

/**
 * Created by haiyang-lu on 16-3-29.
 */
public class WorkRecBean {

    private Object keywords;
    private String nickName;
    private int have1080;
    private int likeCount;
    private String screenshot;
    private String avatar;
    private String type;
    private int userId;
    private int isOfficial;
    private int commentCount;
    private String playUrl;
    private int duration;
    private int is4K;
    private int playCount;
    private int have720;
    private double grade;
    private String name;
    private int id;
    private String inputKey;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Object getKeywords() {
        return keywords;
    }

    public void setKeywords(Object keywords) {
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

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    @Override
    public String toString() {
        return "WorkRecBean{" +
                "keywords=" + keywords +
                ", nickName='" + nickName + '\'' +
                ", have1080=" + have1080 +
                ", likeCount=" + likeCount +
                ", screenshot='" + screenshot + '\'' +
                ", avatar='" + avatar + '\'' +
                ", type='" + type + '\'' +
                ", id=" + userId +
                ", isOfficial=" + isOfficial +
                ", commentCount=" + commentCount +
                ", playUrl='" + playUrl + '\'' +
                ", duration=" + duration +
                ", is4K=" + is4K +
                ", playCount=" + playCount +
                ", have720=" + have720 +
                ", grade=" + grade +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", inputKey='" + inputKey + '\'' +
                '}';
    }
}
