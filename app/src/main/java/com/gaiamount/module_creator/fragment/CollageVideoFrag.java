package com.gaiamount.module_creator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by haiyang-lu on 16-6-27.
 */
public class CollageVideoFrag extends Fragment {
    public static CollageVideoFrag newInstance(long gid) {

        Bundle args = new Bundle();

        CollageVideoFrag fragment = new CollageVideoFrag();
        fragment.setArguments(args);
        return fragment;
    }
}
