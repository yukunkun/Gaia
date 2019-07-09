package com.gaiamount.util;

import android.app.Activity;

import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.official.ChatConstant;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by haiyang-lu on 16-5-11.
 * 即时通信相关
 */
public class ImUtil {

    private static ImUtil mImUtil;
    private EaseConnectionListener mEaseConnectionListener;
    private Activity mActivity;

    private ImUtil(Activity activity) {
        mActivity = activity;

        mEaseConnectionListener = new EaseConnectionListener(activity);
    }

    public EaseConnectionListener getEaseConnectionListener() {
        return mEaseConnectionListener;
    }

    public static ImUtil getImUtil(Activity activity) {
        if (mImUtil==null) {
            mImUtil = new ImUtil(activity);
        }
        return mImUtil;
    }

    //实现ConnectionListener接口
    public class EaseConnectionListener implements EMConnectionListener {

        private Activity mActivity;

        public EaseConnectionListener(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            mActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(mActivity)) {
                            //连接不到聊天服务器

                        } else {
                            //当前网络不可用，请检查网络设置

                        }
                    }
                }
            });
        }
    }


    private EMMessageListener msgListener;
    public void registerEMMessageListener() {
        if (msgListener==null) {
            msgListener = new EMMessageListener() {

                @Override
                public void onMessageReceived(List<EMMessage> messages) {
                    LogUtil.d(ImUtil.class,messages.toString());
                    //收到消息
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
            EMClient.getInstance().chatManager().addMessageListener(msgListener);
        }

    }

    /**
     * 暂时的聊天的修改
     */
    public static void loginEase() {
//        String userName = GaiaApp.getUserInfo().im.getUsername();
//        String password = GaiaApp.getUserInfo().im.getPassword();
//        EMClient.getInstance().login(userName, password, new EMCallBack() {//回调
//            @Override
//            public void onSuccess() {
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
//                LogUtil.d(ImUtil.class,"登录聊天服务器成功！");
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                LogUtil.d(ImUtil.class,"登录聊天服务器失败！"+message);
//            }
//        });
    }

    /**
     * 计算每个类型的消息未读数量
     */
    public static int[] computeMessageCount() {
        int[] countList = new int[8];
//        EMChatManager emChatManager = EMClient.getInstance().chatManager();
//        //官方通知的会话
//        EMConversation conversationOfficial = emChatManager.getConversation(ChatConstant.USER_NAME_OFFICIAL);
//
//        if (conversationOfficial != null) {
//            countList[3] = conversationOfficial.getUnreadMsgCount();
//        }
//        int conversationRlelatNum=0;
//        //官方通知的会话
////        EMConversation conversationRleate = emChatManager.getConversation(ChatConstant.USER_NAME_ME_RELATED);
////
////        if (conversationOfficial != null) {
////            conversationRlelatNum = conversationRleate.getUnreadMsgCount();
////        }
//        int recommentUnReadNum=0;
////
////        EMConversation conversationRecomment = emChatManager.getConversation(ChatConstant.USER_NAME_ME_RELATED);
////
////        if (conversationOfficial != null) {
////            recommentUnReadNum = conversationRecomment.getUnreadMsgCount();
////        }
//
//
//        //私信的未读数量（所有未读-其他类的未读）
//        Map<String, EMConversation> allConversations = emChatManager.getAllConversations();
//        Iterator<Map.Entry<String, EMConversation>> iterator = allConversations.entrySet().iterator();
//        int totalUnReadMsg = 0;
//        while (iterator.hasNext()) {
//            EMConversation next = iterator.next().getValue();
//            totalUnReadMsg += next.getUnreadMsgCount();
//        }
//
//        int otherUnReadMsg = 0;
//        for (int i = 0; i < countList.length; i++) {
//            if (i != 1&&1!=2)//排除自己
//                otherUnReadMsg += countList[i];
//        }
//
//        countList[0] = totalUnReadMsg - countList[3];
//        countList[0] = totalUnReadMsg - otherUnReadMsg;
//        countList[0] = totalUnReadMsg - conversationRlelatNum;
//        countList[0] = totalUnReadMsg - recommentUnReadNum;

//        countList[0] = 0;//临时处理
        return countList;
    }

    /*---kun------------*/
    /**
     * 发送文本消息
     * @param content  发送的内容
     * @param toChatUsername  接收人的name
     */
    public void sendMessage(String content,String toChatUsername){

        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);

        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

    }

    /**
     * 发送图片
     * @param imagePath 图片路径
     * @param toChatUsername  接收人name
     */
    public void sendImage(String imagePath,String toChatUsername){
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);

        EMClient.getInstance().chatManager().sendMessage(message);
    }

}
