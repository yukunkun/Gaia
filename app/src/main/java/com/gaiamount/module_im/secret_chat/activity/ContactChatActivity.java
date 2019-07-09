package com.gaiamount.module_im.secret_chat.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.apis.api_im.ImApiHelper;
import com.gaiamount.gaia_main.GaiaApp;

/*
*   kun  群聊页面
* */

import com.gaiamount.module_im.secret_chat.adapter.ChatAdapter;
import com.gaiamount.module_im.secret_chat.bean.ContentInfo;
import com.gaiamount.module_im.secret_chat.model.MessageEvent;
import com.gaiamount.module_im.secret_chat.model.OnEventNew;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
*   kun 单聊页面
*
* */
public class ContactChatActivity extends AppCompatActivity {
    private ListView mListView;
    ChatAdapter chatAdapter;
    int PHOTO_FROM_MAP=0;
    int TAKE_PICTURE=1;
    ArrayList<ContentInfo> contentInfos=new ArrayList<>();
    private EMMessageListener msgListener;
    private EditText mEditText;
    private TextView mTextViewTitle;
    private String otherAvatar;
    private String otherNickName;
    private String otherId;
    private String myUid;
    private String nickName;
    private String myAvatar;
    private List<EMMessage> messages;
    private TextView photoFromMap,photoFromCarmer,photoQuit;
    private PopupWindow  popupWindow;
    private List<EMMessage> msgs=new ArrayList<>();
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                    mListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
                }
            });
        }
    };
    private int page;
    private FileOutputStream out;
    private String fileNames;
    private String mPath;

    //eventBus接收
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        messages=event.getMessages();
        parseMessage(messages);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);  //注册

        setContentView(R.layout.activity_contact_chat);
