package com.gaiamount.module_academy.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gaiamount.R;

/**
 * Created by yukun on 16-8-2.
 */
public class ViewHolderText extends RecyclerView.ViewHolder {
    public TextView textView;
    public ViewHolderText(View itemView) {
        super(itemView);
        textView= (TextView) itemView.findViewById(R.id.textview_more);
    }
}
