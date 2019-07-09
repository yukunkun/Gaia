package com.gaiamount.module_im.new_friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gaiamount.R;

import org.json.JSONObject;

public class FansActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private int pi=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        init();
        initToolbar();
        getInfo();
        setAdapter();
        setListener();
    }
    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutManager = new LinearLayoutManager(this);
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

       /* MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ScripeApplyActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
            }
        };
        AynamicsHelper.DynamicRe(GaiaApp.getAppInstance().getUserInfo().id,pi,getApplicationContext(),handler);*/

    }

    private void parasJson(JSONObject response) {

    }

    private void setAdapter() {
    }

    private void setListener() {


    }
}
