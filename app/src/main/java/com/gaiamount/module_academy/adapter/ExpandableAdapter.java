package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_academy.bean.Contents;
import com.gaiamount.module_academy.bean.LessonInfo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-8-3.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {
    Context context;
    int isLearning,allowFree,type;
    ArrayList<Contents> contentGroupList;
    ArrayList<ArrayList<LessonInfo>> lessonChildList;

    public ExpandableAdapter(Context context, int isLearning, int allowFree, ArrayList<Contents> contentGroupList, ArrayList<ArrayList<LessonInfo>> lessonChildList) {
        this.context = context;
        this.isLearning = isLearning;
        this.allowFree = allowFree;
        this.contentGroupList = contentGroupList;
        this.lessonChildList = lessonChildList;
    }

    public void updateAdapter(int isLearning, int allowFree,ArrayList<Contents> contentGroupList, ArrayList<ArrayList<LessonInfo>> lessonChildList,int type){
        this.isLearning = isLearning;
        this.allowFree = allowFree;
        this.type=type;
        this.contentGroupList = contentGroupList;
        this.lessonChildList = lessonChildList;
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
        //目录的数据适配;
        if(contentGroupList.get(groupPosition).getName().length()>3){
            textViewContent.setText((contentGroupList.get(groupPosition).getName()).substring(2,contentGroupList.get(groupPosition).getName().length()));
        }else /*if (contentGroupList.size()>0)*/{
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
        TextView textView1= (TextView) convertView.findViewById(R.id.try_watch);
        CircleImageView imageViewSpeed= (CircleImageView) convertView.findViewById(R.id.img_learn_speed);
        //课程数据适配
        textView.setText(lessonChildList.get(groupPosition).get(childPosition).getName());
        int len = lessonChildList.get(groupPosition).get(childPosition).getLen();
        int watchLen = lessonChildList.get(groupPosition).get(childPosition).getWatchLen();
        if (len!=0){
            if(isLearning!=0){
                double integer = (double) watchLen/len;
                if(integer==1){
                    imageViewSpeed.setImageResource(R.mipmap.collage_ic_cle_2);
                }else if(integer==0){
                    imageViewSpeed.setImageResource(R.mipmap.ic_time_slid);
                }else {
                    imageViewSpeed.setImageResource(R.mipmap.collage_ic_cle_1);
                }
            }
        }else {
            imageViewSpeed.setImageResource(R.mipmap.ic_time_slid);
        }
        int isPublic = lessonChildList.get(groupPosition).get(childPosition).getIsPublic();
        //判断显示免费与试看的逻辑
        if(type==0){
            if(isLearning==1||isLearning==2){
                textView1.setText("");
            }else if(isLearning==0){
                if(allowFree==1){
                    textView1.setText("");
                }else {
                    if(isPublic==0){
                        textView1.setText(context.getResources().getString(R.string.try_watch));
                    }else {
                        textView1.setText(context.getResources().getString(R.string.for_free));
                        textView1.setTextColor(context.getResources().getColor(R.color.color_009944));
                    }
                }
            }
        }else if(type==1){
            textView1.setText("");
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
