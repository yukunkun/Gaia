package com.gaiamount.module_im.secret_chat.bean;

/**
 * Created by yukun on 16-7-15.
 */
public class ContactInfo {

    /**
     * uid : 3105
     * id : 5
     * avatar : static/img/avatar.jpg
     * nickName : zzb
     */

    private String uid;
    private int id;
    private String avatar;
    private String nickName;
    private String sortLetters;
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getSortLetters()
    {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters)
    {
        this.sortLetters = sortLetters;
    }
}
