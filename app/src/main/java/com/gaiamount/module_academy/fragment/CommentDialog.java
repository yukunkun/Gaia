package com.gaiamount.module_academy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
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
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by yukun on 16-8-18.
 */
public class CommentDialog extends DialogFragment {

    private TextView textViewGrade;
    private RatingBar ratingBar;
    private EditText editText;
    private Button buttonCancel;
    private Button buttonSure;
    private String content;
    private long cid;
    private long uid;
    private float ratings;
    public static CommentDialog newInstance(long cid) {
        CommentDialog fragment = new CommentDialog();
        Bundle bundle=new Bundle();
        bundle.putLong("cid",cid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        cid= arguments.getLong("cid");
        uid=GaiaApp.getAppInstance().getUserInfo().id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commemt_popup_laout, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView(view);
        setListener();
        return view;
    }

    private void initView(View view) {
        textViewGrade = (TextView) view.findViewById(R.id.comment_dialog_grade);
        ratingBar = (RatingBar) view.findViewById(R.id.comment_dialog_grade_rating);
        editText = (EditText) view.findViewById(R.id.dialog_grade_edittext);
        buttonCancel = (Button) view.findViewById(R.id.comment_dialog_grade_cancel);
        buttonSure = (Button) view.findViewById(R.id.comment_dialog_grade_ok);

    }

    private void setListener() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                    textViewGrade.setText(rating*2+"");
                    ratings=rating*2;
                }
            }
        });
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
                if(TextUtils.isEmpty(content)&&ratings==0.0) {
                    GaiaApp.showToast(getContext().getString(R.string.academy_enter_comment));
                }else {
                    sendMessage();
                }
            }
        });
    }

    private void sendMessage() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(CommentDialog.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast(getContext().getString(R.string.academy_comment_success));
                getDialog().dismiss();
                EventBus.getDefault().post(new OnEventId(1));
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
        AcademyApiHelper.sendComment(cid,uid,(int)ratings,content,ratings,getContext(),handler);
    }

}
