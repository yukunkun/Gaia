package com.gaiamount.module_academy.bean;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-31.
 */
public class EventExpanbableInfo {
    public ArrayList<Contents> contentList;
    public ArrayList<ArrayList<LessonInfo>> lessonList;
    public int childPosition;
    public int groupPosition;

    public EventExpanbableInfo(ArrayList<Contents> contentList, ArrayList<ArrayList<LessonInfo>> lessonListContext, int childPosition, int groupPosition) {
        this.contentList = contentList;
        this.lessonList = lessonListContext;
        this.childPosition = childPosition;
        this.groupPosition = groupPosition;
    }
}
