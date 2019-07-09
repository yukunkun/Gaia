package com.gaiamount.module_material.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gaiamount.R;

/**
 * Created by yukun on 16-9-28.
 */
public class OriRecyclerViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView recyclerView;
    public TextView textView;
    public OriRecyclerViewHolder(View itemView) {
        super(itemView);
        recyclerView= (RecyclerView) itemView.findViewById(R.id.ori_recyclerview);
        textView= (TextView) itemView.findViewById(R.id.material_more);
    }
}
