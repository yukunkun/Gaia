package com.gaiamount.module_creator.beans;

import com.gaiamount.module_creator.sub_module_group.constant.GroupPower;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-7-5.
 */
public class Info implements Serializable {
    public long gid;
    public int memberType;
    public GroupPower groupPower;

}
