package com.gaiamount.module_creator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaiamount.R;
import com.gaiamount.module_creator.fragment.present.OrgBean;
import com.gaiamount.module_creator.fragment.present.OrginAdapter;
import com.gaiamount.module_creator.fragment.present.OrginPresent;
import com.gaiamount.module_creator.fragment.present.OriFragImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-6.
 */
public class OrganizationCreatorFrag extends CreatorBaseFrag implements OriFragImpl {
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private OrginPresent orginPresent;
    private int pi=1;
    List<OrgBean> orgBeens=new ArrayList<>();
    private OrginAdapter orginAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public String getFragmentTitle() {
        return "机构";
    }

    public static OrganizationCreatorFrag newInstance() {
        return new OrganizationCreatorFrag();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orgin_fragment, null);
        ButterKnife.bind(this, view);
        orginPresent = new OrginPresent(this,getContext());
        orginPresent.getInfo(pi);
        setAdapter();
        setListener();
        return view;
    }

    @Override
    public void getInfo(List<OrgBean> orgBeen) {
        orgBeens.addAll(orgBeen);
        orginAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void setAdapter() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(linearLayoutManager);
        orginAdapter = new OrginAdapter(getContext(),orgBeens);
        recyclerview.setAdapter(orginAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setListener() {
        recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == linearLayoutManager.getItemCount()-1) {
                    //加载更多
                    pi=pi+1;
                    orginPresent.getInfo(pi);
                }
            }
        });
        //刷新的监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                orgBeens.clear();
                pi=1;
                orginPresent.getInfo(pi);
            }
        });
    }
}
