package com.gaiamount.module_creator.sub_module_group.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.sub_module_group.adapters.GroupMemberListAdapter;
import com.gaiamount.module_creator.sub_module_group.beans.GroupAdmin;
import com.gaiamount.module_creator.sub_module_group.beans.GroupMemberBean;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-23.
 */
public class GroupChooseAdminFrag extends Fragment {

    private static final String GID = "gid";
    @Bind(R.id.fragment_choose_admin_list)
    ListView mFragmentChooseAdminList;

    List<GroupMemberBean> mList = new ArrayList<>();
    private GroupMemberListAdapter mAdapter;
    private long mGid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mGid = getArguments().getLong(GID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_admin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new GroupMemberListAdapter(getActivity(), mList);
        mFragmentChooseAdminList.setAdapter(mAdapter);
        //获取数据
        getGroupUsers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static GroupChooseAdminFrag newInstance(long gid) {
        GroupChooseAdminFrag groupChooseAdminFrag = new GroupChooseAdminFrag();
        Bundle bundle = new Bundle();
        bundle.putLong(GID, gid);
        groupChooseAdminFrag.setArguments(bundle);
        return groupChooseAdminFrag;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_finish, menu);
    }

    int tempNum = 0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_finish) {
            //获取mid
            final List<String> checkedList = mAdapter.getCheckedList();
            if (checkedList.size()<=0) {
                GaiaApp.showToast("请至少选择一个成员");
                return true;
            }
            if (checkedList.size()>2) {
                GaiaApp.showToast("至多选择两个");
                return true;
            }


            for (int i=0;i<checkedList.size();i++) {
                //提交设置
                JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupChooseAdminFrag.class) {
                    @Override
                    public void onGoodResponse(JSONObject response) {
                        super.onGoodResponse(response);
                        tempNum++;
                        if (tempNum==checkedList.size()) {

                            GaiaApp.showToast("设置成功");
//                            getActivity().getSupportFragmentManager().beginTransaction().remove(GroupChooseAdminFrag.this).commit();
                        }
                    }

                    @Override
                    public void onBadResponse(JSONObject response) {
                        super.onBadResponse(response);
                        if(response.optLong("i")==31110){
                            GaiaApp.showToast("不能设置自己");
                        }
                        GaiaApp.showToast("设置失败.可能已经有两个管理员了");
                    }
                };

                String s = checkedList.get(i);
                long mid = (long) mAdapter.getItem(Integer.valueOf(s));
                GroupAdmin groupAdmin = new GroupAdmin(mGid, mid);
                GroupApiHelper.setGroupAdmin(groupAdmin,1, getActivity(), jsonHttpResponseHandler);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取小组成员列表
     */
    private void getGroupUsers() {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupChooseAdminFrag.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                Log.i("--res",response.toString());
                JSONArray jsonArray = response.optJSONArray("a");
                for (int i = 0; i < jsonArray.length(); i++) {
                    mList.add(new GroupMemberBean(jsonArray.optJSONObject(i)));
                }
                mAdapter.setGroupMemberBeanList(mList);
                mAdapter.notifyDataSetChanged();
            }
        };

        //默sType opr传0
        GroupApiHelper.getGroupUser(mGid, 0, 0, getActivity(), jsonHttpResponseHandler);
    }


}
