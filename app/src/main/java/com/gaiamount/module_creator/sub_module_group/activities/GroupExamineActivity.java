package com.gaiamount.module_creator.sub_module_group.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.module_creator.sub_module_group.adapters.ListsAdapter;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupExamineActivity extends AppCompatActivity {
    private long gid;
    private int pi=1;
    private ListView mListView;
    private ArrayList<ExamineMem> examineMems=new ArrayList<>();
    private ListsAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_examine);
        gid=getIntent().getLongExtra("gid",-1);
        init();
        getInfo();
        setAdapter();

    }

    private void setAdapter() {
        listAdapter = new ListsAdapter(getApplicationContext(),examineMems);
        mListView.setAdapter(listAdapter);
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.list);
    }


    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(GroupExamineActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parseJsons(response);
            }
        };

        GroupApiHelper.getGroupExamine(gid,pi,getApplicationContext(),handler);
    }

    private void parseJsons(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            ExamineMem examineMem=new ExamineMem();
            examineMem.setAvatar(jsonObject.optString("avatar"));
            examineMem.setNickname(jsonObject.optString("nickName"));
            examineMem.setMid(jsonObject.optLong("mid"));
            examineMem.setUid(jsonObject.optLong("uid"));
            examineMems.add(examineMem);
        }
        listAdapter.notifyDataSetChanged();
    }

    public void Listback(View view) {
        finish();
    }

   public class ExamineMem{
        private String avatar;
        private long uid;
        private String nickname;
        private long mid;

        public long getMid() {
            return mid;
        }

        public void setMid(long mid) {
            this.mid = mid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