//        Intent intent = getIntent();
//        otherAvatar=intent.getStringExtra("otherAvatar");
//        otherId=intent.getStringExtra("otherId");
//        otherNickName=intent.getStringExtra("otherNickName");
//        myUid=GaiaApp.getAppInstance().getUserInfo().id+"";
//        myAvatar=GaiaApp.getAppInstance().getUserInfo().avatar;
//        nickName=GaiaApp.getAppInstance().getUserInfo().nickName;
//        init();
//        setAdapter();
//        getAllMessage();
//        setListener();
        Toast.makeText(this,"暂未开放",Toast.LENGTH_SHORT).show();
        finish();

    }

    private void init() {
        mListView= (ListView) findViewById(R.id.chat_listview);
        mEditText= (EditText) findViewById(R.id.edit_contant);
        mTextViewTitle= (TextView) findViewById(R.id.text_title);
        mTextViewTitle.setText(otherNickName);
    }

    private void setAdapter() {
        chatAdapter=new ChatAdapter(getApplicationContext(),contentInfos);
        mListView.setAdapter(chatAdapter);
    }

    /**
     * 获取数据
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
//                parseMessageFrom(allMessages);

            }
        }

        EMConversation conversations = EMClient.getInstance().chatManager().getConversation(otherId);
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
            parseMessageFrom(emMessages);
        }
    }

    /**
     * 数据的适配
     * @param allMessages
     */
    private void parseMessageFrom(List<EMMessage> allMessages) {
        for (EMMessage message : allMessages) {
            if (message.getChatType() == EMMessage.ChatType.Chat) {//单聊消息

                String id = message.getStringAttribute("id", "");

                if (message.getFrom().equals(otherId)) {  //别人发给我的
                    ContentInfo info = new ContentInfo();
                    info.setType(1);
                    EMMessage.Type type = message.getType();

                    if (type == EMMessage.Type.TXT) {
                        EMMessageBody body = message.getBody();
                        if (body instanceof EMTextMessageBody) {
                            //body
                            EMTextMessageBody body1 = (EMTextMessageBody) body;
                            String message1 = body1.getMessage();
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
                    String otherAvatar = message.getStringAttribute("head", "");
                    info.setUid(id);
                    info.setChose(id);
                    info.setOtherNickName(otherNickName);
                    info.setOtherId(message.getFrom());
                    info.setAvatar(otherAvatar);
                    contentInfos.add(info);

                    handler.sendEmptyMessage(0);

                } else if (message.getTo().equals(otherId)/*&&message.getTo().equals()*/) {  //我发给别人的
                    ContentInfo info = new ContentInfo();
                    info.setType(0);
                    EMMessage.Type type = message.getType();

                    // Log.i("----list",message.toString()+"+");
                    if (type == EMMessage.Type.TXT) {
                        EMMessageBody body = message.getBody();
                        if (body instanceof EMTextMessageBody) {
                            //body
                            EMTextMessageBody body1 = (EMTextMessageBody) body;
                            String message1 = body1.getMessage();
                            info.setBody(message1);
                        }
                    }
                    if (type == EMMessage.Type.IMAGE) {
                        EMMessageBody body = message.getBody();
                        if (body instanceof EMImageMessageBody) {
                            EMImageMessageBody body1 = (EMImageMessageBody) body;
                            String localUrl = body1.getLocalUrl();
                            info.setImageUri(localUrl);
                        }
                    }
                    String otherNickName = message.getStringAttribute("otherNickName", "");
                    String otherAvatar = message.getStringAttribute("head", "");

                    info.setUid(id);
                    info.setChose(id);
                    info.setOtherNickName(otherNickName);
                    info.setOtherId(message.getTo());
                    info.setAvatar(otherAvatar);
                    contentInfos.add(info);

                    //标记为已读信息
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(otherId);
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();

                    handler.sendEmptyMessage(0);


                }

            }
        }
    }

    /**
     * 接收到的数据的适配
     * @param list
     */
    private void parseMessage(List<EMMessage> list) {
        for (EMMessage message : list) {
//            Log.i("-----chatMessage",message.toString());
            if (message.getChatType() == EMMessage.ChatType.Chat) {//单聊消息
                String id = message.getStringAttribute("id", "");
//                Log.i("----id",id+"++");

                if (otherId.equals(message.getFrom()+"")) {
                    ContentInfo info = new ContentInfo();
                    EMMessage.Type type = message.getType();

//                    Log.i("----list",message.toString()+"+");
                    if (type == EMMessage.Type.TXT) {
                        EMMessageBody body = message.getBody();
                        if (body instanceof EMTextMessageBody) {
                            //body
                            EMTextMessageBody body1 = (EMTextMessageBody) body;
                            String message1 = body1.getMessage();
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
                    String otherAvatar = message.getStringAttribute("head", "");
                    if(message.getFrom().equals(myUid)){
                        info.setType(0);
                    }else{
                        info.setType(1);
                    }
                    info.setUid(id);
                    info.setChose(id);
                    info.setOtherNickName(otherNickName);
                    info.setOtherId(message.getFrom());
                    info.setAvatar(otherAvatar);
                    contentInfos.add(info);

                    //标记为已读信息
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(otherId);
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();

                    handler.sendEmptyMessage(0);
                }
            }
        }
    }


    private void setListener() {
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SendInfo();
                     /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 发送消息的方法
     */
    public void SendInfo() {
        String content = mEditText.getText().toString();
        if(content.isEmpty()) {
            GaiaApp.showToast(getString(R.string.cant_be_null));
           return;
        }

        EMMessage message = EMMessage.createTxtSendMessage(content, otherId);
        // 增加自己特定的属性
        message.setAttribute("id", myUid);
        message.setAttribute("head", myAvatar);
        message.setAttribute("otherNickName",otherNickName);
        message.setAttribute("imid", otherId);
        message.setAttribute("toID", otherId);
        message.setAttribute("currType", "singlechat");
        message.setAttribute("nickName",nickName);
        message.setAttribute("otherAvatar", otherAvatar);
        message.setAttribute("groupId",-1+"");
        EMClient.getInstance().chatManager().sendMessage(message);
        //设置值
        ContentInfo info=new ContentInfo();
        info.setType(0);
        info.setChose(myUid);
        info.setBody(mEditText.getText().toString());
        info.setUid(myUid);
        info.setAvatar(GaiaApp.getAppInstance().getUserInfo().avatar);
        contentInfos.add(info);
        mEditText.setText("");
        handler.sendEmptyMessage(0);

        //让会话页面更新的回调
        EventBus.getDefault().post(new OnEventNew(1));
        //存储数据到环信
        msgs.clear();
        msgs.add(message);
        EMClient.getInstance().chatManager().importMessages(msgs);
        message.setMessageStatusCallback(new EMCallBack(){
            @Override
            public void onSuccess() {
                Log.i("chatactivity","发送成功");
            }

            @Override
            public void onError(int i, String s) {
                Log.i("chatactivity","发送失败:"+s);
                GaiaApp.showToast("发送失败");

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }

    public void OpenPhoto(View view) {
        try{
            getPermission();
        }catch (Exception e){
            GaiaApp.showToast("请重新登录登录");
            e.printStackTrace();
        }

//        pupuWindow();
//        hintKey();
    }
    //6.0的权限问题
    private void getPermission() {

        if (ContextCompat.checkSelfPermission(ContactChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
             if( ContextCompat.checkSelfPermission(ContactChatActivity.this, Manifest.permission.CAMERA)
                     != PackageManager.PERMISSION_GRANTED){
                        //两个权限都没有
                        ActivityCompat.requestPermissions(ContactChatActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 1);
             }else {
                 //申请第一个权限
                 ActivityCompat.requestPermissions(ContactChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 1);
            }
        }else if( ContextCompat.checkSelfPermission(ContactChatActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
                    //有第一个权限,没有第二个权限
                    ActivityCompat.requestPermissions(ContactChatActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                }else {
                    //两个都有
                    pupuWindow();
                    hintKey();
                }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pupuWindow();
                    hintKey();
                } else {
                   AlertDialog dialog = new AlertDialog.Builder(this)
                           .setMessage("需要赋予访问存储的权限，不开启将无法正常工作！且可能被强制退出登录")
                           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   finish();
                               }
                           })
                           .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   finish();
                               }
                           }).create();
                    dialog.show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    String imagePath=null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 0: //图库选择
                    if(data==null){
                        return;
                    }
                    if ((data.getData()) != null) { //防止没有返回结果
                        Uri uri = data.getData();
                        if (uri != null) {
                            //dismiss
                            if (popupWindow!=null&&popupWindow.isShowing()) {
                                popupWindow.dismiss();
                            }
                            imagePath = getAbsoluteImagePath(uri);
                            //设置值
                            ContentInfo info1=new ContentInfo();
                            info1.setUid(GaiaApp.getAppInstance().getUserInfo().id +"");
                            info1.setAvatar(GaiaApp.getAppInstance().getUserInfo().avatar);
                            info1.setImageUri(imagePath);
                            info1.setType(0);
                            info1.setChose(myUid);
                            contentInfos.add(info1);
                            chatAdapter.notifyDataSetChanged();
                            sendImage(imagePath,otherId+"");  //发送图片
                            mListView.smoothScrollToPosition(contentInfos.size()-1);
                        }else if(data.getExtras() != null){

                        }
                    }else{
                        GaiaApp.showToast(getString(R.string.cant_find_img));
                    }
                    break;
                case 1:     //拍照的照片获取
                    File file=new File(mPath);
                    if(file.exists()){
                        ContentInfo info1=new ContentInfo();
                        info1.setUid(GaiaApp.getAppInstance().getUserInfo().id+"");
                        info1.setAvatar(GaiaApp.getAppInstance().getUserInfo().avatar);
                        info1.setImageUri(mPath);
                        info1.setType(0);
                        info1.setChose(myUid);
                        contentInfos.add(info1);
                        chatAdapter.notifyDataSetChanged();
                        sendImage(mPath,otherId+"");  //发送图片
                        mListView.smoothScrollToPosition(contentInfos.size()-1);
                    }else {
                        GaiaApp.showToast(getString(R.string.send_img_fail));
                    }
                    break;
        }
    }

    /**
     * 发送图片
     * @param imagePath 图片的地址
     * @param otherId 发送对方的id
     */
    private void sendImage(String imagePath, String otherId) {

        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, otherId);
        //如果是群聊，设置chattype，默认是单聊
        message.setAttribute("id", myUid);
        message.setAttribute("head", myAvatar);
        message.setAttribute("otherNickName",otherNickName);
        message.setAttribute("imid", otherId);
        message.setAttribute("nickName",nickName);
        message.setAttribute("toID", otherId);
        message.setAttribute("currType", "singlechat");
        message.setAttribute("otherAvatar", otherAvatar);
        message.setAttribute("groupId",-1+"");
        EMClient.getInstance().chatManager().sendMessage(message);
        msgs.add(message);
        EMClient.getInstance().chatManager().importMessages(msgs);
        message.setMessageStatusCallback(new EMCallBack(){
            @Override
            public void onSuccess() {
                Log.i("chatactivity","发送成功");
            }

            @Override
            public void onError(int i, String s) {
                Log.i("chatactivity","发送失败:"+s);
                GaiaApp.showToast("发送失败");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    /**
     * 弹窗pupupwindow
     */
    private void pupuWindow() {
        View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_popuwindow, null);
        popupWindow=new PopupWindow(inflate,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(this.findViewById(R.id.start), Gravity.BOTTOM,0,0);

        photoFromMap= (TextView) inflate.findViewById(R.id.choose_from_photo);
        photoFromCarmer= (TextView) inflate.findViewById(R.id.choose_from_carmer);
        photoQuit= (TextView) inflate.findViewById(R.id.choose_quit);
        //选择照方式
        photoFromMap.setOnClickListener(new Listener());
        photoFromCarmer.setOnClickListener(new Listener());
        photoQuit.setOnClickListener(new Listener());
    }

    /**
     * 弹窗的点击事件
     */
    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.choose_from_photo:
                    chooseFromPhotos();
                    popupWindow.dismiss();
                    break;
                case R.id.choose_from_carmer:
                    chooseFromPhoto();
                    popupWindow.dismiss();
                    break;
                case R.id.choose_quit:
                    popupWindow.dismiss();
            }
        }
    }

    /**
     * 选择来自相册的action
     */
    private void chooseFromPhoto() {

        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gaiamount" ;
        //存储文件夹操作
        File outFilePath = new File(mFilePath);
        if (!outFilePath.exists()) {
            outFilePath.mkdirs();
        }
        //设置自定义照片的名字
        String fileNames = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        mFilePath = mFilePath + "/" + fileNames + ".jpg";
        mPath = mFilePath;
        File outFile = new File(mFilePath);
        Uri uri = Uri.fromFile(outFile);
        //拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1);

//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 选择来自照相机的图片的action
     */
    public void chooseFromPhotos() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_FROM_MAP);
    }

    /**
     * 隐藏软件盘
     */
    public void hintKey(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
    //将Uri转化成为path路径
    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( uri,
                proj,       // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null);      // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gaiamount/sendimg.jpg");
        if(f.exists()){
            f.delete();
        }
    }
    //返回
    public void ChatBack(View view) {
        finish();
    }
    //拉黑好友按钮
    public void GiveUpFri(View view) {
        selfDialoga();
    }


    /**
     * 拉黑好友
     */
    public void KillFri() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ContactChatActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
            }
        };
        ImApiHelper.killFri(Long.valueOf(myUid),Long.valueOf(otherId),getApplicationContext(),handler);

    }
    //自定义的对话框
    private void selfDialoga() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.kill_or_not));
        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                KillFri();
                GaiaApp.showToast(getString(R.string.kill_success));
                finish();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
