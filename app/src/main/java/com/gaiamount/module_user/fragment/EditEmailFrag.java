package com.gaiamount.module_user.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gaiamount.R;
import com.gaiamount.module_user.user_edit.SendMailSuccessActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.apis.api_user.AccountApi;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.util.network.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by haiyang-lu on 16-3-30.
 * 绑定邮箱
 */
public class EditEmailFrag extends Fragment{
    private EditText mEditText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.layout_email_edit,null,false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        mEditText = (EditText) view.findViewById(R.id.sign_input_email);
        Button button = (Button) view.findViewById(R.id.sign_bind_email);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    private void done() {
        //弹出提示框，询问用户是否真的打算这么做
        new AlertDialog.Builder(getActivity()).setTitle("真的要这么做么").
                setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确定的情况下，联网请求服务
                startChange();
            }
        }).show();
    }

    private void startChange() {
        //将更新的用户信息放入userInfo对象中
        final String email = mEditText.getText().toString().trim();
        UserInfo userInfo = GaiaApp.getUserInfo();
        userInfo.signature = email;
        //调用接口
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("opr", AccountApi.VEF_OPR_EMIAL);
            jsonObject.put("e",email);
            jsonObject.put("type", AccountApi.VEF_TYPE_BIND_EMAIL);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkUtils.post(getActivity(), AccountApi.VEF_URL,jsonObject,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("EditEmailFrag","联网请求成功"+response.toString());
                if(response.optInt("b")==1) {
                    //提示发送邮件成功
                    Intent intent = new Intent(getActivity(), SendMailSuccessActivity.class);
                    intent.putExtra("email_address",email);
                    startActivityForResult(intent, SendMailSuccessActivity.REQUEST_CODE);
                }else {
                    //提示发送邮件失败
                    GaiaApp.showToast("邮件发送失败，请重试");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("EditEmailFrag", "联网请求失败" + responseString + "-----" + throwable.toString());
                GaiaApp.showToast("联网出现问题");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SendMailSuccessActivity.REQUEST_CODE&&resultCode== Activity.RESULT_OK) {
            //完毕此片段
            getActivity().finish();
        }
    }
}
