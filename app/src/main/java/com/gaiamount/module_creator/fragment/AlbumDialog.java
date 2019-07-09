package com.gaiamount.module_creator.fragment;

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
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by yukun on 16-8-25.
 */
public class AlbumDialog extends DialogFragment {

    private long aid;
    private int position;
    private Button buttonCancel ,buttonOk;
    public static AlbumDialog newInstance(long aid, int position) {
        AlbumDialog fragment = new AlbumDialog();
        Bundle bundle=new Bundle();
        bundle.putLong("aid",aid);
        bundle.putLong("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        aid=arguments.getLong("aid");
        position=arguments.getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.album_dialog, null);
        init(inflate);
        setListener();
        return inflate;

    }

    private void init(View inflate) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        buttonCancel = (Button) inflate.findViewById(R.id.album_dialog_grade_cancel);
        buttonOk = (Button) inflate.findViewById(R.id.album_dialog_grade_ok);

    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AlbumDialog.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("删除成功");
                EventBus.getDefault().post(new OnEventId(1,position));//删除成功的回调
            }
            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                GaiaApp.showToast("删除失败");
            }
        };
        AlbumApiHelper.deleteAlbum(aid,getActivity(), handler);
    }
    private void setListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
                getDialog().dismiss();
            }
        });
    }
}
