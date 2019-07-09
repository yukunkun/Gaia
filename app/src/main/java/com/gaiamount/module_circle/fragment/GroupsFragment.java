package com.gaiamount.module_circle.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.gaiamount.module_circle.fragment.basefragment.CircleFragment;

/**
 * Created by yukun on 17-1-4.
 */
public class GroupsFragment extends CircleFragment {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    @Override
    public void getView(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, Context context) {
        this.recyclerView=recyclerView;
        this.refreshLayout=refreshLayout;
    }

    @Override
    protected void setAdapter() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void getInfo() {

    }




}
