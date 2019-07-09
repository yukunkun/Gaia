package com.gaiamount.module_user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gaiamount.R;

/**
 * Created by haiyang-lu on 16-4-11.
 * 选择用户头像
 */
public class EditAvatarFrag extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_avatar, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        ImageView fromCamera = (ImageView) view.findViewById(R.id.from_camera);
        ImageView fromLibrary = (ImageView) view.findViewById(R.id.from_library);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        fromCamera.setOnClickListener(this);
        fromLibrary.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.from_camera:
                break;
            case R.id.from_library:
                break;
            case R.id.cancel:
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
