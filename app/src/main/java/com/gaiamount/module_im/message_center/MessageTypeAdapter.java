package com.gaiamount.module_im.message_center;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaiamount.R;
import com.hyphenate.chat.EMClient;

/**
 * Created by haiyang-lu on 16-5-13.
 */
public class MessageTypeAdapter extends BaseAdapter {
    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_NORMAL = 1;
    String[] typeListStr;
    int[] typeListIcon;
    private final LayoutInflater mInflater;
    private int[] messageCountList;

    public MessageTypeAdapter(Context context, int[] messageCountList) {
        this.messageCountList = messageCountList;
        typeListStr = context.getResources().getStringArray(R.array.message_type_list);
        typeListIcon = new int[]{R.mipmap.message_list_chat,R.mipmap.message_list_focusu,R.mipmap.message_list_focusm,/*R.mipmap.message_list_notice,*/ R.mipmap.message_list_comment,
                R.mipmap.message_list_newfriend,R.mipmap.message_list_groupnews,R.mipmap.message_list_apply};
        mInflater = LayoutInflater.from(context);
    }

    public void setOfficialMessageLocList(int[] messageCountList) {
        this.messageCountList = messageCountList;
    }

    @Override
    public int getCount() {
        return 4;//
//        return typeListStr.length;
    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position == 2 || position == 6) {
//            return TYPE_EMPTY;
//        } else {
//            return TYPE_NORMAL;
//        }
//    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (getItemViewType(position) == TYPE_EMPTY) {
//            return mInflater.inflate(R.layout.item_list_divider_8dp, parent, false);
//        } else
//        {
        View view = mInflater.inflate(R.layout.item_messge_type, parent, false);
        ImageView icon = (ImageView) view.findViewById(R.id.message_type_icon);
        TextView title = (TextView) view.findViewById(R.id.message_type_title);
        final TextView count = (TextView) view.findViewById(R.id.message_type_count);
        RelativeLayout layout= (RelativeLayout) view.findViewById(R.id.empty);
        View viewCon = view.findViewById(R.id.item_root);
        viewCon.setVisibility(View.VISIBLE);
        icon.setBackgroundResource(typeListIcon[position]);

        if (position==1||position==5||position==3) {
            layout.setVisibility(View.VISIBLE);
        }

        //标题
        title.setText(typeListStr[position]);

        //官方通知消息数量
        int num = messageCountList[position];

        if (num != 0) {
            count.setText(num+"");
            count.setVisibility(View.VISIBLE);
        } else {
            count.setText("");
            count.setVisibility(View.GONE);
        }
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    //第一个小红点.所有消息清零
                    EMClient.getInstance().chatManager().markAllConversationsAsRead();//清空聊天
                    count.setVisibility(View.GONE);
                }
            }
        });
        return view;

    }


}
