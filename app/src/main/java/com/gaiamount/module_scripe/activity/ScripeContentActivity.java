package com.gaiamount.module_scripe.activity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.activity.AcademyDetailActivity;
import com.gaiamount.module_academy.adapter.ExpandAdapter;
import com.gaiamount.module_academy.adapter.TuWenAdapter;
import com.gaiamount.module_scripe.adapter.ContentAdapter;
import com.gaiamount.module_scripe.adapter.ScripeContentAdapter;
import com.gaiamount.module_scripe.bean.ScriptContent;
import com.gaiamount.module_scripe.fragment.ScripeAgreeDialog;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScripeContentActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageViewBack,imageViewMore;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout layout;
    private ListView mListView;
    private ExpandAdapter expandAdapter;
    private DrawerLayout drawerLayout;
    private ArrayList<String> strings=new ArrayList<>();
    private ScripeContentAdapter adapter1;
    private ContentAdapter contentAdapter;
    private long ind;
    private long sid;
    private long uid;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripe_content);
        sid = getIntent().getLongExtra("sid",-1);
        ind = getIntent().getLongExtra("ind",-1);
        uid = GaiaApp.getAppInstance().getUserInfo().id;
        pos = getIntent().getIntExtra("position",-1);
        init();
        getInfo();
        setAdapter();
        setListener();
        initListViewPos();
    }

    private void initListViewPos() {
        if(contentAdapter!=null){
            contentAdapter.setSelectedItem(pos);
        }
    }

    private void setListener() {
        imageViewBack.setOnClickListener(this);
        imageViewMore.setOnClickListener(this);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(scriptContents.get(position).getIsPublic()==1){ //1公开
                    contentAdapter.setSelectedItem(position);
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    contentAdapter.notifyDataSetChanged();
                }else {
                    ScripeAgreeDialog scripeAgreeDialog=ScripeAgreeDialog.newInstance(sid);
                    scripeAgreeDialog.show(getSupportFragmentManager(),"ScripeAgreeDialog");
                }

            }
        });
    }

    private void init() {
        imageViewBack= (ImageView) findViewById(R.id.tuwen_back);
        imageViewMore= (ImageView) findViewById(R.id.image_more);
        layout = (LinearLayout) findViewById(R.id.lin_drawer);
        mListView = (ListView) findViewById(R.id.scripe_listview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewss);
        recyclerView.setLayoutManager(linearLayoutManager);

        //动态设置drawer的宽度
        ///设置drawlayout的宽度
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.width = getWidth();
        layout.setLayoutParams(layoutParams);
    }

    private void getInfo() {
        //目录的网络请求
        MJsonHttpResponseHandler handler1=new MJsonHttpResponseHandler(AcademyDetailActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                ParaJson(response);
                contentAdapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        };
        ScriptApiHelper.getScriptContent(sid,uid,getApplicationContext(),handler1);
    }

    ArrayList<ScriptContent> scriptContents=new ArrayList<>();
    private void ParaJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            ScriptContent scriptContent=new ScriptContent();

            scriptContent.setTitle(jsonObject.optString("title"));
            scriptContent.setInd(jsonObject.optLong("ind"));
            scriptContent.setId(jsonObject.optLong("id"));
            scriptContent.setIsPublic(jsonObject.optInt("isPublic"));
            scriptContents.add(scriptContent);
        }
    }

    private void setAdapter() {

        adapter1 = new ScripeContentAdapter(strings,getApplicationContext());
        recyclerView.setAdapter(adapter1);

        contentAdapter = new ContentAdapter(getApplicationContext(),scriptContents);
        mListView.setAdapter(contentAdapter);
    }

    public int getWidth(){
        int width= ScreenUtils.instance().getWidth();
        return (int) (width*0.8);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.tuwen_back:
                finish();
            break;
            //more
            case R.id.image_more:
                boolean drawerOpen = drawerLayout.isDrawerOpen(Gravity.RIGHT);
                if(!drawerOpen){
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
        }

    }
}
