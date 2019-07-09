package com.gaiamount.module_down_up_load.upload.upload_bean;

/**
 * Created by haiyang-lu on 16-3-23.
 * 用户上传的相关状态信息
 */
public class UploadingData {
    private int id;
    private String name;
    private String schedule;
    private String cover;
    private String screenshot;
    private int status;

    public UploadingData(int id, String name, String schedule, String cover, String screenshot, int status) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.cover = cover;
        this.screenshot = screenshot;
        this.status = status;
    }

    public UploadingData() {
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UploadingData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", schedule='" + schedule + '\'' +
                ", cover='" + cover + '\'' +
                ", screenshot='" + screenshot + '\'' +
                ", status=" + status +
                '}';
    }
}
