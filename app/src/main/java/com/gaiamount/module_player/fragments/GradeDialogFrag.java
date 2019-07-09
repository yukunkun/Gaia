package com.gaiamount.module_player.fragments;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;


public class GradeDialogFrag extends DialogFragment {

    public static final String WID = "wid";
    public static final String GRADE = "grade";
    private long mWid;
    private Double mGrade;

    public static GradeDialogFrag newInstance(long wid, double grade) {
        GradeDialogFrag fragment = new GradeDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putLong(WID, wid);
        bundle.putDouble(GRADE, grade);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mWid = arguments.getLong(WID);
        mGrade = arguments.getDouble(GRADE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade_dialog, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView(view);
        return view;
    }

    private void initView(View view) {
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.player_dialog_grade_rating);
        final TextView gradeTxt = (TextView) view.findViewById(R.id.player_dialog_grade);
        final TextView prompt = (TextView) view.findViewById(R.id.player_dialog_grade_prompt);
        final Button cancel = (Button) view.findViewById(R.id.player_dialog_grade_cancel);
        final Button ok = (Button) view.findViewById(R.id.player_dialog_grade_ok);

        //初始化值
        ratingBar.setRating(mGrade.floatValue() / 2);
        gradeTxt.setText(mGrade.toString());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    ratingBar.setRating(rating);
                    gradeTxt.setText(String.valueOf(rating * 2));
                    setPrompt(prompt, rating);
                }
            }
        });
        setPrompt(prompt, ratingBar.getRating());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSelf();

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //联网提交打分请求
                submitGrade(ratingBar.getRating());
            }
        });

    }

    private void closeSelf() {
        //关闭自己
        DialogFragment grade = (DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("grade");
        grade.dismiss();
    }

    /**
     * 提交打分
     *
     * @param rating
     */
    private void submitGrade(float rating) {
        final double grade = rating * 2;
        long uid = GaiaApp.getUserInfo().id;
        JsonHttpResponseHandler hanlder = new MJsonHttpResponseHandler(GradeDialogFrag.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                //关闭自己
                closeSelf();
                //提示
                GaiaApp.showToast("评分成功");
                //调用接口
                if (mOnGradeChangeListener != null) {
                    mOnGradeChangeListener.onGradeChange(grade);
                }
            }
        };
        WorksApiHelper.setGrade(uid, mWid, grade, getActivity(), hanlder);
    }

    private void setPrompt(TextView prompt, float rating) {
        switch ((int) rating) {
            case 0:
                prompt.setText("");
                break;
            case 1:
                prompt.setText("一般～");
                break;
            case 2:
                prompt.setText("好～");
                break;
            case 3:
                prompt.setText("非常好～");
                break;
            case 4:
                prompt.setText("赞～");
                break;
            case 5:
                prompt.setText("超级赞～");
                break;
            default:
                break;
        }
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.setTitle("我来打分");
//        return dialog;
//    }

    private OnGradeChangeListener mOnGradeChangeListener;

    public void setOnGradeChangeListener(OnGradeChangeListener onGradeChangeListener) {
        mOnGradeChangeListener = onGradeChangeListener;
    }

    public interface OnGradeChangeListener {
        public void onGradeChange(double grade);
    }
}
