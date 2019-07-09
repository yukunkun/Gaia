package com.gaiamount.module_scripe.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_comment.CommentApiHelper;
import com.gaiamount.apis.api_scripe.ScriptApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.activity.AcademyDetailActivity;
import com.gaiamount.module_academy.bean.CommentInfo;
import com.gaiamount.module_material.activity.video.MaterialGropDialog;
import com.gaiamount.module_player.fragments.PlayerColFrag;
import com.gaiamount.module_scripe.adapter.ContentAdapter;
import com.gaiamount.module_scripe.adapter.ScripeCommAdapter;
import com.gaiamount.module_scripe.bean.OnEventScripeComm;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.module_scripe.bean.ScriptContent;
import com.gaiamount.module_scripe.fragment.ScripeAgreeDialog;
import com.gaiamount.module_scripe.fragment.ScripeCommentDialog;
import com.gaiamount.module_scripe.fragment.ScripeGropDialog;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.ShareUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.improved.NOScrolledListView;
import com.google.gson.JsonObject;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScripeDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewBack;
    private ExpandableTextView yinYan1;
    private ExpandableTextView daGan;
    private DrawerLayout drawerLayout;
    private RelativeLayout relatConAll;
    private LinearLayout layout;
    private long sid;
    private String[] stringArray;

    /**
     * keywords : 数量的减肥绿色的,是看得出
     * introduce : 灵感来自于盗墓笔记
     * collectCount : 0
     * avatar : avatar/u1688/1466747698611.png
     * type : 1,2,3
     * title : 寻龙诀
     * space : 1
     * outline : 水电费水电费
     * commendCount : 16
     * isFree : 1
     * price : 0
     * background : 现代
     * id : 1
     * state : 1
     */

    private String keywords;
    private String introduce;
    private int collectCount;
    private String avatar;
    private String type;
    private String title;
    private int space;
    private String outline;
    private int commendCount;
    private int isFree;
    private int price;
    private String background;
    private long id;
    private int state;
    private int pi=1;

    private ImageView imageViewCover;
    private TextView textViewLoading;
    private TextView textViewTitle;
    private CircleImageView imageViewHead;
    private TextView textViewEdit;
    private TextView textViewTicai;
    private TextView textViewRead;
    private TextView textViewPrice;
    private TextView textViewTag_1;
    private TextView textViewTag_2;
    private TextView textViewTag_3;
    private TextView textViewTag_4;
    private TextView textViewTag_5;
    private TextView textViewTag_6;
    private String nickName;
    private RelativeLayout layoutCon_1;
    private RelativeLayout layoutCon_2;
    private RelativeLayout layoutCon_3;
    private TextView textViewCon_1;
    private TextView textViewCon_2;
    private TextView textViewCon_3;
    private ImageView imageViewLoc_1;
    private ImageView imageViewLoc_2;
    private ImageView imageViewLoc_3;
    private ContentAdapter contentAdapter;
    private NOScrolledListView noSrolistView;
    private ListView listView;
    private long uid;
    private LinearLayout layoutColl;
    private LinearLayout layoutAdd;
    private LinearLayout layoutShare;
    private ImageView imageViewColl;
    private ScripeCommAdapter adapter;
    private TextView textViewComm;
    private String cover;
    private String upDateTime;
    private int isCollect;
    private int isSale;
    private String time;
    private int status;
    private long sUid;
    private TextView textViewPinLun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripe_detail);
        EventBus.getDefault().register(this);
        sid = getIntent().getLongExtra("sid",-1);
        uid = GaiaApp.getAppInstance().getUserInfo().id;
        init();
        getInfo();
        getComm();
        setAdapter();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(toString());
    }

    private void init() {
        imageViewBack = (ImageView) findViewById(R.id.scripe_detail_back);
        yinYan1 = (ExpandableTextView)findViewById(R.id.expand_text_view);
        daGan = (ExpandableTextView)findViewById(R.id.expand_text_view_1);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        relatConAll = (RelativeLayout) findViewById(R.id.ic_script_con_all);
        layout = (LinearLayout) findViewById(R.id.layout);
        //封面
        imageViewCover = (ImageView) findViewById(R.id.scripe_detail_cover);
        //连载
        textViewLoading = (TextView) findViewById(R.id.scripe_detail_lin);
        textViewTitle = (TextView) findViewById(R.id.scripe_detail_name);
        imageViewHead = (CircleImageView) findViewById(R.id.scripe_detail_head);
        textViewEdit = (TextView) findViewById(R.id.scripe_detail_editor);
        textViewTicai = (TextView) findViewById(R.id.scripe_detail_theme);
        textViewRead = (TextView) findViewById(R.id.scripe_detail_read);
        textViewPrice = (TextView) findViewById(R.id.scripe_detail_price);
        layoutCon_1 = (RelativeLayout) findViewById(R.id.scripe_detail_1);
        layoutCon_2 = (RelativeLayout) findViewById(R.id.scripe_detail_2);
        layoutCon_3 = (RelativeLayout) findViewById(R.id.scripe_detail_3);
        textViewCon_1 = (TextView) findViewById(R.id.scripe_detail_cont_1);
        textViewCon_2 = (TextView) findViewById(R.id.scripe_detail_cont_2);
        textViewCon_3 = (TextView) findViewById(R.id.scripe_detail_cont_3);
        imageViewLoc_1 = (ImageView) findViewById(R.id.scripe_detail_lock_1);
        imageViewLoc_2 = (ImageView) findViewById(R.id.scripe_detail_lock_2);
        imageViewLoc_3 = (ImageView) findViewById(R.id.scripe_detail_lock_3);
        listView = (ListView) findViewById(R.id.scripe_listview);                      //目录的listview
        noSrolistView = (NOScrolledListView) findViewById(R.id.scripe_detail_comment); //评论的listview

        textViewPinLun = (TextView) findViewById(R.id.scrite_detail_pinlin);
        //中间的三个按钮
        layoutColl = (LinearLayout) findViewById(R.id.scripe_detail_coll);
        layoutAdd = (LinearLayout) findViewById(R.id.scripe_detail_add_group);
        layoutShare = (LinearLayout) findViewById(R.id.scripe_detail_share);
        imageViewColl = (ImageView) findViewById(R.id.scripe_detail_heart);

        //标签
        textViewTag_1 = (TextView) findViewById(R.id.tag_1);
        textViewTag_2 = (TextView) findViewById(R.id.tag_2);
        textViewTag_3 = (TextView) findViewById(R.id.tag_3);
        textViewTag_4 = (TextView) findViewById(R.id.tag_4);
        textViewTag_5 = (TextView) findViewById(R.id.tag_5);
        textViewTag_6 = (TextView) findViewById(R.id.tag_6);
        //设置标签的背景
        textViewTag_1.setBackgroundResource(R.drawable.shape_script_tags);
        textViewTag_2.setBackgroundResource(R.drawable.shape_script_tags);
        textViewTag_3.setBackgroundResource(R.drawable.shape_script_tags);
        textViewTag_4.setBackgroundResource(R.drawable.shape_script_tags);
        textViewTag_5.setBackgroundResource(R.drawable.shape_script_tags);
        textViewTag_6.setBackgroundResource(R.drawable.shape_script_tags);
        //评论
        textViewComm = (TextView) findViewById(R.id.scripe_detail_write_comm);
        ///设置drawlayout的宽度
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.width = getHeight();
        layout.setLayoutParams(layoutParams);

        stringArray=getResources().getStringArray(R.array.scripe_list);

    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ScripeDetailActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
                setViewInfo();
            }
        };

        ScriptApiHelper.getScriptDetail(sid,uid ,getApplicationContext(),handler);

    }

    private void getComm() {
        //目录的网络请求
        MJsonHttpResponseHandler handler1=new MJsonHttpResponseHandler(AcademyDetailActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                ParaJson(response);
                contentAdapter.notifyDataSetChanged();
            }
        };
        ScriptApiHelper.getScriptContent(sid,uid,getApplicationContext(),handler1);
        //获取评论列表
        MJsonHttpResponseHandler handler2=new MJsonHttpResponseHandler(ScripeDetailActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJsons(response);
            }
        };
        CommentApiHelper.getCommentData(sid,3,pi,50,getApplicationContext(), handler2);
    }

    ArrayList<CommentInfo> commentInfos=new ArrayList<>();
    private void paraJsons(JSONObject response) {

        JSONArray jsonObject2 = response.optJSONArray("a");
        if(response.length()==0){
            return;
        }
        for (int i = 0; i < jsonObject2.length(); i++) {
            JSONObject jsonObject = jsonObject2.optJSONObject(i);
            CommentInfo info=new CommentInfo();
            info.setContent(jsonObject.optString("content"));
            info.setAvatar(jsonObject.optString("avatar"));
            JSONObject jsonObject1 = jsonObject.optJSONObject("createTime");
            info.setTime(jsonObject1.optLong("time"));
            info.setNickName(jsonObject.optString("nickName"));
            commentInfos.add(info);
        }
        adapter.notifyDataSetChanged();
        JSONObject jsonObject = response.optJSONObject("o");
        int total = jsonObject.optInt("total");
        textViewPinLun.setText("评论"+"("+total+")");
    }

    Handler handler=new Handler();
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnEventScripeComm event) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            commentInfos.clear();
                            pi=1;
                            getComm();
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void setAdapter() {
        contentAdapter = new ContentAdapter(getApplicationContext(),scriptContents);
        listView.setAdapter(contentAdapter);
        //评论列表
        adapter = new ScripeCommAdapter(getApplicationContext(),commentInfos);
        noSrolistView.setAdapter(adapter);
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
            scriptContent.setIsPublic(jsonObject.optInt("isPublic")); //0 非公开 1,公开
            scriptContents.add(scriptContent);
            setContent();
        }
    }

    private void setContent() {
        if(scriptContents.size()==1){
            layoutCon_1.setVisibility(View.VISIBLE);
            textViewCon_1.setText(scriptContents.get(0).getTitle());
            if(scriptContents.get(0).getIsPublic()==1){  //1 公开
                imageViewLoc_1.setVisibility(View.GONE);
            }
        }else if(scriptContents.size()==2){
            layoutCon_1.setVisibility(View.VISIBLE);
            textViewCon_1.setText(scriptContents.get(0).getTitle());
            layoutCon_2.setVisibility(View.VISIBLE);
            textViewCon_2.setText(scriptContents.get(1).getTitle());
            if(scriptContents.get(0).getIsPublic()==1){
                imageViewLoc_1.setVisibility(View.GONE);
            }
            if(scriptContents.get(1).getIsPublic()==1){
                imageViewLoc_2.setVisibility(View.GONE);
            }
        }else if (scriptContents.size()>2){
            layoutCon_1.setVisibility(View.VISIBLE);
            textViewCon_1.setText(scriptContents.get(0).getTitle());
            layoutCon_2.setVisibility(View.VISIBLE);
            textViewCon_2.setText(scriptContents.get(1).getTitle());
            layoutCon_3.setVisibility(View.VISIBLE);
            textViewCon_3.setText(scriptContents.get(2).getTitle());
            if(scriptContents.get(0).getIsPublic()==1){
                imageViewLoc_1.setVisibility(View.GONE);
            }
            if(scriptContents.get(1).getIsPublic()==1){
                imageViewLoc_2.setVisibility(View.GONE);
            }
            if(scriptContents.get(2).getIsPublic()==1){
                imageViewLoc_3.setVisibility(View.GONE);
            }
        }
    }

    private void parasJson(JSONObject response) {

        JSONObject jsonObject = response.optJSONObject("o");
        keywords=jsonObject.optString("keywords");
        introduce=jsonObject.optString("introduce");
        collectCount=jsonObject.optInt("collectCount");
        avatar=jsonObject.optString("avatar");
        type=jsonObject.optString("type");
        title=jsonObject.optString("title");
        space=jsonObject.optInt("space");
        outline=jsonObject.optString("outline");
        commendCount=jsonObject.optInt("commendCount");
        isFree=jsonObject.optInt("isFree");
        price=jsonObject.optInt("price");
        nickName = jsonObject.optString("nickName");
        background=jsonObject.optString("background");
        id=jsonObject.optLong("id");
        state=jsonObject.optInt("state");
        cover = jsonObject.optString("cover");
        upDateTime = jsonObject.optString("upDateTime");
        isCollect = jsonObject.optInt("isCollect");
        isSale = jsonObject.optInt("isSale");
        time = jsonObject.optString("time");
        status = jsonObject.optInt("status");
        sUid = jsonObject.optLong("userId");
    }

    private void setViewInfo() {
        textViewTitle.setText(title);
        textViewRead.setText(commendCount+"阅读");
        textViewEdit.setText("编剧:"+nickName);
        //头像
        Glide.with(getApplicationContext()).load(Configs.COVER_PREFIX+avatar).placeholder(R.mipmap.ic_avatar_default).into(imageViewHead);
        //图片
        Glide.with(getApplicationContext()).load(Configs.COVER_PREFIX+cover).placeholder(R.mipmap.personal_bg).into(imageViewCover);

        yinYan1.setText(introduce);
        daGan.setText(outline);

        if(isFree==0){
            textViewPrice.setText("仅展示");
            textViewPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.color_33bbff));
        }else {
            textViewPrice.setText(price+"元");
        }
        //连载状态
        if(state==0){
            textViewLoading.setText("连载中");
        }else {
            textViewLoading.setText("已完结");
        }

        //题材
        StringBuilder builder=new StringBuilder();
        builder.append("题材:");
        if(type!=null&& !TextUtils.isEmpty(type)) {
            String[] split = type.split(",");
            for (int i = 0; i < split.length; i++) {
                builder.append(changeString(split[i])).append("/");
            }
        }
        builder.append(" 背景:"+background);
        if(space==1){  // 1 长篇 0 短篇
            builder.append(" 篇幅:"+"长篇");
        }else {
            builder.append(" 篇幅:"+"短篇");
        }
        //题材设置
        textViewTicai.setText(new String(builder));

        //设置标签
        String[] split = keywords.split(",");
        int length = split.length;
        //收藏显示
        if(isCollect==1){
            imageViewColl.setImageResource(R.mipmap.ic_script_like_ss);
        }else {
            imageViewColl.setImageResource(R.mipmap.ic_script_like_n);
        }


        //设置标签数据匹配
        if(length==1){
            textViewTag_1.setText(split[0]);
            textViewTag_1.setVisibility(View.VISIBLE);
        }else if(length==2){
            textViewTag_1.setText(split[0]);
            textViewTag_1.setVisibility(View.VISIBLE);
            textViewTag_2.setText(split[1]);
            textViewTag_2.setVisibility(View.VISIBLE);
        }else if(length==3){
            textViewTag_1.setText(split[0]);
            textViewTag_1.setVisibility(View.VISIBLE);
            textViewTag_2.setText(split[1]);
            textViewTag_2.setVisibility(View.VISIBLE);
            textViewTag_3.setText(split[2]);
            textViewTag_3.setVisibility(View.VISIBLE);
        }else if(length==4){
            textViewTag_1.setText(split[0]);
            textViewTag_1.setVisibility(View.VISIBLE);
            textViewTag_2.setText(split[1]);
            textViewTag_2.setVisibility(View.VISIBLE);
            textViewTag_3.setText(split[2]);
            textViewTag_3.setVisibility(View.VISIBLE);
            textViewTag_4.setText(split[3]);
            textViewTag_4.setVisibility(View.VISIBLE);
        }else if(length==5){
            textViewTag_1.setText(split[0]);
            textViewTag_1.setVisibility(View.VISIBLE);
            textViewTag_2.setText(split[1]);
            textViewTag_2.setVisibility(View.VISIBLE);
            textViewTag_3.setText(split[2]);
            textViewTag_3.setVisibility(View.VISIBLE);
            textViewTag_4.setText(split[3]);
            textViewTag_4.setVisibility(View.VISIBLE);
            textViewTag_5.setText(split[4]);
            textViewTag_5.setVisibility(View.VISIBLE);
        }else if(length>=6){
            textViewTag_1.setText(split[0]);
            textViewTag_1.setVisibility(View.VISIBLE);
            textViewTag_2.setText(split[1]);
            textViewTag_2.setVisibility(View.VISIBLE);
            textViewTag_3.setText(split[2]);
            textViewTag_3.setVisibility(View.VISIBLE);
            textViewTag_4.setText(split[3]);
            textViewTag_4.setVisibility(View.VISIBLE);
            textViewTag_5.setText(split[4]);
            textViewTag_5.setVisibility(View.VISIBLE);
            textViewTag_6.setText(split[5]);
            textViewTag_6.setVisibility(View.VISIBLE);
        }

    }

    private void setListener() {

        relatConAll.setOnClickListener(this);//所有目录
        layoutColl.setOnClickListener(this);//收藏
        layoutShare.setOnClickListener(this); //分享
        layoutAdd.setOnClickListener(this); //添加小组
        textViewComm.setOnClickListener(this);

        //标签的点击事件
        textViewTag_1.setOnClickListener(this);
        textViewTag_2.setOnClickListener(this);
        textViewTag_3.setOnClickListener(this);
        textViewTag_4.setOnClickListener(this);
        textViewTag_5.setOnClickListener(this);
        textViewTag_6.setOnClickListener(this);

        //点击头像
        imageViewHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startPersonalActivity(getApplicationContext(),sUid);
            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contentAdapter.setSelectedItem(position);
                if(scriptContents.get(position).getIsPublic()==1){  //公开
                    ScriptContent item = (ScriptContent)contentAdapter.getItem(position);
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    ActivityUtil.startScripeContentActivity(getApplicationContext(),sid,item.getInd(),position);
                    contentAdapter.notifyDataSetChanged();
                }else {
                    ScripeAgreeDialog scripeAgreeDialog=ScripeAgreeDialog.newInstance(sid);
                    scripeAgreeDialog.show(getSupportFragmentManager(),"ScripeAgreeDialog");
                }
            }
        });

        noSrolistView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount==firstVisibleItem+visibleItemCount-1){
                    pi++;
                    getComm();
                    Toast.makeText(ScripeDetailActivity.this, "jiazia", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_script_con_all:
                if(!drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                   drawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
            case R.id.scripe_detail_coll:
                //收藏
                collect();

                break;
            case R.id.scripe_detail_add_group:
                //添加到小组
                ScripeGropDialog scripeGropDialog=ScripeGropDialog.newInstance(sid,1);//传递过去之后,就是sid的值
                scripeGropDialog.show(getSupportFragmentManager(), "ScripeGropDialog");
                break;
            case R.id.scripe_detail_share:
                //分享
                ShareUtil.newInstance(ScripeDetailActivity.this).share("Gaiamount", title,
                        Configs.COVER_PREFIX ,"http://www.gaiamount.com/video/"+sid);
                break;
            case R.id.scripe_detail_write_comm:
                ScripeCommentDialog  commentDialog=ScripeCommentDialog.newInstance(sid);
                commentDialog.show(getSupportFragmentManager(),"ScripeCommentDialog");

                break;

        }

    }

    private void collect() {

        if(isCollect==0){
            //收藏
            collectNet(1);
        }else {
            //取消收藏
            collectNet(0);
        }
    }

    Handler mHandler=new Handler();

    private void collectNet(final int t) {

        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(ScripeDetailActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                if (t == 1) {
                    imageViewColl.setImageResource(R.mipmap.ic_script_like_ss);
                    final PlayerColFrag playerColFrag = PlayerColFrag.newInstance();
                    playerColFrag.show(getSupportFragmentManager(), "collection");
                    //3s后隐藏界面
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //自动隐藏
                            playerColFrag.dismiss();
                        }
                    }, 1500);
                    isCollect = 1;
                } else {
                    imageViewColl.setImageResource(R.mipmap.ic_script_like_n);
                    GaiaApp.showToast("取消成功");
                    isCollect = 0;
                }
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                if (t == 0) {
                    GaiaApp.showToast("取消失败");
                } else {
                    GaiaApp.showToast("收藏失败");
                }
            }
        };
        ScriptApiHelper.getScriptCollect(sid, uid, t, getApplicationContext(), handler);
    }


    private int getHeight(){
        int width = ScreenUtils.instance().getWidth();
        return  (int)(width*0.8);
    }

    private String changeString(String strings) {

        if (!strings.equals("")) {
            if (strings.equals("0")) {
                return stringArray[0];
            } else if (strings.equals("1")) {
                return stringArray[1];
            } else if (strings.equals("2")) {
                return stringArray[2];
            } else if (strings.equals("3")) {
                return stringArray[3];
            } else if (strings.equals("4")) {
                return stringArray[4];
            } else if (strings.equals("5")) {
                return stringArray[5];
            } else if (strings.equals("6")) {
                return stringArray[6];
            } else if (strings.equals("7")) {
                return stringArray[7];
            } else if (strings.equals("8")) {
                return stringArray[8];
            } else if (strings.equals("9")) {
                return stringArray[9];
            } else if (strings.equals("10")) {
                return stringArray[10];
            } else if (strings.equals("11")) {
                return stringArray[11];
            } else if (strings.equals("12")) {
                return stringArray[12];
            }else if (strings.equals("13")) {
                return stringArray[13];
            }else if (strings.equals("14")) {
                return stringArray[14];
            }else if (strings.equals("15")) {
                return stringArray[15];
            }else if (strings.equals("16")) {
                return stringArray[16];
            }else if (strings.equals("17")) {
                return stringArray[17];
            }else if (strings.equals("18")) {
                return stringArray[18];
            }
        }
        return "";

    }

}
