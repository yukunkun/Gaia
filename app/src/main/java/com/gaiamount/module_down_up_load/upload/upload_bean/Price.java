package com.gaiamount.module_down_up_load.upload.upload_bean;

/**
 * Created by haiyang-lu on 16-3-18.
 */
public class Price {
    private float priceOriginal = 0;
    private float price720 = 0;
    private float price1080 = 0;
    private float price2k = 0;
    private float price4k = 0;

    public float getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(float priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public float getPrice720() {
        return price720;
    }

    public void setPrice720(float price720) {
        this.price720 = price720;
    }

    public float getPrice1080() {
        return price1080;
    }

    public void setPrice1080(float price1080) {
        this.price1080 = price1080;
    }

    public float getPrice2k() {
        return price2k;
    }

    public void setPrice2k(float price2k) {
        this.price2k = price2k;
    }

    public float getPrice4k() {
        return price4k;
    }

    public void setPrice4k(float price4k) {
        this.price4k = price4k;
    }

    @Override
    public String toString() {
        return "Price{" +
                "priceOriginal=" + priceOriginal +
                ", price720=" + price720 +
                ", price1080=" + price1080 +
                ", price2k=" + price2k +
                ", price4k=" + price4k +
                '}';
    }

    public Price(float priceOriginal, float price720, float price1080, float price2k, float price4k) {
        this.priceOriginal = priceOriginal;
        this.price720 = price720;
        this.price1080 = price1080;
        this.price2k = price2k;
        this.price4k = price4k;
    }

    public Price() {
    }
}
