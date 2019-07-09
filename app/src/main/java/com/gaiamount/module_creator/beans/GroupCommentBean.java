package com.gaiamount.module_creator.beans;

/**
 * Created by haiyang-lu on 16-6-15.
 */
public class GroupCommentBean {

    /**
     * originContent : 这是一个小组
     * createTime : {"date":12,"day":0,"hours":17,"minutes":53,"month":5,"nanos":0,"seconds":21,"time":1465725201000,"timezoneOffset":-480,"year":116}
     * nickName : 你好世界
     * originAvatar : avatar/u1688_1465281187739.png
     * originNickName : 你好世界
     * id : 4
     * avatar : avatar/u1688_1465281187739.png
     * id : 1688
     * content : 这是一个小组
     */

    private String originContent;
    /**
     * date : 12
     * day : 0
     * hours : 17
     * minutes : 53
     * month : 5
     * nanos : 0
     * seconds : 21
     * time : 1465725201000
     * timezoneOffset : -480
     * year : 116
     */

    private CreateTimeBean createTime;
    private String nickName;
    private String originAvatar;
    private String originNickName;
    private int id;
    private String avatar;
    private int userId;
    private String content;

    public String getOriginContent() {
        return originContent;
    }

    public void setOriginContent(String originContent) {
        this.originContent = originContent;
    }

    public CreateTimeBean getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeBean createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOriginAvatar() {
        return originAvatar;
    }

    public void setOriginAvatar(String originAvatar) {
        this.originAvatar = originAvatar;
    }

    public String getOriginNickName() {
        return originNickName;
    }

    public void setOriginNickName(String originNickName) {
        this.originNickName = originNickName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
