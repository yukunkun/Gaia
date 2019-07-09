package com.gaiamount.module_im.relate_me;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.apis.api_im.AynamicsHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.my_attention.AtentionAdapter;
import com.gaiamount.module_im.my_attention.AttentInfo;
import com.gaiamount.util.SPUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RelateActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;
    private int pi=1;
    private RelateAdapter atentionAdapter;
    private ArrayList<AttentInfo> attentInfos=new ArrayList<>();
    private boolean readTag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relate);
        init();
        initToolbar();
        getInfo();
        setAdapter();
        setListener();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutManager = new LinearLayoutManager(this);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_fresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.attent_recyclerview);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
            }
        });
    }

    private void getInfo() {

        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(RelateActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
            }
        };
        AynamicsHelper.DynamicRe(GaiaApp.getAppInstance().getUserInfo().id,pi,getApplicationContext(),handler);

    }

    private void parasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        JSONObject o=response.optJSONObject("o");
        int total = o.optInt("total");
        for (int i = 0; i <a.length(); i++) {
            AttentInfo attentInfo=new AttentInfo();
            JSONObject jsonObject = a.optJSONObject(i);
            attentInfo.setTypes(jsonObject.optInt("types"));
            attentInfo.setType(jsonObject.optInt("type"));
            attentInfo.setAddress(jsonObject.optString("address"));
            attentInfo.setNickName(jsonObject.optString("nickName"));
            attentInfo.setContentId(jsonObject.optInt("contentId"));
            attentInfo.setDescription(jsonObject.optString("description"));
            attentInfo.setMessage(jsonObject.optString("message"));
            attentInfo.setAvatar(jsonObject.optString("avatar"));
            attentInfo.setCover(jsonObject.optString("cover"));
            attentInfo.setFocus(jsonObject.optInt("focus"));
            attentInfo.setGroupName(jsonObject.optString("groupName"));
            attentInfo.setScreenshot(jsonObject.optString("screenshot"));
            attentInfo.setOtherId(jsonObject.optLong("otherId"));
            attentInfo.setContentName(jsonObject.optString("contentName"));
            attentInfo.setFocusName(jsonObject.optString("focusName"));
            AttentInfo.CreateTimeBean timeBean=new AttentInfo.CreateTimeBean();
            JSONObject createTime = jsonObject.optJSONObject("createTime");
            timeBean.setTime(createTime.optLong("time"));
            attentInfo.setCreateTime(timeBean);
            attentInfos.add(attentInfo);
        }
        atentionAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        if(!readTag){
            readTag=true;
            SPUtils.getInstance(this).save("relate_me",total);
        }
    }

    private void setAdapter() {

        atentionAdapter = new RelateAdapter(getApplicationContext(),attentInfos);
        mRecyclerView.setAdapter(atentionAdapter);
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pi=1;
                attentInfos.clear();
                getInfo();
            }
        });
        //加载更多
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if(lastVisibleItemPosition==layoutManager.getItemCount() - 1){
                    pi++;
                    getInfo();
                }
            }
        });

    }
}
