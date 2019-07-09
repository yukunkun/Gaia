package com.gaiamount.module_scripe.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_academy.viewholder.NoScrollViewHolder;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.util.ScreenUtils;

import java.util.List;

/**
 * Created by yukun on 16-8-22.
 */
public class NoScrollGrideScripeAdapter extends BaseAdapter {
    private Context context;
    private List<ScripeInfo> scripeInfos;
    private String[] stringArray;

    public NoScrollGrideScripeAdapter(Context context,List<ScripeInfo> scripeInfos) {
        this.context = context;
        this.scripeInfos = scripeInfos;
        stringArray=context.getResources().getStringArray(R.array.scripe_list);
    }

    @Override
    public int getCount() {
        if(scripeInfos.size()<=4){
            return scripeInfos.size();
        }else {
            return 4;
        }
    }

    @Override
    public Object getItem(int position) {
        return scripeInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoScrollViewHolder holder=null;
        if(convertView==null){
            holder=new NoScrollViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.scripe_noscrol_glide,parent,false);
            holder.textViewIntroduce= (TextView) convertView.findViewById(R.id.scripe_intr);
            holder.textViewPrice= (TextView) convertView.findViewById(R.id.scripe_name);
            holder.imageViewImg= (ImageView) convertView.findViewById(R.id.scripe_cover);
            convertView.setTag(holder);
        }else{
            holder= (NoScrollViewHolder) convertView.getTag();
        }

        if(scripeInfos!=null&&scripeInfos.size()!=0){
            StringBuilder builder=new StringBuilder();
            String type = scripeInfos.get(position).getType();
            if(type!=null&& !TextUtils.isEmpty(type)) {

                String[] split = type.split(",");
                for (int i = 0; i < 1; i++) {
                    builder.append(changeString(split[i])).append("/");
                }
            }else {

            }
            int state = scripeInfos.get(position).getState(); //1 完结
            if(state==1){
                builder.append("完结").append("/");

            }else if(state==0){

                builder.append("连载中").append("/");
            }
            int collectCount = scripeInfos.get(position).getCollectCount();

            holder.textViewIntroduce.setText(new String(builder)+collectCount+"收藏");

            holder.textViewPrice.setText(scripeInfos.get(position).getNickName());//这里是剧本的名字

            ViewGroup.LayoutParams layoutParams = holder.imageViewImg.getLayoutParams();
            layoutParams.height = getHeight();
            (holder).imageViewImg.setLayoutParams(layoutParams);
    //        //设置图片
            Glide.with(context).load(Configs.COVER_PREFIX+scripeInfos.get(position).getCover()).placeholder(R.mipmap.bg_general).into(holder.imageViewImg);
        }
        return convertView;
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width-20)/2);
        return itemHeight;
    }

    private String changeString(String strings) {

        if (!strings.equals("")) {
            if (strings.equals("0")) {
                return stringArray[0];
            } else if (strings.equals("1")) {
                return stringArray[1];
            } else if (strings.equals("2")) {
                return stringArray[2];
            } else if (strings.equals("3")) {
                return stringArray[3];
            } else if (strings.equals("4")) {
                return stringArray[4];
            } else if (strings.equals("5")) {
                return stringArray[5];
            } else if (strings.equals("6")) {
                return stringArray[6];
            } else if (strings.equals("7")) {
                return stringArray[7];
            } else if (strings.equals("8")) {
                return stringArray[8];
            } else if (strings.equals("9")) {
                return stringArray[9];
            } else if (strings.equals("10")) {
                return stringArray[10];
            } else if (strings.equals("11")) {
                return stringArray[11];
            } else if (strings.equals("12")) {
                return stringArray[12];
            }else if (strings.equals("13")) {
                return stringArray[13];
            }else if (strings.equals("14")) {
                return stringArray[14];
            }else if (strings.equals("15")) {
                return stringArray[15];
            }else if (strings.equals("16")) {
                return stringArray[16];
            }else if (strings.equals("17")) {
                return stringArray[17];
            }else if (strings.equals("18")) {
                return stringArray[18];
            }
        }
        return "";

    }
}
