package com.gaiamount.module_im.official;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.module_im.message_center.MyEmMessageListener;
import com.gaiamount.module_im.message_center.MessageActivity;
import com.gaiamount.module_im.secret_chat.bean.ContentInfo;
import com.gaiamount.util.LogUtil;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by haiyang-lu on 16-5-11.
 */
public class ChatFragment extends BaseChatFragment implements View.OnClickListener {

    private int mChatType;
    private String mUserName;
    private Activity mActivity;
    private View mView;
    private TextView mToChat;
    private ImageView mChatback;
    protected List<ChatContent> chatList = new ArrayList<>();
    protected ChatListAdapter mAdapter;
    private ListView mChatListView;
    private EditText mChatInput;
    private ImageView mSendMessage;
    private List<EMMessage> mEmMessageList;
    /**
     * 消息类型
     * 目前有
     *type= 0 系统消息  1 与我相关的消息 2评论留言
     */
    private int mMessageType;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    mChatListView.setSelection(mAdapter.getCount());
                }
            });
        }
    };

    private EMMessageListener msgListener = new MyEmMessageListener(){
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            super.onMessageReceived(list);
            mEmMessageList.addAll(list);

            parseMessage(list);
            //导入数据库
            EMClient.getInstance().chatManager().importMessages(list);
        }
    };
    private int page;


    public static ChatFragment newInstance(int messageType) {

        Bundle bundle = new Bundle();
        bundle.putInt(ChatConstant.EXTRA_CHAT_TYPE, ChatConstant.CHATTYPE_SINGLE);
        if (messageType==0) {
            bundle.putString(ChatConstant.EXTRA_USER_ID,ChatConstant.USER_NAME_OFFICIAL);
        }else if (messageType==1) {
            bundle.putString(ChatConstant.EXTRA_USER_ID,ChatConstant.USER_NAME_ME_RELATED);
        }else if (messageType==2) {
            bundle.putString(ChatConstant.EXTRA_USER_ID,ChatConstant.USER_NAME_COMMENT_MESSAGE);
        }

        bundle.putInt(MessageActivity.MESSAGE_TYPE,messageType);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mChatType = arguments.getInt(ChatConstant.EXTRA_CHAT_TYPE, ChatConstant.CHATTYPE_SINGLE);
        mUserName = arguments.getString(ChatConstant.EXTRA_USER_ID);
        //消息的类型
        mMessageType = arguments.getInt(MessageActivity.MESSAGE_TYPE);

    }

    /**
     * kun 只是添加了以下两个泛方法
     */
    private void getAllMessage() {

        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        Set<Map.Entry<String, EMConversation>> entries = allConversations.entrySet();
        Iterator<Map.Entry<String, EMConversation>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, EMConversation> next = iterator.next();
            EMConversation value = next.getValue();
            if (value!=null){
                List<EMMessage> allMessages = value.getAllMessages();
                parseMessageFrom(allMessages);
                parseMessage(allMessages);
            }
        }
    }

    private void parseMessageFrom(List<EMMessage> allMessages) {
        for (EMMessage message : allMessages) {
            if (message.getChatType() == EMMessage.ChatType.Chat) {//单聊消息
                String id = message.getStringAttribute("id", "");

                if (message.getFrom().equals(ChatConstant.USER_NAME_OFFICIAL)||message.getFrom().equals(ChatConstant.USER_NAME_ME_RELATED)||
                        message.getFrom().equals(ChatConstant.USER_NAME_ADMIN)||message.getFrom().equals(ChatConstant.USER_NAME_COMMENT_MESSAGE)) {  //别人发给我的
                        ChatContent content=new ChatContent();
                    EMMessage.Type type = message.getType();
                    if (type == EMMessage.Type.TXT) {
                        EMMessageBody body = message.getBody();
                        if (body instanceof EMTextMessageBody) {
                            //body
                            EMTextMessageBody body1 = (EMTextMessageBody) body;
                            content.textContent = body1.getMessage();
                            //extra
                            try {
                                String head = message.getStringAttribute("head");
                                content.avatar = head;
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }

                            LogUtil.i(ChatFragment.class, "message", content.textContent);
                            chatList.add(content);
                            mAdapter.setChatList(chatList);
                            handler.sendEmptyMessage(0);

                        }
                    }

                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chat, container, false);

        return mView;

    }

    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mUserName);
        if (conversation!=null) {
            mEmMessageList = conversation.getAllMessages();
//            parseMessage(mEmMessageList);
        }

        EMConversation conversations = EMClient.getInstance().chatManager().getConversation(mUserName);
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        if(conversations!=null){
            conversations.markAllMessagesAsRead();
            if(conversations.getAllMsgCount()>50){
                page = 50;
            }else {
                page=conversations.getAllMsgCount();
            }
            final List<EMMessage> msgs = conversations.getAllMessages();
            int msgCount = msgs != null ? msgs.size() : 0;
            if (msgCount < conversations.getAllMsgCount() && msgCount < page) {
                String msgId = null;
                if (msgs != null && msgs.size() > 0) {
                    msgId = msgs.get(0).getMsgId();
                }
                conversations.loadMoreMsgFromDB(msgId, page - msgCount);
            }
            List<EMMessage> emMessages = conversations.getAllMessages();
            parseMessage(emMessages);
        }

    }

    @Override
    protected void initView() {
        mChatListView = (ListView) mView.findViewById(R.id.chat_list);
        mToChat = (TextView) mView.findViewById(R.id.chat_to_user_name);
        mChatback = (ImageView) mView.findViewById(R.id.chat_back);


        mAdapter = new ChatListAdapter(mActivity, chatList);
        mChatListView.setAdapter(mAdapter);

        mChatInput = (EditText) mView.findViewById(R.id.chat_input);
        mSendMessage = (ImageView) mView.findViewById(R.id.chat_send);
        getAllMessage();
    }

    @Override
    protected void setUpView() {
        mToChat.setText(mUserName);
        mChatback.setOnClickListener(this);
        mSendMessage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.chat_back) {//返回
            mActivity.finish();
        } else if (v.getId() == R.id.chat_send) {
            //发送一条消息
            String message = mChatInput.getText().toString();
            ChatContent content = new ChatContent();
            content.chatSender = ChatConstant.ME;
            content.textContent = message;
            chatList.add(content);
            mAdapter.notifyDataSetChanged();
            //隐藏输入法
            hideSoftKeyboard();
            //清楚焦点和内容
            mChatInput.clearFocus();
            mChatInput.setText("");

        }

    }

    protected void parseMessage(List<EMMessage> list) {
        for (EMMessage message : list) {

            LogUtil.d(ChatFragment.class, message.toString());

            String username;
            int attributeType = 0;
            if (message.getChatType() == EMMessage.ChatType.Chat) {//单聊消息
                username = message.getFrom();//对方名称
//                try {
//                    attributeType = message.getIntAttribute("type");
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }
                //与当前回话者比较，如果相同
                if (username.equals(mUserName)&&attributeType==mMessageType) {
                    ChatContent content = new ChatContent();
                    content.chatSender = ChatConstant.OTHER;
                    EMMessage.Type type = message.getType();

                    if (type == EMMessage.Type.TXT) {
                        EMMessageBody body = message.getBody();
                        if (body instanceof EMTextMessageBody) {
                            //body
                            EMTextMessageBody body1 = (EMTextMessageBody) body;
                            content.textContent = body1.getMessage();
                            //extra
                            try {
                                String head = message.getStringAttribute("head");
                                content.avatar = head;
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }

                            LogUtil.i(ChatFragment.class, "message", content.textContent);
                            chatList.add(content);
                            mAdapter.setChatList(chatList);
                            handler.sendEmptyMessage(0);
                        }
                    }
                } else {
                    LogUtil.w(ChatFragment.class,"不是当前会话");
                }
            }
        }
    }
}
