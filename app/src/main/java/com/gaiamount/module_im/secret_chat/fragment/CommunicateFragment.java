package com.gaiamount.module_im.secret_chat.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.secret_chat.adapter.RecyclerViewAdapter;
import com.gaiamount.module_im.secret_chat.bean.ContentInfo;
import com.gaiamount.module_im.secret_chat.model.MessageEvent;
import com.gaiamount.module_im.secret_chat.model.OnEventNew;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yukun on 16-7-14.
 * 会话列表
 */
public class CommunicateFragment extends Fragment {
    RecyclerView mRecyclerView;
    RelativeLayout layoutTiShi;
    RecyclerViewAdapter recyclerViewAdapter;
    List<ContentInfo> contentInfos=new ArrayList<>();
    List<EMMessage> sendMessage;
    String message1;
    String myUid;

    List<EMMessage> messages;
    private long msgTime=0;
    private boolean tag=false;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            });
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);  //注册
    }

    /**
     *  eventBus的回调,实时刷新
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        messages=event.getMessages();
        layoutTiShi.setVisibility(View.GONE);
        contentInfos.clear();
        getAllMessage();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    //会话的回调接收
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventNew(OnEventNew event){
        contentInfos.clear();
        getAllMessage();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.secretcomm_fragment, null);
        myUid=GaiaApp.getAppInstance().getUserInfo().id+"";
        initView(view);
        getInfo();
        setAdapter();
        getChatMess();
        recyclerViewAdapter.notifyDataSetChanged();
        return view;
    }

    private void getChatMess() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(GaiaApp.getAppInstance().getUserInfo().id+"");
        //获取此会话的所有消息
        if(conversation!=null){
            List<EMMessage> messages = conversation.getAllMessages();

        }
    }


    private void initView(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.sec_fragment_recycler);
        LinearLayoutManager linear=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linear);
        layoutTiShi= (RelativeLayout) view.findViewById(R.id.sec_tishi);
        getAllMessage();
    }

    /**
     * 获取到的聊天数据
     */
    private void getAllMessage() {

        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        Set<Map.Entry<String, EMConversation>> entries = allConversations.entrySet();
        Iterator<Map.Entry<String, EMConversation>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, EMConversation> next = iterator.next();
            EMConversation value = next.getValue();
            int unreadMsgCount = value.getUnreadMsgCount();

            if (value!=null){
                EMMessage lastMessage = value.getLastMessage();
                List<EMMessage> allMessages = value.getAllMessages();
                layoutTiShi.setVisibility(View.GONE);
                if (lastMessage!=null){
                    msgTime = lastMessage.getMsgTime();
                }
                paraJson(allMessages,msgTime,unreadMsgCount);
            }
        }
    }

    private void getInfo() {

    }

    /**
     *  解析获取到的数据
     * @param messages
     * @param msgTime
     * @param unreadMsgCount
     */
    private void paraJson(List<EMMessage> messages, long msgTime, int unreadMsgCount) {
        for (EMMessage message : messages) {
            if (message.getFrom().equals(message.getTo())||message.getTo().equals("gaiamount1")||
                    message.getFrom().equals("gaiamount1")||message.getTo().equals("gaiamount0")||
                    message.getFrom().equals("gaiamount0")||message.getTo().isEmpty()||message.getFrom().isEmpty()) {
                return;
            } else  if(message.getChatType() == EMMessage.ChatType.Chat) {//群聊消息
                ContentInfo info = new ContentInfo();
                EMMessage.Type type = message.getType();
                if (type == EMMessage.Type.TXT) {
                    EMMessageBody body = message.getBody();
                    if (body instanceof EMTextMessageBody) {
                        //body
                        EMTextMessageBody body1 = (EMTextMessageBody) body;
                        message1 = body1.getMessage();
                        info.setBody(message1);
                    }
                }
                if (type == EMMessage.Type.IMAGE) {
                    EMMessageBody body = message.getBody();
                    if (body instanceof EMImageMessageBody) {
                        EMImageMessageBody body1 = (EMImageMessageBody) body;
                        String thumbnailUrl = body1.getThumbnailUrl();
                        info.setImageUri(thumbnailUrl);
                    }
                }

                String otherNickName = message.getStringAttribute("otherNickName", null);
                String otherAvatar = message.getStringAttribute("otherAvatar", null);
                String nickName = message.getStringAttribute("nickName", null);
                String head = message.getStringAttribute("head", null);

                String ID="";
                String HEAD="";
                String NAME="";
                long TIME=0;
                int cishu=0;

                if(myUid.equals(message.getFrom())){//我发给别人的
                    ID=message.getTo();
                    HEAD=otherAvatar;
                    TIME=msgTime;
                    NAME=otherNickName;
                }
                if(myUid.equals(message.getTo())){//别人发给我的
                    ID=message.getFrom();
                    HEAD=head;
                    TIME=msgTime;
                    NAME=nickName;
                }

                boolean choose = choose(ID, TIME, msgTime,unreadMsgCount,message1);
                if(!choose){
                    info.setUid(ID);
                    info.setOtherNickName(NAME);
                    info.setAvatar(HEAD);
                    info.setGroup("person");
                    info.setTime(getTime(TIME));
                    info.setType(cishu);
                    info.setUnReadCount(unreadMsgCount);
                    contentInfos.add(info);

                }

                handler.sendEmptyMessage(0);
            }else if(message.getChatType() == EMMessage.ChatType.GroupChat){ //群聊的消息设置

                ContentInfo info = new ContentInfo();
                EMMessage.Type type = message.getType();
                if (type == EMMessage.Type.TXT) {
                    EMMessageBody body = message.getBody();
                    if (body instanceof EMTextMessageBody) {
                        //body
                        EMTextMessageBody body1 = (EMTextMessageBody) body;
                        message1 = body1.getMessage();
                        info.setBody(message1);
                    }
                }
                if (type == EMMessage.Type.IMAGE) {
                    EMMessageBody body = message.getBody();
                    if (body instanceof EMImageMessageBody) {
                        EMImageMessageBody body1 = (EMImageMessageBody) body;
                        String thumbnailUrl = body1.getThumbnailUrl();
                        info.setImageUri(thumbnailUrl);
                    }
                }

                String otherNickName = message.getStringAttribute("otherNickName", "");
                String otherAvatar = message.getStringAttribute("otherAvatar", "");
                String nickName = message.getStringAttribute("nickName", "");
                String head = message.getStringAttribute("head", "");
                String imid=message.getStringAttribute("imid","");
                String groupId=message.getStringAttribute("groupId","");
                String ID="";
                String HEAD="";
                String NAME="";
                long TIME=0;
                int cishu=0;
                if(imid.isEmpty()||groupId.isEmpty()){
                    return;
                }
                if(imid.equals(message.getTo())){//别人发给群的消息
                    ID=message.getTo();
                    HEAD=otherAvatar;
                    TIME=msgTime;
                    NAME=otherNickName;
                }

                boolean chooses = chooseGroup(ID, TIME, msgTime,unreadMsgCount,imid,message1);
                if(!chooses){
                    info.setUid(ID);
                    info.setOtherNickName(NAME);
                    info.setAvatar(HEAD);
                    info.setTime(getTime(TIME));
                    info.setType(cishu);
                    info.setGroup("group");
                    info.setGroupId(groupId);
                    info.setUnReadCount(unreadMsgCount);
                    contentInfos.add(info);

                }
                handler.sendEmptyMessage(0);
            }
        }
    }
    //控制小组的刷新
    private boolean chooseGroup(String ID, long TIME, long msgTime, int unreadMsgCount,String imid,String s) {
        for (int i = 0; i < contentInfos.size(); i++) {

            if(contentInfos.get(i).getUid().equals(imid)){
                contentInfos.get(i).setTime(getTime(msgTime));
                contentInfos.get(i).setUnReadCount(unreadMsgCount);
                contentInfos.get(i).setBody(s);
                return true;
            }
        }
        return false;
    }


    /**
     * 控制单聊的刷新
     * @param ID
     * @param TIME
     * @param msgTime
     * @param unreadMsgCount
     * @param message1
     * @return
     */
    private boolean choose(String ID, long TIME, long msgTime, int unreadMsgCount, String message1) {
        for (int i = 0; i < contentInfos.size(); i++) {
            if(contentInfos.get(i).getUid().equals(ID)){
                contentInfos.get(i).setTime(getTime(msgTime));
                contentInfos.get(i).setUnReadCount(unreadMsgCount);
                contentInfos.get(i).setBody(message1);
                contentInfos.get(i).setType((contentInfos.get(i).getType()+1));
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param msgTime 获取到的时间
     * @return
     */
    private String getTime(Long msgTime) {
        long l = System.currentTimeMillis();
        if(l-msgTime<3600000){
            SimpleDateFormat formatter = new SimpleDateFormat    ("mm");
            Date curDate = new Date(msgTime);//获取时间
            String str1 = formatter.format(curDate);
            Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate2);
            int i = Integer.valueOf(str) - Integer.valueOf(str1);
            if(i>=0){
                return i+getContext().getResources().getString(R.string.time_minute_ago);
            }else {
                return 60+i+getContext().getResources().getString(R.string.time_minute_ago);
            }
        }
        if(3600000<=l-msgTime&l-msgTime<86400000/2){
            SimpleDateFormat formatter = new SimpleDateFormat    ("hh");
            Date curDate = new Date(msgTime);//获取时间
            String str1 = formatter.format(curDate);
            Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate2);
            int i = Integer.valueOf(str) - Integer.valueOf(str1);
            if(i>=0){
                return i+getContext().getResources().getString(R.string.time_hours_ago);
            }else {
                return 12+i+getContext().getResources().getString(R.string.time_hours_ago);
            }
        }else if(l-msgTime>86400000/2 & l-msgTime<=86400000*2){
            return getContext().getResources().getString(R.string.one_day_ago);
        }else if(l-msgTime>86400000*2 & l-msgTime<=86400000*3){
            return getContext().getResources().getString(R.string.two_day_ago);
        }else if(l-msgTime>86400000*3 & l-msgTime<=86400000*4) {
            return getContext().getResources().getString(R.string.three_day_ago);
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat    ("yyyy/MM/dd");
            Date curDate = new Date(msgTime);//获取时间
            return formatter.format(curDate);
        }
    }

    private void setAdapter() {
        recyclerViewAdapter=new RecyclerViewAdapter(getContext(),contentInfos);
        mRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }


}
