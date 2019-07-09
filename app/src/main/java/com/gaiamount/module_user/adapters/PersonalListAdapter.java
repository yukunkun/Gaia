package com.gaiamount.module_user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;

/**
 * Created by haiyang-lu on 16-4-26.
 */
public class PersonalListAdapter extends BaseAdapter {
    private String[] item_titles;
    private int[] item_title_icons;
    private final LayoutInflater mInflater;

    public PersonalListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        item_titles = context.getResources().getStringArray(R.array.personal_list_items);
        item_title_icons = new int[]
                {
//                        R.mipmap.ic_personal_dairy,
                        R.mipmap.ic_personal_create,
//                        R.mipmap.ic_personal_special,
//                        R.mipmap.ic_personal_circle,
                        R.mipmap.ic_personal_collection
                };
    }

    @Override
    public int getCount() {
        return item_titles.length;
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
        //实例化ViewHolder
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_personal, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设值
        holder.setValue(position);
        return convertView;
    }

    class ViewHolder {
        ImageView itemIcon;
        TextView itemText;

        public ViewHolder(View convertView) {
            itemIcon = (ImageView) convertView.findViewById(R.id.personal_page_item_icon);
            itemText = (TextView) convertView.findViewById(R.id.personal_page_item_text);
        }

        public void setValue(int position) {
            itemIcon.setImageResource(item_title_icons[position]);
            itemText.setText(item_titles[position]);
        }
    }
}