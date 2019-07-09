package com.gaiamount.module_academy.viewholder;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.widgets.improved.NoScrolledGridView;

import org.w3c.dom.Text;

/**
 * Created by yukun on 16-8-2.
 */
public class ViewHolderMore extends RecyclerView.ViewHolder {
    public TextView textViewtitle;
    public NoScrolledGridView noScrolledGridView;
    public TextView mTextViewMore;

    public ViewHolderMore(View itemView) {
        super(itemView);
        textViewtitle= (TextView) itemView.findViewById(R.id.academy_title);
        noScrolledGridView= (NoScrolledGridView) itemView.findViewById(R.id.academy__recommend);
        mTextViewMore = (TextView) itemView.findViewById(R.id.material_more);
    }
}
