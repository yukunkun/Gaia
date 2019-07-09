package com.gaiamount.module_down_up_load.upload_manage;

/**
 * Created by haiyang-lu on 16-7-27.
 */
public class UploadedWorks {

    /**
     * name : OPPO R7 拍照篇
     * cover :
     * schedule : 0
     * id : 5423
     * screenshot :
     * status : 0
     */

    private String name;
    private String cover;
    private int schedule;
    private long id;
    private String screenshot;
    private int status;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getSchedule() {
        return schedule;
    }

    public void setSchedule(int schedule) {
        this.schedule = schedule;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
