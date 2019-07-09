package com.gaiamount.module_academy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.module_academy.adapter.AcademyAdapter;
import com.gaiamount.module_academy.bean.AcademyAll;
import com.gaiamount.module_academy.bean.AcademyInfo;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * kun 学院首页
 */
public class AcademyActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AcademyAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> covers=new ArrayList<>();
    private int id=0;
    private boolean tag=false;
    private ArrayList<AcademyAll> academyAlls=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy);

        initView();
        getInfo();
        setAdapter();
    }

    private void initView() {
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView= (RecyclerView) findViewById(R.id.academy_recyclerview);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getInfo() {
        //获取banner条
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
                tag=true;
            }
        };
        AcademyApiHelper.getCollege(handler);
        //获取列表
        MJsonHttpResponseHandler handler1=new MJsonHttpResponseHandler(AcademyActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JsonParasMain(response);
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
            }
        };
        AcademyApiHelper.getAcademyMain(getApplicationContext(),handler1);

    }
    //banner条解析
    private void parasJson(JSONObject response) {
        JSONObject jsonObject = response.optJSONObject("o");
        if(jsonObject!=null){
            String cover1 = jsonObject.optString("cover1");
            covers.add(cover1);
            String cover2 = jsonObject.optString("cover2");
            covers.add(cover2);
            String cover3 = jsonObject.optString("cover3");
            covers.add(cover3);
            String cover4 = jsonObject.optString("cover4");
            covers.add(cover4);
            String cover5 = jsonObject.optString("cover5");
            covers.add(cover5);
            id=jsonObject.optInt("id");
            EventBus.getDefault().post(new OnEventId(id));
        }
        adapter.notifyDataSetChanged();
    }

    //列表解析
    private void JsonParasMain(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {

            JSONObject object = a.optJSONObject(i);
            AcademyAll academyAll=new AcademyAll();
            academyAll.setListName(object.optString("listName"));
            JSONArray courseInfo = object.optJSONArray("courseInfo");
            ArrayList<AcademyInfo> infos=new ArrayList<>();
            for (int j = 0; j < courseInfo.length(); j++) {
                AcademyInfo academyInfo=new AcademyInfo();
                JSONObject jsonObject = courseInfo.optJSONObject(j);
                academyInfo.setCover(jsonObject.optString("cover"));
                academyInfo.setLearningCount(jsonObject.optInt("learningCount"));
                academyInfo.setAllowFree(jsonObject.optInt("allowFree"));
                academyInfo.setPrice(jsonObject.optInt("price"));
                academyInfo.setName(jsonObject.optString("name"));
                academyInfo.setCourseId(jsonObject.optLong("courseId"));
                infos.add(academyInfo);
            }
            academyAll.setAcademyInfos(infos);
            academyAlls.add(academyAll);
        }

        adapter.notifyDataSetChanged();
    }


    private void setAdapter() {
        adapter = new AcademyAdapter(getApplicationContext(),id,covers,academyAlls);
        mRecyclerView.setAdapter(adapter);
    }

    //返回
    public void AcademyBack(View view) {
        finish();
    }

    public void AcademySearch(View view) {
        ActivityUtil.startGlobalSearchActivity(AcademyActivity.this,"",4);
    }
}
