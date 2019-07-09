package com.gaiamount.module_creator.sub_module_group.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.search.GlobalSearchActivity;
import com.gaiamount.module_creator.beans.GroupDetailInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupMoreActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GROUP_DETAIL_INFO = "group_detail_info";

    private GroupDetailInfo mGroupDetailInfo;
    private ArrayList<TextView> textViews;
    private TextView textViewTag1;
    private TextView textViewTag2;
    private TextView textViewTag3;
    private TextView textViewTag4;
    private TextView textViewTag5;
    private TextView textViewNameTime;
    private TextView textViewIntroduce;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_more);
        ButterKnife.bind(this);
        context=this;
        mGroupDetailInfo = (GroupDetailInfo) getIntent().getSerializableExtra(GROUP_DETAIL_INFO);
        init();
    }

    private void init() {
        textViewNameTime = (TextView) findViewById(R.id.group_name_time);
        textViewIntroduce = (TextView) findViewById(R.id.group_introduce);

        textViews=new ArrayList<>();
        textViewTag1 = (TextView) findViewById(R.id.tag_1);
        textViewTag2 = (TextView) findViewById(R.id.tag_2);
        textViewTag3 = (TextView) findViewById(R.id.tag_3);
        textViewTag4 = (TextView) findViewById(R.id.tag_4);
        textViewTag5 = (TextView) findViewById(R.id.tag_5);
        textViews.add(textViewTag1);
        textViews.add(textViewTag2);
        textViews.add(textViewTag3);
        textViews.add(textViewTag4);
        textViews.add(textViewTag5);

        if(mGroupDetailInfo!=null){
            String keywords=mGroupDetailInfo.getO().getGroup().getKeywords();
            String[] split = keywords.split(",");
            for (int i = 0; i < split.length && i<5; i++) {
                textViews.get(i).setVisibility(View.VISIBLE);
                textViews.get(i).setText(split[i]);
                textViews.get(i).setBackgroundResource(R.drawable.shape_group_detail);
            }
        }

        textViewNameTime.setText("创建人:"+mGroupDetailInfo.getO().getUser().getNickName()+"          "+mGroupDetailInfo.getO().getGroup().getCreateTime());
        textViewIntroduce.setText(mGroupDetailInfo.getO().getGroup().getDescription());
        textViewTag1.setOnClickListener(this);
        textViewTag2.setOnClickListener(this);
        textViewTag3.setOnClickListener(this);
        textViewTag4.setOnClickListener(this);
        textViewTag5.setOnClickListener(this);
    }

    //返回
    public void groupback(View view) {
        finish();
    }


    public void startSearchActivity(String text){
        Intent intent = new Intent(getApplicationContext(), GlobalSearchActivity.class);
        intent.putExtra(GlobalSearchActivity.WORDS, "");
        intent.putExtra(GlobalSearchActivity.KEY_WORDS,text);
        intent.putExtra(GlobalSearchActivity.KIND, 2);//2 表示小组
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tag_1:
                startSearchActivity(textViewTag1.getText().toString());
                break;
            case R.id.tag_2:
                startSearchActivity(textViewTag2.getText().toString());
                break;
            case R.id.tag_3:
                startSearchActivity(textViewTag3.getText().toString());
                break;
            case R.id.tag_4:
                startSearchActivity(textViewTag4.getText().toString());
                break;
            case R.id.tag_5:
                startSearchActivity(textViewTag5.getText().toString());
                break;
        }

    }
}
