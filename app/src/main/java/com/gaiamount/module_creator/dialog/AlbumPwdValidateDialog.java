package com.gaiamount.module_creator.dialog;

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
import android.widget.EditText;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-7-4.
 */
public class AlbumPwdValidateDialog extends DialogFragment {

    public static final String AID = "aid";
    @Bind(R.id.remove_admin_cancel)
    Button mRemoveAdminCancel;
    @Bind(R.id.remove_admin_sure)
    Button mRemoveAdminSure;

    @Bind(R.id.album_pwd_et)
    EditText mET_albumPwd;
    private long mAid;

    public static AlbumPwdValidateDialog newInstance(long aid) {

        Bundle args = new Bundle();
        args.putLong(AID, aid);
        AlbumPwdValidateDialog fragment = new AlbumPwdValidateDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof OnContactWithActivity)) {
            throw new RuntimeException("必须实现OnContactWithActivity接口");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAid = getArguments().getLong(AID);
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
        View view = inflater.inflate(R.layout.dialog_album_pwd, container, false);
        ButterKnife.bind(this, view);
        mRemoveAdminCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mRemoveAdminSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = mET_albumPwd.getText().toString();
                if (!pwd.isEmpty()) {
                    if (mOnContactWithActivity!=null) {
                        mOnContactWithActivity.onOKButtonClicked(AlbumPwdValidateDialog.this, pwd);
                    }
                }else {
                    GaiaApp.showToast("密码不能为空");
                }
            }
        });
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private OnContactWithActivity mOnContactWithActivity;

    public void setOnContactWithActivity(OnContactWithActivity onContactWithActivity) {
        mOnContactWithActivity = onContactWithActivity;
    }

    public interface OnContactWithActivity{
        void onOKButtonClicked(DialogFragment dialogFragment,String pwd);
    }
}
