package com.gaiamount.module_im.new_friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gaiamount.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-10-19.
 */
public class NoScrolListAdapter extends BaseAdapter {
    Context context;

    public NoScrolListAdapter(Context context) {
        this.context=context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.no_listview,null);
        CircleImageView imageView= (CircleImageView) convertView.findViewById(R.id.circle_head);
        TextView textViewName= (TextView) convertView.findViewById(R.id.action_name);
        TextView textViewJiaRu= (TextView) convertView.findViewById(R.id.action_jiaru);
        TextView textViewGroupName= (TextView) convertView.findViewById(R.id.action_group_name);
        TextView textViewPass= (TextView) convertView.findViewById(R.id.action_pass);
        TextView textViewRefuse= (TextView) convertView.findViewById(R.id.action_refuse);

        return convertView;
    }

}
