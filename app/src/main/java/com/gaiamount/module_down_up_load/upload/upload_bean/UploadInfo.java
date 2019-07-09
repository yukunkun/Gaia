package com.gaiamount.module_down_up_load.upload.upload_bean;

/**
 * Created by haiyang-lu on 16-3-17.
 * 上传所需要的对象
 */
public class UploadInfo {
    private long uid;
    private UpdateWorksBean mUpdateWorks;
    private String format;
    private long mSize;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public UpdateWorksBean getUpdateWorks() {
        return mUpdateWorks;
    }

    public void setUpdateWorks(UpdateWorksBean updateWorks) {
        mUpdateWorks = updateWorks;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        mSize = size;
    }
}
