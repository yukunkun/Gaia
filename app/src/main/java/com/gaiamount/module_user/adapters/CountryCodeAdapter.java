package com.gaiamount.module_user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gaiamount.module_user.bean.CountryCode;
import com.gaiamount.R;

import java.util.ArrayList;

/**
 * Created by xiaoyu-liu on 16-2-15.
 */

public class CountryCodeAdapter extends BaseAdapter implements SectionIndexer {
    Context mContext = null;
    LayoutInflater mInflater = null;
    ArrayList<CountryCode> mCountryCodes = null;

    public CountryCodeAdapter(Context con, ArrayList<CountryCode> list){
        mContext = con;
        mInflater = LayoutInflater.from(con);
        mCountryCodes = list;
    }

    @Override
    public int getCount() {
        return mCountryCodes.size();
    }

    @Override
    public Object getItem(int i) {
        return mCountryCodes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        final CountryCode mContent = mCountryCodes.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.country_picker_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.listview_title);
            viewHolder.tvKey=(TextView) view.findViewById(R.id.sort_key);
            viewHolder.tvPhone=(TextView) view.findViewById(R.id.listview_phone);
            viewHolder.sortKeyLayout = (LinearLayout) view.findViewById(R.id.sort_key_layout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        viewHolder.tvTitle.setText(mContent.GetName());
        viewHolder.tvPhone.setText(mContent.GetCode());
        if (position == getPositionForSection(section)) {
            viewHolder.tvKey.setText(mContent.getSortKey());
            viewHolder.sortKeyLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.sortKeyLayout.setVisibility(View.GONE);
        }

        return view;
    }

    final static class ViewHolder {
        TextView tvKey;
        TextView tvTitle;
        TextView tvPhone;
        LinearLayout sortKeyLayout;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mCountryCodes.get(i).getSortKey();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    public int getSectionForPosition(int position) {
        return mCountryCodes.get(position).getSortKey().charAt(0);
    }
}
