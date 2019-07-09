//package com.gaiamount.module_im.secret_chat.fragment;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.gaiamount.R;
//import com.gaiamount.module_im.official.ChatConstant;
//import com.gaiamount.module_im.official.ChatContent;
//import com.gaiamount.module_im.secret_chat.adapter.RecyclerViewAdapter;
//import com.gaiamount.module_im.secret_chat.bean.Contents;
//import com.gaiamount.module_im.secret_chat.model.MessageEvent;
//import com.hyphenate.chat.EMImageMessageBody;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.chat.EMMessageBody;
//import com.hyphenate.chat.EMTextMessageBody;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by yukun on 16-7-14.
// * 会话列表
// */
//public class CommunicateFragment extends Fragment {
//    RecyclerView mRecyclerView;
//    RecyclerViewAdapter recyclerViewAdapter;
//    List<Contents> contentInfos=new ArrayList<>();
//    List<EMMessage> messages;
//    protected Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    recyclerViewAdapter.notifyDataSetChanged();
//                }
//            });
//        }
//    };
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);  //注册
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//        messages=event.messages;
//        Log.i("--------eventBusGet", messages + "+");
//        paraJson(messages);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.secretcomm_fragment, null);
//        initView(view);
//
//        getInfo();
//        setAdapter();
//        return view;
//    }
//
//
//    private void initView(View view) {
//        mRecyclerView= (RecyclerView) view.findViewById(R.id.sec_fragment_recycler);
//        LinearLayoutManager linear=new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(linear);
//    }
//
//
//    private void getInfo() {
//
//    }
//
//    private void paraJson(List<EMMessage> messages) {
//        for (EMMessage message : messages) {
//            Log.i("---mess", message.toString());
//
//            Contents info = new Contents();
//
////            if (message.getChatType() == EMMessage.ChatType.Chat) {//单聊消息
//
//            ChatContent content = new ChatContent();
//            content.chatSender = ChatConstant.OTHER;
//            EMMessage.Type type = message.getType();
//
//            if (type == EMMessage.Type.TXT) {
//                EMMessageBody body = message.getBody();
//                if (body instanceof EMTextMessageBody) {
//                    //body
//                    EMTextMessageBody body1 = (EMTextMessageBody) body;
//                    content.textContent = body1.getMessage();
//                    String message1 = body1.getMessage();
//                    Log.i("--------message1", message1 + "");
//                    info.setBody(message1);
//                }
//            }
//            if (type == EMMessage.Type.IMAGE) {
//                EMMessageBody body = message.getBody();
//                if (body instanceof EMImageMessageBody) {
//                    EMImageMessageBody body1 = (EMImageMessageBody) body;
//                    String thumbnailUrl = body1.getThumbnailUrl();
//                    Log.i("--------img", thumbnailUrl + "-");
//                    info.setImageUri(thumbnailUrl);
//                }
//            }
//
//            String otherId = message.getStringAttribute("otherId", null);
//            String otherAvatar = message.getStringAttribute("otherAvatar", null);
//            String otherNickName = message.getStringAttribute("otherNickName", null);
//            info.setType(1);
//            info.setUid(message.getFrom());
//            info.setAvatar(otherAvatar);
//            info.setOtherNickName(otherNickName);
//            contentInfos.add(info);
//        }
//        handler.sendEmptyMessage(0);
//
////        }
//    }
//    private void setAdapter() {
//        recyclerViewAdapter=new RecyclerViewAdapter(getContext(),contentInfos);
//        mRecyclerView.setAdapter(recyclerViewAdapter);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);//取消注册
//    }
//
////    private EMMessageListener msgListener;
////    private void registerListener() {
////        msgListener = new EMMessageListener() {
////
////            @Override
////            public void onMessageReceived(List<EMMessage> messages) {
////                parseMessage(messages);
////            }
////
////            @Override
////            public void onCmdMessageReceived(List<EMMessage> messages) {
////                //收到透传消息
////            }
////
////            @Override
////            public void onMessageReadAckReceived(List<EMMessage> messages) {
////                //收到已读回执
////
////            }
////
////            @Override
////            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
////                //收到已送达回执
////            }
////
////            @Override
////            public void onMessageChanged(EMMessage message, Object change) {
////                //消息状态变动
////            }
////        };
////        EMClient.getInstance().chatManager().addMessageListener(msgListener);
////    }
////
////    private void parseMessage(List<EMMessage> list) {
////        for (EMMessage message : list) {
////            Log.i("---mess",message.toString());
////
////            Contents info=new Contents();
////
//////            if (message.getChatType() == EMMessage.ChatType.Chat) {//单聊消息
////
////            ChatContent content = new ChatContent();
////            content.chatSender = ChatConstant.OTHER;
////            EMMessage.Type type = message.getType();
////
////            if (type == EMMessage.Type.TXT) {
////                EMMessageBody body = message.getBody();
////                if (body instanceof EMTextMessageBody) {
////                    //body
////                    EMTextMessageBody body1 = (EMTextMessageBody) body;
////                    content.textContent = body1.getMessage();
////                    String message1 = body1.getMessage();
////                    Log.i("--------message1", message1 + "");
////                    info.setBody(message1);
////                }
////
////            }if(type==EMMessage.Type.IMAGE){
////                EMMessageBody body = message.getBody();
////                if(body instanceof EMImageMessageBody){
////                    EMImageMessageBody body1 = (EMImageMessageBody) body;
////                    String thumbnailUrl = body1.getThumbnailUrl();
////                    Log.i("--------img",thumbnailUrl+"-");
////                    info.setImageUri(thumbnailUrl);
////                }
////            }
////
////            String otherId = message.getStringAttribute("otherId", null);
////            String OtherAvatar = message.getStringAttribute("OtherAvatar", null);
////            info.setType(1);
////            info.setUid(message.getFrom());
////            info.setAvatar(OtherAvatar);
////            contentInfos.add(info);
////        }
////        handler.sendEmptyMessage(0);
////    }
//
//
//}
