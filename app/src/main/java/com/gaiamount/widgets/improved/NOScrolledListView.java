package com.gaiamount.widgets.improved;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by luxiansheng on 16/3/6.
 *
 */
public class NOScrolledListView extends ListView {
    public NOScrolledListView(Context context) {
        super(context);
    }

    public NOScrolledListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NOScrolledListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
