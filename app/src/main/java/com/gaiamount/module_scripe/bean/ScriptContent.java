package com.gaiamount.module_scripe.bean;

/**
 * Created by yukun on 16-10-26.
 */
public class ScriptContent {
    /**
     * title : 第一幕
     * ind : 1.0
     * id : 1.0
     */

    private String title;
    private long ind;
    private long id;
    private int isPublic;

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getInd() {
        return ind;
    }

    public void setInd(long ind) {
        this.ind = ind;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
