package com.gaiamount.module_creator.sub_module_group.constant;

import com.gaiamount.module_creator.GroupCreationsActivity;

import java.io.Serializable;

/**
 * Created by haiyang-lu on 16-7-1.
 * 小组的成员权限
 */
public class GroupPower implements Serializable{

    public GroupPower(int allowExamine, int allowManagerSpecial, int allowCleanCreation, int allowCleanMember) {
        this.allowExamine = allowExamine;
        this.allowManagerSpecial = allowManagerSpecial;
        this.allowCleanCreation = allowCleanCreation;
        this.allowCleanMember = allowCleanMember;
    }

    /**
     * allowExamine 是否允许审核成员能否加入的权限
     */
    public  int allowExamine;

    /**
     * 是否允许编辑小组中专辑信息（包括增删改）的权限
     */
    public  int allowManagerSpecial;

    /**
     * 是否允许清理小组视频的权限
     * @see GroupCreationsActivity
     */
    public  int allowCleanCreation;

    /**
     * 是否允许删除小组组员的权限
     * @see com.gaiamount.module_creator.sub_module_group.activities.GroupMemberActivity
     */
    public  int allowCleanMember;
}
