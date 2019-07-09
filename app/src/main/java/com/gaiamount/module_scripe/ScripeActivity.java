package com.gaiamount.module_scripe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_material.adapters.MaterialAdapter;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_scripe.adapter.ScripeAdapter;
import com.gaiamount.module_scripe.bean.EdidsInfo;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScripeActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<ScripeInfo> scripeInfos=new ArrayList<>();
    private ArrayList<EdidsInfo> edidsInfos=new ArrayList<>();
    private ScripeAdapter adapter;
    private int pi=1;
    private long uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripe);
        uid= GaiaApp.getAppInstance().getUserInfo().id;
        init();
        getInfo();
        setAdapter();
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    ArrayList<String> imagePath=new ArrayList<>();
    ArrayList<Long> ids=new ArrayList<>();
    private void getInfo() {
        //获取banner条的数据
        MJsonHttpResponseHandler handler1=new MJsonHttpResponseHandler(ScripeActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray a = response.optJSONArray("a");
                if(a.length()>5){
                    for (int i = 0; i < 5; i++) {
                        JSONObject jsonObject = a.optJSONObject(1);
                        imagePath.add(jsonObject.optString("cover"));
                        String url = jsonObject.optString("url");
                        int i1 = url.lastIndexOf("/");
                        ids.add(Long.valueOf(url.substring(i1+1,url.length())));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };
        ScriptApiHelper.getScripeBanner(handler1);

        //获取中间的数据
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ScripeActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parseJsons(response);
            }
        };
        ScriptApiHelper.getRec(0,0,6,1,pi,4,getApplicationContext(),handler);

        //获取最佳编剧的数据
        MJsonHttpResponseHandler handler2=new MJsonHttpResponseHandler(ScripeActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parseJsonEdits(response);

            }
        };
        ScriptApiHelper.getScripeEdits(uid,getApplicationContext(),handler2);
    }

    private void parseJsonEdits(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            EdidsInfo edidsInfo=new EdidsInfo();
            JSONObject object = a.optJSONObject(i);
            edidsInfo.setAvatar(object.optString("avatar"));
            edidsInfo.setUid(object.optLong("uid"));
            edidsInfo.setNickName(object.optString("nickName"));
            edidsInfo.setSignature(object.optString("signature"));
            edidsInfo.setJob(object.optString("job"));
            edidsInfo.setIsFocus(object.optInt("isFocus"));
            edidsInfos.add(edidsInfo);
        }
        adapter.notifyDataSetChanged();
    }

    private void parseJsons(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject object = a.optJSONObject(i);
            ScripeInfo scripeInfo=new ScripeInfo();
            scripeInfo.setNickName(object.optString("nickName"));
            scripeInfo.setIntroduce(object.optString("introduce"));
            scripeInfo.setAvatar(object.optString("avatar"));
            scripeInfo.setType(object.optString("type"));
            scripeInfo.setTitle(object.optString("title"));
            scripeInfo.setSpace(object.optInt("space"));  // 1 长篇 0 短篇
            scripeInfo.setBrowserCount(object.optInt("browserCount"));
            scripeInfo.setSid(object.optInt("sid"));
            scripeInfo.setOutline(object.optString("outline"));
            scripeInfo.setIsFree(object.optInt("isFree"));
            scripeInfo.setPrice(object.optInt("price"));
            scripeInfo.setState(object.optInt("state"));  // 1 完结 0 连载
            scripeInfo.setId(object.optLong("id"));
            scripeInfo.setCover(object.optString("cover"));
            scripeInfo.setCollectCount(object.optInt("collectCount"));
            scripeInfo.setCommentCount(object.optInt("commentCount"));
            scripeInfos.add(scripeInfo);
        }
        adapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        adapter = new ScripeAdapter(getApplicationContext(),scripeInfos,imagePath,ids,edidsInfos);
        mRecyclerView.setAdapter(adapter);
    }
    public void MaterialBacks(View view) {
        finish();
    }

    public void GolobSearch(View view) {
        ActivityUtil.startGlobalSearchActivity(this,"",5);
    }

}
