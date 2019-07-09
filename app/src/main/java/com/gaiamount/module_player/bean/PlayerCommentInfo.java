package com.gaiamount.module_player.bean;

/**
 * Created by haiyang-lu on 16-4-27.
 */
public class PlayerCommentInfo {

    /**
     * originContent : 提示文件损坏是因为浏览器不能直接播放原始文件，这个不用管它......直接右键-页面另存为就可以啦！
     * createTime : {"date":25,"day":1,"hours":18,"minutes":14,"month":3,"nanos":0,"seconds":33,"time":1461579273000,"timezoneOffset":-480,"year":116}
     * nickName : 18080130627
     * originAvatar : /static/img/avatar.jpg
     * originNickName : zunzheng
     * likeCount : 0
     * id : 168
     * avatar : avatar/u4902.jpg
     * content : 已经好啦
     */

    private String originContent;
    /**
     * date : 25
     * day : 1
     * hours : 18
     * minutes : 14
     * month : 3
     * nanos : 0
     * seconds : 33
     * time : 1461579273000
     * timezoneOffset : -480
     * year : 116
     */

    private CreateTimeBean createTime;
    private String nickName;
    private String originAvatar;
    private String originNickName;
    private int likeCount;
    private long id;
    private long userId;
    private String avatar;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PlayerCommentInfo{" +
                "originContent='" + originContent + '\'' +
                ", createTime=" + createTime +
                ", nickName='" + nickName + '\'' +
                ", originAvatar='" + originAvatar + '\'' +
                ", originNickName='" + originNickName + '\'' +
                ", likeCount=" + likeCount +
                ", id=" + id +
                ", avatar='" + avatar + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
