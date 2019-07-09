package com.gaiamount.module_scripe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_scripe.activity.ScripeDetailActivity;
import com.gaiamount.module_scripe.bean.ScriptContent;

import java.util.ArrayList;

/**
 * Created by yukun on 16-10-26.
 */
public class ContentAdapter extends BaseAdapter {
    int selectedItem;
    Context context;
    ArrayList<ScriptContent> scriptContents;

    public ContentAdapter(Context content, ArrayList<ScriptContent> scriptContents) {
        this.context = content;
        this.scriptContents = scriptContents;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public int getCount() {
        return scriptContents.size();
    }

    @Override
    public Object getItem(int position) {
        return scriptContents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(context).inflate(R.layout.scripe_content_item,null);
        TextView textViewCon= (TextView) convertView.findViewById(R.id.scripe_detail_cont_1);
        ImageView imageViewLock= (ImageView) convertView.findViewById(R.id.scripe_detail_lock_1);
        textViewCon.setText(scriptContents.get(position).getTitle());

        if(scriptContents.get(position).getIsPublic()==1){
            imageViewLock.setVisibility(View.GONE);
        }

        if(position==selectedItem){
            textViewCon.setTextColor(context.getResources().getColor(R.color.color_ff5773));
        }else {
            textViewCon.setTextColor(context.getResources().getColor(R.color.text_999));
        }

        return convertView;
    }


}
