package com.gaiamount.module_academy.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.Contents;
import com.gaiamount.module_academy.bean.EventDetailInfo;
import com.gaiamount.module_academy.bean.EventSendContent;
import com.gaiamount.module_academy.bean.LessonInfo;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.module_academy.bean.OnEventLearn;
import com.gaiamount.module_academy.fragment.CommentFragment;
import com.gaiamount.module_academy.fragment.ContentFragment;
import com.gaiamount.module_academy.fragment.GropDialog;
import com.gaiamount.module_academy.fragment.IntroduceFragment;
import com.gaiamount.module_im.secret_chat.adapter.SecViewPagerAdapter;
import com.gaiamount.module_player.fragments.PlayerColFrag;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.ShareUtil;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * kun academy的详情页
 */
public class AcademyDetailActivity extends AppCompatActivity {
    private ImageView imageViewCover;
    private TextView textViewIntro, textViewLearnNum,textViewPrice,textViewLearn,textViewGrade;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private RatingBar ratingBar;
    private SecViewPagerAdapter adapter;
    private ArrayList<Fragment> fragments;
    private String[] strings;
    private Toolbar mToolbar;
    private long uid;
    private long cid;
    private int isLearning=0;
    private int t=1; // 中文/英文教程 	0中文教程　１英文教程
    private String cover;
    private String name;
    private String  responseString;
    private int isCollect;//是否收藏
    private int lessId;//教程的id
    private double grade;
    private Drawable iconCollect;
    Handler handler=new Handler();
    private int language;
    private int hid=-1;
    private long chapterId=-1;
    private int type;
    private int allowFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_detail);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        cid=intent.getLongExtra("id",-1);
        uid= GaiaApp.getAppInstance().getUserInfo().id;
        init();
        initToobar();
        getInfo();
        getFragment();
        setListener();
        setAdapter();
    }

    //获取到屏幕的宽度
    public int getWidght(){
        int width = ScreenUtils.instance().getWidth();
        return width;
    }

    private void init() {
        imageViewCover= (ImageView) findViewById(R.id.academy_cover);
        //动态设置imageview的宽度
        ViewGroup.LayoutParams linearParams =imageViewCover.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = (int)(getWidght()/1.66);// 控件的高强制设成1.66:1
        imageViewCover.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

        //动态设置textview的宽度
        textViewIntro= (TextView) findViewById(R.id.academy_detail_name);
        ViewGroup.LayoutParams linearParams1 = textViewIntro.getLayoutParams();
        linearParams1.width = (int)(getWidght()*0.8);
        textViewIntro.setLayoutParams(linearParams1);

        textViewLearnNum= (TextView) findViewById(R.id.learn_number);
        textViewPrice= (TextView) findViewById(R.id.learn_price);
        mTabLayout= (TabLayout) findViewById(R.id.academy_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.academy_viewpager);
        mToolbar = (Toolbar) findViewById(R.id.academy_toobar);
        textViewLearn= (TextView) findViewById(R.id.start_learn);
        ratingBar= (RatingBar) findViewById(R.id.room_ratingbar);
        textViewGrade= (TextView) findViewById(R.id.grade_academy);
    }

    //toobar
    private void initToobar() {

        mToolbar.setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.inflateMenu(R.menu.academy_mix_menu);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
        //获取到收藏icon
        Menu menu = mToolbar.getMenu();
        MenuItem item = menu.findItem(R.id.action_collection);
        iconCollect=item.getIcon();

    }
    //toobar的点击事件
    private boolean tag=false;//收藏的tag
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_share_academy:
                    share();
                    break;
                case R.id.action_add_to_group:
                    addToGroup();//添加到小组
                    break;
                case R.id.action_collection:
                    //收藏
                    addColect();
                    break;
                case R.id.action_tip_off:
                    break;


            }
            return true;
            }
        };

    private void addColect() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyDetailActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                long o = response.optLong("o");
                if(!tag){
                    addToCollection(o);
                }else {
                    cancelCollection(o);
                }

            }
        };
        AcademyApiHelper.getCollectIs(cid,handler);
    }

    private void addToGroup() {
            MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyDetailActivity.class){
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    long o = response.optLong("o");
                    Log.i("AcademyDetail_o",o+"");
                    GropDialog commentDialog=GropDialog.newInstance(o,language);//传递过去之后,就是cid的值
                    commentDialog.show(getSupportFragmentManager(), "GropDialog");
                }
            };
            AcademyApiHelper.getCollectIs(cid,handler);
    }

    private void cancelCollection(long o) {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyDetailActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                tag=false;
                GaiaApp.showToast("取消收藏");
                iconCollect.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            }
        };
        AcademyApiHelper.collectLesson(o,getApplicationContext(),0,handler);
    }

    private void addToCollection(long o) {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyDetailActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                tag=true;
                showDialog();
                iconCollect.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            }
        };
        AcademyApiHelper.collectLesson(o,getApplicationContext(),1,handler);
    }

    //分享功能
    private void share() {
        ShareUtil.newInstance(AcademyDetailActivity.this).share(name, "分享自Gaiamount的学院",
                Configs.COVER_PREFIX + cover,"http://www.gaiamount.com/academy/detail/"+lessId);
    }

    //show展示的dialog
    public void showDialog(){
        //显示成功收藏
        final PlayerColFrag playerColFrag = PlayerColFrag.newInstance();
        playerColFrag.show(getSupportFragmentManager(), "AcademyDetailCollection");
        //3s后隐藏界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //自动隐藏
                playerColFrag.dismiss();
            }
        }, 1500);
    }

    //获取数据
    private void getInfo() {

        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyDetailActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);
                //判断是否收藏
                if(isCollect==1){
                    tag=true;
                    iconCollect.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                }else {
                    tag=false;
                    iconCollect.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                }
            }
        };
        AcademyApiHelper.getDetail(uid,cid,t,getApplicationContext(),handler);
    }

    private void parasJson(JSONObject response) {
        JSONObject oo = response.optJSONObject("o");
        //获取上一次播放的参数,便于遍历传值
        JSONObject hour = oo.optJSONObject("hour");
        if(hour!=null){
            hid=hour.optInt("hid",-1);
            chapterId=hour.optLong("chapterId",-1);
        }

        JSONObject course = oo.optJSONObject("course");
        int learningCount = course.optInt("learningCount");
        textViewLearnNum.setText(learningCount+"");
        name = course.optString("name");
        textViewIntro.setText(name);
        isLearning=course.optInt("isLearning");

        lessId=course.optInt("id");//教程的id
        isCollect=course.optInt("isCollect");//是否收藏
        grade=course.optDouble("grade");//评分
        ratingBar.setMax(10);
        ratingBar.setProgress((int)grade);//评分的星星
        textViewGrade.setText(((float)(((int) (grade * 10)))/ 10)+"");
        language = course.optInt("language");

        //免费
        allowFree = course.optInt("allowFree");
        int price = course.optInt("price");
        if(allowFree ==1){
            textViewPrice.setText(R.string.for_free);
        }else {
            textViewPrice.setText(price+getString(R.string.academy_yuan));
        }
        cover = course.optString("cover");
        Glide.with(getApplicationContext()).load(Configs.COVER_PREFIX+ cover).placeholder(R.mipmap.bg_general).into(imageViewCover);
        //传值到fragment
        long id = course.optLong("id");
        String description = course.optString("description");
        String equipment = course.optString("equipment");
        String author=course.optString("author");
        type = course.optInt("type");

        if(temp){
            EventBus.getDefault().post(new EventDetailInfo(description,equipment,id,t,cid,author,isLearning, allowFree,type));
        }

        //判断学习状态
        if(isLearning==1){
            textViewLearn.setText(getResources().getString(R.string.academy_studying));
            textViewLearn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_f7f7f7));
            textViewLearn.setTextColor(getApplicationContext().getResources().getColor(R.color.color_9a9a9a));
        }else if(isLearning==2){
            textViewLearn.setText(getResources().getString(R.string.detail_study_over));
            textViewLearn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_f7f7f7));
            textViewLearn.setTextColor(getApplicationContext().getResources().getColor(R.color.color_9a9a9a));
        }
    }

    private void getFragment() {
        //设置viewpager的fragment
        fragments=new ArrayList<>();
        IntroduceFragment introduceFragment=new IntroduceFragment();
        fragments.add(introduceFragment);
        ContentFragment contentFragment=new ContentFragment();
        fragments.add(contentFragment);
        CommentFragment commentFragment=new CommentFragment();
        fragments.add(commentFragment);
        //设置tablayout的标签
        strings=getApplicationContext().getResources().getStringArray(R.array.academy_tablayout);
        for (int i = 0; i < strings.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(strings[i]));
        }
    }

    //联动的监听
    private void setListener() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTabLayout.setScrollPosition(position,0,false);
            }
        });

        //学习的点击事件
        textViewLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewLearn.getText().equals(getResources().getString(R.string.academy_studying))){
                    //比对数据,为了给继续播放传递数据给播放页
                    if(type==0) {//视频的点击事件
                        sendMessageToPlay();
                    }
                    else if(type==1){//图文的点击事件
                        sendMessageToTuWen();
                    }

                }else if(textViewLearn.getText().equals(getResources().getString(R.string.stare_study))){
                    temp=false;
                    getInfo();
                    study();
                }else if(textViewLearn.getText().equals(getResources().getString(R.string.detail_study_over))){
                    GaiaApp.showToast(getString(R.string.have_study_over));
                }

            }
        });


        imageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(isLearning==0){
                        GaiaApp.showToast(getString(R.string.choose_study));
                    }else if(isLearning==1){
                        if(type==0){
                            if(hid==-1||chapterId==-1){//默认播放第一章第一节
                                playFirst();
                            }else {
                                sendMessageToPlay();
                            }
                        }else if(type==1){
                            if(hid==-1||chapterId==-1){
                                showFirst();//默认观看第一章第一节
                            }else {
                                sendMessageToTuWen();
                            }
                        }
                    }else if(isLearning==2){
                        GaiaApp.showToast(getString(R.string.have_study_over));
                    }
                }
        });
    }

    private void sendMessageToTuWen() {
        //判断选中的位置
        if(responseString.length()==0||hid==-1||chapterId==-1){
            return;
        }
        for (int i = 0; i < contentList.size(); i++) {
            if(chapterId==contentList.get(i).getChapterId()){
                chapterPosition=i;
                break;
            }
        }
        //获取到当前为哪一节的位置
        for (int j = 0; j < lessonList.get(chapterPosition).size(); j++) {
            if(hid==lessonList.get(chapterPosition).get(j).getId()){
                lessonPosition=j;
                break;
            }
        }
        //跳转到图文页
        ActivityUtil.startTuWenDetailActivity(cid,lessonPosition,chapterPosition,responseString,getApplicationContext());
    }

    //默认播放第一章第一节
    private void playFirst() {

        ArrayList<Integer> longHid=new ArrayList<Integer>();
        ArrayList<String> lessons=new ArrayList<String>();
        ArrayList<Integer> isPublicList=new ArrayList<Integer>();

        //获取到值,并且跳转
        for (int i = 0; i <lessonList.get(0).size() ; i++) {
            int hid = lessonList.get(0).get(i).getId();
            longHid.add(hid);
            String name = lessonList.get(0).get(i).getName();
            lessons.add(name);
            int isPublic = lessonList.get(0).get(i).getIsPublic();
            isPublicList.add(isPublic);
        }
        ActivityUtil.startAcademyPlayActivity(isLearning,allowFree,cid,0,contentList.get(0).getChapterId(),
                longHid,lessons,isPublicList,getApplicationContext());
    }

    //默认图文第一章第一节
    private void showFirst() {
        ActivityUtil.startTuWenDetailActivity(cid,0,0,responseString,getApplicationContext());
    }


    private void setAdapter() {
        //设置适配器
        adapter=new SecViewPagerAdapter(getSupportFragmentManager(),fragments,strings);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
    }


    //获取到目录页的数据回调,为继续播放准备数据;
    private ArrayList<Contents> contentList=new ArrayList<>();
    private ArrayList<ArrayList<LessonInfo>> lessonList=new ArrayList<>();
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventSendContent event) {
        contentList=event.contentList;
        lessonList=event.lessonList;
        responseString=event.responseString;
    }


    //遍历方法
    private int chapterPosition;
    private int lessonPosition;
    private void sendMessageToPlay() {


        if(lessonList.size()==0||contentList.size()==0||hid==-1||chapterId==-1){
            return;
        }

        for (int i = 0; i < contentList.size(); i++) {
            if(chapterId==contentList.get(i).getChapterId()){
                chapterPosition=i;
                break;
            }
        }
        //获取到当前为哪一节的位置
        for (int j = 0; j < lessonList.get(chapterPosition).size(); j++) {
            if(hid==lessonList.get(chapterPosition).get(j).getId()){
                lessonPosition=j;
                break;
            }
        }

        ArrayList<Integer> longHid=new ArrayList<Integer>();
        ArrayList<String> lessons=new ArrayList<String>();
        ArrayList<Integer> isPublicList=new ArrayList<Integer>();
        //获取到值,并且跳转
        for (int i = 0; i <lessonList.get(chapterPosition).size() ; i++) {
            int hid = lessonList.get(chapterPosition).get(i).getId();
            longHid.add(hid);
            String name = lessonList.get(chapterPosition).get(i).getName();
            lessons.add(name);
            int isPublic = lessonList.get(chapterPosition).get(i).getIsPublic();
            isPublicList.add(isPublic);
        }

        //开启视频播放页
        ActivityUtil.startAcademyPlayActivity(isLearning,allowFree,cid,lessonPosition,contentList.get(chapterPosition).getChapterId(),
                longHid,lessons,isPublicList,getApplicationContext());
        }

    public void study(){
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyPlayActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                textViewLearn.setText(getResources().getString(R.string.academy_studying));
                textViewLearn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_f7f7f7));
                textViewLearn.setTextColor(getApplicationContext().getResources().getColor(R.color.text_666));
                EventBus.getDefault().post(new OnEventLearn(1));
                isLearning=1;
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                int i = response.optInt("i");
                if(i==37505){
                    textViewLearn.setText(getResources().getString(R.string.academy_studying));
                    textViewLearn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_f7f7f7));
                    textViewLearn.setTextColor(getApplicationContext().getResources().getColor(R.color.text_666));
                }else if(i==37504){
                    GaiaApp.showToast(getString(R.string.academy_buy_lesson));
                }
            }
        };
        AcademyApiHelper.stareStudy(cid,getApplicationContext(),handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //返回就更新数据,只是更新本页的数据,fragment不用更新
    private boolean temp=true;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnEventId event) {
        temp=false;
        getInfo();
    }

}
