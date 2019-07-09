package com.gaiamount.module_creator.sub_module_group.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.sub_module_group.beans.GroupAdmin;
import com.gaiamount.module_creator.sub_module_group.fragment.SetGroupAdminFrag;
import com.gaiamount.util.image.ImageUtils;

import java.util.List;

/**
 * Created by haiyang-lu on 16-6-23.
 */
public class GroupAdminListAdapter extends BaseAdapter {

    private static final int TYPE_ADD_ADMIN = 1;
    private static final int TYPE_SET_ADMIN = 2;
    private  Context mContext;
    private  List<GroupAdmin> mGroupAdminList;
    private SetGroupAdminFrag mFrag;

    public GroupAdminListAdapter(Context context, List<GroupAdmin> groupAdminList, SetGroupAdminFrag frag) {
        mContext = context;
        mGroupAdminList = groupAdminList;
        mFrag = frag;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_ADD_ADMIN;
        } else {
            return TYPE_SET_ADMIN;
        }

    }

    @Override
    public int getCount() {

        return mGroupAdminList.size()+1;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;

        if (getItemViewType(position)==TYPE_ADD_ADMIN) {
            view = View.inflate(mContext, R.layout.item_set_group_admin_admin_list, null);
            TextView adminAdd  = (TextView) view.findViewById(R.id.admin_add);
            TextView adminSet = (TextView) view.findViewById(R.id.admin_set);

            adminAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFrag.addAdmin();
                }
            });
            ImageView avatar = (ImageView) view.findViewById(R.id.admin_avatar);
            TextView memberNick = (TextView) view.findViewById(R.id.examile_name);
            Glide.with(mContext).load(Configs.COVER_PREFIX+ GaiaApp.getAppInstance().getUserInfo().avatar).placeholder(R.mipmap.ic_avatar_default).into(avatar);
            memberNick.setText(GaiaApp.getAppInstance().getUserInfo().nickName);
            adminSet.setBackgroundColor(mContext.getResources().getColor(R.color.color_9a9a9a));
            adminSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mFrag.setAdminPower(mGroupAdminList.get(position-1));
                }
            });

        }else if (getItemViewType(position)==TYPE_SET_ADMIN) {
            final GroupAdmin groupAdmin = mGroupAdminList.get(position-1);
            view = View.inflate(mContext, R.layout.item_set_group_admin_admin_list2, null);
            TextView adminRemove  = (TextView) view.findViewById(R.id.admin_delete);
            TextView adminSet = (TextView) view.findViewById(R.id.admin_set);

            ImageView avatar = (ImageView) view.findViewById(R.id.admin_avatar);
            TextView memberNick = (TextView) view.findViewById(R.id.examile_name);
            TextView memberJobAddress = (TextView) view.findViewById(R.id.member_job_address);
            Glide.with(mContext).load(Configs.COVER_PREFIX+groupAdmin.getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(avatar);
            if(groupAdmin.getNickName()!=null){
                memberNick.setText(groupAdmin.getNickName());
            }
            adminRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFrag.removeAdmin(groupAdmin);
                }
            });
            adminSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFrag.setAdminPower(groupAdmin);
                }
            });
        }
        return view;
    }

    /**
     * 更新适配器
     * @param list
     */
    public void update(List<GroupAdmin> list) {
        mGroupAdminList = list;
        notifyDataSetChanged();
    }
}
