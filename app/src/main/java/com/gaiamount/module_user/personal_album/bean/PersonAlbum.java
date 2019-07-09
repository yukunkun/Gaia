package com.gaiamount.module_user.personal_album.bean;

/**
 * Created by yukun on 17-1-4.
 */
public class PersonAlbum {

    /**
     * courseCount : 0
     * isDelete : 0
     * name : 888888888
     * isPublic : 1
     * bgImg :
     * id : 113
     * type : 0
     * scriptCount : 0
     * userId : 3109
     * materialCount : 3
     * content : 0.0
     * worksCount : 2
     */

    private int courseCount;
    private int isDelete;
    private String name;
    private int isPublic;
    private String bgImg;
    private long id;
    private int type;
    private int scriptCount;
    private long userId;
    private int materialCount;
    private String content;
    private int worksCount;

    public int getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
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

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
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

    public int getScriptCount() {
        return scriptCount;
    }

    public void setScriptCount(int scriptCount) {
        this.scriptCount = scriptCount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(int materialCount) {
        this.materialCount = materialCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWorksCount() {
        return worksCount;
    }

    public void setWorksCount(int worksCount) {
        this.worksCount = worksCount;
    }
}
