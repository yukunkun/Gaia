package com.gaiamount.module_player.dialogs;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;

/**
 * Created by haiyang-lu on 16-7-2.
 */
public class DataBean extends BaseObservable {
    private String title;
    private ArrayList<MyGroup> contentList;

    public DataBean() {

    }

    public DataBean(String title, ArrayList<MyGroup> contentList) {
        this.title = title;
        this.contentList = contentList;
    }


    @Bindable
    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public ArrayList<MyGroup> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<MyGroup> contentList) {
        this.contentList = contentList;
    }
}