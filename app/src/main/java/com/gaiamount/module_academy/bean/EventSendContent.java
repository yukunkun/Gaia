package com.gaiamount.module_academy.bean;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-30.
 */
public class EventSendContent {
    public ArrayList<Contents> contentList;
    public ArrayList<ArrayList<LessonInfo>> lessonList;
    public String responseString;

    public EventSendContent(ArrayList<Contents> contentList, ArrayList<ArrayList<LessonInfo>> lessonList,String responseString) {
        this.contentList = contentList;
        this.lessonList = lessonList;
        this.responseString=responseString;
    }

}
