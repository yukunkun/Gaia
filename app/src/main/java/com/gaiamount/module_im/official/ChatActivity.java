package com.gaiamount.module_im.official;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.module_im.message_center.MessageActivity;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toast.makeText(this,"暂时未开放",Toast.LENGTH_SHORT).show();
        finish();
//        int messageType = getIntent().getIntExtra(MessageActivity.MESSAGE_TYPE, -1);

        //打开会话界面
//        ChatFragment chatFragment = ChatFragment.newInstance(messageType);
//
//        getSupportFragmentManager().beginTransaction().replace(R.id.root,chatFragment).commit();
    }

}
