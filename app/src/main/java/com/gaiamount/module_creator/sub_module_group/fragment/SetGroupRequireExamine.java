package com.gaiamount.module_creator.sub_module_group.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.sub_module_group.activities.GroupEditActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-16.
 */
public class SetGroupRequireExamine extends Fragment {

    @Bind(R.id.set_group_need_examine)
    RadioButton mSetGroupNeedExamine;
    @Bind(R.id.set_group_not_need_examine)
    RadioButton mSetGroupNotNeedExamine;
    private GroupEditActivity mHostActivity;

    private int isExamine;
    private RadioGroup radioGroup;

    public static SetGroupRequireExamine newInstance() {
        SetGroupRequireExamine setGroupDescFrag = new SetGroupRequireExamine();
        return setGroupDescFrag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupEditActivity) {
            mHostActivity = (GroupEditActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_group_examine, container, false);
        ButterKnife.bind(this, view);
        radioGroup = (RadioGroup) view.findViewById(R.id.set_group_radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if(((RadioButton)(group.getChildAt(i))).isChecked()){
                        isExamine=i;
                        mHostActivity.setExamination(isExamine);
                        ((RadioButton)(group.getChildAt(i))).setChecked(false);
//                        mHostActivity.getSupportFragmentManager().beginTransaction().detach(SetGroupRequireExamine.this);
//                        mHostActivity.getSupportFragmentManager().popBackStack();
                    }
                }
            }
        });

        return view;
    }

    private void finishSelf() {
        FragmentManager supportFragmentManager = mHostActivity.getSupportFragmentManager();
        supportFragmentManager.beginTransaction().detach(SetGroupRequireExamine.this).commit();
        supportFragmentManager.popBackStack();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_finish, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_finish) {
//            //设置审核
//            mHostActivity.setExamination(isExamine);
//            finishSelf();
//        }
//        return super.onOptionsItemSelected(item);
//
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
