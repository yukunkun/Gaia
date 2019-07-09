package com.gaiamount.module_user.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gaiamount.R;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.BaseActionBarActivity;
import com.gaiamount.module_user.adapters.PersonalCreateAdapter;
import com.gaiamount.gaia_main.signin_signup.LoginActivity;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class PersonalWorksActivity extends BaseActionBarActivity {

    private long mUid;
    private GridView mCreateList;
    private List<PersonalWorks> mPersonalWorkses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_works);

        mUid = getIntent().getLongExtra("uid", -1);

        if (mUid != -1) {
            //获取作品列表
            mCreateList = (GridView) findViewById(R.id.personal_create_list);
            mCreateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ActivityUtil.startPlayerActivity(PersonalWorksActivity.this, mPersonalWorkses.get(position).getId(), 0);
                }
            });
            getDataFromNet(0, 0);

        } else {
            //去登陆
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void getDataFromNet(int ptype, int stype) {
        MJsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(PersonalWorksActivity.class) {
            @Override
            public void parseJson(JSONObject response) {
                super.parseJson(response);
                JSONArray a = response.optJSONArray("a");
                mPersonalWorkses = new Gson().fromJson(a.toString(), new TypeToken<List<PersonalWorks>>() {
                }.getType());

                mCreateList.setAdapter(new PersonalCreateAdapter(PersonalWorksActivity.this, mPersonalWorkses));
            }
        };
        WorksApiHelper.getMyWorks(mUid, 1,ptype, stype, this, jsonHttpResponseHandler);
    }

}
