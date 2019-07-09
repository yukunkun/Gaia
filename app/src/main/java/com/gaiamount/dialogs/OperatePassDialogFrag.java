package com.gaiamount.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.gaiamount.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OperatePassDialogFrag extends DialogFragment {


    public static final String TITLE = "title";
    @Bind(R.id.title)
    TextView mTitle;
    private String mTitleStr;

    public static OperatePassDialogFrag newInstance(String title) {
        OperatePassDialogFrag fragment = new OperatePassDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleStr = getArguments().getString(TITLE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //去标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_player_col, container, false);
        ButterKnife.bind(this, inflate);

        mTitle.setText(mTitleStr);

        return inflate;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
