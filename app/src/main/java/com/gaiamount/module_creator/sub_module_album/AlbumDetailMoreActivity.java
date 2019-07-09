package com.gaiamount.module_creator.sub_module_album;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gaiamount.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlbumDetailMoreActivity extends AppCompatActivity {
    private AlbumDetail albumDetail;
    private TextView mTextViewName;
    private TextView mTextViewTime;
    private TextView textViewIntroduce;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail_more);
        albumDetail= (AlbumDetail) getIntent().getSerializableExtra("Info");
        init();
        setInfo();

    }

    private void setInfo() {
        if(albumDetail!=null&&!albumDetail.equals("null")){
            mTextViewName.setText(albumDetail.getName());
            mTextViewTime.setText(albumDetail.getName()+"           "+getTime(albumDetail.getCreateTime().getTime()));
            textViewIntroduce.setText(albumDetail.getContent());
        }
    }

    private void init() {
        context=getApplicationContext();
        mTextViewName = (TextView) findViewById(R.id.album_group_name);
        mTextViewTime = (TextView) findViewById(R.id.album_name_time);
        textViewIntroduce = (TextView) findViewById(R.id.album_introduce);
    }

    public void albumback(View view) {
        finish();
    }

    private String getTime(Long msgTime) {
        long l = System.currentTimeMillis();
        if(l-msgTime<3600000){
            SimpleDateFormat formatter = new SimpleDateFormat    ("mm");
            Date curDate = new Date(msgTime);//获取时间
            String str1 = formatter.format(curDate);
            Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate2);
            int i = Integer.valueOf(str) - Integer.valueOf(str1);
            if(i>=0){
                return i+context.getString(R.string.time_minute_ago);
            }else {
                return 60+i+context.getString(R.string.time_minute_ago);
            }
        }
        if(3600000<l-msgTime&l-msgTime<86400000){
            SimpleDateFormat formatter = new SimpleDateFormat    ("hh");
            Date curDate = new Date(msgTime);//获取时间
            String str1 = formatter.format(curDate);
            Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate2);
            int i = Integer.valueOf(str) - Integer.valueOf(str1);
            if(i>=0){
                return i+context.getString(R.string.time_hours_ago);
            }else {
                return 12+i+context.getString(R.string.time_hours_ago);
            }
        }else if(l-msgTime>86400000 & l-msgTime<=86400000*2){
            return context.getString(R.string.one_day_ago);
        }else if(l-msgTime>86400000*2 & l-msgTime<=86400000*3){
            return context.getString(R.string.two_day_ago);
        }else if(l-msgTime>86400000*3 & l-msgTime<=86400000*4) {
            return context.getString(R.string.three_day_ago);
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat    ("yyyy/MM/dd");
            Date curDate = new Date(msgTime);//获取时间
            return formatter.format(curDate);
        }
    }

}
