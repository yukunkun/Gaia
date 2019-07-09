package com.gaiamount.module_creator.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.sub_module_group.beans.GroupAdmin;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupAdminFrag;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-6-23.
 */
public class AdminSetDialog extends DialogFragment {

    public static final String ADMIN_POWER = "admin_power";
    private GroupAdmin mAdminPower;
    private List<Integer> mGroupAdminPowers=new ArrayList<>();

    public static AdminSetDialog newInstance(GroupAdmin power) {
        AdminSetDialog adminSetDialog = new AdminSetDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ADMIN_POWER, power);
        adminSetDialog.setArguments(bundle);

        return adminSetDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdminPower = (GroupAdmin) getArguments().getSerializable(ADMIN_POWER);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //审核权限
        int exa = mAdminPower.getExa();
        //审核创作权限
        int ccre = mAdminPower.getCcre();
        //成员管理
        int mb = mAdminPower.getMb();
        //专辑管理
        int ms = mAdminPower.getMs();
        boolean[] checkItems = new boolean[4];
        checkItems[0] = exa == 1 ? true : false;
        checkItems[1] = ccre == 1 ? true : false;
        checkItems[2] = mb == 1 ? true : false;
        checkItems[3] = ms == 1 ? true : false;

        int a1=-1,a2=0,a3=0,a4=0;
        mGroupAdminPowers.add(a1);
        mGroupAdminPowers.add(a2);
        mGroupAdminPowers.add(a3);
        mGroupAdminPowers.add(a4);
        builder.setTitle("管理员权限设置")
                .setMultiChoiceItems(R.array.group_admin_power, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mGroupAdminPowers.set(which,which);
                        } else if (mGroupAdminPowers.contains(which)) {
                            mGroupAdminPowers.remove(Integer.valueOf(which));
                            mGroupAdminPowers.set(which,-1);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mGroupAdminPowers.size()!=0){
                            mAdminPower.setExa(mGroupAdminPowers.get(0)==0?1:0);
                            mAdminPower.setCcre(mGroupAdminPowers.get(1)==1?1:0);
                            mAdminPower.setMb(mGroupAdminPowers.get(2)==2?1:0);
                            mAdminPower.setMs(mGroupAdminPowers.get(3)==3?1:0);
                            //更改权限
                            setGroupAdminPower();

                        }else {
                            GaiaApp.showToast("请选择");
                        }

                    }
                });

        return builder.create();
    }

    /**
     * 设置小组管理员的权限
     */
    private void setGroupAdminPower() {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(AdminSetDialog.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                //刷新管理员列表
//                SetGroupAdminFrag frag = (SetGroupAdminFrag) getActivity().getSupportFragmentManager().findFragmentByTag("set_admin_fragment");
//                frag.updateAdminList();
                GaiaApp.showToast("设置成功");
            }
        };

        //1代表设置管理员
        GroupApiHelper.setGroupAdmin(mAdminPower,1, getActivity(), jsonHttpResponseHandler);
    }


}
