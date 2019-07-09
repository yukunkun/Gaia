package com.gaiamount.module_circle.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.gaiamount.module_circle.fragment.adapter.AttentAdapter;
import com.gaiamount.module_circle.fragment.basefragment.CircleFragment;

/**
 * Created by yukun on 17-1-4.
 */
public class AttentionFragment extends CircleFragment {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private AttentAdapter attentAdapter;
    private LinearLayoutManager layoutManager;
    private Context context;

    @Override
    public void getView(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, Context context) {
        this.recyclerView=recyclerView;
        this.refreshLayout=refreshLayout;
        this.context=context;
    }

    @Override
    protected void getInfo() {

    }

    @Override
    protected void setAdapter() {
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        attentAdapter = new AttentAdapter();
        recyclerView.setAdapter(attentAdapter);
    }

    @Override
    protected void setListener() {

    }

}
