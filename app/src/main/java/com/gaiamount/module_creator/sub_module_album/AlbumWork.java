package com.gaiamount.module_creator.sub_module_album;

/**
 * Created by haiyang-lu on 16-7-4.
 */
public class AlbumWork {

    /**
     * keywords : HMM，牛肉
     * nickName : gouxin123
     * have1080 : 1
     * likeCount : 0
     * screenshot : screenshot/u3094/v4836.png
     * avatar : avatar/u3094/1467019802933.png
     * type : 6,5,8,
     * id : 3094
     * isOfficial : 0
     * commentCount : 3
     * playUrl : http://7xpmfz.com5.z0.glb.clouddn.com/playlist/u3094/v4836_720.m3u8?e=1468224208&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:aM4odsiWWkPqLxdGM4qR4rOSsPY=
     * cover : cover/w3094/1466477813787.png
     * duration : 28
     * is4K : 0
     * playCount : 510
     * createTime : {"date":1,"day":3,"hours":17,"minutes":31,"month":5,"nanos":0,"seconds":44,"time":1464773504000,"timezoneOffset":-480,"year":116}
     * have720 : 1
     * grade : 6.8
     * name : 摸你
     * id : 4804
     * inputKey : playlist/u3094/v4836
     */

    private String keywords;
    private String nickName;
    private int have1080;
    private int likeCount;
    private String screenshot;
    private String avatar;
    private String type;
    private long userId;
    private int isOfficial;
    private int commentCount;
    private String playUrl;
    private String cover;
    private int duration;
    private int is4K;
    private int playCount;
    private long avid;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * date : 1
     * day : 3
     * hours : 17
     * minutes : 31
     * month : 5
     * nanos : 0
     * seconds : 44
     * time : 1464773504000
     * timezoneOffset : -480
     * year : 116
     */

    private CreateTimeBean createTime;
    private int have720;
    private double grade;
    private String name;
    private int id;
    private String inputKey;

    public long getAvid() {
        return avid;
    }

    public void setAvid(long avid) {
        this.avid = avid;
    }

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public CreateTimeBean getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeBean createTime) {
        this.createTime = createTime;
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

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
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
