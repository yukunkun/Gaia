package com.gaiamount.module_scripe.bean;

import java.io.Serializable;

/**
 * Created by yukun on 16-10-25.
 */
public class ScripeInfo implements Serializable{

    /**
     * nickName : 你好世界
     * introduce : 灵感来自于盗墓笔记
     * avatar : avatar/u1688/1466747698611.png
     * type : 1,2,3
     * title : 寻龙诀
     * space : 1
     * browserCount : 0
     * sid : 1
     * outline : 水电费水电费
     * isFree : 1
     * price : 0
     * state : 1
     * id : 1688
     * collectCount : 0
     * commentCount : 16
     */

    private String nickName;
    private String introduce;
    private String avatar;
    private String type;
    private String title;
    private int space;
    private int browserCount;
    private int sid;
    private String outline;
    private int isFree;
    private int price;
    private int state;
    private long id;
    private int collectCount;
    private int commentCount;
    private String cover;
    private long time;
    private String upDateTime;
    private int category;
    private long gvid;
    private long avid;

    public long getAvid() {
        return avid;
    }

    public void setAvid(long avid) {
        this.avid = avid;
    }

    public long getGvid() {
        return gvid;
    }

    public void setGvid(long gvid) {
        this.gvid = gvid;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getUpDateTime() {
        return upDateTime;
    }

    public void setUpDateTime(String upDateTime) {
        this.upDateTime = upDateTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getBrowserCount() {
        return browserCount;
    }

    public void setBrowserCount(int browserCount) {
        this.browserCount = browserCount;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
