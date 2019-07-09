package com.gaiamount.module_academy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.adapter.MixAdapter;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_creator.creater_circle.bean.GroupSpacesDecoration;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MixingLightActivity extends AppCompatActivity {

    private RadioGroup radioGroupLesson;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private long cid;
    private int s=2; //0默认　１推荐　２视频　３图文　４免费　５付费
    private int opr=5; //０默认　１最新发布　２最多浏览　３最多学员　４最多收藏　５　最多评论
    private int t=1;  //查看英文/中文版本 	0中文版本 1英文版本
    private long uid; //用户
    private int pi=1;
    private ArrayList<MixInfo> mixInfos=new ArrayList<>();
    private MixAdapter mixAdapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixing_light);
        Intent intent = getIntent();
        cid=Long.valueOf(intent.getIntExtra("id",-1));
        uid= GaiaApp.getAppInstance().getUserInfo().id;
        init();
        getInfo();
        setAdapter();
        setListener();
    }

    private void init() {
        radioGroupLesson= (RadioGroup) findViewById(R.id.mixing_radiogroup1);
        radioGroup = (RadioGroup) findViewById(R.id.mixing_radiogroup);
        gridLayoutManager = new GridLayoutManager(getApplication(),2);
        recyclerView = (RecyclerView) findViewById(R.id.mix_recyclerview);

        recyclerView.setLayoutManager(gridLayoutManager);

        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
        ((RadioButton)radioGroupLesson.getChildAt(0)).setChecked(true);

    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(MixingLightActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);

                parasJson(response);
            }
        };
        AcademyApiHelper.getMixList(2,s,opr,pi,t,uid,getApplicationContext(),handler);
    }

    private void parasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            MixInfo mixInfo=new MixInfo();
            JSONObject object = a.optJSONObject(i);
            mixInfo.setLearningCount(object.optInt("learningCount"));
            mixInfo.setAuthor(object.optString("auther"));
            mixInfo.setAvatar(object.optString("avatar"));
            mixInfo.setCover(object.optString("cover"));
            mixInfo.setAllowFree(object.optInt("allowFree"));
            mixInfo.setPrice(object.optLong("price"));
            mixInfo.setId(object.optInt("id"));
            mixInfo.setType(object.optInt("type"));//1,0,2,图文,视频,直播
            mixInfo.setName(object.optString("name"));
            mixInfo.setChaptCount(object.optInt("chapterCount"));
            mixInfo.setNickName(object.optString("nickName"));
            mixInfo.setPlayCount(object.optInt("playCount"));
            mixInfo.setBrowseCount(object.optInt("browseCount"));
            mixInfo.setGrade(object.optInt("grade"));
            mixInfo.setHourCount(object.optInt("hourCount"));
            mixInfo.setProprity(object.optInt("proprity"));
            JSONObject jsonObject = object.optJSONObject("createTime");
            Long time = jsonObject.optLong("time");
            mixInfo.setTime(time);
            mixInfos.add(mixInfo);
        }
        mixAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        mixAdapter = new MixAdapter(getApplicationContext(),mixInfos);
        recyclerView.setAdapter(mixAdapter);
        recyclerView.addItemDecoration(new MixLightDecoration(10,10,24,24));
    }



    private void setListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == gridLayoutManager.getItemCount() - 1) {
                    //加载更多
                    pi++;
                    getInfo();
                    mixAdapter.notifyDataSetChanged();
                }
            }
        });
        //最新最热的切换
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if(((RadioButton)radioGroup.getChildAt(i)).isChecked()){
                        if(i==0){
                            mixInfos.clear();
                            pi=1;
                            opr=5;
                            getInfo();
                        }else if(i==1){
                            mixInfos.clear();
                            pi=1;
                            opr=1;
                            getInfo();
                        }
                    }
                }
            }
        });
        //视频类型的切换
        radioGroupLesson.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if(((RadioButton)radioGroupLesson.getChildAt(i)).isChecked()){
                        if(i==0){
                            mixInfos.clear();
                            pi=1;
                            s=2;
                            getInfo();
                        }else if(i==1){
                            mixInfos.clear();
                            pi=1;
                            s=3;
                            getInfo();
                        }else if(i==2){
                            //Todo 没有直播
                            GaiaApp.showToast("暂时没有直播");
                        }
                    }
                }
            }
        });
    }
    public void MiXingBack(View view) {
        finish();
    }


    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width)*0.3);
        return itemHeight;
    }
}
