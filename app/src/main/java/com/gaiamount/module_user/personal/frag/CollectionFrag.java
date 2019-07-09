package com.gaiamount.module_user.personal.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaiamount.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-7-21.
 */
public class CollectionFrag extends Fragment {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public static CollectionFrag newInstance() {

        Bundle args = new Bundle();

        CollectionFrag fragment = new CollectionFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_coll, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.setAdapter(new MAdpater());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class MAdpater extends RecyclerView.Adapter<MViewHolder> {

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getActivity(), R.layout.item_person_coll, null);
            MViewHolder holder = new MViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 40;
        }
    }

    class MViewHolder extends RecyclerView.ViewHolder {

        public MViewHolder(View itemView) {
            super(itemView);
        }
    }
}
