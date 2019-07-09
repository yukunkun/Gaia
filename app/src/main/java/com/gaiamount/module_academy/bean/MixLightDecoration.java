package com.gaiamount.module_academy.bean;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by haiyang-lu on 16-5-4.
 */
public class MixLightDecoration extends RecyclerView.ItemDecoration{
    private int top;
    private int bollow;
    private int left;
    private int right;

    public MixLightDecoration(int top,int bollow,int left,int right) {
        this.top = top;
        this.bollow=bollow;
        this.left=left;
        this.right=right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        outRect.top=top;
        outRect.bottom=bollow;
        if(childLayoutPosition%2==0) {//偶数位
            outRect.right = left/2-2;
            outRect.left = left;
        } else {//奇数位
            outRect.left=right/2-2;
            outRect.right = right;
        }
    }
}
