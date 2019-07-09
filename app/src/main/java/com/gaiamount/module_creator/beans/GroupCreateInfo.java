package com.gaiamount.module_creator.beans;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-6-16.
 * 创建小组所需要的信息
 */
public class GroupCreateInfo implements Serializable{
    public String name;
    public String keywords;
    public int isExamine = 0;//默认不需要审核
    public String description;
    public String background;
}
