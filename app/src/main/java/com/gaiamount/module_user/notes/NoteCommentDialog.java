package com.gaiamount.module_user.notes;

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
import com.gaiamount.apis.api_user.NoteApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_scripe.bean.OnEventScripeComm;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by yukun on 16-8-18.
 */
public class NoteCommentDialog extends DialogFragment {

    private Button buttonCancel;
    private Button buttonSure;
    private long nid;
    private long uid;
    public static NoteCommentDialog newInstance(long nid) {
        NoteCommentDialog fragment = new NoteCommentDialog();
        Bundle bundle=new Bundle();
        bundle.putLong("nid",nid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        nid= arguments.getLong("nid");
        uid=GaiaApp.getAppInstance().getUserInfo().id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_notes_dia, container, false);
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
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(NoteCommentDialog.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("转载成功");
                dismiss();
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                long res = response.optLong("i");
                if(res==38411){
                    GaiaApp.showToast("已经转载过该手记");
                }else if(res==34400){
                    GaiaApp.showToast("不能转载自己的呦");
                } else {
                    GaiaApp.showToast("转载失败");
                    dismiss();
                }
            }
        };
        NoteApiHelper.rePrint(uid,nid,getContext(),handler);
    }
}
