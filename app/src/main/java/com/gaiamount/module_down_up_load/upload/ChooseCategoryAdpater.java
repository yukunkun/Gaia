package com.gaiamount.module_down_up_load.upload;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_down_up_load.upload.upload_bean.ChooseCategoryItem;

import java.util.List;

/**
 * Created by haiyang-lu on 16-3-24.
 * 选择分类页面的适配器
 */
public class ChooseCategoryAdpater extends BaseAdapter {
    private Context context;
    private List<ChooseCategoryItem> mList;

    private String[] mType;

    public ChooseCategoryAdpater(Context context, List<ChooseCategoryItem> list) {

        this.context = context;
        mList = list;
    }

    public void setType(String type) {
        mType = type.split(",");
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = View.inflate(context, R.layout.item_choose_category, null);
        TextView checkedTextView = (TextView) item.findViewById(R.id.checked_tv);
        CheckBox checkBox = (CheckBox) item.findViewById(R.id.checked_cb);

        ChooseCategoryItem chooseCategoryItem = mList.get(position);
        checkedTextView.setText(chooseCategoryItem.name);

        if (mType!=null) {
            for (int i=0;i<mType.length;i++) {
                if (mType[i].equals(chooseCategoryItem.name)) {
                    chooseCategoryItem.isChecked = true;
                }
            }
        }

        if (chooseCategoryItem.isChecked) {
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }

        return item;
    }
}
