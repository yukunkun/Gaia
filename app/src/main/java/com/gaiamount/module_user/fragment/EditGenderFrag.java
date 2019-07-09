package com.gaiamount.module_user.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.util.UserUtils;

/**
 * Created by haiyang-lu on 16-4-11.
 * 编辑性别
 */
public class EditGenderFrag extends DialogFragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gender_edit, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        ImageView male = (ImageView) view.findViewById(R.id.gender_male);
        ImageView femaile = (ImageView) view.findViewById(R.id.gender_female);
        male.setOnClickListener(this);
        femaile.setOnClickListener(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(null);
        //5.0以下去除标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        UserInfo userInfo = GaiaApp.getUserInfo();
        switch (id) {
            case R.id.gender_male:
                userInfo.gender = 0;
                break;
            case R.id.gender_female:
                userInfo.gender = 1;
                break;
            default:
                userInfo.gender = 2;
                break;

        }
        UserUtils.updateUserInfo(userInfo,getActivity());
        dismiss();
    }
}
