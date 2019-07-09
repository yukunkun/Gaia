package com.gaiamount.module_workpool.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by haiyang-lu on 16-5-4.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration{
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        if(childLayoutPosition%2==0) {//偶数位
            outRect.right = space/2;
        } else {//奇数位
            outRect.left = space/2;
        }
    }
}
