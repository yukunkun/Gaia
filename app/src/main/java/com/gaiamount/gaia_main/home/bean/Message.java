package com.gaiamount.gaia_main.home.bean;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import com.gaiamount.R;
import com.gaiamount.module_im.message_center.MessageActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import java.util.List;

/**
 * Created by yukun on 16-12-30.
 */
public class Message {
    private  EMMessageListener msgListener;
    private Context context;


    public void setMessage(Context context){
        this.context=context;
        getContentInfo();
    }

    public  void getContentInfo() {
        //收到透传消息getLoacalBitmap
        //收到已读回执
        //收到已送达回执
        //消息状态变动
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {

                    if (message.getFrom().equals("gaiamount0")/*||message.getFrom().equals("gaiamount2")*/) {
                        start();
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

    private  void start() {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            //此Builder为android.support.v4.app.NotificationCompat.Builder中的，下同。
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            //系统收到通知时，通知栏上面显示的文字。
            mBuilder.setTicker("通知栏上面显示的文字");
            //显示在通知栏上的小图标
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            //通知标题
            mBuilder.setContentTitle("Gaiamount");
            //通知内容
            mBuilder.setContentText("你有官方通知了~");

            //设置大图标，即通知条上左侧的图片（如果只设置了小图标，则此处会显示小图标）
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
            //显示在小图标左侧的数字
//            mBuilder.setNumber(6);
            //设置为不可清除模式
            mBuilder.setOngoing(false);
            //显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
            Intent notificationIntent =new Intent(context, MessageActivity.class);

            PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pIntent);
            //添加震动
            Notification notification=mBuilder.build();
            notification.defaults=Notification.DEFAULT_VIBRATE;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            //将notification传递给manner
            nm.notify(1, notification);

    }
}
