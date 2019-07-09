package com.gaiamount.module_creator.sub_module_album;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-6-30.
 */
public class AlbumBean implements Serializable{


    /**
     * cover :
     * collegeCount : 0
     * name : 规定法规
     * isPublic : 1
     * scriptCount : 0
     * aid : 28
     * materialCount : 0
     * worksCount : 0
     */

    private String cover;
    private int collegeCount;
    private String name;
    private int isPublic;
    private int scriptCount;
    private long aid;
    private int materialCount;
    private int worksCount;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getCollegeCount() {
        return collegeCount;
    }

    public void setCollegeCount(int collegeCount) {
        this.collegeCount = collegeCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getScriptCount() {
        return scriptCount;
    }

    public void setScriptCount(int scriptCount) {
        this.scriptCount = scriptCount;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public int getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(int materialCount) {
        this.materialCount = materialCount;
    }

    public int getWorksCount() {
        return worksCount;
    }

    public void setWorksCount(int worksCount) {
        this.worksCount = worksCount;
    }
}
