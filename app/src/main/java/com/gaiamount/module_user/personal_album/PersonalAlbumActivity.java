package com.gaiamount.module_user.personal_album;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.module_creator.fragment.AlbumDialog;
import com.gaiamount.module_creator.sub_module_album.SetUpAlbumActivity;
import com.gaiamount.module_user.personal_album.adapter.MyAdapter;
import com.gaiamount.module_user.personal_album.bean.OnEventPersonAlbum;
import com.gaiamount.module_user.personal_album.bean.PersonAlbum;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PersonalAlbumActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RelativeLayout layout;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private long uid;
    private ArrayList<PersonAlbum> personAlums=new ArrayList<>();
    private MyAdapter myAdapter;
    private static final int CREATE_ALBUM = 23;
    private static final int ALBUM_DETAIL = 24;
    private ImageView imageView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_album);
        EventBus.getDefault().register(this);
        uid = getIntent().getLongExtra("uid",0);
        context = this;
        init();
        getInfo();
        setAdapter();
        setListener();
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        layout = (RelativeLayout) findViewById(R.id.empty_hint);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_album);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        imageView = (ImageView)findViewById(R.id.add);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new MixLightDecoration(10,10,20,20));
        if(uid!= GaiaApp.getAppInstance().getUserInfo().id){
            imageView.setVisibility(View.GONE);
        }

        //进入创建页面
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalAlbumActivity.this, SetUpAlbumActivity.class);
                intent.putExtra(SetUpAlbumActivity.GID, Long.valueOf(0));//个人专辑,不要这个参数,这是小组专辑的参数
                intent.putExtra(SetUpAlbumActivity.ALBUM_TYPE, 0);
                startActivityForResult(intent, CREATE_ALBUM);
            }
        });
    }

    private void setAdapter() {
        myAdapter = new MyAdapter(context,personAlums,uid);
        mRecyclerView.setAdapter(myAdapter);
    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(PersonalAlbumActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
            }
        };
        AlbumApiHelper.getPersonAlbumList(uid,this,handler);
    }

    private void parasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        if(a.length()!=0){
            layout.setVisibility(View.GONE);
            for (int i = 0; i < a.length(); i++) {
                PersonAlbum personAlbum=new PersonAlbum();
                JSONObject jsonObject = a.optJSONObject(i);
                personAlbum.setBgImg(jsonObject.optString("bgImg"));
                personAlbum.setName(jsonObject.optString("name"));
                personAlbum.setIsPublic(jsonObject.optInt("isPublic"));
                personAlbum.setId(jsonObject.optLong("id"));
                personAlbum.setUserId(jsonObject.optLong("userId"));
                personAlums.add(personAlbum);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnEventPersonAlbum event) {
        if(event.position!=-1){
            AlbumDialog alertDialog= AlbumDialog.newInstance(event.aid,event.position);
            alertDialog.show(getSupportFragmentManager(),"personAlbumActivity");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAlbumFragment(OnEventId event) {
        if(event.id==1){
            personAlums.remove(event.position);
            myAdapter.notifyItemRemoved(event.position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALBUM_DETAIL || requestCode == CREATE_ALBUM && resultCode == RESULT_OK) {
            //刷新
            personAlums.clear();
            getInfo();
        }
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
