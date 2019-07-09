package com.gaiamount.module_im.message_center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.gaiamount.R;
import com.gaiamount.apis.api_im.AynamicsHelper;
import com.gaiamount.gaia_main.BaseActionBarActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.my_attention.AttentionActivity;
import com.gaiamount.module_im.new_friends.FansActivity;
import com.gaiamount.module_im.official.ChatActivity;
import com.gaiamount.module_im.official.ChatConstant;
import com.gaiamount.module_im.relate_me.RelateActivity;
import com.gaiamount.module_im.scripe_apply.ScripeApplyActivity;
import com.gaiamount.module_im.secret_chat.MySecretActivity;
import com.gaiamount.module_im.secret_chat.model.MessageEvent;
import com.gaiamount.module_im.system_group_message.SysGroupMessActivity;
import com.gaiamount.util.ImUtil;
import com.gaiamount.util.SPUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;


public class MessageActivity extends BaseActionBarActivity {

    public static final String MESSAGE_TYPE = "message_type";
    /**
     * 各类消息的未读数量
     */
    private int[] mCountList = new int[8];
    private MessageTypeAdapter mAdapter;
    private ListView typeList;
    private EMMessageListener msgListener;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        typeList = (ListView) findViewById(R.id.message_type_list);
        mIvBack = findViewById(R.id.iv_back);
        //获取各类消息的数量
//        mCountList = ImUtil.computeMessageCount();
        getInfo();
        getContentInfo();
        mAdapter = new MessageTypeAdapter(this, mCountList);
        typeList.setAdapter(mAdapter);
        typeList.setOnItemClickListener(onItemClickListener);
    }

    private void getInfo() {
        for (int i = 0; i < 8; i++) {
            mCountList[i]=0;
        }
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AttentionActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
            }
        };
        AynamicsHelper.MyAttention(GaiaApp.getAppInstance().getUserInfo().id,1,getApplicationContext(),handler);
        MJsonHttpResponseHandler handler1=new MJsonHttpResponseHandler(RelateActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJsons(response);
            }
        };
        AynamicsHelper.DynamicRe(GaiaApp.getAppInstance().getUserInfo().id,1,getApplicationContext(),handler1);
    }

    private void parasJson(JSONObject response) {
        JSONObject o=response.optJSONObject("o");
        int total = o.optInt("total");
        int my_attention = (Integer) SPUtils.getInstance(this).load("my_attention", 0);
        mCountList[1]=total-my_attention;
        mAdapter.setOfficialMessageLocList(mCountList);
        mAdapter.notifyDataSetChanged();
    }

    private void parasJsons(JSONObject response) {
        JSONObject o=response.optJSONObject("o");
        int total = o.optInt("total");
        int my_attention = (int)SPUtils.getInstance(this).load("relate_me", 0);
        mCountList[2]=total-my_attention;
        mAdapter.setOfficialMessageLocList(mCountList);
        mAdapter.notifyDataSetChanged();

    }
    public  void getContentInfo() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    if (message.getFrom().equals("gaiamount0")/*||message.getFrom().equals("gaiamount2")*/) {
                        mCountList = ImUtil.computeMessageCount();
                        getInfo();
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {

                //收到透传消息
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
//        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        msgListener=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCountList = ImUtil.computeMessageCount();
        getInfo();
    }

    public void markRead(EMConversation conversation) {
        if (conversation != null) {
            conversation.markAllMessagesAsRead();
        }
    }

    //及时消息的更新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(mAdapter!=null){
            mCountList = ImUtil.computeMessageCount();
            getInfo();
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            View rootView = parent.getRootView();
            TextView textCount = (TextView) rootView.findViewById(R.id.message_type_count);
            textCount.setVisibility(View.GONE);
            //进入私信
            if (position == 0) {
//                EMClient.getInstance().chatManager().markAllConversationsAsRead();//清空聊天
                Intent intent = new Intent(MessageActivity.this, MySecretActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }

            //进入我的关注
            if (position == 1) {
                Intent intent = new Intent(MessageActivity.this, AttentionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }

            //进入与我相关
            if (position == 2) {
                Intent intent = new Intent(MessageActivity.this, RelateActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
            //进入评论留言
//            if (position == 3) {
//                Intent intent = new Intent(MessageActivity.this, CommMessActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in,R.anim.left_out);
//            }
            //点击进入官方通知
            if (position == 3) {
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                intent.putExtra(MESSAGE_TYPE, ChatConstant.EXT_SYSTEM);
//                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(ChatConstant.USER_NAME_OFFICIAL);
//                markRead(conversation);
//                getInfo();
                startActivity(intent);
            }
            //新的粉丝
            if (position == 4) {
                Intent intent = new Intent(MessageActivity.this, FansActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
            //群组系统消息
            if (position == 6) {
                Intent intent = new Intent(MessageActivity.this, SysGroupMessActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
            //剧本申请
            if (position == 7) {
                Intent intent = new Intent(MessageActivity.this, ScripeApplyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
            mAdapter.setOfficialMessageLocList(mCountList);
            mAdapter.notifyDataSetChanged();

        }
    };

}
