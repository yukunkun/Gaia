package com.gaiamount.module_down_up_load.upload.upload_bean;

/**
 * Created by haiyang-lu on 16-3-18.
 */
public class TransCoding {
    private int have4k = 0;
    private int have2k = 0;
    private int have1080 = 0;
    private int have720 = 0;

    public int getHave4k() {
        return have4k;
    }

    public void setHave4k(int have4k) {
        this.have4k = have4k;
    }

    public int getHave2k() {
        return have2k;
    }

    public void setHave2k(int have2k) {
        this.have2k = have2k;
    }

    public int getHave1080() {
        return have1080;
    }

    public void setHave1080(int have1080) {
        this.have1080 = have1080;
    }

    public int getHave720() {
        return have720;
    }

    public void setHave720(int have720) {
        this.have720 = have720;
    }

    public TransCoding(int have4k, int have2k, int have1080, int have720) {
        this.have4k = have4k;
        this.have2k = have2k;
        this.have1080 = have1080;
        this.have720 = have720;
    }

    public TransCoding() {
    }

    @Override
    public String toString() {
        return "TransCoding{" +
                "have4k=" + have4k +
                ", have2k=" + have2k +
                ", have1080=" + have1080 +
                ", have720=" + have720 +
                '}';
    }
}