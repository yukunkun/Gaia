package com.gaiamount.module_down_up_load.upload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gaiamount.R;

/**
 * Created by haiyang-lu on 16-3-24.
 * 作品信息的适配器
 */
public class WorkInfoAdpater extends BaseAdapter {
    private String[] names;
    private String[] data;
    private final LayoutInflater inflater;

    public WorkInfoAdpater(Context context,String[] names,String[] data) {
        this.names = names;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = inflater.inflate(R.layout.item_work_info_frag, null, true);
        TextView name = (TextView) item.findViewById(R.id.name);
        TextView value = (TextView) item.findViewById(R.id.value);
        name.setText(names[position]);
        value.setText(data[position]);
        return item;
    }
}
