package com.gaiamount.module_player.dialogs;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-7-4.
 */
public class MyGroup implements Serializable{

    /**
     * name : 赵波，我的小组呢
     * description : 测试，都说是测试啦
     * isExamine : 0
     * id : 86
     * keywords : 测试
     * domain : null
     */

    private String name;
    private String description;
    private int isExamine;
    private int id;
    private String keywords;
    private String domain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsExamine() {
        return isExamine;
    }

    public void setIsExamine(int isExamine) {
        this.isExamine = isExamine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
