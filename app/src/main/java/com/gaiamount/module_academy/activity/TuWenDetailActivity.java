package com.gaiamount.module_academy.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.adapter.ExpandAdapter;
import com.gaiamount.module_academy.adapter.TuWenAdapter;
import com.gaiamount.module_academy.bean.Contents;
import com.gaiamount.module_academy.bean.LessonInfo;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TuWenDetailActivity extends AppCompatActivity {
    private ImageView imageViewBack,imageViewMore;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout layout;
    private ExpandableListView mExpandableListView;
    private TuWenAdapter adapter;
    private ExpandAdapter expandAdapter;
    private ArrayList<Contents> contentList=new ArrayList<>();
    private ArrayList<ArrayList<LessonInfo>> lessonList=new ArrayList<>();
    private int groupPosition;
    private int childPosition;
    private ArrayList<String> stringsList=new ArrayList<>();
    private String response;
    private long cid;
    private long uid;
    private DrawerLayout drawerLayout;
    private String uri= Configs.TUWEN_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_wen_detail);
        Intent intent = getIntent();
        childPosition=intent.getIntExtra("childPosition",-1);
        groupPosition=intent.getIntExtra("groupPosition",-1);
        response=intent.getStringExtra("response");
        cid=intent.getLongExtra("cid",-1);
        uid= GaiaApp.getAppInstance().getUserInfo().id;
        init();
        getJson();
        getinfo();
        sendWatchLeng();
        setAdapter();
        setListener();
    }



    private void init() {
        imageViewBack= (ImageView) findViewById(R.id.tuwen_back);
        imageViewMore= (ImageView) findViewById(R.id.image_more);
        layout = (LinearLayout) findViewById(R.id.drawer_linlayout);
        mExpandableListView = (ExpandableListView) findViewById(R.id.expandable);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewss);
        recyclerView.setLayoutManager(linearLayoutManager);

        //动态设置drawer的宽度
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.width = getWidth();
        layout.setLayoutParams(layoutParams);
    }

    private void getJson() {
        try {
            JSONObject responJsonobject=new JSONObject(response);
            JSONArray a = responJsonobject.optJSONArray("a");
            for (int i = 0; i < a.length(); i++) {
                Contents info=new Contents();
                //章节的数据
                JSONObject object = a.optJSONObject(i);
                JSONObject chapter = object.optJSONObject("chapter");
                info.setName(chapter.optString("name"));
                info.setInd(chapter.optInt("ind"));
                info.setChapterId(chapter.optLong("id"));
                contentList.add(info);
                //节数的数据
                JSONArray hour = object.optJSONArray("hour");
                ArrayList<LessonInfo> lessonInfos=new ArrayList<>();
                for (int j = 0; j <hour.length() ; j++) {
                    LessonInfo lessonInfo=new LessonInfo();
                    JSONObject jsonObject = hour.optJSONObject(j);
                    lessonInfo.setName(jsonObject.optString("name"));
                    lessonInfo.setId(jsonObject.optInt("id"));
                    lessonInfo.setScreenshot(jsonObject.optString("screenshot"));
                    lessonInfo.setLen(jsonObject.optInt("len"));
                    lessonInfo.setWatchLen(jsonObject.optInt("watchLen"));
                    lessonInfo.setIsPublic(jsonObject.optInt("isPublic"));
                    lessonInfos.add(lessonInfo);
                }
                lessonList.add(lessonInfos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getinfo() {
        int hid = lessonList.get(groupPosition).get(childPosition).getId();
        String URL=uri+"uid="+uid+"&cid="+cid+"&hid="+hid;
        stringsList.add(URL);
    }

    private long id;
    //发送进度
    private void sendWatchLeng() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(TuWenDetailActivity.class){

            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                if(response!=null){
                    JSONObject o = response.optJSONObject("o");
                    id = o.optLong("lrid");
                    sendProgress();
                }
            }
        };

        AcademyApiHelper.getTuWen(cid,contentList.get(groupPosition).getChapterId(),lessonList.get(groupPosition).get(childPosition).getId(),
                getApplicationContext(),handler);
    }

    //发送的网络请求
    public void sendProgress(){
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(TuWenDetailActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
            }
        };
        AcademyApiHelper.stareTuWenRecord(id,-1,getApplicationContext(),handler);
    }



    private void setAdapter() {
        adapter = new TuWenAdapter(stringsList,getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //设置适配器
        expandAdapter = new ExpandAdapter(getApplicationContext(),contentList,lessonList);
        mExpandableListView.setAdapter(expandAdapter);
        //默认选中变色
        if(expandAdapter!=null){
            expandAdapter.setSelectedItem(groupPosition,childPosition);
            expandAdapter.notifyDataSetChanged();
        }

        open();
    }

    //让所有的子控件都是打开状态
    private void open() {
        for(int i = 0; i <expandAdapter.getGroupCount(); i++){
            mExpandableListView.expandGroup(i);
        }
    }

    private void setListener() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OnEventId(0,0));
                finish();
            }
        });

        imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition1, int childPosition1, long id) {
                stringsList.clear();
                long chapterId = contentList.get(groupPosition1).getChapterId();
                int hid = lessonList.get(groupPosition1).get(childPosition1).getId();
                String URL=uri+"uid="+uid+"&cid="+cid+"&hid="+hid;
                stringsList.add(URL);
                //选中变色
                expandAdapter.setSelectedItem(groupPosition1,childPosition1);
                adapter.notifyDataSetChanged();
                expandAdapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(Gravity.RIGHT);
                //数据更新获取到点击的位置,发送进度
                groupPosition=groupPosition1;
                childPosition=childPosition1;
                sendWatchLeng();

                return false;
            }
        });
    }

    public int getWidth(){
        int width= ScreenUtils.instance().getWidth();
        return (int) (width*0.8);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
