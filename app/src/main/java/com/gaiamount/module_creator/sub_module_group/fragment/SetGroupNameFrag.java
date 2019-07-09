package com.gaiamount.module_creator.sub_module_group.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.beans.GroupCreateInfo;
import com.gaiamount.module_creator.sub_module_group.activities.GroupCreateActivity;
import com.gaiamount.module_creator.sub_module_group.activities.GroupEditActivity;
import com.gaiamount.util.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-15.
 */
public class SetGroupNameFrag extends Fragment {

    @Bind(R.id.fragment_set_group_name_edit)
    EditText groupNameEdit;

    private GroupCreateInfo mGroupBean;
    private Activity hostActivity;
    private GroupCreateActivity mGroupCreateActivity;
    private GroupEditActivity mGroupEditActivity;


    public static SetGroupNameFrag newInstance(GroupCreateInfo groupBean) {
        SetGroupNameFrag setGroupNameFrag = new SetGroupNameFrag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("groupBean", groupBean);
        setGroupNameFrag.setArguments(bundle);
        return setGroupNameFrag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupCreateActivity) {
            mGroupCreateActivity = (GroupCreateActivity) context;
            hostActivity = mGroupCreateActivity;
        }

        if (context instanceof GroupEditActivity) {
            mGroupEditActivity = (GroupEditActivity) context;
            hostActivity = mGroupEditActivity;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupBean = (GroupCreateInfo) getArguments().getSerializable("groupBean");

        if (hostActivity instanceof GroupEditActivity) {
            //显示菜单
            setHasOptionsMenu(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_group_name, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mGroupCreateActivity != null) {
            mGroupCreateActivity.setToolbarTitle("小组名称");
        }
        if (mGroupEditActivity !=null) {
            mGroupEditActivity.setToolbarTitle("小组名称");
        }

    }

    public GroupCreateInfo getGroupBean() {
        if (mGroupCreateActivity != null) {
            mGroupCreateActivity.currentFragment = -1;
            //隐藏输入法
            UIUtils.hideSoftInputMethod(getActivity(), groupNameEdit);
            mGroupBean.name = groupNameEdit.getText().toString();
            return mGroupBean;
        } else {
            return null;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_finish, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            if (groupNameEdit.getText().toString().equals("")) {
                GaiaApp.showToast("小组名称不能为空");
                return true;
            }
            mGroupEditActivity.setGroupName(groupNameEdit.getText().toString());
        } else if (item.getItemId() == android.R.id.home) {
            mGroupEditActivity.getSupportFragmentManager().beginTransaction().detach(this);
            mGroupEditActivity.getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
