package com.gaiamount.module_scripe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.gaiamount.R;
import com.gaiamount.apis.api_comment.CommentApiHelper;
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_scripe.bean.OnEventScripeComm;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by yukun on 16-8-18.
 */
public class ScripeAgreeDialog extends DialogFragment {

    private EditText editText;
    private Button buttonCancel;
    private Button buttonSure;
    private String content;
    private long sid;
    private long uid;
    public static ScripeAgreeDialog newInstance(long sid) {
        ScripeAgreeDialog fragment = new ScripeAgreeDialog();
        Bundle bundle=new Bundle();
        bundle.putLong("sid",sid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        sid= arguments.getLong("sid");
        uid=GaiaApp.getAppInstance().getUserInfo().id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_scripe_agree, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView(view);
        setListener();
        return view;
    }

    private void initView(View view) {

        buttonCancel = (Button) view.findViewById(R.id.comment_dialog_grade_cancel);
        buttonSure = (Button) view.findViewById(R.id.comment_dialog_grade_ok);

    }

    private void setListener() {

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getDialog().dismiss();
            }
        });

        buttonSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送申请
                    sendMessage();
            }
        });
    }

    private void sendMessage() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ScripeAgreeDialog.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("发送成功");
                getDialog().dismiss();
                EventBus.getDefault().post(new OnEventScripeComm(1));
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                long i = response.optLong("i");
                if(i==37601){
                    GaiaApp.showToast(getContext().getString(R.string.comment_star));
                }else {
                    GaiaApp.showToast("发送失败");
                }
                getDialog().dismiss();
            }
        };
        ScriptApiHelper.getScriptAgree(sid,uid,1,handler);
    }

}
