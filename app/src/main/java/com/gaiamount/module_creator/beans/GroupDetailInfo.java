package com.gaiamount.module_creator.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by haiyang-lu on 16-6-14.
 */
public class GroupDetailInfo implements Serializable{

    /**
     * b : 1
     * o : {"recommend":[{"name":"泰坦尼克号","nickName":"柒月JULY","have720":1,"have1080":1,"playUrl":"http://7xpmfz.com5.z0.glb.clouddn.com/playlist/u4/v1519_720.m3u8?e=1466489345&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:6pzuAM2Dwo75OD6Kfh_tZwUrfv8="},{"name":"草地.MOV","nickName":"壮飞","have720":0,"have1080":1,"playUrl":"http://7xpmfz.com5.z0.glb.clouddn.com/playlist/u21/v1011_mp4.m3u8?e=1466489345&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:eQRsges5AjF8hVve4Nh27T_ngws="}],"user":{"nickName":"你好世界"},"group":{"background":"a.jpg","commentCount":0,"createTime":"2016-06-12 16:17:55","creationCount":0,"description":"是否坚实价的深刻了减肥","domain":"","id":42,"isExamine":0,"keywords":"没有开始","memberCount":3,"name":"黑","priority":0,"visitCount":0}}
     */

    private int b;
    /**
     * recommend : [{"name":"泰坦尼克号","nickName":"柒月JULY","have720":1,"have1080":1,"playUrl":"http://7xpmfz.com5.z0.glb.clouddn.com/playlist/u4/v1519_720.m3u8?e=1466489345&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:6pzuAM2Dwo75OD6Kfh_tZwUrfv8="},{"name":"草地.MOV","nickName":"壮飞","have720":0,"have1080":1,"playUrl":"http://7xpmfz.com5.z0.glb.clouddn.com/playlist/u21/v1011_mp4.m3u8?e=1466489345&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:eQRsges5AjF8hVve4Nh27T_ngws="}]
     * user : {"nickName":"你好世界"}
     * group : {"background":"a.jpg","commentCount":0,"createTime":"2016-06-12 16:17:55","creationCount":0,"description":"是否坚实价的深刻了减肥","domain":"","id":42,"isExamine":0,"keywords":"没有开始","memberCount":3,"name":"黑","priority":0,"visitCount":0}
     */

    private OBean o;

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public OBean getO() {
        return o;
    }

    public void setO(OBean o) {
        this.o = o;
    }

    public static class OBean implements Serializable{
        /**
         * nickName : 你好世界
         */

        private UserBean user;
        /**
         * background : a.jpg
         * commentCount : 0
         * createTime : 2016-06-12 16:17:55
         * creationCount : 0
         * description : 是否坚实价的深刻了减肥
         * domain :
         * id : 42
         * isExamine : 0
         * keywords : 没有开始
         * memberCount : 3
         * name : 黑
         * priority : 0
         * visitCount : 0
         */

        private GroupBean group;
        /**
         * name : 泰坦尼克号
         * nickName : 柒月JULY
         * have720 : 1
         * have1080 : 1
         * playUrl : http://7xpmfz.com5.z0.glb.clouddn.com/playlist/u4/v1519_720.m3u8?e=1466489345&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:6pzuAM2Dwo75OD6Kfh_tZwUrfv8=
         */

        private List<RecommendBean> recommend;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public GroupBean getGroup() {
            return group;
        }

        public void setGroup(GroupBean group) {
            this.group = group;
        }

        public List<RecommendBean> getRecommend() {
            return recommend;
        }

        public void setRecommend(List<RecommendBean> recommend) {
            this.recommend = recommend;
        }

        public static class UserBean implements Serializable{
            private int isJoin;

            private int allowExamine;

            private int allowManagerSpecial;

            private int allowCleanCreation;

            private int allowCleanMember;

            private String nickName;

            private int memberType;

            public int getIsJoin() {
                return isJoin;
            }

            public void setIsJoin(int isJoin) {
                this.isJoin = isJoin;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getMemberType() {
                return memberType;
            }

            public void setAdmin(int admin) {
                memberType = admin;
            }

            public int getAllowExamine() {
                return allowExamine;
            }

            public void setAllowExamine(int allowExamine) {
                this.allowExamine = allowExamine;
            }

            public int getAllowManagerSpecial() {
                return allowManagerSpecial;
            }

            public void setAllowManagerSpecial(int allowManagerSpecial) {
                this.allowManagerSpecial = allowManagerSpecial;
            }

            public int getAllowCleanCreation() {
                return allowCleanCreation;
            }

            public void setAllowCleanCreation(int allowCleanCreation) {
                this.allowCleanCreation = allowCleanCreation;
            }

            public int getAllowCleanMember() {
                return allowCleanMember;
            }

            public void setAllowCleanMember(int allowCleanMember) {
                this.allowCleanMember = allowCleanMember;
            }

            public void setMemberType(int memberType) {
                this.memberType = memberType;
            }
        }

        public static class GroupBean implements Serializable{
            private String background;
            private int commentCount;
            private String createTime;
            private int creationCount;
            private String description;
            private String domain;
            private long id;
            private int isExamine;
            private String keywords;
            private int memberCount;
            private String name;
            private int priority;
            private int visitCount;
            private long userId;

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getBackground() {
                return background;
            }

            public void setBackground(String background) {
                this.background = background;
            }

            public int getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(int commentCount) {
                this.commentCount = commentCount;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getCreationCount() {
                return creationCount;
            }

            public void setCreationCount(int creationCount) {
                this.creationCount = creationCount;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getIsExamine() {
                return isExamine;
            }

            public void setIsExamine(int isExamine) {
                this.isExamine = isExamine;
            }

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
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

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public int getVisitCount() {
                return visitCount;
            }

            public void setVisitCount(int visitCount) {
                this.visitCount = visitCount;
            }
        }

        public static class RecommendBean implements Serializable{

            private String name;
            private String nickName;
            private int have720;
            private int have1080;
            private String playUrl;
            private String cover;

            private String screenshot;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getHave720() {
                return have720;
            }

            public void setHave720(int have720) {
                this.have720 = have720;
            }

            public int getHave1080() {
                return have1080;
            }

            public void setHave1080(int have1080) {
                this.have1080 = have1080;
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

            public String getScreenshot() {
                return screenshot;
            }

            public void setScreenshot(String screenshot) {
                this.screenshot = screenshot;
            }
        }
    }
}
