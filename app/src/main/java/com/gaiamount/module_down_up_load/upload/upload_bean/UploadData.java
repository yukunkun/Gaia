package com.gaiamount.module_down_up_load.upload.upload_bean;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-3-25.
 * 记录上传的数据：inputKey，token，videoPath
 */
public class UploadData implements Serializable{
    private String inputKey;
    private String token;
    private String videoPath;
    private int progress;

    public UploadData(String inputKey, String token, String videoPath,int progress) {
        this.inputKey = inputKey;
        this.token = token;
        this.videoPath = videoPath;
        this.progress = progress;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "UploadData{" +
                "inputKey='" + inputKey + '\'' +
                ", token='" + token + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", progress=" + progress +
                '}';
    }
}

