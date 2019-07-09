package com.gaiamount.module_user.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.module_user.user_edit.UserInfoEditActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.util.UIUtils;
import com.gaiamount.util.UserUtils;

/**
 * Created by haiyang-lu on 16-4-13.
 *
 */
public class ChooseJobFrag extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_choose_job,null);
        initView(layoutView);
        return layoutView;
    }

    private void initView(View view) {
        final ListView jobList = (ListView) view.findViewById(R.id.job_list);
        final String[] jobListStr= getResources().getStringArray(R.array.job_list);
        jobList.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,jobListStr));
        jobList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==parent.getLastVisiblePosition()) {
                    //关掉自己
                    ChooseJobFrag.this.dismiss();
                    //进入自定义界面
                    Intent intent = new Intent(getActivity(), UserInfoEditActivity.class);
                    intent.putExtra("theme", UIUtils.ActionBarTheme);
                    intent.putExtra("kind", UserInfoEditActivity.JOB);
                    startActivity(intent);
                } else {
                    UserInfo userInfo = GaiaApp.getUserInfo();
                    userInfo.job = jobListStr[position];
                    UserUtils.updateUserInfo(userInfo,getActivity());
                    //关掉自己
                    ChooseJobFrag.this.dismiss();
                }
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getString(R.string.choose_job));
        return dialog;
    }
}
