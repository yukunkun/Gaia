package com.gaiamount.module_down_up_load.download;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-5-17.
 */
public class DownloadItem implements Serializable{
    /**
     * 是否选中
     */
    public boolean isChecked = false;
    /**
     * 是否显示
     */
    public boolean isShow = false;

    public String url;

    /**
     * 本地的绝对地址
     */
    public String data;

    /**
     * 作品的封面
     */
    public String cover;

    public String screenShot;

    /**
     * 视频名称
     */
    public String videoName;

    /**
     * 视频后缀名
     */
    public String videoPostfix;

    /**
     * 视频大小（byte）
     */
    public long  videoSize;

    /**
     * 已经下载的大小
     */
    public long downloadedSize;

    /**
     * 状态 下载中，暂停中，可播放，出错
     */
    public String status;

    /**
     * 视频时长
     */
    public long duration;

    /**
     * 视频id
     */
    public long id;

    /**
     * 视频类型
     */
    public int contentType;

    public DownloadItem(boolean isChecked, boolean isShow) {
        this.isChecked = isChecked;
        this.isShow = isShow;
    }

    public DownloadItem(boolean isChecked,boolean isShow,String data,String videoName, String videoPostfix, long videoSize, long duration) {
        this.isChecked = isChecked;
        this.isShow = isShow;
        this.data = data;
        this.videoName = videoName;
        this.videoPostfix = videoPostfix;
        this.videoSize = videoSize;
        this.duration = duration;

        downloadedSize = videoSize;
    }
}
