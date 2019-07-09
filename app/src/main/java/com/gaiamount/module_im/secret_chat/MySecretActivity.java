package com.gaiamount.module_im.secret_chat;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_im.ImApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.secret_chat.adapter.SecViewPagerAdapter;
import com.gaiamount.module_im.secret_chat.bean.OnEventChatGroup;
import com.gaiamount.module_im.secret_chat.fragment.CommunicateFragment;
import com.gaiamount.module_im.secret_chat.fragment.ContentPerson;
import com.gaiamount.module_im.secret_chat.fragment.GroupFragment;
import com.gaiamount.module_im.secret_chat.model.MessageEvent;
import com.gaiamount.util.network.NetworkUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

/**
 * kun
 *
 */
public class MySecretActivity extends AppCompatActivity {
    ViewPager mViewPager;
    TabLayout mTabLayout;
    SecViewPagerAdapter secViewPagerAdapter;
    ArrayList<Fragment> fragments;
    String[] strings;
    boolean tag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_secret);
        EventBus.getDefault().register(this);
        Toast.makeText(this,"暂时未开放",Toast.LENGTH_SHORT).show();
        finish();
//        init();
//        initToken();
//        initSet();
//        initFragment();
//        getContentInfo();
//        setAdapter();
//        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (msgListener!=null) {
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void getMesageSend(OnEventChatGroup chatGroup){
        getContentInfo();
    }

    //获取ImToken，且添加到Application中
    private void initToken() {
        JsonHttpResponseHandler jsonHttpResponseHandler=new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                GaiaApp.setImToken(response.optString("o"));
            }
        };
        ImApiHelper.getToken(jsonHttpResponseHandler);

    }

    //初始化view
    private void init() {
        mViewPager= (ViewPager) findViewById(R.id.sec_viewpager);
        mTabLayout= (TabLayout) findViewById(R.id.sec_tablayout);
    }
    //设置tablayout的内容
    private void initSet() {
        strings=getApplicationContext().getResources().getStringArray(R.array.sec_tab);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < 3; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(strings[i]));
        }
    }
    //添加viewpager的fragment
    private void initFragment() {
        fragments=new ArrayList<>();
        CommunicateFragment fragment=new CommunicateFragment();
        fragments.add(fragment);
        ContentPerson person=new ContentPerson();
        fragments.add(person);
        GroupFragment groupFragment=new GroupFragment();
        fragments.add(groupFragment);

    }
    //设置适配器
    private void setAdapter() {
        secViewPagerAdapter=new SecViewPagerAdapter(getSupportFragmentManager(),fragments,strings);
        mViewPager.setAdapter(secViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
    }

    //设置联动效果
    private void setListener() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTabLayout.setScrollPosition(position,0,false);
            }
        });
    }

    private EMMessageListener msgListener;
    private void getContentInfo() {
        //收到透传消息getLoacalBitmap
        //收到已读回执
        //收到已送达回执
        //消息状态变动
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                Log.i("---activity","activity");
                EventBus.getDefault().post(new MessageEvent(messages));
//                EventBus.getDefault().post(new MessageEvent(messages,tag));

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                Log.i("---activity","onCmdMessageReceived");

                //收到透传消息
//                EMClient.getInstance().chatManager().importMessages(messages);
//                Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
//                Set<Map.Entry<String, EMConversation>> entries = allConversations.entrySet();
//                Iterator<Map.Entry<String, EMConversation>> iterator = entries.iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry<String, EMConversation> next = iterator.next();
//                    EMConversation value = next.getValue();
//                    value.getUserName();
//                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private void getInf(List<EMMessage> messages) {

//            Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
//            Set<Map.Entry<String, EMConversation>> entries = allConversations.entrySet();
//            Iterator<Map.Entry<String, EMConversation>> iterator = entries.iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, EMConversation> next = iterator.next();
//                EMConversation value = next.getValue();
//                String key = next.getKey();
//                EMMessage lastMessage = value.getLastMessage();
//                String userName = value.getUserName();
//
//                List<EMMessage> allMessages = value.getAllMessages();
////                Log.i("---all",allMessages.toString()+"+"+userName);
//            }
    }


    //返回键
    public void SecBack(View view) {
        finish();
    }

}

