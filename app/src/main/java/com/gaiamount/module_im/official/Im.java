package com.gaiamount.module_im.official;

/**
 * Created by haiyang-lu on 16-5-12.
 */
public class Im {

    /**
     * isDelete : 0
     * createTime : 2016-05-12 09:50:14
     * username : helloworld
     * nickname :
     * id : 1688
     * params : {"password":"1688","nickname":null,"username":"helloworld"}
     * password : 1688
     * upDateTime : 2016-05-12 09:50:14
     */

    private int isDelete;
    private String createTime;
    private String username;
    private String nickname;
    private long userId;
    private String params;
    private String password;
    private String upDateTime;

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUpDateTime() {
        return upDateTime;
    }

    public void setUpDateTime(String upDateTime) {
        this.upDateTime = upDateTime;
    }

    @Override
    public String toString() {
        return "Im{" +
                "isDelete=" + isDelete +
                ", createTime='" + createTime + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", id=" + userId +
                ", params='" + params + '\'' +
                ", password='" + password + '\'' +
                ", upDateTime='" + upDateTime + '\'' +
                '}';
    }
}
