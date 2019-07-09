package com.gaiamount.module_user.notes.bean;

/**
 * Created by yukun on 16-12-28.
 */
public class NoteInfo {

    /**
     * isRecommend : 0
     * address : 你回复打开视频f奇偶iwe
     * reprintCount : 0
     * browseCount : 0
     * nickName : zzb
     * title : 我的所发生的电费
     * content : 呵呵呵呵呵第三节方式贷款甲方是地产金科啊得分王
     * cover :
     * shareCount : 0
     * recommendCount : 3
     * createTime : {"date":21,"day":1,"hours":11,"minutes":21,"month":10,"nanos":0,"seconds":5,"time":1479698465000,"timezoneOffset":-480,"year":116}
     * reprintId : 123
     * id : 4
     * isCopy : 0
     */

    private int isRecommend;
    private String address;
    private int reprintCount;
    private int browseCount;
    private String nickName;
    private String title;
    private String content;
    private String cover;
    private int shareCount;
    private int recommendCount;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private String text;
    private long time;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * date : 21
     * day : 1
     * hours : 11
     * minutes : 21
     * month : 10
     * nanos : 0
     * seconds : 5
     * time : 1479698465000
     * timezoneOffset : -480
     * year : 116
     */

    private CreateTimeBean createTime;
    private int reprintId;
    private int id;
    private int isCopy;

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getReprintCount() {
        return reprintCount;
    }

    public void setReprintCount(int reprintCount) {
        this.reprintCount = reprintCount;
    }

    public int getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(int browseCount) {
        this.browseCount = browseCount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
    }

    public CreateTimeBean getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeBean createTime) {
        this.createTime = createTime;
    }

    public int getReprintId() {
        return reprintId;
    }

    public void setReprintId(int reprintId) {
        this.reprintId = reprintId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(int isCopy) {
        this.isCopy = isCopy;
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
