package com.gaiamount.module_player.bean;

/**
 * Created by haiyang-lu on 16-4-29.
 */
public class PlayerRec {

    /**
     * keywords :
     * nickName : gouxin123
     * have1080 : 0
     * likeCount : 0
     * screenshot : origin/u3094/v4576_screen.png
     * avatar : avatar_default.jpg
     * type :
     * id : 3094
     * isOfficial : 0
     * commentCount : 0
     * cover :
     * duration : 4
     * is4K : 0
     * realName :
     * playCount : 0
     * have720 : 1
     * grade : 0
     * name : 测试20
     * id : 4579
     * time : {"date":8,"day":5,"hours":13,"minutes":40,"month":3,"nanos":0,"seconds":5,"time":1460094005000,"timezoneOffset":-480,"year":116}
     */

    private String keywords;
    private String nickName;
    private int have1080;
    private int likeCount;
    private String screenshot;
    private String avatar;
    private String type;
    private int userId;
    private int isOfficial;
    private int commentCount;
    private String cover;
    private int duration;
    private int is4K;
    private String realName;
    private int playCount;
    private int have720;
    private double grade;
    private String name;
    private int id;
    /**
     * date : 8
     * day : 5
     * hours : 13
     * minutes : 40
     * month : 3
     * nanos : 0
     * seconds : 5
     * time : 1460094005000
     * timezoneOffset : -480
     * year : 116
     */

    private TimeBean time;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getHave1080() {
        return have1080;
    }

    public void setHave1080(int have1080) {
        this.have1080 = have1080;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(int isOfficial) {
        this.isOfficial = isOfficial;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIs4K() {
        return is4K;
    }

    public void setIs4K(int is4K) {
        this.is4K = is4K;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getHave720() {
        return have720;
    }

    public void setHave720(int have720) {
        this.have720 = have720;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TimeBean getTime() {
        return time;
    }

    public void setTime(TimeBean time) {
        this.time = time;
    }

    public static class TimeBean {
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

    @Override
    public String toString() {
        return "PlayerRec{" +
                "keywords='" + keywords + '\'' +
                ", nickName='" + nickName + '\'' +
                ", have1080=" + have1080 +
                ", likeCount=" + likeCount +
                ", screenshot='" + screenshot + '\'' +
                ", avatar='" + avatar + '\'' +
                ", type='" + type + '\'' +
                ", id=" + userId +
                ", isOfficial=" + isOfficial +
                ", commentCount=" + commentCount +
                ", cover='" + cover + '\'' +
                ", duration=" + duration +
                ", is4K=" + is4K +
                ", realName='" + realName + '\'' +
                ", playCount=" + playCount +
                ", have720=" + have720 +
                ", grade=" + grade +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", time=" + time +
                '}';
    }
}
