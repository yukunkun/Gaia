package com.gaiamount.module_user.notes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.apis.api_user.NoteApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_user.notes.adapter.NotesAdapter;
import com.gaiamount.module_user.notes.bean.NoteInfo;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {

    private Toolbar toobar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private NotesAdapter notesAdapter;
    private ArrayList<NoteInfo>  noteInfos=new ArrayList<>();
    private int page=1;
    private long uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        uid = getIntent().getLongExtra("uid", 0);
        init();
        getInfo();
        setAdapter();
        setListener();
    }

    private void init() {
        toobar = (Toolbar) findViewById(R.id.notes_toobar);
        toobar.setNavigationIcon(R.mipmap.back);

        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.notes_recycler);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(NotesListActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
            }
        };
        NoteApiHelper.noteList(uid,5,page,getApplicationContext(),handler);
    }

    private void paraJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        Log.i("----res", response.toString());
        for (int i = 0; i < a.length(); i++) {
            NoteInfo noteInfo=new NoteInfo();
            JSONObject jsonObject = a.optJSONObject(i);
            noteInfo.setIsRecommend(jsonObject.optInt("isRecommend"));
            noteInfo.setAddress(jsonObject.optString("address"));
            noteInfo.setNickName(jsonObject.optString("nickName"));
            noteInfo.setBrowseCount(jsonObject.optInt("browseCount"));
            noteInfo.setReprintCount(jsonObject.optInt("reprintCount"));
            noteInfo.setTitle(jsonObject.optString("title"));
            noteInfo.setContent(jsonObject.optString("content"));
            noteInfo.setCover(jsonObject.optString("cover"));
            noteInfo.setText(jsonObject.optString("text"));
            noteInfo.setShareCount(jsonObject.optInt("shareCount"));
            noteInfo.setRecommendCount(jsonObject.optInt("recommendCount"));
            noteInfo.setId(jsonObject.optInt("id"));
            noteInfo.setReprintId(jsonObject.optInt("reprintId"));
            JSONObject jsonObject1 = jsonObject.optJSONObject("createTime");
            noteInfo.setTime(jsonObject1.optLong("time"));
            noteInfos.add(noteInfo);
        }
        notesAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        notesAdapter = new NotesAdapter(getApplicationContext(),noteInfos,uid);
        mRecyclerView.setAdapter(notesAdapter);
        mRecyclerView.addItemDecoration(new MixLightDecoration(5,10,0,0));
    }

    private void setListener() {
        toobar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(lastVisibleItemPosition==linearLayoutManager.getItemCount() - 1){
                    page++;
                    getInfo();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
