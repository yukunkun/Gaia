package com.gaiamount.module_creator.sub_module_group.beans;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-6-23.
 */
public class GroupAdmin implements Serializable{
    /**
     * 小组id
     */
    private long gid;

    /**
     * 小组成员id
     */
    private long mid;

    /**
     * 审核权限(0 不允许 1 允许)
     */
    private int exa;

    /**
     * 审创作权限(0 不允许 1 允许)
     */
    private int ccre;

    /**
     *  成员管理权限(0 不允许 1 允许)
     */
    private int mb;

    /**
     * 专辑管理权限(0 不允许 1 允许)
     */
    private int ms;

    private String avatar;
    private String nickName;

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

    public GroupAdmin() {

    }

    public GroupAdmin(long gid, long mid) {
        this.gid = gid;
        this.mid = mid;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public int getExa() {
        return exa;
    }

    public void setExa(int exa) {
        this.exa = exa;
    }

    public int getCcre() {
        return ccre;
    }

    public void setCcre(int ccre) {
        this.ccre = ccre;
    }

    public int getMb() {
        return mb;
    }

    public void setMb(int mb) {
        this.mb = mb;
    }

    public int getMs() {
        return ms;
    }

    public void setMs(int ms) {
        this.ms = ms;
    }

    @Override
    public String toString() {
        return "GroupAdmin{" +
                "gid=" + gid +
                ", mid=" + mid +
                ", exa=" + exa +
                ", ccre=" + ccre +
                ", mb=" + mb +
                ", ms=" + ms +
                ", avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
