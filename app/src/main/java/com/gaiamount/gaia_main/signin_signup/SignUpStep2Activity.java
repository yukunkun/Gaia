package com.gaiamount.gaia_main.signin_signup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gaiamount.R;
import com.gaiamount.gaia_main.BaseActionBarActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.apis.api_user.AccountApi;
import com.gaiamount.util.ErrorUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpStep2Activity extends BaseActionBarActivity {

    private TextView phoneNumber;
    private EditText sign_input_vertify_code;
    private TextView prompt;
    private Button next;
    private String phone_number;
    private int refreshTime = 60;

    @SuppressWarnings("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SpannableStringBuilder text = new SpannableStringBuilder(refreshTime+"s"+"后可重新发送");
            text.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            prompt.setText(text);
            if(refreshTime>0) {
                refreshTime--;
                sendEmptyMessageDelayed(0, 1000);
            } else {
                prompt.setClickable(true);
                prompt.setText(R.string.resend);
                prompt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendVertifyCode();
                    }
                });
                return;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GaiaApp.getAppInstance().getActivityList().add(this);

        setContentView(R.layout.activity_sign_up_step2);
        //手机号码（带前缀）
        phone_number = getIntent().getStringExtra("phone_number");

        initView();

        phoneNumber.setText(phone_number);
    }



    private void initView() {
        phoneNumber = (TextView) findViewById(R.id.txt2);
        sign_input_vertify_code = (EditText) findViewById(R.id.sign_input_vertify_code);
        sign_input_vertify_code.requestFocus();
        prompt = (TextView) findViewById(R.id.sign_time_prompt);
        next = (Button) findViewById(R.id.sign_next);
        sign_input_vertify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)) {
                    next.setClickable(true);
                } else {
                    next.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(sign_input_vertify_code.getText().toString())) {
                    Intent intent = new Intent(SignUpStep2Activity.this,SignUpStep3Activity.class);
                    intent.putExtra("phone_number",phone_number);
                    intent.putExtra("vertify_code",sign_input_vertify_code.getText().toString().trim());
                    startActivity(intent);
                } else {
                    GaiaApp.showToast("验证码不能为空");
                }

            }
        });
        handler.sendEmptyMessage(0);
    }


    private void sendVertifyCode() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //验证成功，发送验证码
        Map<String, String> params = new HashMap<>();
        params.put("opr", "2");
        params.put("type","1");//随便传一个
        params.put("m", phone_number);

        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest sendVerfityCode = new JsonObjectRequest(Request.Method.POST, AccountApi.VEF_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("SignUpStep2:重发成功", "发送验证码请求失败" + response.toString());
                if(response.optInt("b")==1) {
                    GaiaApp.showToast(getString(R.string.send_vertify_code_success));
                    handler.sendEmptyMessage(0);
                } else {
                    ErrorUtils.processError(response.optJSONArray("ec"),response.optInt("i"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SignUpStep2:重发失败", "发送验证码请求失败" + error.toString());
            }
        });
        requestQueue.add(sendVerfityCode);
        requestQueue.start();
    }
}
