package com.gaiamount.module_im.official;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.image.ImageUtils;

import java.util.List;

/**
 * Created by haiyang-lu on 16-5-12.
 *
 */
public class ChatListAdapter extends BaseAdapter {
    private static final int TYPE_ME = 0;
    private static final int TYPE_OTHER = 1;
    private Context mContext;
    private List<ChatContent> mChatList;
    private final LayoutInflater mInflater;

    public ChatListAdapter(Context context, List<ChatContent> chatList) {

        mContext = context;
        mChatList = chatList;
        mInflater = LayoutInflater.from(context);
    }

    public void setChatList(List<ChatContent> chatList) {
        mChatList = chatList;
    }

    @Override
    public int getCount() {
        return mChatList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ChatContent content = mChatList.get(position);
        String chatOrientaion = content.chatSender;
        if (ChatConstant.ME.equals(chatOrientaion)) {
            return TYPE_ME;
        }else {
            return TYPE_OTHER;
        }

    }

    @Override
    public Object getItem(int position) {
        return mChatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position)==TYPE_OTHER) {
            View view = mInflater.inflate(R.layout.item_chat_txt_other, parent, false);
            TextView chatText = (TextView) view.findViewById(R.id.item_chat_txt);
            ImageView avatar = (ImageView) view.findViewById(R.id.avatar);

            ChatContent content = mChatList.get(position);

            ImageUtils.getInstance(mContext).getAvatar(avatar,content.avatar);
            chatText.setText(content.textContent);
            return view;
        }else {
            View view = mInflater.inflate(R.layout.item_chat_txt_me, parent, false);
            TextView chatText = (TextView) view.findViewById(R.id.item_chat_txt);
            ImageView avatar = (ImageView) view.findViewById(R.id.avatar);

            ChatContent content = mChatList.get(position);
            chatText.setText(content.textContent);

            ImageUtils.getInstance(mContext).getAvatar(avatar, GaiaApp.getUserInfo().avatar);
            return view;
        }
    }
}
