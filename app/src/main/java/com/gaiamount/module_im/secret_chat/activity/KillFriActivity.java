package com.gaiamount.module_im.secret_chat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gaiamount.R;
import com.gaiamount.apis.api_im.ImApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.secret_chat.adapter.KillListAdapter;
import com.gaiamount.module_im.secret_chat.bean.KillInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/*
*   kun 拉黑页面
* */
public class KillFriActivity extends AppCompatActivity {
    ListView mListView;
    private List<KillInfo> killList=new ArrayList<>();
    private int pi=1;
    private KillListAdapter listAdapter;
    private RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill_fri);
        init();
        getInfo();
        setAdapter();
        Listener();
    }

    private void init() {
        mListView= (ListView) findViewById(R.id.kill_list);
        layout= (RelativeLayout) findViewById(R.id.kill_tishi);
    }
    private void getInfo() {

        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(KillFriActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                ParasJson(response);
            }
        };

        ImApiHelper.getKillList(GaiaApp.getAppInstance().getUserInfo().id,pi,getApplicationContext(),handler);
    }

    private void ParasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            KillInfo killInfo=new KillInfo();
            JSONObject jsonObject = a.optJSONObject(i);
            killInfo.setId(jsonObject.optInt("id"));
            killInfo.setName(jsonObject.optString("nickname"));
            killInfo.setAvatar(jsonObject.optString("avatar"));
            killList.add(killInfo);
            layout.setVisibility(View.GONE);
        }
        listAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        listAdapter=new KillListAdapter(getApplicationContext(),killList);
        mListView.setAdapter(listAdapter);
    }

    private void Listener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(visibleItemCount+firstVisibleItem==totalItemCount-1){
                    pi++;
                    getInfo();
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void Kill(View view) {
        finish();
    }
}
