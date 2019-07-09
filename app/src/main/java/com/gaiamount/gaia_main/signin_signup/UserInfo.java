package com.gaiamount.gaia_main.signin_signup;

import com.gaiamount.module_im.official.Im;

/**
 *haiyang-lu
 */
public class UserInfo {
    public long id;//用户id
    public int gender;//用户性别
    public int ulv;//用户等级
    public int type;//用户类型
    public int status;//用户状态
    public int isVip;//是否时ｖｉｐ
    public int vlv;//ｖｉｐ的等级
    public int vType;//ｖｉｐ的类型
    public int language;//语言
    public int source;//平台标识

    public String avatar;//用户头像
    public String nickName;//用户昵称
    public String realName;//用户真实姓名
    public String job;//工作职业
    public String domain;//个人域名
    public String signature;//个性签名
    public String address;//地址
    public String resume;//个人简介
    public String mobile;//绑定的手机号码
    public String email;//用户邮箱
    public String background;//用户背景
    public String description;//简介


    public int vipLevel;

    public String bankCard;
    public String bankName;
    public String bankUserName;
    public String bankBranch;
    public String fansCount;
    public int createCount;

    public int browseCount;
    public int circleCount;
    public int works_collectCount;
    public Im im;
    public int college_collect;
    public int noteCount;
    public int albumCount;
    public int material_collect;
    public int materialCount;
    public int collegeCount;

    public UserInfo() {

    }

    public UserInfo(long id, int gender, int ulv, int type, int status, int isVip, int vipLevel,int vlv, int vType, int language, int source,
                    String avatar, String nickName, String realName, String job, String domain,
                    String signature, String address, String resume, String mobile, String email, String bankCard, String bankName,
                    String bankUserName, String bankBranch, int createCount, Im im,int college_collect,String background,int noteCount,
                    int albumCount,int materialCount,int material_collect,int collegeCount,String description) {
        this.id = id;
        this.gender = gender;
        this.ulv = ulv;
        this.type = type;
        this.status = status;
        this.isVip = isVip;
        this.vipLevel = vipLevel;
        this.vlv = vlv;
        this.vType = vType;
        this.language = language;
        this.source = source;
        this.avatar = avatar;
        this.nickName = nickName;
        this.realName = realName;
        this.job = job;
        this.domain = domain;
        this.signature = signature;
        this.address = address;
        this.resume = resume;
        this.mobile = mobile;
        this.email = email;
        this.bankCard = bankCard;
        this.bankName = bankName;
        this.bankUserName = bankUserName;
        this.bankBranch = bankBranch;
        this.createCount = createCount;
        this.college_collect=college_collect;
        this.background=background;
        this.noteCount=noteCount;
        this.albumCount=albumCount;
        this.materialCount=materialCount;
        this.material_collect=material_collect;
        this.collegeCount=collegeCount;
        this.im  = im;
        this.description=description;
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", gender=" + gender +
                ", ulv=" + ulv +
                ", type=" + type +
                ", status=" + status +
                ", isVip=" + isVip +
                ", vlv=" + vlv +
                ", vType=" + vType +
                ", language=" + language +
                ", source=" + source +
                ", avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", realName='" + realName + '\'' +
                ", job='" + job + '\'' +
                ", domain='" + domain + '\'' +
                ", signature='" + signature + '\'' +
                ", address='" + address + '\'' +
                ", resume='" + resume + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", bankCard='" + bankCard + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankUserName='" + bankUserName + '\'' +
                ", background='" + background + '\'' +
                ", noteCount='" + noteCount + '\'' +
                ", albumCount='" + albumCount + '\'' +
                ", materialCount='" + materialCount + '\'' +
                ", material_collect='" + material_collect + '\'' +
                ", collegeCount='" + collegeCount + '\'' +
                ", im=" + im +
                ", description=" + description +

                '}';
    }
}
