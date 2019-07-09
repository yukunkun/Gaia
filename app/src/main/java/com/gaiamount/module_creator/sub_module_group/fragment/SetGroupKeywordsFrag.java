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
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.beans.GroupCreateInfo;
import com.gaiamount.module_creator.sub_module_group.activities.GroupCreateActivity;
import com.gaiamount.module_creator.sub_module_group.activities.GroupEditActivity;
import com.gaiamount.util.UIUtils;
import com.gaiamount.widgets.improved.ChipsMultiAutoCompleteTextview;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-15.
 */
public class SetGroupKeywordsFrag extends Fragment {
    @Bind(R.id.fragment_set_group_keywords_text)
    EditText mFragmentSetGroupKeywordsText;
//    ChipsMultiAutoCompleteTextview mFragmentSetGroupKeywordsText;

    private GroupCreateInfo mGroupBean;
    private GroupCreateActivity mGroupCreateActivity;
    private GroupEditActivity mGroupEditActivity;
    private Activity hostActivity;


    public static SetGroupKeywordsFrag newInstance(GroupCreateInfo groupBean) {
        SetGroupKeywordsFrag setGroupKeywordsFrag = new SetGroupKeywordsFrag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("groupBean", groupBean);
        setGroupKeywordsFrag.setArguments(bundle);
        return setGroupKeywordsFrag;
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
            setHasOptionsMenu(true);
        }

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_group_keywords, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mGroupCreateActivity != null) {
            mGroupCreateActivity.setToolbarTitle("设置关键字");
        }
        if (mGroupEditActivity !=null) {
            mGroupEditActivity.setToolbarTitle("设置关键字");
        }
    }

    public GroupCreateInfo getGroupBean() {
        if (mGroupCreateActivity != null) {
            mGroupCreateActivity.currentFragment = -1;
        }
        //隐藏输入法
        UIUtils.hideSoftInputMethod(getActivity(), mFragmentSetGroupKeywordsText);
        mGroupBean.keywords = mFragmentSetGroupKeywordsText.getText().toString();
        return mGroupBean;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_finish, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            if (mFragmentSetGroupKeywordsText.getText().toString().equals("")) {
                GaiaApp.showToast("小组关键字不能为空");
                return true;
            }
            mGroupEditActivity.setGroupKeywords(mFragmentSetGroupKeywordsText.getText().toString());
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
