package com.gaiamount.module_player.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

/**
 * Created by haiyang-lu on 16-5-4.
 */
public class PlayerTipOffFrag extends DialogFragment {

    private long mWid;
    private StringBuilder content = new StringBuilder();

    public static PlayerTipOffFrag newInstance(long wid) {
        PlayerTipOffFrag playerTipOffFrag = new PlayerTipOffFrag();
        Bundle bundle = new Bundle();
        bundle.putLong("wid", wid);
        playerTipOffFrag.setArguments(bundle);
        return playerTipOffFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mWid = arguments.getLong("wid");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(null);
        //5.0以下去除标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }

    private String[] listStr = new String[3];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_tipoff, container, false);
        final ListView tipOffList = (ListView) view.findViewById(R.id.tip_off_list);

        tipOffList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.item_tip_off_desc);

                boolean checked = checkedTextView.isChecked();
                if (!checked) {
                    listStr[position] = checkedTextView.getText().toString();
                    checkedTextView.setChecked(true);
                    checkedTextView.setCheckMarkDrawable(R.mipmap.ic_done_black_24dp);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        checkedTextView.setCheckMarkTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    }else {
                        Drawable drawable = getResources().getDrawable(R.mipmap.ic_done_black_24dp);
                        drawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                        checkedTextView.setCheckMarkDrawable(drawable);
                    }
                } else {
                    listStr[position] = "";
                    checkedTextView.setChecked(false);
                    checkedTextView.setCheckMarkDrawable(null);
                }
            }
        });
        final EditText detail = (EditText) view.findViewById(R.id.tip_off_detail);

        Button cancel = (Button) view.findViewById(R.id.tip_off_cancel);
        Button ok = (Button) view.findViewById(R.id.tip_off_ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏自己
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去举报
                String s = detail.getText().toString();
                if (!s.isEmpty()) {
                    content.append(s);
                }

                for (int i = 0; i < listStr.length; i++) {
                    content.append(listStr[i] + "");
                }

                if (content.toString().isEmpty()) {
                    GaiaApp.showToast("举报内容不能为空");
                } else {
                    ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, "正在发送");
                    JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(PlayerTipOffFrag.class, progressDialog) {
                        @Override
                        public void onGoodResponse(JSONObject response) {
                            super.onGoodResponse(response);
                            GaiaApp.showToast("已收到举报请求，我们将尽快处理");
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            //隐藏界面
                            dismiss();
                        }
                    };
                    WorksApiHelper.sendTipOff(mWid, content, getActivity(), jsonHttpResponseHandler);
                }

            }
        });
        tipOffList.setAdapter(new MyAdapter(getActivity()));
        return view;
    }

    private class MyAdapter extends BaseAdapter {
        private final String[] mStringArray;
        private final LayoutInflater mInflater;

        MyAdapter(Context context) {
            mStringArray = getResources().getStringArray(R.array.tip_off);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mStringArray.length;
        }

        @Override
        public Object getItem(int position) {
            return mStringArray[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.item_tip_off, parent, false);
            final CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.item_tip_off_desc);
            checkedTextView.setText(mStringArray[position]);
            return view;
        }


    }
}
