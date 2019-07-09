package com.gaiamount.module_user.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.util.UserUtils;

/**
 * Created by haiyang-lu on 16-4-14.
 * 选择所在城市
 */
public class EditCityFrag extends Fragment {
    private EditText mEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_choose_city,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mEditText = (EditText) view.findViewById(R.id.editText);
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    private void done() {
        //弹出提示框，询问用户是否真的打算这么做
        new AlertDialog.Builder(getActivity()).setTitle("真的要这么做么").
                setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确定的情况下，联网请求服务
                startChange();
            }
        }).show();
    }

    private void startChange() {
        //将更新的用户信息放入userInfo对象中
        String address = mEditText.getText().toString().trim();
        UserInfo userInfo = GaiaApp.getUserInfo();
        userInfo.address = address;
        //调用接口
        UserUtils.updateUserInfo(userInfo, getActivity());
    }
}
