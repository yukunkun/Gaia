package com.gaiamount.module_academy.bean;

/**
 * Created by yukun on 16-8-4.
 */
public class EventDetailInfo {
    public String intr;
    public String sofeware;
    public long id;
    public long cid;
    public int t;
    public String author;
    public int isLearning;
    public int type;
    public int allowFree;
    public EventDetailInfo(String Intr, String sofeware, long id, int t,long cid,String author,int isLearning,int allowFree,int type){
        this.intr=Intr;
        this.sofeware=sofeware;
        this.id=id;
        this.cid=cid;
        this.t=t;
        this.author=author;
        this.isLearning=isLearning;
        this.allowFree=allowFree;
        this.type=type;
    }
}
