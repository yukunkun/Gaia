package com.gaiamount.gaia_main.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.gaiamount.apis.Configs;
import com.gaiamount.dialogs.CountryPickerDlg;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpStep1Activity extends BaseActionBarActivity implements View.OnClickListener {

    private Button sign_choose_nation;
    private Button sign_show_phone_number;
    private EditText sign_edit_phone;
    private Button sign_send_vertify_code;
    /**
     * 前缀，区号
     */
    private String prefix="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step1);

        GaiaApp.getAppInstance().getActivityList().add(this);

        sign_choose_nation = (Button) findViewById(R.id.sign_choose_nation);
        sign_show_phone_number = (Button) findViewById(R.id.sign_show_phone_number_prefix);
        sign_edit_phone = (EditText) findViewById(R.id.sign_edit_phone);
        sign_send_vertify_code = (Button) findViewById(R.id.sign_send_vertify_code);

        setListener();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Configs.PICK_COUNTRY_CODE_REQUEST:
                if (resultCode == RESULT_OK) {
                    String prefixText;
                    prefixText = data.getStringExtra(Configs.PICK_COUNTRY_CODE_KEY);
                    prefix = "00"+prefixText.substring(1);
                    if("0086".equals(prefix)) {
                        prefix="";
                    }
                    Log.d("SignUpStep1,prefix",prefix);
                    //区号
                    sign_show_phone_number.setText(data.getStringExtra(Configs.PICK_COUNTRY_CODE_KEY));
                    //国家名称
                    sign_choose_nation.setText(data.getStringExtra("nation_name"));
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setListener() {
        sign_choose_nation.setOnClickListener(this);
        sign_send_vertify_code.setOnClickListener(this);
        sign_edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0) {
                    //隐藏区号
                    sign_show_phone_number.setVisibility(View.INVISIBLE);
                }else {
                    //显示区号
                    sign_show_phone_number.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_choose_nation:
                startActivityForResult(new Intent(SignUpStep1Activity.this, CountryPickerDlg.class),
                        Configs.PICK_COUNTRY_CODE_REQUEST,
                        null);
                break;
            case R.id.sign_send_vertify_code:
                sendVertifyCode();
                break;
            default:
                break;
        }
    }

    private void sendVertifyCode() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //验证成功，发送验证码
        Map<String, String> params = new HashMap<>();
        params.put("opr", "2");
        params.put("type","1");//随便传一个
        params.put("m", prefix+sign_edit_phone.getText().toString());

        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest sendVerfityCode = new JsonObjectRequest(Request.Method.POST, AccountApi.VEF_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("SignUpStep1:成功", "发送验证码请求失败" + response.toString());
                int b = response.optInt("b");
                if (b == 1) {
                    //发送成功
                    Intent intent = new Intent(SignUpStep1Activity.this,SignUpStep2Activity.class);
                    intent.putExtra("phone_number",prefix+sign_edit_phone.getText().toString());
                    startActivity(intent);

                } else {
                    GaiaApp.showToast("发送不成功，请检查手机格式或重试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SignUpStep1:失败", "发送验证码请求失败" + error.toString());
            }
        });
        requestQueue.add(sendVerfityCode);
        requestQueue.start();
    }

}
