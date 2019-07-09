package com.gaiamount.module_player.dialogs;

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
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-7-2.
 * 播放器页面点击右上角的添加按钮调用此fragment
 */
public class AddToDialog extends DialogFragment {

    public static final String CONTENT_LIST = "content_list";
    public static final String VID = "vid";
    public static final String V_TYPE = "vType";
    @Bind(R.id.add_to_group)
    TextView mAddToGroup;
    @Bind(R.id.cancel)
    Button mCancel;

    private ArrayList<MyGroup> groupList = new ArrayList<>();

    private ArrayList<String> albumList;
    private long mVid;
    private int mVType;

    public static AddToDialog newInstance(long vid, int vType) {

        Bundle args = new Bundle();
        args.putLong(VID, vid);
        args.putInt(V_TYPE, vType);
        AddToDialog fragment = new AddToDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //隐藏标题栏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVid = getArguments().getLong(VID);
        mVType = getArguments().getInt(V_TYPE);

        //获取数据
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AddToDialog.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray jsonArray = response.optJSONObject("o").optJSONArray("myGroups");
                for (int i = 0; i < jsonArray.length(); i++) {
                    MyGroup myGroup = GsonUtil.getInstannce().getGson().fromJson(jsonArray.optJSONObject(i).toString(), MyGroup.class);
                    groupList.add(myGroup);
                }
                JSONArray myControllGroups = response.optJSONObject("o").optJSONArray("myControllGroups");
                for (int i = 0; i < myControllGroups.length(); i++) {
                    MyGroup myGroup = GsonUtil.getInstannce().getGson().fromJson(myControllGroups.optJSONObject(i).toString(), MyGroup.class);

                    groupList.add(myGroup);
                }
                JSONArray joinGroups = response.optJSONObject("o").optJSONArray("joinGroups");
                for (int i = 0; i < joinGroups.length(); i++) {
                    MyGroup myGroup = GsonUtil.getInstannce().getGson().fromJson(joinGroups.optJSONObject(i).toString(), MyGroup.class);
                    groupList.add(myGroup);
                }


                mAddToGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(groupList.size()>0){
                            ListDialog.newInstance("小组列表", mVid, mVType, groupList).show(getActivity().getSupportFragmentManager(), "group_list");
                        }
                        dismiss();
                    }
                });
            }
        };
        GroupApiHelper.myGroup(getActivity(), handler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_to, container, false);
        ButterKnife.bind(this, view);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
