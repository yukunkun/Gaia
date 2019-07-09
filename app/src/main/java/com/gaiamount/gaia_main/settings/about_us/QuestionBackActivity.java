package com.gaiamount.gaia_main.settings.about_us;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.GaiaApp;

import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

/**
 * kun 问题反馈
 */
public class QuestionBackActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout mLinearLayoutName,mLinearLayoutNum,mLinearLayoutEmail;
    TextView mTextViewName,mTextViewNum,mTextViewEmail;
    EditText mEditText;
    private final  String URL="/account/feedback";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_back);
        initView();
        getInfo();
        setListener();

    }



    //初始化View
    private void initView() {
        mLinearLayoutName= (LinearLayout) findViewById(R.id.que_name);
        mLinearLayoutNum= (LinearLayout) findViewById(R.id.que_num);
        mLinearLayoutEmail= (LinearLayout) findViewById(R.id.que_email);
        mTextViewName= (TextView) findViewById(R.id.question_name);
        mTextViewNum= (TextView) findViewById(R.id.question_num);
        mTextViewEmail= (TextView) findViewById(R.id.question_email);
        mEditText= (EditText) findViewById(R.id.que_edit);
    }
    //获取用户信息
    private void getInfo() {
        String realName = GaiaApp.getUserInfo().realName;
        if(!TextUtils.isEmpty(realName)&&realName!="null"){
            mTextViewName.setText(realName);
        }
        else{
            mTextViewName.setText("");
        }
        String mobile = GaiaApp.getUserInfo().mobile;
        if(!TextUtils.isEmpty(mobile)&&mobile!="null"){
            mTextViewNum.setText(mobile);
        }else{
            mTextViewNum.setText("");
        }
        String email = GaiaApp.getUserInfo().email;
        if(!TextUtils.isEmpty(email)&&email!="null"){
            mTextViewEmail.setText(email);
        }else{
            mTextViewEmail.setText("");
        }

    }

    //设置点击的监听
    private void setListener() {
        mLinearLayoutName.setOnClickListener(this);
        mLinearLayoutNum.setOnClickListener(this);
        mLinearLayoutEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.que_name:
                selfDialoga(getString(R.string.name),mTextViewName);
                break;
            case R.id.que_num:
                selfDialoga(getString(R.string.tel_num),mTextViewNum);
                break;
            case R.id.que_email:
                selfDialoga(getString(R.string.you_email),mTextViewEmail);
                break;

        }
    }
    //自定义的对话框
    private void selfDialoga(String title, final TextView textView) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.my_dialog, null);

        builder.setTitle(title);
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                EditText editText= (EditText) view.findViewById(R.id.my_dialog_edit);
                textView.setText(editText.getText());
            }
        });

        builder.setNegativeButton(getString(R.string.give_up), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //发送消息
    public void SendMessage(View view) {
        String name = mTextViewName.getText().toString();
        String number = mTextViewNum.getText().toString();
        String email = mTextViewEmail.getText().toString();
        String content = mEditText.getText().toString();

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(QuestionBackActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast(getString(R.string.return_success));
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                GaiaApp.showToast(getString(R.string.return_fail));

            }
        };
        AccountApiHelper.sendFeedBack(name,number,email,content,this,handler);
    }
    //返回键
    public void QueBack(View view) {
        finish();
        overridePendingTransition(0,R.anim.push_left_out);
    }

}
