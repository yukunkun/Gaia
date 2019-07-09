package com.gaiamount.module_creator.fragment;

/**
 * Created by haiyang-lu on 16-6-6.
 */
public class PersonalCreatorFrag extends CreatorBaseFrag {
    @Override
    public String getFragmentTitle() {
        return "个人";
    }

    public static  PersonalCreatorFrag newInstance() {
        return new PersonalCreatorFrag();
    }
}
