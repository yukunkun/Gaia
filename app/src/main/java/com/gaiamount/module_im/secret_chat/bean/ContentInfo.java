package com.gaiamount.module_im.secret_chat.bean;

/**
 * Created by yukun on 16-7-18.
 */
public class ContentInfo {
    private String name;
    private String body;
    private String uid;
    private String avatar;
    private String imageUri;
    private String path;
    private  int Type;
    private String otherNickName;
    private String time;
    private String group;
    private String otherId;
    private String chose;
    private String cishu;
    private int unReadCount;
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getCishu() {
        return cishu;
    }

    public void setCishu(String cishu) {
        this.cishu = cishu;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getChose() {
        return chose;
    }

    public void setChose(String chose) {
        this.chose = chose;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOtherNickName() {
        return otherNickName;
    }

    public void setOtherNickName(String otherNickName) {
        this.otherNickName = otherNickName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String  getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Contents{" +
                "name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", uid='" + uid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", path='" + path + '\'' +
                ", otherNickName='" + otherNickName + '\'' +
                ", time='" + time + '\'' +
                ", group='" + group + '\'' +
                ", otherId='" + otherId + '\'' +
                ", chose='" + chose + '\'' +
                '}';
    }
}
