package com.gaiamount.module_creator.sub_module_album;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-7-4.
 */
public class AlbumDetail implements Serializable{

    /**
     * bgImg : ablum/bg/1467344489598.JPG
     * type : 1
     * scriptCount : 0
     * id : 3098
     * content : 了解
     * visitCount : 0
     * createTime : {"date":1,"day":5,"hours":11,"minutes":41,"month":6,"nanos":0,"seconds":55,"time":1467344515000,"timezoneOffset":-480,"year":116}
     * courseCount : 0
     * name : 加密的路海洋专辑
     * isPublic : 0
     * id : 46
     * materialCount : 0
     * worksCount : 0
     */

    private String bgImg;
    private int type;
    private int scriptCount;
    private long userId;
    private String content;
    private int visitCount;
    /**
     * date : 1
     * day : 5
     * hours : 11
     * minutes : 41
     * month : 6
     * nanos : 0
     * seconds : 55
     * time : 1467344515000
     * timezoneOffset : -480
     * year : 116
     */

    private CreateTimeBean createTime;
    private int courseCount;
    private String name;
    private int isPublic;
    private int id;
    private int materialCount;
    private int worksCount;

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public CreateTimeBean getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeBean createTime) {
        this.createTime = createTime;
    }

    public int getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static class CreateTimeBean implements Serializable{
        private int date;
        private int day;
        private int hours;
        private int minutes;
        private int month;
        private int nanos;
        private int seconds;
        private long time;
        private int timezoneOffset;
        private int year;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getNanos() {
            return nanos;
        }

        public void setNanos(int nanos) {
            this.nanos = nanos;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}
