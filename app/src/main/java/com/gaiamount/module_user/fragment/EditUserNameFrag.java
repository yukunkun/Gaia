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
import android.widget.ImageView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.util.UserUtils;

/**
 * Created by haiyang-lu on 16-3-11.
 * 更改用户名的碎片界面
 */
public class EditUserNameFrag extends Fragment {

    private EditText mEditText;
    private String nickName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_name_edit, null, true);
        initViews(view);
        return view;

    }

    /**
     * 初始化views
     */
    private void initViews(View view) {
        mEditText = (EditText) view.findViewById(R.id.editText);
        Button button = (Button) view.findViewById(R.id.done_button);
        ImageView iv = (ImageView) view.findViewById(R.id.imageview);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    /**
     * 确认并提交
     */
    public void done() {
        nickName = mEditText.getText().toString().trim();
        if ("".equals(nickName)) {
            GaiaApp.showToast("昵称不能为空");
            return;
        }
        //弹出提示框，询问用户是否真的打算这么做
        new AlertDialog.Builder(getActivity()).setTitle("真的要这么做么").setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startChange();
            }
        }).show();

    }

    private void startChange() {
        UserInfo userInfo = GaiaApp.getUserInfo();
        userInfo.nickName = nickName;
        //联网请求更改
        UserUtils.updateUserInfo(userInfo, getActivity());
    }
}
