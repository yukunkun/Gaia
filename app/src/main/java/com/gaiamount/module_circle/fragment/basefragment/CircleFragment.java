package com.gaiamount.module_circle.fragment.basefragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaiamount.R;

/**
 * Created by yukun on 17-1-4.
 */
public abstract class CircleFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.my_lesson_fragment, null);
        init(inflate);
        getView(refreshLayout,recyclerView,getContext());
        getInfo();
        setAdapter();
        setListener();
        moreMethod();
        return inflate;
    }

    public void moreMethod() {

    }

    private void init(View inflate) {
        refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swiplayout);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_lesson);
    }

    public abstract void getView(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, Context context);

    protected abstract void getInfo();

    protected abstract void setAdapter();

    protected abstract void setListener();

}
