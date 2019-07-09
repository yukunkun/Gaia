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
import android.widget.RatingBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.apis.api_comment.CommentApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.module_scripe.bean.OnEventScripeComm;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by yukun on 16-8-18.
 */
public class ScripeCommentDialog extends DialogFragment {

    private EditText editText;
    private Button buttonCancel;
    private Button buttonSure;
    private String content;
    private long sid;
    private long uid;
    public static ScripeCommentDialog newInstance(long sid) {
        ScripeCommentDialog fragment = new ScripeCommentDialog();
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
        View view = inflater.inflate(R.layout.commemt_scripe_dia, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView(view);
        setListener();
        return view;
    }

    private void initView(View view) {
        editText = (EditText) view.findViewById(R.id.dialog_grade_edittext);
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
                content=editText.getText().toString();
                if(TextUtils.isEmpty(content)) {
                    GaiaApp.showToast(getContext().getString(R.string.academy_enter_comment));
                }else {
                    sendMessage();

                }
            }
        });
    }

    private void sendMessage() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ScripeCommentDialog.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast(getContext().getString(R.string.academy_comment_success));
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
                    GaiaApp.showToast(getContext().getString(R.string.academy_comment_fail));
                }
                getDialog().dismiss();
            }
        };
        CommentApiHelper.sendComment(uid,editText.getText().toString(),sid,3,0,0,getContext(),handler);
    }

}
