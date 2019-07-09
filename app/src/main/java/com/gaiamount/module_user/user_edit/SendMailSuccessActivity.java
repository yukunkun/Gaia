package com.gaiamount.module_user.user_edit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gaiamount.R;

public class SendMailSuccessActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 10;
    private String email_address;
    private TextView mEmail;
    private Button mFinishBtn;
    private TextView mHowToReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail_success);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email_address = getIntent().getStringExtra("email_address");

        initViews();

        setListener();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        mEmail = (TextView) findViewById(R.id.email_address);
        mFinishBtn = (Button) findViewById(R.id.finish);
        mHowToReset = (TextView) findViewById(R.id.sign_how_to_reset);
    }


    private void setListener() {
        mEmail.setText(email_address);
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        mHowToReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16-3-31
            }
        });
    }


}
