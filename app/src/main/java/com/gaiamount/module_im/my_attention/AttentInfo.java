package com.gaiamount.module_im.my_attention;

/**
 * Created by yukun on 16-12-29.
 */
public class AttentInfo {

    /**
     * types : 3
     * address : 成都
     * nickName : Kunkun
     * contentId : 1688
     * focus : 1
     * description : 这里，有我就可以，你是多余！
     * likeCount : 4
     * avatar : avatar/u8676/1474876377270
     * message : 关注了'soymilk'
     * userId : 8676
     * browserCount : 1927
     * cover : avatar/u1688/1482110254486.jpeg
     * uid : 1688
     * focusName : soymilk
     * createCount : 9
     * createTime : {"date":29,"day":4,"hours":16,"minutes":36,"month":11,"nanos":0,"seconds":18,"time":1483000578000,"timezoneOffset":-480,"year":116}
     * job : 摄影师
     * contentType : 0
     * type  : 3
     */

    private int types;
    private String address;
    private String nickName;
    private int contentId;
    private int focus;
    private String description;
    private int likeCount;
    private String avatar;
    private String message;
    private long userId;
    private int browserCount;
    private String cover;
    private String screenshot;
    private String groupName;
    private int uid;
    private long otherId;

    private String focusName;
    private int createCount;
    private String contentName;
    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    /**
     * date : 29
     * day : 4
     * hours : 16
     * minutes : 36
     * month : 11
     * nanos : 0
     * seconds : 18
     * time : 1483000578000
     * timezoneOffset : -480
     * year : 116
     */

    private CreateTimeBean createTime;

    private String job;
    private int contentType;
    private int type;
    public int getTypes() {
        return types;
    }

    public long getOtherId() {
        return otherId;
    }

    public void setOtherId(long otherId) {
        this.otherId = otherId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getBrowserCount() {
        return browserCount;
    }

    public void setBrowserCount(int browserCount) {
        this.browserCount = browserCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFocusName() {
        return focusName;
    }

    public void setFocusName(String focusName) {
        this.focusName = focusName;
    }

    public int getCreateCount() {
        return createCount;
    }

    public void setCreateCount(int createCount) {
        this.createCount = createCount;
    }

    public CreateTimeBean getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeBean createTime) {
        this.createTime = createTime;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
