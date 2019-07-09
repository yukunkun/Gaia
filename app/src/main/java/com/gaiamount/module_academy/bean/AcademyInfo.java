package com.gaiamount.module_academy.bean;

/**
 * Created by yukun on 16-8-22.
 */
public class AcademyInfo {

    /**
     * cover : course/abcnd.jpg
     * learningCount : 1
     * allowFree : 1
     * price : 0
     * name : zzb的教程^^!哈哈哈哈aa
     * courseId : 6
     */

    private String cover;
    private int learningCount;
    private int allowFree;
    private int price;
    private String name;
    private long courseId;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getLearningCount() {
        return learningCount;
    }

    public void setLearningCount(int learningCount) {
        this.learningCount = learningCount;
    }

    public int getAllowFree() {
        return allowFree;
    }

    public void setAllowFree(int allowFree) {
        this.allowFree = allowFree;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
