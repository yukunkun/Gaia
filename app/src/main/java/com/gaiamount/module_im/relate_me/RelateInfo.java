package com.gaiamount.module_im.relate_me;

/**
 * Created by yukun on 16-12-29.
 */
public class RelateInfo {

    /**
     * learningCount : 1
     * note : 151
     * types : 0
     * otherId : 3105
     * relateId : 0
     * contentId : 29
     * collectCount : 1
     * description : course summarize
     * message : 购买了我的教程
     * type : 1
     * userId : 1688
     * contentName : This is an English Course
     * cover : course/bg/1473407521934.jpeg
     * avg : 5.285714285714286
     * createTime : {"date":7,"day":3,"hours":10,"minutes":36,"month":11,"nanos":0,"seconds":25,"time":1481078185000,"timezoneOffset":-480,"year":116}
     * price : 1
     * grade : 5.285714285714286
     * id : 42
     * category : 色彩管理,摄影
     * contentType : 3
     */

    private int learningCount;
    private int note;
    private int types;
    private int otherId;
    private int relateId;
    private int contentId;
    private int collectCount;
    private String description;
    private String message;
    private int type;
    private int userId;
    private String contentName;
    private String cover;
    private double avg;
    /**
     * date : 7
     * day : 3
     * hours : 10
     * minutes : 36
     * month : 11
     * nanos : 0
     * seconds : 25
     * time : 1481078185000
     * timezoneOffset : -480
     * year : 116
     */

    private CreateTimeBean createTime;
    private int price;
    private double grade;
    private int id;
    private String category;
    private int contentType;

    public int getLearningCount() {
        return learningCount;
    }

    public void setLearningCount(int learningCount) {
        this.learningCount = learningCount;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getOtherId() {
        return otherId;
    }

    public void setOtherId(int otherId) {
        this.otherId = otherId;
    }

    public int getRelateId() {
        return relateId;
    }

    public void setRelateId(int relateId) {
        this.relateId = relateId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public CreateTimeBean getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeBean createTime) {
        this.createTime = createTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public static class CreateTimeBean {
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
