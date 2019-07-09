package com.gaiamount.module_academy.bean;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-22.
 */
public class AcademyAll {
    private String listName;
    private ArrayList<AcademyInfo> academyInfos;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public ArrayList<AcademyInfo> getAcademyInfos() {
        return academyInfos;
    }

    public void setAcademyInfos(ArrayList<AcademyInfo> academyInfos) {
        this.academyInfos = academyInfos;
    }
}
