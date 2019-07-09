package com.gaiamount.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gaiamount.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-23.
 */
public class NoticeDialogFrag extends DialogFragment {

    public static final String MESSAGE = "message";
    @Bind(R.id.remove_admin_cancel)
    Button mRemoveAdminCancel;
    @Bind(R.id.remove_admin_sure)
    Button mRemoveAdminSure;
    @Bind(R.id.title)
    TextView title;

    private String mTitleStr;

    public static NoticeDialogFrag newInstance(String message) {
        NoticeDialogFrag removeAdminDialog = new NoticeDialogFrag();

        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE,message);
        removeAdminDialog.setArguments(bundle);

        return removeAdminDialog;
    }

    public interface NoticeDialogListener{
         void onDialogPositiveClick(DialogFragment dialogFragment);
         void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+"必须实现 noticeDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleStr = getArguments().getString("message");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //去标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_frag_remove_admin, container, false);
        ButterKnife.bind(this, view);

        //设置提醒话语
        if (mTitleStr!=null) {
            title.setText(mTitleStr);
        }

        mRemoveAdminCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogNegativeClick(NoticeDialogFrag.this);
            }
        });

        mRemoveAdminSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogPositiveClick(NoticeDialogFrag.this);
            }
        });

        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
