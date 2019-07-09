package com.gaiamount.module_creator.sub_module_group.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.module_creator.sub_module_group.activities.GroupCreateActivity;
import com.gaiamount.module_creator.sub_module_group.activities.GroupEditActivity;
import com.gaiamount.module_creator.sub_module_group.adapters.GroupAdminListAdapter;
import com.gaiamount.module_creator.sub_module_group.beans.GroupAdmin;
import com.gaiamount.module_creator.dialog.AdminSetDialog;
import com.gaiamount.module_creator.dialog.RemoveAdminDialog;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-16.
 */
public class SetGroupAdminFrag extends Fragment {

    public static final String GROUP_ID = "groupBean";
    @Bind(R.id.fragment_set_group_admin_text)
    TextView mFragmentSetGroupAdminText;
    @Bind(R.id.fragment_set_group_admin_list)
    ListView mFragmentSetGroupAdminList;

    private Activity hostActivity;
    private GroupCreateActivity mGroupCreateActivity;
    private GroupEditActivity mGroupEditActivity;

    /**
     * 小组管理员列表
     */
    private List<GroupAdmin> mList;
    /**
     * 列表适配器
     */
    private GroupAdminListAdapter mAdapter;

    /**
     * 小组id
     */
    private long mGid;


    public static SetGroupAdminFrag newInstance(long gid) {
        SetGroupAdminFrag setGroupDescFrag = new SetGroupAdminFrag();
        Bundle bundle = new Bundle();
        bundle.putLong(GROUP_ID, gid);
        setGroupDescFrag.setArguments(bundle);
        return setGroupDescFrag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupCreateActivity) {
            mGroupCreateActivity = (GroupCreateActivity) context;
            hostActivity = mGroupCreateActivity;
        }

        if (context instanceof GroupEditActivity) {
            mGroupEditActivity = (GroupEditActivity) context;
            hostActivity = mGroupEditActivity;
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGid = getArguments().getLong(GROUP_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_group_admin, container, false);
        ButterKnife.bind(this, view);

        mList = new ArrayList<>();
        mAdapter = new GroupAdminListAdapter(getActivity(), mList, this);

        mFragmentSetGroupAdminList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getAdminList();
    }

    public void getAdminList() {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(SetGroupAdminFrag.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray a = response.optJSONArray("a");
                for (int i = 0; i < a.length(); i++) {
                    GroupAdmin groupAdmin = new GroupAdmin();
                    JSONObject jsonObject = a.optJSONObject(i);
                    groupAdmin.setGid(jsonObject.optLong("groupId"));
                    groupAdmin.setMid(jsonObject.optLong("id"));
                    groupAdmin.setCcre(jsonObject.optInt("allowCleanMember"));
                    groupAdmin.setExa(jsonObject.optInt("allowExamine"));
                    groupAdmin.setMb(jsonObject.optInt("allowCleanCreation"));
                    groupAdmin.setMs(jsonObject.optInt("allowManageSpecial"));
                    groupAdmin.setNickName(jsonObject.optString("nickName"));
                    groupAdmin.setAvatar(jsonObject.optString("avatar"));
                    mList.add(groupAdmin);
                }
                mFragmentSetGroupAdminText.setText("管理员(" + a.length() + "/2)");
                //刷新适配器
                mAdapter.update(mList);
            }
        };

        GroupApiHelper.getAdmin(mGid, getActivity(), jsonHttpResponseHandler);
    }

    public void updateAdminList() {
        mList = new ArrayList<>();
        getAdminList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 打开成员列表选择成员成为管理员
     */
    public void addAdmin() {
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.edit_group_container, GroupChooseAdminFrag.newInstance(mGid)).addToBackStack(null).commit();
    }

    /**
     * 移除该组员的管理员权限
     */
    public void removeAdmin(GroupAdmin groupAdmin) {
        RemoveAdminDialog.newInstance(groupAdmin).show(getActivity().getSupportFragmentManager(), "remove_admin");
    }

    /**
     * 设置管理员权限
     */
    public void setAdminPower(GroupAdmin groupAdmin) {
        AdminSetDialog.newInstance(groupAdmin).show(getActivity().getSupportFragmentManager(), "admin_power");
    }


}
