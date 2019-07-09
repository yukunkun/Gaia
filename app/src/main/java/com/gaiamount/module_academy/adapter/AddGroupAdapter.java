package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_academy.bean.GroupInfo;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-19.
 */
public class AddGroupAdapter extends BaseAdapter {
    ArrayList<GroupInfo> arrayList;
    Context context;

    public AddGroupAdapter( Context context,ArrayList<GroupInfo> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.group_listview_item, null);
        TextView textView= (TextView) convertView.findViewById(R.id.add_group_news);
        textView.setText(arrayList.get(position).getName());
        return convertView;
    }
}
