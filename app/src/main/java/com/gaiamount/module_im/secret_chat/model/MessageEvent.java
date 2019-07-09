package com.gaiamount.module_im.secret_chat.model;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by yukun on 16-7-21.
 */
public class MessageEvent  {
    //注册的方法
    private List<EMMessage> messages;
    public boolean tag;
    public MessageEvent(List<EMMessage> messages) {
        this.setMessages(messages);
    }public List<EMMessage> getMessages() {
    return messages;
}public void setMessages(List<EMMessage> messages) {
    this.messages = messages;
}
//    public MessageEvent(List<EMMessage> messages,boolean tag) {
//        this.messages = messages;
//        this.tag=tag;
//    }
}
