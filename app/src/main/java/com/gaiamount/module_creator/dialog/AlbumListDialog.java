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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-7-5.
 * 添加小组视频到小组的专辑中，提示选择的专辑列表对话框
 */
public class AlbumListDialog extends DialogFragment {

    public static final String WID = "wid";
    public static final String C = "c";
    public static final String INFO = "info";
    @Bind(R.id.dialog_list)
    ListView mDialogList;
    @Nullable
    @Bind(R.id.cancelBtn)
    Button mCancelBtn;
    @Nullable
    @Bind(R.id.okBtn)
    Button mOkBtn;
    @Bind(R.id.empty_hint)
    TextView mTV_EmptyHint;

    private long mWid;
    /**
     * 视频类型
     */
    private long mCtype;
    private Info mInfo;

    public static AlbumListDialog newInstance(Info info, long wid, int c) {

        Bundle args = new Bundle();
        args.putSerializable(INFO, info);
        args.putLong(WID, wid);
        args.putInt(C, c);
        AlbumListDialog fragment = new AlbumListDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInfo = (Info) getArguments().getSerializable(INFO);
        mWid = getArguments().getLong(WID);
        mCtype = getArguments().getInt(C);

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
        View view = inflater.inflate(R.layout.dialog_album_list, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //联网获取专辑列表
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbumListDialog.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                List<String> list = new ArrayList<>();
                JSONArray jsonArray = response.optJSONArray("a");
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(jsonArray.optJSONObject(i).optString("name"));
                }
                mDialogList.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list));

                final List<Long> aidList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    aidList.add(jsonArray.optJSONObject(i).optLong("aid"));
                }

                //如果小组内还没有专辑
                if (aidList.size()==0) {
                    mTV_EmptyHint.setVisibility(View.VISIBLE);
                }else {
                    mTV_EmptyHint.setVisibility(View.GONE);
                }

                //item的点击事件
                mDialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //添加视频到专辑
                        Long[] wids = new Long[]{mWid};
                        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbumListDialog.class){
                            @Override
                            public void onGoodResponse(JSONObject response) {
                                super.onGoodResponse(response);
                                GaiaApp.showToast("添加成功");
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                dismiss();
                            }
                        };
                        AlbumApiHelper.addVideoToAlbum(wids,mCtype,aidList.get(position),getActivity(),handler);
                    }
                });
            }
        };
        AlbumApiHelper.getAlbumList(mInfo.gid, 1, 0, 1,getActivity(), handler);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
