package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_academy.bean.Contents;
import com.gaiamount.module_academy.bean.LessonInfo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-8-29.
 */
public class ExpandAdapter extends BaseExpandableListAdapter{
    private int selectedItem;
    private int groupPosition1;
    Context context;
    ArrayList<Contents> contentGroupList;
    ArrayList<ArrayList<LessonInfo>> lessonChildList;
    public ExpandAdapter(Context context, ArrayList<Contents> contentList,
                             ArrayList<ArrayList<LessonInfo>> lessonList){
        this.context=context;
        this.contentGroupList=contentList;
        this.lessonChildList=lessonList;
    }

    public void setSelectedItem(int groupPosition1, int selectedItem) {
        this.groupPosition1=groupPosition1;
        this.selectedItem = selectedItem;
    }
    @Override
    public int getGroupCount() {
        return contentGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lessonChildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return contentGroupList.get(groupPosition).getName();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lessonChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.expandable_group_item, null);
        TextView textViewContent= (TextView) convertView.findViewById(R.id.group_content);
        TextView textViewNum= (TextView) convertView.findViewById(R.id.group_num);
        //目录的数据适配
        if(contentGroupList.get(groupPosition).getName().length()>2){
            textViewContent.setText((contentGroupList.get(groupPosition).getName()).substring(2,contentGroupList.get(groupPosition).getName().length()));
        }else {
            textViewContent.setText(contentGroupList.get(groupPosition).getName());
        }

        String process = getContent(contentGroupList.get(groupPosition).getInd());
        textViewNum.setText(process);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.expandable_child_item, null);
        TextView textView= (TextView) convertView.findViewById(R.id.child_content);
        //课程数据适配
        textView.setText(lessonChildList.get(groupPosition).get(childPosition).getName());
        if(groupPosition==groupPosition1&&childPosition==selectedItem){
            textView.setTextColor(context.getResources().getColor(R.color.color_ff5773));
        }else {
            textView.setTextColor(context.getResources().getColor(R.color.text_666));
        }
        return convertView;
    }

    public String getContent(int integer){
        switch (integer){
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "七";
            case 8:
                return "八";
            case 9:
                return "九";
            case 10:
                return "十";
            case 11:
                return "十一";
            case 12:
                return "十二";
            case 13:
                return "十三";
            case 14:
                return "十四";
            case 15:
                return "十五";
            case 16:
                return "十六";
            case 17:
                return "十七";
            case 18:
                return "十八";
            case 19:
                return "十九";
            case 20:
                return "二十";
            default:
                return "更多";
        }
    }

}
