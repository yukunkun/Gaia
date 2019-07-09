package com.gaiamount.module_material.activity.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.adapter.AddGroupAdapter;
import com.gaiamount.module_academy.bean.GroupInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-19.
 */
public class MaterialGropDialog extends DialogFragment{

    private Button buttonCancel;
    private ListView listView;
    private TextView textviewNew;
    private ArrayList<GroupInfo> arrayList=new ArrayList();
    private AddGroupAdapter adapter;
    private long cid;
    private int language;

    public static MaterialGropDialog newInstance(long cid, int language) {
        MaterialGropDialog fragment = new MaterialGropDialog();
        Bundle bundle=new Bundle();
        bundle.putLong("cid",cid);
        bundle.putInt("language",language);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        cid=arguments.getLong("cid");
        language=arguments.getInt("language");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.commemt_add_group_dialog, null);
        init(inflate);
        getInfo();
        setAdapter();
        setListener();
        return inflate;

    }

    private void init(View inflate) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        buttonCancel = (Button) inflate.findViewById(R.id.comment_dialog_grade_cancel);
        listView = (ListView) inflate.findViewById(R.id.comment_dialog_listview);
        View inflate1 = LayoutInflater.from(getContext()).inflate(R.layout.add_group_dialog, null);
        textviewNew = (TextView) inflate1.findViewById(R.id.add_group_new);
        listView.addHeaderView(inflate1);
    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(MaterialGropDialog.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);

            }
        };

        GroupApiHelper.myGroup(getActivity(), handler);
    }

    private void paraJson(JSONObject response) {
        JSONObject o = response.optJSONObject("o");
        JSONArray myGroups = o.optJSONArray("myGroups");
        for (int i = 0; i < myGroups.length(); i++) {
            GroupInfo groupInfo=new GroupInfo();
            JSONObject object = myGroups.optJSONObject(i);
            groupInfo.setName(object.optString("name"));
            groupInfo.setId(object.optLong("id"));
            arrayList.add(groupInfo);
        }
        JSONArray myControllGroups = o.optJSONArray("myControllGroups");

        for (int i = 0; i < myControllGroups.length(); i++) {
            GroupInfo groupInfo=new GroupInfo();
            JSONObject object = myControllGroups.optJSONObject(i);
            groupInfo.setName(object.optString("name"));
            groupInfo.setId(object.optLong("id"));
            arrayList.add(groupInfo);
        }
        JSONArray joinGroups = o.optJSONArray("joinGroups");
        for (int i = 0; i < joinGroups.length(); i++) {
            GroupInfo groupInfo=new GroupInfo();
            JSONObject object = joinGroups.optJSONObject(i);
            groupInfo.setName(object.optString("name"));
            groupInfo.setId(object.optLong("id"));
            arrayList.add(groupInfo);
        }
        adapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        adapter = new AddGroupAdapter(getContext(),arrayList);
        listView.setAdapter(adapter);
    }

    private void setListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        //新建的点击事件
        textviewNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startGroupCreateActivity(getContext());
            }
        });

        //小组的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addVideoToGroup(arrayList.get(position-1).getId());
            }
        });
    }

    private void addVideoToGroup(long id) {
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(MaterialGropDialog.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("添加成功");
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                if (response.optInt("i") == 32403) {
                    GaiaApp.showToast("视频已存在");
                }else {
                    GaiaApp.showToast("添加失败");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismiss();
            }
        };
        //cid 就是mid.没有改而已
        GroupApiHelper.addVideoToGroup(id, cid, 1, getActivity(), handler);
    }
}
