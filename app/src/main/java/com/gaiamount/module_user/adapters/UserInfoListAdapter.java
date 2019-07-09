package com.gaiamount.module_user.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.util.image.ImageUtils;

/**
 * Created by haiyang-lu on 16-5-3.
 */
public class UserInfoListAdapter extends BaseAdapter {
    private static final int TYPE3 = 3;
    private static final int TYPE2 = 2;
    private static final int TYPE1 = 1;
    private Context mContext;
    private UserInfo mUserInfo;
    private String[] mList;
    private final LayoutInflater mInflater;
    private final ImageUtils mImageUtils;

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public UserInfoListAdapter(Context context, UserInfo userInfo, String[] list) {

        mContext = context;
        mUserInfo = userInfo;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        mImageUtils = ImageUtils.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 9) {
            return TYPE3;
        }
        else if (position == 1) {
            return TYPE2;
        }
        else {
            return TYPE1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;

        if (convertView == null) {
            if (getItemViewType(position) == TYPE2) {//ViewHolder2
                convertView = mInflater.inflate(R.layout.item_user_info_type2, parent, false);
                holder2 = new ViewHolder2(convertView);
                convertView.setTag(holder2);

            } else if (getItemViewType(position) == TYPE3) {
                convertView = mInflater.inflate(R.layout.item_user_info_type3, parent, false);
                holder3 = new ViewHolder3(convertView);
                convertView.setTag(holder3);

            } else {
                convertView = mInflater.inflate(R.layout.item_user_info_type1, parent, false);
                holder1 = new ViewHolder1(convertView);
                convertView.setTag(holder1);

            }
        } else {
            if (getItemViewType(position) == TYPE2) {
                holder2 = (ViewHolder2) convertView.getTag();
            } else if (getItemViewType(position) == TYPE3) {
                holder3 = (ViewHolder3) convertView.getTag();
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
        }
        //设置值
        if (holder1 != null) {
            holder1.setValue(position);
        }
        if (holder2 != null) {
            holder2.setValue(position);
        }
        if (holder3 != null) {
            holder3.setValue(position);
        }

        return convertView;
    }

    /**
     * 包括列表名和值
     */
    class ViewHolder1 {
        TextView name;
        TextView value;

        public ViewHolder1(View convertView) {
            name = (TextView) convertView.findViewById(R.id.personal_user_info_type1_name);
            value = (TextView) convertView.findViewById(R.id.personal_user_info_type1_value);
        }

        public void setValue(int position) {
            name.setText(mList[position]);
            //begin 必填
            if (position == 2) value.setText(mUserInfo.nickName);
            if (position == 3) value.setText(mUserInfo.gender == 1 ? "女" : "男");
            if (position == 4) value.setText(mUserInfo.job);
            if (position == 5) value.setText(mUserInfo.address);
            if (position == 6)
                value.setText(mUserInfo.signature.isEmpty() ? "必填" : mUserInfo.signature);
            if (position == 7) value.setText(mUserInfo.description.isEmpty() ? "必填" : mUserInfo.description);
            if (position == 8) value.setText(mUserInfo.resume.isEmpty() ? "必填" : mUserInfo.resume);
            if (position == 9) value.setText("");
            if (position == 10) value.setText("");
            if (position == 11)
                value.setText(mUserInfo.mobile.isEmpty() ? "未验证" : mUserInfo.mobile);
            if (position == 13) value.setText(mUserInfo.email.isEmpty() ? "未验证" : mUserInfo.email);
            //end
        }
    }

    /**
     * 包括列表名和头像
     */
    class ViewHolder2 {
        TextView name;
        ImageView avatar;

        public ViewHolder2(View convertView) {
            name = (TextView) convertView.findViewById(R.id.personal_user_info_type2_name);
            avatar = (ImageView) convertView.findViewById(R.id.personal_user_info_type_avatar);
        }

        public void setValue(int position) {
            name.setText(mList[position]);

//            if(mUserInfo.avatar!=null&&!mUserInfo.avatar.equals("null")){
//                if(!mUserInfo.avatar.endsWith(".jpg")&&!mUserInfo.avatar.endsWith(".png")&&!mUserInfo.avatar.endsWith(".jpeg")){
//
//                    Glide.with(mContext).load(Configs.COVER_PREFIX+mUserInfo.avatar+".jpg").placeholder(R.mipmap.ic_avatar_default).into(avatar);
//                }else {
//                    Glide.with(mContext).load(Configs.COVER_PREFIX+mUserInfo.avatar).placeholder(R.mipmap.ic_avatar_default).into(avatar);
//                }
//            }else {
                Glide.with(mContext).load(Configs.COVER_PREFIX+mUserInfo.avatar).placeholder(R.mipmap.ic_avatar_default).into(avatar);
//            }
        }
    }

    /**
     * 包括子标题
     */
    class ViewHolder3 {
        TextView subTitle;

        public ViewHolder3(View convertView) {
            subTitle = (TextView) convertView.findViewById(R.id.item_personal_user_info_type3_subtitle);
        }

        public void setValue(int position) {
            subTitle.setText(mList[position]);
        }
    }
}
