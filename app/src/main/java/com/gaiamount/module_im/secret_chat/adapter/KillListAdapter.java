package com.gaiamount.module_im.secret_chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_im.ImApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.secret_chat.activity.KillFriActivity;
import com.gaiamount.module_im.secret_chat.bean.KillInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-7-26.
 */
public class KillListAdapter extends BaseAdapter {
    Context context;
    List<KillInfo> killList;
    public KillListAdapter(Context context,List<KillInfo> killList){
        this.context=context;
        this.killList=killList;
    }
    @Override
    public int getCount() {
        return killList.size();
    }

    @Override
    public Object getItem(int position) {
        return killList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        KillViewHolder holder=null;
        if(convertView==null){
            holder=new KillViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.kill_list_item,null);
            holder.circleImageViewHead= (CircleImageView) convertView.findViewById(R.id.item_head);
            holder.textViewName= (TextView) convertView.findViewById(R.id.item_name);
            holder.aSwitch= (Switch) convertView.findViewById(R.id.item_switch);
            convertView.setTag(holder);
        }else {
            holder= (KillViewHolder) convertView.getTag();
        }
        Glide.with(context).load(Configs.COVER_PREFIX+killList.get(position).getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(holder.circleImageViewHead);
        holder.textViewName.setText(killList.get(position).getName());
        holder.aSwitch.setChecked(true);
        holder.aSwitch.setButtonDrawable(R.color.color_ff5773);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(false==isChecked){
                    ReSetFriend(killList.get(position).getId(),context);
                }else{

                }
            }
        });
        return convertView;
    }

    public void ReSetFriend(int id, Context context){
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(KillFriActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GaiaApp.showToast("取消拉黑成功");
            }
        };
        ImApiHelper.ResetFri((long)id,context,handler);
    }
    class KillViewHolder{
        TextView textViewName;
        CircleImageView circleImageViewHead;
        Switch aSwitch;
    }
}
