package com.gaiamount.module_creator.creater_circle.bean;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by haiyang-lu on 16-5-4.
 */
public class SpacesDecoration extends RecyclerView.ItemDecoration{
    private int space;

    public SpacesDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        outRect.top=space/4;
        outRect.bottom=space/4;
        if(childLayoutPosition%2==0) {//偶数位
            outRect.right = space/4;
            outRect.left = space/2;
        } else {//奇数位
            outRect.left=space/4;
            outRect.right = space/2;
        }
    }
}
