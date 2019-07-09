package com.gaiamount.widgets.common_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by haiyang-lu on 16-6-27.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    Context mContext;

    List<T> mData;

    private int mItemLayoutId = -1;

    public CommonAdapter(Context context, int itemLayoutId, List<T> data) {
        checkParams(context,itemLayoutId,data);
        mContext = context;
        mItemLayoutId = itemLayoutId;
        mData = data;
    }

    public Context getContext() {
        return mContext;
    }

    private void checkParams(Context context, int itemLayoutId, List<T> data) {
        if (context == null || itemLayoutId < 0 || data == null) {
            throw new RuntimeException(
                    "context == null || itemLayoutResId < 0 || dataSource == null, please check your params");
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取viewholder
        CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(mContext,convertView,parent,mItemLayoutId);

        //填充数据
        fillItemData(viewHolder,position,getItem(position));

        return viewHolder.getContentView();
    }

    /**
     * 用户必须覆写该方法来讲数据填充到视图中
     *
     * @param viewHolder 通用的ViewHolder, 里面会装载listview,
     *            gridview等组件的每一项的视图，并且缓存其子view
     * @param item 数据源的第position项数据
     */
    protected abstract void fillItemData(CommonViewHolder viewHolder, int position, T item);


}
