package com.gaiamount.module_down_up_load.upload.upload_bean;

/**
 * Created by haiyang-lu on 16-3-18.
 * 下载设置的bean
 */
public class DownloadSetting {
    private int allowDownload =1;//默认允许下载
    private int allowCharge = 0;//默认免费
    private TransCoding transCoding = new TransCoding();
    private Price price = new Price();

    public int getAllowDownload() {
        return allowDownload;
    }

    public void setAllowDownload(int allowDownload) {
        this.allowDownload = allowDownload;
    }


    public int getAllowCharge() {
        return allowCharge;
    }

    public void setAllowCharge(int allowCharge) {
        this.allowCharge = allowCharge;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public TransCoding getTransCoding() {
        return transCoding;
    }

    public void setTransCoding(TransCoding transCoding) {
        this.transCoding = transCoding;
    }

    @Override
    public String toString() {
        return "DownloadSetting{" +
                "allowDownload=" + allowDownload +
                ", allowCharge=" + allowCharge +
                ", transCoding=" + transCoding +
                ", price=" + price +
                '}';
    }

}
