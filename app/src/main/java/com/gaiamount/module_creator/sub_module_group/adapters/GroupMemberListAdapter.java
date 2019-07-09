package com.gaiamount.module_creator.sub_module_group.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_creator.sub_module_group.beans.GroupMemberBean;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haiyang-lu on 16-6-23.
 */
public class GroupMemberListAdapter extends BaseAdapter {

    private  Context mContext;
    private  List<GroupMemberBean> mGroupMemberBeanList;
    private  ImageUtils mImageUtils;

    private List<String> mCheckedList = new ArrayList<>();

    public GroupMemberListAdapter(Context context, List<GroupMemberBean> groupMemberBeanList) {

        mContext = context;
        mGroupMemberBeanList = groupMemberBeanList;
        mImageUtils = ImageUtils.getInstance(mContext);

    }

    public void setGroupMemberBeanList(List<GroupMemberBean> groupMemberBeanList) {
        mGroupMemberBeanList = groupMemberBeanList;
    }

    @Override
    public int getCount() {
        return mGroupMemberBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        long memberId = mGroupMemberBeanList.get(position).getMid();
        LogUtil.d(GroupMemberListAdapter.class,"memberId:"+memberId);
        return memberId;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_set_group_admin_member_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GroupMemberBean groupMemberBean = mGroupMemberBeanList.get(position);

        mImageUtils.getAvatar(holder.mMemberAvatar,groupMemberBean.getMemberAvatar());
        holder.mMemberNick.setText(groupMemberBean.getMemberNickName());
        holder.mMemberJobAddress.setText(groupMemberBean.getMemberJob()+","+groupMemberBean.getMemberAddress());

        holder.mMemberChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckedList.add(String.valueOf(position));
                }else {
                    mCheckedList.remove(String.valueOf(position));
                }
            }
        });

        return convertView;
    }

    public List<String> getCheckedList() {
        return mCheckedList;
    }

    class ViewHolder {
        CircleImageView mMemberAvatar;
        TextView mMemberNick;
        TextView mMemberJobAddress;
        CheckBox mMemberChecked;

        ViewHolder(View view) {
            mMemberAvatar = (CircleImageView) view.findViewById(R.id.member_avatar);
            mMemberNick = (TextView) view.findViewById(R.id.member_nick);
            mMemberJobAddress = (TextView) view.findViewById(R.id.member_job_address);
            mMemberChecked = (CheckBox) view.findViewById(R.id.member_check);
        }
    }
}
