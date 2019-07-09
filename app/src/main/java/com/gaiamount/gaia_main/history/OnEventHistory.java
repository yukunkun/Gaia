package com.gaiamount.gaia_main.history;

/**
 * Created by haiyang-lu on 16-7-26.
 * 历史记录的java bean
 */
public class OnEventHistory {
    private String screenShot;
    private String cover;
    private String name;
    private long timeStamp;
    private int id;
    private int contentType;

    public OnEventHistory(String screenShot, String cover, String name, long timeStamp, int id, int contentType) {
        this.screenShot = screenShot;
        this.cover = cover;
        this.name = name;
        this.timeStamp = timeStamp;
        this.id = id;
        this.contentType = contentType;
    }

    public String getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(String screenShot) {
        this.screenShot = screenShot;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}
