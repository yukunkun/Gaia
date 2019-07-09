package com.gaiamount.widgets.recyc;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by haiyang-lu on 16-7-20.
 */
public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {
    int space;

    public RecyclerItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
//        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
//            outRect.top = 0;
//            outRect.bottom = 0;
//        } else {
//            outRect.top = space / 2;
//            outRect.bottom = space / 2;
//        }
        outRect.top = space / 2;
        outRect.bottom = space / 2;

    }
}