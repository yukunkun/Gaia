package com.gaiamount.module_player.dialogs;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.databinding.DialogListBinding;
import com.gaiamount.databinding.ItemSimpleListItemBinding;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by haiyang-lu on 16-7-2.
 * 对话框，里面显示选择的列表
 */
public class ListDialog extends DialogFragment {

    public static final String TITLE = "title";
    public static final String CONTENT_LIST = "content_list";
    public static final String VID = "vid";
    public static final String V_TYPE = "vType";
    private String mTitle;
    private ArrayList<MyGroup> contentList;
    /**
     * 视频id
     */
    private long mVid;
    /**
     * 视频类型
     */
    private int mVType;
    /**
     * 选中的小组id
     */
    private long mGid;

    public static ListDialog newInstance(String title, long vid, int vType, ArrayList<MyGroup> contentList) {

        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putSerializable(CONTENT_LIST, contentList);
        args.putLong(VID, vid);
        args.putInt(V_TYPE, vType);
        ListDialog fragment = new ListDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString(TITLE);
        contentList = (ArrayList<MyGroup>) getArguments().getSerializable(CONTENT_LIST);
        mVid = getArguments().getLong(VID);
        mVType = getArguments().getInt(V_TYPE);


        if (mTitle == null || mTitle.isEmpty() || contentList == null || contentList.size() == 0) {
            throw new RuntimeException("传入的标题或者内容数组为空");
        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //隐藏标题栏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private View lastView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final DialogListBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_list, container, false);
        DataBean dataBean = new DataBean(mTitle, contentList);
        binding.setData(dataBean);

        binding.dialogList.setAdapter(new MAdapter());

        binding.dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastView != null) {
                    lastView.setBackgroundColor(Color.WHITE);
                }
                lastView = view;

                view.setBackgroundColor(getResources().getColor(R.color.bg_light));
                mGid = contentList.get(position).getId();
            }
        });


        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVideoToGroup();
            }
        });
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return binding.getRoot();
    }

    /**
     * 向小组中添加视频
     */
    private void addVideoToGroup() {
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(ListDialog.class) {
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
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismiss();
            }
        };
        GroupApiHelper.addVideoToGroup(mGid, mVid, mVType, getActivity(), handler);
    }


    class MAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;

        public MAdapter() {
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return contentList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyGroup myGroup = contentList.get(position);
            ItemSimpleListItemBinding binding = DataBindingUtil.inflate(mInflater, R.layout.item_simple_list_item, null, false);
            binding.setMyGroup(myGroup);
            return binding.getRoot();
        }
    }


}
