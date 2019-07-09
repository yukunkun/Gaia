package com.gaiamount.module_im.secret_chat.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.official.ChatConstant;
import com.gaiamount.module_im.secret_chat.adapter.ChatAdapter;
import com.gaiamount.module_im.secret_chat.bean.ContentInfo;
import com.gaiamount.module_im.secret_chat.bean.OnEventChatGroup;
import com.gaiamount.module_im.secret_chat.model.MessageEvent;
import com.gaiamount.module_im.secret_chat.model.OnEventNew;
import com.gaiamount.util.ActivityUtil;
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

/**
 * kun
 */
public class GroupChatActivity extends AppCompatActivity {
    private EMMessageListener msgListener;
    private String imid;
    private String otherAvatar;
    private String otherNickName;
    private EditText mEditText;
    private ListView mListView;
    ChatAdapter chatAdapter;
    int PHOTO_FROM_MAP=0;
    int TAKE_PICTURE=1;
    private String from="";
    private String groupId;
    ArrayList<ContentInfo> contentInfos=new ArrayList<>();
    private TextView mTextViewTitle;
    private List<EMMessage> messages;
    private TextView photoFromMap,photoFromCarmer,photoQuit;
    private PopupWindow popupWindow;
    private List<EMMessage> msgs=new ArrayList<>();
    private boolean isFirst=true;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                    mListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
//                    mListView.setSelection(chatAdapter.getCount()-1);
                }
            });
        }
    };
    private int page;
    private FileOutputStream out;
    private String mPath;

    /**
     * 获取到的消息的回调
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        messages=event.getMessages();
        parseMessage(messages);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);  //注册
        setContentView(R.layout.activity_group_chat);
        Intent intent = getIntent();
        imid=intent.getStringExtra("imid");
        otherAvatar=intent.getStringExtra("otherAvatar");
        otherNickName=intent.getStringExtra("otherNickName");
        groupId=intent.getStringExtra("groupId");
        init();
        setAdapter();
        getAllMessage();
        setListener();
    }

    private void setAdapter() {
        chatAdapter=new ChatAdapter(getApplicationContext(),contentInfos);
        mListView.setAdapter(chatAdapter);

    }


    /**
     * 获取到消息的方法
     */
    private void getAllMessage() {

        EMConversation conversations = EMClient.getInstance().chatManager().getConversation(imid);
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        if(conversations!=null) {
            conversations.markAllMessagesAsRead();
            if (conversations.getAllMsgCount() > 50) {
                page = 50;
            } else {
                page = conversations.getAllMsgCount();
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

    private void init() {
        mListView= (ListView) findViewById(R.id.group_chat_listview);
        mEditText= (EditText) findViewById(R.id.group_edit_contant);
        mTextViewTitle= (TextView) findViewById(R.id.group_text_title);
        mTextViewTitle.setText(otherNickName);
    }

    /**
     * 适配获取到的数据
     * @param list
     */
    private void parseMessage(List<EMMessage> list) {
        for (EMMessage message : list) {
            from = message.getFrom();
            if (message.getChatType() == EMMessage.ChatType.GroupChat) {//群聊消息
                EMMessage.Type type = message.getType();
                if(isFirst){
                    if (imid.equals(message.getTo())) { //判断是不是同一个聊天室的消息
                        ContentInfo info = new ContentInfo();

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
                                if(!thumbnailUrl.isEmpty()|| !TextUtils.isEmpty(thumbnailUrl)){
                                    info.setImageUri(thumbnailUrl);
                                }else{
                                    String localUrl = body1.getLocalUrl();
                                    info.setImageUri(localUrl);
                                }
                            }
                        }
                        String id = message.getStringAttribute("id", "");
                        String otherAvatar = message.getStringAttribute("head", "");
//                Log.i("------meass",message.toString());
                        if(message.getFrom().equals(GaiaApp.getAppInstance().getUserInfo().id+"")){
                            info.setType(0);
                        }else{
                            info.setType(1);
                        }
                        info.setOtherId(message.getTo());
                        info.setUid(imid);
                        info.setChose(id);
                        info.setAvatar(otherAvatar);
                        contentInfos.add(info);
                        handler.sendEmptyMessage(0);
                    }
                }else {
                    if ((!from.equals(GaiaApp.getAppInstance().getUserInfo().id+""))&&imid.equals(message.getTo())) { //判断是不是同一个聊天室的消息
                        ContentInfo info = new ContentInfo();

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
                                if(!thumbnailUrl.isEmpty()|| !TextUtils.isEmpty(thumbnailUrl)){
                                    info.setImageUri(thumbnailUrl);
                                }else{
                                    String localUrl = body1.getLocalUrl();
                                    info.setImageUri(localUrl);
                                }
                            }
                        }
                        String id = message.getStringAttribute("id", "");
                        String otherAvatar = message.getStringAttribute("head", "");
//                Log.i("------meass",message.toString());
                        if(message.getFrom().equals(GaiaApp.getAppInstance().getUserInfo().id+"")){
//                    Log.i("------zijide",GaiaApp.getAppInstance().getUserInfo().id+"");
                            info.setType(0);
                        }else{
                            info.setType(1);
//                    Log.i("------taren","taren");
                        }
                        info.setOtherId(message.getTo());
                        info.setUid(imid);
                        info.setChose(id);
                        info.setAvatar(otherAvatar);
                        contentInfos.add(info);
                        handler.sendEmptyMessage(0);
                    }
                }
            }
        }
    }

    private void setListener() {
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    GroupSend();
                     /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }
    //刷新列表
    private void getMessage (){
        Log.i("--send","message");
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                Log.i("--mesage","message");
                parseMessage(messages);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {

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
    }


    /**
     * 发送消息的方法
     */
    public void GroupSend() {
        isFirst=false;
        String content=mEditText.getText().toString();
        EMMessage message = EMMessage.createTxtSendMessage(content, imid);
        if(message!=null){
            message.setChatType(EMMessage.ChatType.GroupChat);//群聊的标志
        }
       if((mEditText.getText().toString().isEmpty())){
           GaiaApp.showToast(getString(R.string.cant_be_null));
           return;
       }
        // 增加自己特定的属性
        message.setAttribute("id", GaiaApp.getAppInstance().getUserInfo().id);
        message.setAttribute("head", GaiaApp.getAppInstance().getUserInfo().avatar);
        message.setAttribute("nickName",GaiaApp.getAppInstance().getUserInfo().nickName);
        message.setAttribute("otherNickName",otherNickName);
        message.setAttribute("otherAvatar", otherAvatar);
        message.setAttribute("imid", imid);
        message.setAttribute("toID", imid);
        message.setAttribute("currType", "groupchat");
        message.setAttribute("groupId",groupId);
        EMClient.getInstance().chatManager().sendMessage(message);
        //设置值
        ContentInfo info=new ContentInfo();
        //if(message.getFrom().toString().equals(GaiaApp.getAppInstance().getUserInfo().id+"");
        info.setType(0);
        info.setBody(mEditText.getText().toString());
        info.setUid(GaiaApp.getAppInstance().getUserInfo().id+"");
        info.setChose(GaiaApp.getAppInstance().getUserInfo().id+"");
        info.setAvatar(GaiaApp.getAppInstance().getUserInfo().avatar);
        contentInfos.add(info);
        mEditText.setText("");
        chatAdapter.notifyDataSetChanged();
        mListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
        handler.sendEmptyMessage(0);
        //存储数据到环信
//        msgs.add(message);
//        EMClient.getInstance().chatManager().importMessages(msgs);
        message.setMessageStatusCallback(new EMCallBack(){
            @Override
            public void onSuccess() {
                //获取到消息
//                EventBus.getDefault().post(new OnEventChatGroup("send"));
            }

            @Override
            public void onError(int i, String s) {
                GaiaApp.showToast(getString(R.string.please_wait_check));
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(imid);
            if(conversation!=null){
                conversation.markAllMessagesAsRead();
            }
        //更新会话页未读个数
        EventBus.getDefault().post(new OnEventNew(1));
        }

        public void GroupChatBack(View view) {
            finish();
        }


        @Override
        protected void onDestroy() {
            super.onDestroy();
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
            EventBus.getDefault().unregister(this);//取消注册

        }

    public void GroupOpenPhoto(View view) {
        try{
            getPermission();
        }catch (Exception e){
            GaiaApp.showToast("请重新登录登录");
            e.printStackTrace();
        }
    }

    String imagePath=null;

    /**
     * 获取到图片的回传消息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 0: //图库选择
                    if(data==null){
                        return;
                    }
                    if (data.getData() != null || data.getExtras() != null) { //防止没有返回结果
                        Uri uri = data.getData();
                        if (uri != null) {
                            //dismiss
                            if (popupWindow!=null&&popupWindow.isShowing()) {
                                popupWindow.dismiss();
                            }
                            imagePath = getAbsoluteImagePath(uri);
                            //设置值
                            ContentInfo info1=new ContentInfo();
                            info1.setUid(GaiaApp.getAppInstance().getUserInfo().id+"");
                            info1.setAvatar(GaiaApp.getAppInstance().getUserInfo().avatar);
                            info1.setImageUri(imagePath);
                            info1.setType(0);
                            info1.setChose(GaiaApp.getAppInstance().getUserInfo().id+"");
                            contentInfos.add(info1);
                            chatAdapter.notifyDataSetChanged();
                            sendImage(imagePath,imid);  //发送图片
                            mListView.smoothScrollToPosition(contentInfos.size()-1);
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
                        info1.setChose(GaiaApp.getAppInstance().getUserInfo().id+"");
                        contentInfos.add(info1);
                        chatAdapter.notifyDataSetChanged();
                        sendImage(mPath,imid);  //发送图片
                        mListView.smoothScrollToPosition(contentInfos.size()-1);
                    }else {
                        GaiaApp.showToast(getString(R.string.send_img_fail));
                    }
                    break;
            }
    }

    /**
     * 发送图片的方法
     * @param imagePath
     * @param otherId
     */
    private void sendImage(String imagePath, String otherId) {
        isFirst=false;
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, otherId);
        message.setChatType(EMMessage.ChatType.GroupChat);//群聊的表标志
        //如果是群聊，设置chattype，默认是单聊
        message.setAttribute("id", GaiaApp.getAppInstance().getUserInfo().id);
        message.setAttribute("head", GaiaApp.getAppInstance().getUserInfo().avatar);
        message.setAttribute("nickName",GaiaApp.getAppInstance().getUserInfo().nickName);
        message.setAttribute("otherNickName",otherNickName);
        message.setAttribute("otherAvatar", otherAvatar);
        message.setAttribute("imid", imid);
        message.setAttribute("toID", imid);
        message.setAttribute("currType", "groupchat");
        message.setAttribute("groupId",groupId);
        EMClient.getInstance().chatManager().sendMessage(message);
        //存储消息数据
        msgs.add(message);
        EMClient.getInstance().chatManager().importMessages(msgs);
    }
    //弹窗
    private void pupuWindow() {
        View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_popuwindow, null);
        popupWindow=new PopupWindow(inflate,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(this.findViewById(R.id.rea), Gravity.BOTTOM,0,0);

        photoFromMap= (TextView) inflate.findViewById(R.id.choose_from_photo);
        photoFromCarmer= (TextView) inflate.findViewById(R.id.choose_from_carmer);
        photoQuit= (TextView) inflate.findViewById(R.id.choose_quit);
        //选择照方式
        photoFromMap.setOnClickListener(new Listener());
        photoFromCarmer.setOnClickListener(new Listener());
        photoQuit.setOnClickListener(new Listener());
    }

    /**
     * 跳转到小组成员页面
     * @param view
     */
    public void GroupShow(View view) {
        if(groupId.isEmpty()){
            return;
        }
        ActivityUtil.startGroupActivity(GroupChatActivity.this,Long.valueOf(groupId));
    }

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
    private void getPermission() {

        if (ContextCompat.checkSelfPermission(GroupChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if( ContextCompat.checkSelfPermission(GroupChatActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                //两个权限都没有
                ActivityCompat.requestPermissions(GroupChatActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 1);
            }else {
                //申请第一个权限
                ActivityCompat.requestPermissions(GroupChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 1);
            }
        }else if( ContextCompat.checkSelfPermission(GroupChatActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            //有第一个权限,没有第二个权限
            ActivityCompat.requestPermissions(GroupChatActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        }else {
            //两个都有
            pupuWindow();
            hintKey();
        }
    }

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
    /**
     * 从相册获取到图片
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
     * 从相机获取到图片
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
}
