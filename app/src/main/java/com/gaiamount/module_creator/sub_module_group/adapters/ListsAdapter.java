package com.gaiamount.module_creator.sub_module_group.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.sub_module_group.activities.GroupExamineActivity;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-9-21.
 */
public class ListsAdapter extends BaseAdapter {
        private Context context;
    private ArrayList<GroupExamineActivity.ExamineMem> examineMems;

    public ListsAdapter(Context context, ArrayList<GroupExamineActivity.ExamineMem> examineMems) {
        this.context = context;
        this.examineMems = examineMems;
    }

    @Override
    public int getCount() {
        return examineMems.size();
    }

    @Override
    public Object getItem(int position) {
        return examineMems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_set_group_admin_admin_list, null);
        CircleImageView imageViewHead = (CircleImageView) convertView.findViewById(R.id.admin_avatar);
        TextView textViewName= (TextView) convertView.findViewById(R.id.examile_name);
        TextView textViewOk= (TextView) convertView.findViewById(R.id.admin_add);
        TextView textViewNo= (TextView) convertView.findViewById(R.id.admin_set);
        textViewOk.setText("同意");
        textViewNo.setText("拒绝");
        textViewNo.setOnClickListener(new Listener(examineMems.get(position).getMid()));
        textViewOk.setOnClickListener(new Listener(examineMems.get(position).getMid()));
        imageViewHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startPersonalActivity(context,examineMems.get(position).getUid());
            }
        });
        textViewName.setText(examineMems.get(position).getNickname());
        Glide.with(context).load(Configs.COVER_PREFIX+examineMems.get(position).getNickname()).placeholder(R.mipmap.ic_avatar_default).into(imageViewHead);
        return convertView;
    }
    class Listener implements View.OnClickListener {
        private long mid;
        public Listener(long mid) {
            this.mid=mid;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.admin_add:
                    MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ListsAdapter.class){
                        @Override
                        public void onGoodResponse(JSONObject response) {
                            super.onGoodResponse(response);
                            GaiaApp.showToast("审核通过");
                        }
                    };
                    GroupApiHelper.getExamineOk(mid,1,context,handler);
                    break;
                case R.id.admin_set:
                    MJsonHttpResponseHandler handler1=new MJsonHttpResponseHandler(ListsAdapter.class){
                        @Override
                        public void onGoodResponse(JSONObject response) {
                            super.onGoodResponse(response);
                            GaiaApp.showToast("审核拒绝");
                        }
                    };
                    GroupApiHelper.getExamineOk(mid,0,context,handler1);
                    break;
            }
        }
    }
}
