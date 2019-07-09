package com.gaiamount.gaia_main.home.bean;

/**
 * Created by yukun on 16-10-31.
 */
public class Banner {


    /**
     * shuffl : course/bg/1477018568403.jpeg
     * id : 2
     * type : 0
     * url : http://www.baidu.com
     */

    private String shuffl;
    private long id;
    private int type;
    private String url;
    private long wid;

    public long getWid() {
        return wid;
    }

    public void setWid(long wid) {
        this.wid = wid;
    }

    public String getShuffl() {
        return shuffl;
    }

    public void setShuffl(String shuffl) {
        this.shuffl = shuffl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
