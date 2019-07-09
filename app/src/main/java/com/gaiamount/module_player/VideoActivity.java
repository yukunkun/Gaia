package com.gaiamount.module_player;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.history.HistoryDBManager;
import com.gaiamount.gaia_main.settings.Settings;
import com.gaiamount.module_material.activity.util.EventInitView;
import com.gaiamount.module_material.activity.util.EventPosition;
import com.gaiamount.module_material.activity.util.EventRatio;
import com.gaiamount.module_material.activity.util.EventbusProgress;
import com.gaiamount.module_material.activity.util.FileDele;
import com.gaiamount.module_material.activity.util.M3U8Service;
import com.gaiamount.module_material.activity.util.NetUtil;
import com.gaiamount.module_material.activity.util.VideoControl;
import com.gaiamount.module_player.adapters.PlayerViewPagerAdapter;
import com.gaiamount.module_player.bean.VideoDetailInfo;
import com.gaiamount.module_player.dialogs.AddToDialog;
import com.gaiamount.module_player.fragments.PlayerColFrag;
import com.gaiamount.module_player.fragments.PlayerCommentFrag;
import com.gaiamount.module_player.fragments.PlayerDetailFrag;
import com.gaiamount.module_player.fragments.PlayerDownloadFrag;
import com.gaiamount.module_player.fragments.PlayerRecFrag;
import com.gaiamount.module_player.fragments.PlayerTipOffFrag;
import com.gaiamount.module_user.task.ScreenUtils;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ShareUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.gaiamount.widgets.media.GMediaController;
import com.gaiamount.widgets.media.GVideoView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import okhttp3.Call;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = "VideoActivity";
    public static final String TYPE = "type";
    public static final String WORK_ID = "work_id";

    private int mWorkId;
    private int mContentType;

    private View mDecorView;
    private GMediaController mMediaController;
    private GVideoView mVideoView;

    private Drawable mDb_collection;
    private VideoDetailInfo mDetailInfo;
    private TextView mTitle;
    private VolumeBroadCastReceiver mVolumeReceiver;
    private HistoryDBManager mManager;

    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.fragment_progress)
    ProgressBar mFragmentProgress;
    private FrameLayout mReplayWrapper;
    @Bind(R.id.video_player_tabs)
    TabLayout mTabLayout;

    @Bind(R.id.video_player_vp)
    ViewPager mViewPager;
    /**
     * 四个遮罩
     */
    @Bind(R.id.top_cover)
    View viewTop;
    @Bind(R.id.bottom_cover)
    View viewBottom;
    @Bind(R.id.left_cover)
    View viewLeft;
    @Bind(R.id.right_cover)
    View viewRight;
    private String serviceUri;
    private VideoControl videoControl;
    private ArrayList<String> tsString;
    private ImageView ivReplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);
        M3U8Service.finish();//先关闭
        //判断端口是否被占用
        boolean portAvailable = M3U8Service.isPortAvailable(M3U8Service.PORT);
        if(portAvailable){
            M3U8Service.execute();//再打开
        }
        ButterKnife.bind(this);
        mDecorView = getWindow().getDecorView();
        // handle arguments
        mWorkId = getIntent().getIntExtra(WORK_ID, -1);
        mContentType = getIntent().getIntExtra(TYPE, -1);
        String format = String.format("http://localhost:%d", M3U8Service.PORT);
        serviceUri = format+"/gaiamount/gaia/test.m3u8";
        //做一些初始化的操作
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(toolbar);
        mMediaController = new GMediaController(this);
        mMediaController.setToolbar(toolbar);
        videoControl = new VideoControl();
        mMediaController.setVideoControl(videoControl);
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (GVideoView) findViewById(R.id.gaia_video_view);
        mVideoView.setMediaController(mMediaController);
        //设置监听
        mVideoView.setOnInfoListener(onInfoListener);
        mVideoView.setOnPreparedListener(onPreparedListener);
        mVideoView.setOnCompletionListener(onComplietionListener);

        VideoUtil videoUtil = new VideoUtil();
        //获取视频详细信息
        videoUtil.getVideoInfo(this, mWorkId);

        //注册到事件总线
        EventBus.getDefault().register(this);

        mVolumeReceiver = new VolumeBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(mVolumeReceiver, filter);

        //是否已收藏
        isCollected();
    }

    @Override
    public void onBackPressed() {
        if (mMediaController.isFullScreen()) {
            mMediaController.switchPortrait();
        } else {

            FileDele.deleteAll();
            videoControl.setHandlerStop(3);
            mVideoView.stopPlayback();
            mVideoView.release(true);
            M3U8Service.finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoControl.setDownloadStop();
        M3U8Service.finish();
        FileDele.deleteAll();
        videoControl.setHandlerStop(3);
        mMediaController.setHandlerStop();
        videoControl.setdownThreadCon();

        mVideoView.stopPlayback();
        mVideoView.release(true);
        if(videoControl!=null){
            //取消计时
            videoControl.setdownThreadCon();
        }

        //注销
        EventBus.getDefault().unregister(this);
        //释放数据库资源
        if (mManager != null) {
            mManager.closeDB();
        }

        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FileDele.deleteAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
        FileDele.deleteAll();
        mMediaController.updatePausePlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoView.pause();
        mMediaController.updatePausePlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoControl.setHandlerStop(3);
//        M3U8Service.finish();
        mVideoView.stopPlayback();
        mVideoView.release(true);
        if(videoControl!=null){
            //取消计时
            videoControl.setdownThreadCon();
        }
        mMediaController.setHandlerStop();
//        videoControl.setDownloadStop();//停止下载
        videoControl.setHandlerStop(3);
//        FileDele.deleteAll();
        //注销
        EventBus.getDefault().unregister(this);
        //解注册
        unregisterReceiver(mVolumeReceiver);
        //释放数据库资源
        if (mManager != null) {
            mManager.closeDB();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mMediaController.updateSwitch(true);
        } else {
            mMediaController.updateSwitch(false);
        }
    }

    /**
     * 是否被收藏了
     */
    private boolean isCollected = false;
    private final Handler mHandler = new Handler();


    private void isCollected() {
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(VideoActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                int a = response.optInt("a");
//                if (a == 1) {
//                    isCollected = true;
//                    mDb_collection.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
//                } else {
//                    isCollected = false;
//                    mDb_collection.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//                }

            }
        };
        WorksApiHelper.isCollected(mWorkId, this, handler);
    }

    /**
     * 收藏作品的开关
     *
     * @param collection 0 取消收藏 1收藏
     * @param drawable   收藏按钮的drawable对象 用于着色
     */
    private void collectionWorksToggle(final int collection, final Drawable drawable) {
        //作品类型
        ProgressDialog progressDialog = ProgressDialog.show(this, null, getString(R.string.dealing));
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(VideoActivity.class, progressDialog) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                //改变收藏图标颜色
                if (collection == 1) {
                    isCollected = true;

                    //红色
                    drawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    //显示成功收藏
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
                } else {
                    isCollected = false;
                    //白色
                    drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                    //toast
                    GaiaApp.showToast(getString(R.string.cancel_collection_success));
                }
            }
        };
        WorksApiHelper.collect(mWorkId, mContentType, collection, this, jsonHttpResponseHandler);
    }

    /**
     * 初始化actionBar
     *
     * @param toolbar
     */
    private void initToolBar(Toolbar toolbar) {
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu_player);
        MenuItem item_coll = toolbar.getMenu().findItem(R.id.action_collection);
        mDb_collection = item_coll.getIcon();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == android.R.id.home) {
                    finish();
                } else if (itemId == R.id.action_tip_off) {
                    //去举报
                    long wid = mDetailInfo.getResource().getFid();
                    PlayerTipOffFrag playerTipOffFrag = PlayerTipOffFrag.newInstance(wid);
                    playerTipOffFrag.show(getSupportFragmentManager(), "tip_off");
                } else if (itemId == R.id.action_collection) {
                    //收藏/取消收藏作品
                    if (isCollected) {//已经收藏，则取消收藏
                        collectionWorksToggle(0, mDb_collection);
                    } else {
                        collectionWorksToggle(1, mDb_collection);
                    }
                } /*else if (itemId == R.id.action_download) {
                    //显示下载选项
                    PlayerDownloadFrag playerDownloadFrag = PlayerDownloadFrag.newInstance(mDetailInfo, mWorkId, mContentType);
                    playerDownloadFrag.show(getSupportFragmentManager(), "download");
                } */else if (itemId == R.id.action_work_setting) {
                    long userId = mDetailInfo.getUser().getUid();
                    //判断是否是原作者
                    if (userId == GaiaApp.getUserInfo().id) {
                        ActivityUtil.startWorkSettingActivity(VideoActivity.this, mWorkId, mContentType);
                    } else {
                        GaiaApp.showToast(getString(R.string.no_power));
                    }

                } else if (itemId == R.id.action_share) {
//                    ShareUtil.newInstance(VideoActivity.this).startShare("Gaiamount", "http://www.gaiamount.com", "mDetailInfo.getWorks().getName()",
//                            Configs.COVER_PREFIX + mDetailInfo.getVideoInfo().getScreenshot());
                    String screenshot = mDetailInfo.getVideoInfo().getScreenshot();
                    if(screenshot.endsWith(".png")){
                        screenshot=screenshot.replace(".png","_18.png");
                    }else {
                        screenshot=screenshot+".png";
                    }
                    ShareUtil.newInstance(getApplicationContext()).share(mDetailInfo.getWorks().getName(), "分享自Gaiamount的作品池",
                            Configs.COVER_PREFIX + screenshot,"https://www.gaiamount.com/video/"+mDetailInfo.getWorksProperties().getId());

                } else if (itemId == R.id.action_add_to_group) {
                    //添加到...
                    long vid = mWorkId;
                    int vType = mContentType;
                    AddToDialog.newInstance(vid, vType).show(getSupportFragmentManager(), "add_to_group_or_album");
                }
                return true;
            }
        });
        mTitle = (TextView) toolbar.findViewById(R.id.title);
        mTitle.requestFocus();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChangeRatio(OnEventRatio eventRatio){
        if(eventRatio.position==1){
            if(viewTop.getVisibility()==View.GONE){
                viewTop.setVisibility(View.VISIBLE);
                viewBottom.setVisibility(View.VISIBLE);
                if(viewRight.getVisibility()==View.VISIBLE){
                    viewRight.setVisibility(View.GONE);
                    viewLeft.setVisibility(View.GONE);
                }
            }
            ViewGroup.LayoutParams layoutParamsTop=viewTop.getLayoutParams();
            ViewGroup.LayoutParams layoutParamsBottom=viewBottom.getLayoutParams();
            int width = com.gaiamount.util.ScreenUtils.instance().getWidth();
            int height=(int)(width/2.35);
            int cover_height=(com.gaiamount.util.ScreenUtils.instance().getHeight()-height)/2;
            if(cover_height>0){
                layoutParamsTop.height=cover_height;
                layoutParamsBottom.height=cover_height;
                viewTop.setLayoutParams(layoutParamsTop);
                viewBottom.setLayoutParams(layoutParamsBottom);
            }
        }
        if(eventRatio.position==2){
            if(viewTop.getVisibility()==View.GONE){
                viewTop.setVisibility(View.VISIBLE);
                viewBottom.setVisibility(View.VISIBLE);
                if(viewRight.getVisibility()==View.VISIBLE){
                    viewRight.setVisibility(View.GONE);
                    viewLeft.setVisibility(View.GONE);
                }
            }
            ViewGroup.LayoutParams layoutParamsTop=viewTop.getLayoutParams();
            ViewGroup.LayoutParams layoutParamsBottom=viewBottom.getLayoutParams();
            int width = com.gaiamount.util.ScreenUtils.instance().getWidth();
            int height=(int)(width/1.85);
            int cover_height=(com.gaiamount.util.ScreenUtils.instance().getHeight()-height)/2;
            if(cover_height>0){
                layoutParamsTop.height=cover_height;
                layoutParamsBottom.height=cover_height;
                viewTop.setLayoutParams(layoutParamsTop);
                viewBottom.setLayoutParams(layoutParamsBottom);
            }
        }
        if(eventRatio.position==3){
            if(viewTop.getVisibility()==View.VISIBLE){
                viewTop.setVisibility(View.GONE);
                viewBottom.setVisibility(View.GONE);
            }
            if(viewRight.getVisibility()==View.VISIBLE){
                viewRight.setVisibility(View.GONE);
                viewLeft.setVisibility(View.GONE);
            }
//            ViewGroup.LayoutParams layoutParamsTop=viewTop.getLayoutParams();
//            ViewGroup.LayoutParams layoutParamsBottom=viewBottom.getLayoutParams();
//            int width = com.gaiamount.util.ScreenUtils.instance().getWidth();
//            int height=(int)(width/1.85);
//            int cover_height=(com.gaiamount.util.ScreenUtils.instance().getHeight()-height)/2;
//            layoutParamsTop.height=cover_height;
//            layoutParamsBottom.height=cover_height;
//            viewTop.setLayoutParams(layoutParamsTop);
//            viewBottom.setLayoutParams(layoutParamsBottom);
        }
        if(eventRatio.position==4){
            if(viewRight.getVisibility()==View.GONE){
                viewRight.setVisibility(View.VISIBLE);
                viewLeft.setVisibility(View.VISIBLE);
                if(viewTop.getVisibility()==View.VISIBLE){
                    viewTop.setVisibility(View.GONE);
                    viewBottom.setVisibility(View.GONE);
                }
            }
            ViewGroup.LayoutParams layoutParamsTop=viewLeft.getLayoutParams();
            ViewGroup.LayoutParams layoutParamsBottom=viewRight.getLayoutParams();
            int height = com.gaiamount.util.ScreenUtils.instance().getHeight();
            int width=(int)((height)*4/3);
            int cover_height=(com.gaiamount.util.ScreenUtils.instance().getWidth()-width)/2;
            layoutParamsTop.width=cover_height;
            layoutParamsBottom.width=cover_height;
            viewLeft.setLayoutParams(layoutParamsTop);
            viewRight.setLayoutParams(layoutParamsBottom);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAfterGetData(VideoUtil.OnVideoInfoGettedEvent event) {
        mDetailInfo = event.getDetailInfo();

        String defaultUri = event.getDefaultPath();

        //设置标题
        if (mTitle != null) {
            mTitle.setText(mDetailInfo.getWorks().getName());
        }
        //隐藏下部分中的progressBar
        mFragmentProgress.setVisibility(View.GONE);
        //设置底下三个Fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("video_detail_info", mDetailInfo);
        bundle.putInt("contentType", mContentType);
        bundle.putLong("contentId", mWorkId);

        PlayerCommentFrag commentFrag = new PlayerCommentFrag();
        PlayerRecFrag playerRecFrag = new PlayerRecFrag();

        commentFrag.setArguments(bundle);
        playerRecFrag.setArguments(bundle);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(PlayerDetailFrag.newInstance(mDetailInfo, mWorkId));
        fragmentList.add(commentFrag);
        fragmentList.add(playerRecFrag);

        mViewPager.setAdapter(new PlayerViewPagerAdapter(getSupportFragmentManager(), fragmentList, this));
        mTabLayout.setupWithViewPager(mViewPager);
        getUri(defaultUri);

//        //2016.8.12 判断网络情况
//        int connection = NetworkUtils.checkNetworkConnection();
//        if (connection == NetworkUtils.TYPE_MOBILE) {
//            //获取是否允许移动网络下观看视频的设置
//            boolean onlyWifi = Settings.newInstance(this).getWifiDownloadSettings();
//            if (onlyWifi) {//仅允许wifi下观看
//                GaiaApp.showToast(getString(R.string.watch_video_without_wifi_and_permission));
//            } else {
//                prepareVideo(defaultUri);
//                GaiaApp.showToast(getString(R.string.watch_video_without_wifi));
//            }
//        } else if (connection == NetworkUtils.TYPE_WIFI) {
//            prepareVideo(defaultUri);
//        }
    }

    private int initView=0;
    //下载完成,播放的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPlay(EventPosition event) {
        //判断快进快退的进度
        initView=mMediaController.getInitView();
        tsString = videoControl.getTsString();

        if(ivReplay!=null&&ivReplay.getVisibility()==View.VISIBLE){
            ivReplay.setVisibility(View.GONE);
        }
        if(mReplayWrapper!=null&&mReplayWrapper.getVisibility()==View.VISIBLE){
            mReplayWrapper.setVisibility(View.GONE);
        }
        videoControl.setDownloadTag(event.position);//下载一次就加入一个值
//        Log.i("----posit+initView+size",event.position+"+"+initView+"+"+ tsString.size());
        if(tsString.size()>3){
            if(event.position< tsString.size()){
                if(event.position==2+initView){
                    videoControl.setHandlerStart(1);
                }
                videoControl.load();
            }
        }
        //只有两片的视频
        if(tsString.size()==2){
            if(event.position< tsString.size()){
                if(event.position==1+initView){
                    videoControl.setHandlerStart(2);
                }
                videoControl.load();
            }
        }
        //只有两片的视频
        if(tsString.size()==3) {
            if (event.position < tsString.size()) {
                if (event.position == 1 + initView) {
                    videoControl.setHandlerStart(4);
                }
                videoControl.load();
            }
        }
    }

    //mProgressBar有快进控制
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventProgress(EventbusProgress event) {
        if(mProgressBar.getVisibility()==View.GONE){
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    //下载好了,初始化播放器
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInitView(EventInitView event) {
        if(event.play==1){
            initView();
        }
    }

    //下载好了,初始化播放器
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRatio(EventRatio event) {
        String ratioUri = event.ratioUri;
        videoControl.setHandlerStop(3);
        if(videoControl!=null){
            videoControl=null;
            videoControl=new VideoControl();
        }

        mMediaController.setVideoControl(videoControl);
        if(mProgressBar.getVisibility()==View.GONE){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        getUri(ratioUri);
    }

    private void getUri(final String uri) {
        Log.i("---def",uri);
        if(uri==null){
            GaiaApp.showToast("请求失败");
            return;
        }

        OkHttpUtils.get().url(uri).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception error, int id) {
                Log.i("-------m3u8error",error.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("-------m3u8",response.toString());
                videoControl.setM3u8String(response);
            }
        });

//        NetUtil.get(uri/*.replaceAll("https","http")*/, new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    String srt2 = new String(responseBody,"UTF-8");
//                    videoControl.setM3u8String(srt2);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.i("-------m3u8error",error.toString());
//            }
//
//        });
    }

    boolean tag=true;
    public void initView(){
        if(tag){
            if(mProgressBar.getVisibility()==View.VISIBLE){
                mProgressBar.setVisibility(View.GONE);
            }
            tag=false;
            //2016.8.12 判断网络情况
            int connection = NetworkUtils.checkNetworkConnection();
            if (connection == NetworkUtils.TYPE_MOBILE) {
                //获取是否允许移动网络下观看视频的设置
                boolean onlyWifi = Settings.newInstance(this).getWifiDownloadSettings();
                if (onlyWifi) {//仅允许wifi下观看
                    GaiaApp.showToast(getString(R.string.watch_video_without_wifi_and_permission));
                } else {
                    prepareVideo(serviceUri);  //本地的uri
                    GaiaApp.showToast(getString(R.string.watch_video_without_wifi));
                }
            } else if (connection == NetworkUtils.TYPE_WIFI) {
                prepareVideo(serviceUri);  //本地的uri
            }
            mMediaController.setHandlerStart(2);
        }
        else {
            mMediaController.switchUri();
        }
    }

    private void prepareVideo(String defaultUri) {
        mVideoView.setVideoURI(Uri.parse(defaultUri));
        //下面两句顺序不能对调
        mMediaController.setDefaultUri(defaultUri);
        mMediaController.setVideoInfo(mDetailInfo);

    }

    private void addToHistory() {
        mManager = new HistoryDBManager(VideoActivity.this);
        mManager.add(mDetailInfo.getVideoInfo().getScreenshot(), mDetailInfo.getWorks().getCover(), mDetailInfo.getWorks().getName(), System.currentTimeMillis(), mWorkId, mContentType);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGetBlur(Bitmap bitmap) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        mReplayWrapper.setBackground(bitmapDrawable);
    }

    private IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            switch (what) {
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    mProgressBar.setVisibility(View.GONE);
                    break;
            }
            return false;
        }
    };
    private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            mProgressBar.setVisibility(View.GONE);
            //添加到历史记录
            addToHistory();
        }
    };
    private IMediaPlayer.OnCompletionListener onComplietionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            mMediaController.stop();

            //准备图片
            ImageUtils.convertToBlur(VideoActivity.this, mDetailInfo.getVideoInfo().getScreenshot(), mDetailInfo.getWorks().getCover());

            final ViewStub viewStub = (ViewStub) findViewById(R.id.replay);
            if (viewStub != null) {
                viewStub.inflate();
                mReplayWrapper = (FrameLayout) findViewById(R.id.replay_wrapper);
                ivReplay = (ImageView) findViewById(R.id.ic_replay);
                ivReplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mVideoView.start();
//                        mVideoView.seekTo(0);
                        mReplayWrapper.setVisibility(View.GONE);
                    }
                });
            } else {
                mReplayWrapper.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 更新评论数量
     *
     * @param commentCount
     */
    public void updateCommentCount(int commentCount) {
//        int tabCount = mTabLayout.getTabCount();
//        if (tabCount==3) {
//            mTabLayout.getTabAt(1).setText(getResources().getStringArray(R.array.player_tabs)[1] + "(" + commentCount + ")");
//        }
    }

    class VolumeBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //更改声音控件的进度
            mMediaController.updateSoundProgress();
        }

    }

}
