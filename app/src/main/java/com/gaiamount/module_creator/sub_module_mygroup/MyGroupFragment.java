package com.gaiamount.module_creator.sub_module_mygroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_creator.CreatorActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-7-18.
 * 我的小组列表片段(包括我创建的，我管理的，我加入的）
 */
public class MyGroupFragment extends Fragment {

    public static final String MY_GROUP_INFOS = "my_group_infos";
    public static final String POS = "pos";
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.empty_hint)
    LinearLayout mEmptyHint;
    @Bind(R.id.goGroup)
    Button mGoGroup;
    @Bind(R.id.empty_hint_text)
    TextView mEmptyHintText;
    private List<MyGroupInfo> mList;
    private int mPos;

    public static MyGroupFragment newInstance(ArrayList<MyGroupInfo> myGroupInfos, int pos) {

        Bundle args = new Bundle();
        args.putSerializable(MY_GROUP_INFOS, myGroupInfos);
        args.putInt(POS, pos);
        MyGroupFragment fragment = new MyGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = (List<MyGroupInfo>) getArguments().getSerializable(MY_GROUP_INFOS);
        mPos = getArguments().getInt(POS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_group, container, false);
        ButterKnife.bind(this, view);
        if (mList.size() > 0) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(new MyGroupListAdapter(getActivity(), mList));
        } else {
            mEmptyHint.setVisibility(View.VISIBLE);
            mGoGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), CreatorActivity.class));
                }
            });
            if (mPos==0) {
                mEmptyHintText.setText(R.string.own_group_empty);
            }else if (mPos==1) {
                mEmptyHintText.setText(R.string.manage_group_empty);
            }else {
                mEmptyHintText.setText(R.string.joined_group_empty);
            }

        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
