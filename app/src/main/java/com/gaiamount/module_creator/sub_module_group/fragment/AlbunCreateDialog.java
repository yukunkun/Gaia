package com.gaiamount.module_creator.sub_module_group.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.adapter.AddGroupAdapter;
import com.gaiamount.module_academy.bean.GroupInfo;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_creator.beans.GroupVideoBean;
import com.gaiamount.module_creator.sub_module_album.AlbumBean;
import com.gaiamount.module_creator.sub_module_album.SetUpAlbumActivity;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-8-19.
 */
public class AlbunCreateDialog extends DialogFragment{

    private Button buttonCancel;
    private ListView listView;
    private TextView textviewNew;
    private ArrayList<GroupInfo> arrayList=new ArrayList();
    private AddGroupAdapter adapter;
    private long gid;
    private int position;
    private GroupVideoBean mList;

    public static AlbunCreateDialog newInstance(long gid, GroupVideoBean mList, int position) {
        AlbunCreateDialog fragment = new AlbunCreateDialog();
        Bundle bundle=new Bundle();
        bundle.putLong("gid",gid);
        bundle.putInt("position",position);
        bundle.putSerializable("mList", mList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        gid=arguments.getLong("gid");
        position=arguments.getInt("position");
        mList= (GroupVideoBean) arguments.getSerializable("mList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.commemt_add_group_dialog, container,false);
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
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbunCreateDialog.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
            }
        };
        AlbumApiHelper.getAlbumList(gid, 1, 0, 1,getContext(), handler);
    }

    private void paraJson(JSONObject response) {

        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            GroupInfo groupInfo=new GroupInfo();
            JSONObject object = a.optJSONObject(i);
            groupInfo.setName(object.optString("name"));
            groupInfo.setId(object.optLong("aid"));
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
                //新建专辑
                    Intent intent = new Intent(getContext(), SetUpAlbumActivity.class);
                    intent.putExtra("gid",gid);
                    intent.putExtra("album_type", 1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    getDialog().dismiss();
            }
        });

        //小组的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                addGroup(arrayList.get(position-1).getId());

            }
        });
    }

    private void addGroup(long aid) {

        Long[] wids = new Long[]{mList.getId()};
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbunCreateDialog.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("添加成功");
                getDialog().dismiss();

            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                long i = response.optLong("i");
                if(i==37609){
                    GaiaApp.showToast("已经在该专辑了");
                }else {
                    GaiaApp.showToast("添加失败");
                }
                getDialog().dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        };
        AlbumApiHelper.addVideoToAlbum(wids,0,aid,getActivity(),handler);
    }
}
