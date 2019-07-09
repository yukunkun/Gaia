package com.gaiamount.module_user.bean;

/**
 * Created by xiaoyu-liu on 16-2-15.
 */
public class CountryCode {
    /**
     * Country name
     */
    String mName;
    /**
     * Country name
     */
    String mCode;
    /**
     * sort key for country
     */
    private String sortKey;

    /**
     * @param name Country name
     * @param code Country name
     */
    public CountryCode(String name, String code){
        mName = name;
        mCode = code;
    }

    public String GetName(){return mName;}
    public String GetCode(){return mCode;}
    public String getSortKey() {
        return sortKey;
    }

    /**
     * get first char of sort key
     * if is a letter return if
     * else return '#'
     *
     * @param sortKeyString sort key
     * @return letter or #
     */
    private String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }
}
