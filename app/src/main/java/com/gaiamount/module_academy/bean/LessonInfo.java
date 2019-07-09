package com.gaiamount.module_academy.bean;

import java.io.Serializable;

/**
 * Created by yukun on 16-8-4.
 */
public class LessonInfo implements Serializable{
    private String name;
    private int id;
    private String screenshot;
    private int isPublic;
    private int len;
    private int  progress;
    private long cid;
    private String cover;
    private int isLearning;
    private int type;
    private long time;
    private int duration;
    private int watchLen;

    private int hasLeanCount;
    private int hourCount;

    public int getHasLeanCount() {
        return hasLeanCount;
    }

    public void setHasLeanCount(int hasLeanCount) {
        this.hasLeanCount = hasLeanCount;
    }

    public int getHourCount() {
        return hourCount;
    }

    public void setHourCount(int hourCount) {
        this.hourCount = hourCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWatchLen() {
        return watchLen;
    }

    public void setWatchLen(int watchLen) {
        this.watchLen = watchLen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsLearning() {
        return isLearning;
    }

    public void setIsLearning(int isLearning) {
        this.isLearning = isLearning;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
