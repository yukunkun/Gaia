package com.gaiamount.module_creator.sub_module_mygroup;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-7-18.
 */
public class MyGroupInfo implements Serializable {

    /**
     * creationCount : 2
     * createTime : {"date":1,"day":5,"hours":10,"minutes":27,"month":6,"nanos":0,"seconds":28,"time":1467340048000,"timezoneOffset":-480,"year":116}
     * background : group/bg86/1467343166582.JPG
     * nickName : lhy
     * memberCount : 4
     * name : 赵波，我的小组呢
     * description : 测试，都说是测试啦
     * id : 86
     * id : 3098
     * imId : 219362208423870900
     */

    private int creationCount;
    /**
     * date : 1
     * day : 5
     * hours : 10
     * minutes : 27
     * month : 6
     * nanos : 0
     * seconds : 28
     * time : 1467340048000
     * timezoneOffset : -480
     * year : 116
     */

    private CreateTimeBean createTime;
    private String background;
    private String nickName;
    private int memberCount;
    private String name;
    private String description;
    private int id;
    private int userId;
    private String imId;

    public int getCreationCount() {
        return creationCount;
    }

    public void setCreationCount(int creationCount) {
        this.creationCount = creationCount;
    }

    public CreateTimeBean getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeBean createTime) {
        this.createTime = createTime;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
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
