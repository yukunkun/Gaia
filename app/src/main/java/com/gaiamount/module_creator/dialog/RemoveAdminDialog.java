package com.gaiamount.module_creator.dialog;

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

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.sub_module_group.beans.GroupAdmin;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupAdminFrag;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-23.
 */
public class RemoveAdminDialog extends DialogFragment {


    private static final String GROUP_ADMIN = "group_admin";
    @Bind(R.id.remove_admin_cancel)
    Button mRemoveAdminCancel;
    @Bind(R.id.remove_admin_sure)
    Button mRemoveAdminSure;
    private GroupAdmin mGroupAdmin;

    public static RemoveAdminDialog newInstance(GroupAdmin groupAdmin) {
        RemoveAdminDialog removeAdminDialog = new RemoveAdminDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(GROUP_ADMIN, groupAdmin);
        removeAdminDialog.setArguments(bundle);

        return removeAdminDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupAdmin = (GroupAdmin) getArguments().getSerializable(GROUP_ADMIN);
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

        mRemoveAdminCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mRemoveAdminSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行取消此用户的管理员操作
                doRemoveAdmin();
            }
        });

        return view;

    }

    /**
     * 取消次用户的管理员权限
     */
    private void doRemoveAdmin() {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(AdminSetDialog.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("设置成功");
                dismiss();
                //刷新管理员列表
                SetGroupAdminFrag frag = (SetGroupAdminFrag) getActivity().getSupportFragmentManager().findFragmentByTag("set_admin_fragment");
                frag.updateAdminList();
            }
        };

        //0代表移除管理员
        GroupApiHelper.setGroupAdmin(mGroupAdmin, 0, getActivity(), jsonHttpResponseHandler);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
