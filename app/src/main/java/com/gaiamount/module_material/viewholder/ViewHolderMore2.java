package com.gaiamount.module_material.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.widgets.improved.NoScrolledGridView;

/**
 * Created by yukun on 16-8-2.
 */
public class ViewHolderMore2 extends RecyclerView.ViewHolder {
    public TextView textViewtitle;
    public NoScrolledGridView noScrolledGridView;
    public  TextView mTextViewMores;

    public ViewHolderMore2(View itemView) {
        super(itemView);
        textViewtitle= (TextView) itemView.findViewById(R.id.academy_title);
        noScrolledGridView= (NoScrolledGridView) itemView.findViewById(R.id.academy__recommend);
        mTextViewMores = (TextView) itemView.findViewById(R.id.material_more);
    }
}
