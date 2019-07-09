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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-7-5.
 * 设置小组推荐视频的对话框
 */
public class SetRecommendDialog extends DialogFragment {

    public static final String VID = "vid";
    public static final String INFO = "info";
    private Info mInfo;
    private long mVid;
    private ListView mListView;

    public static SetRecommendDialog newInstance(Info info, long vid) {

        Bundle args = new Bundle();
        args.putSerializable(INFO, info);
        args.putLong(VID,vid);
        SetRecommendDialog fragment = new SetRecommendDialog();
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInfo = (Info) getArguments().getSerializable(INFO);
        mVid = getArguments().getLong(VID);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_set_recommend,container,false);
        mListView = (ListView) view.findViewById(R.id.listview);

        String[] strings = new String[]{"形象视频1","形象视频2","形象视频3"};
        mListView.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,strings));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(SetRecommendDialog.class){
                    @Override
                    public void onGoodResponse(JSONObject response) {
                        super.onGoodResponse(response);
                        GaiaApp.showToast("设置成功");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismiss();
                    }
                };
                GroupApiHelper.setGroupRecommend(mInfo.gid,mVid,position+1,getActivity(),handler);
            }
        });
        return view;
    }
}
