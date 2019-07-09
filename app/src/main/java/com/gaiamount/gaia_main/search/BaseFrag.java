package com.gaiamount.gaia_main.search;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiamount.R;

import java.util.List;

/**
 * Created by haiyang-lu on 16-7-19.
 */
public abstract class BaseFrag extends Fragment {
    public LinearLayout lastSelectedView;

    public abstract void update(List<?> list);

    public void changeStyle(LinearLayout view) {
        if (lastSelectedView != null &&lastSelectedView.getChildAt(0) instanceof TextView) {
            TextView child = (TextView) lastSelectedView.getChildAt(0);
            child.setTextColor(getResources().getColor(R.color.text_b1b1b1));
            child.setBackgroundResource(R.color.transparent);
        }
        TextView textViewChild = (TextView) view.getChildAt(0);
        textViewChild.setTextColor(Color.WHITE);
        textViewChild.setBackgroundResource(R.drawable.shape_tv_radius12_drakgray);
        lastSelectedView = view;
    }
}
