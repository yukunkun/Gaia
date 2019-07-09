package com.gaiamount.module_player.bean;

/**
 * Created by haiyang-lu on 16-3-28.
 * 视频对象信息
 */
//public class VideoInfo implements Parcelable {
public class VideoInfo{
    /**
     * keywords : null
     * nickName : 壮飞
     * have1080 : 1
     * likeCount : 0
     * screenshot : screenshot/u21/v1320.png
     * avatar :
     * type : 12,
     * id : 21
     * isOfficial : 0
     * commentCount : 0
     * playUrl : http://7xpmfz.com5.z0.glb.clouddn.com/playlist/u21/v1320_720.m3u8?pm3u8/0/43200/1461532241&e=1461532241&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:TE_x4wvpuNBr784_WdkGyBetZ-k=
     * cover : null
     * duration : 153
     * is4K : 1
     * playCount : 3
     * have720 : 1
     * grade : 0
     * name : KineMAX高感测试ISO160-10240
     * id : 256
     * time : {"date":17,"day":6,"hours":9,"minutes":47,"month":9,"nanos":0,"seconds":15,"time":1445046435000,"timezoneOffset":-480,"year":115}
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
    private String playUrl;
    private String cover;
    private int duration;
    private int is4K;
    private int playCount;
    private int have720;
    private Double grade;
    private String name;
    private int id;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * date : 17
     * day : 6
     * hours : 9
     * minutes : 47
     * month : 9
     * nanos : 0
     * seconds : 15
     * time : 1445046435000
     * timezoneOffset : -480
     * year : 115
     */

    private TimeBean time;

    public Object getKeywords() {
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

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
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

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
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

        @Override
        public String toString() {
            return "TimeBean{" +
                    "date=" + date +
                    ", day=" + day +
                    ", hours=" + hours +
                    ", minutes=" + minutes +
                    ", month=" + month +
                    ", nanos=" + nanos +
                    ", seconds=" + seconds +
                    ", time=" + time +
                    ", timezoneOffset=" + timezoneOffset +
                    ", year=" + year +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
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
                ", playUrl='" + playUrl + '\'' +
                ", cover='" + cover + '\'' +
                ", duration=" + duration +
                ", is4K=" + is4K +
                ", playCount=" + playCount +
                ", have720=" + have720 +
                ", grade=" + grade +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", time=" + time +
                '}';
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }


//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//        dest.writeString(keywords);
//        dest.writeString(nickName);
//        dest.writeInt(likeCount);
//        dest.writeString(screenshot);
//        dest.writeString(avatar);
//        dest.writeString(type);
//        dest.writeInt(isOfficial);
//        dest.writeInt(commentCount);
//        dest.writeString(cover);
//        dest.writeString(duration);
//        dest.writeInt(is4K);
//        dest.writeInt(playCount);
//        dest.writeString(grade);
//        dest.writeString(name);
//        dest.writeInt(id);
//        dest.writeString(playUrl);
//        dest.writeString(inputKey);
//    }
//    public static final Parcelable.Creator<VideoInfo> CREATOR = new Creator<VideoInfo>()
//    {
//        @Override
//        public VideoInfo[] newArray(int size)
//        {
//            return new VideoInfo[size];
//        }
//
//        @Override
//        public VideoInfo createFromParcel(Parcel in)
//        {
//            return new VideoInfo(in);
//        }
//    };


}
