package com.gaiamount.module_academy.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.module_material.activity.util.EventInitView;
import com.gaiamount.module_material.activity.util.EventPosition;
import com.gaiamount.module_material.activity.util.FileDele;
import com.gaiamount.module_material.activity.util.M3U8Service;
import com.gaiamount.module_material.activity.util.NetUtil;
import com.gaiamount.module_material.activity.util.VideoControl;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.media.GMediaController;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import okhttp3.Call;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayerProxy;

//kun  academy播放页
public class AcademyNewPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private int HINT_CONTROL=1;
    private int UPDATE_SEEKBAR=0;
    private int CONTROLL_PROGRESS=2;
    private SurfaceView mSurfaceView;
    private IjkMediaPlayer mIjkMediaPlayer;
    private MediaPlayerProxy mPlayerProxy;
    private Toolbar toolbar;
    private LinearLayout linShareNext,mLinHeadToobar;
    private ImageView mPause,mVoice;
    private SeekBar mSeekBar;
    private LinearLayout mLinDowmContril,linInclude;
    private TextView mCurrentTime,mAllTime,mStudyOver,mRatioText,mNext,mLessonUp;
    private StringUtil mStringUtil=StringUtil.getInstance();
    private ProgressBar progressBar;
    private int videoDuration;
    private AudioManager am;
    private long id;//获取进度的id
    private double watchLen;//观看进度
    private long watchProgress=0;

    private long cid;
    private long chapterId;
    private ArrayList<Integer> hidList;
    private ArrayList<Integer> isPublicList;
    private ArrayList<String> lessons;
    private int childPosition;
    private int isPublic;
    private int allowFree;
    private int isLearning;
    private boolean watchTag=false;
    private boolean tagClick=false;
    private String format = String.format("http://localhost:%d", M3U8Service.PORT);
    private String serviceUri = format+"/gaiamount/gaia/test.m3u8";
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==UPDATE_SEEKBAR){
//                long videoCachedDuration = mIjkMediaPlayer.getVideoCachedDuration();
//                int currentPosition = (int) mIjkMediaPlayer.getCurrentPosition();
//                String currenPos = mStringUtil.stringForTime(currentPosition);
//                mSeekBar.setSecondaryProgress(currentPosition+(int) videoCachedDuration);
//                if(currentPosition<=mPlayerProxy.getDuration()){
//                    mCurrentTime.setText(currenPos);
//                }

                if(mPlayerProxy.getCurrentPosition()==mPlayerProxy.getDuration()){
                    watchLen=mPlayerProxy.getDuration();
                    handler.removeMessages(UPDATE_SEEKBAR);
                    mHandler.removeMessages(CONTROLL_PROGRESS);
//                    mPause.setImageResource(R.mipmap.play);
                }
            }
            if(msg.what==HINT_CONTROL){
                hint();
                showOrHint=true;
            }
            handler.sendEmptyMessageDelayed(UPDATE_SEEKBAR,1000);
        }
    };
    private VideoControl videoControl;
    private ArrayList<String> tsString;
    private int load_time=0;
    private int seekChange=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_academy_play);
        M3U8Service.finish();//先关闭
        powerManager = (PowerManager) this.getSystemService(Service.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Lock");
        //判断端口是否被占用
        boolean portAvailable = M3U8Service.isPortAvailable(M3U8Service.PORT);
        if(portAvailable){
            M3U8Service.execute();//再打开
        }
        EventBus.getDefault().register(this);
        videoControl = new VideoControl();
        //初始化IJKPlayer
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mIjkMediaPlayer = new IjkMediaPlayer();
        mPlayerProxy = new MediaPlayerProxy(mIjkMediaPlayer);
        mPlayerProxy.setLooping(true);
        Intent intent = getIntent();
        cid = intent.getLongExtra("cid", -1);
        chapterId=intent.getLongExtra("chapterId",-1);
        hidList=intent.getIntegerArrayListExtra("hidList");
        lessons=intent.getStringArrayListExtra("lessons");
        childPosition=intent.getIntExtra("childPosition",-1);
        isLearning=intent.getIntExtra("isLearning",-1);
        allowFree=intent.getIntExtra("allowFree",-1);
        isPublicList=intent.getIntegerArrayListExtra("isPublicList");
        init();
        dealTag();
        initToobar();
        getInfo();
        setListener();
    }

    //判断试看条件
    private void dealTag() {
//        Log.i("---try","isLearn:"+isLearning+"allowfree:"+allowFree+"ispub:"+isPublic);
        if(isLearning==0&&allowFree==0&&isPublicList!=null&&isPublicList.get(childPosition)==0){
            watchTag=true;
            GaiaApp.showToast(getString(R.string.just_watch_six));
            mStudyOver.setBackgroundColor(getResources().getColor(R.color.black50));
            mStudyOver.setTextColor(getResources().getColor(R.color.white50));
        }
    }

    //初始化toobar
    private void initToobar() {
        toolbar.setTitle(lessons.get(childPosition)+"");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.back_normal);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getProgress(mPlayerProxy.getCurrentPosition()/1000);
                EventBus.getDefault().post(new OnEventId(0,0));
                if(null!=handler){
                    handler.removeMessages(UPDATE_SEEKBAR);//移除进度的刷新的handler
                }
                if(null!=mHandler){
                    mHandler.removeMessages(CONTROLL_PROGRESS);//移除播放片段到的记录的handler
                }
                handlers.removeMessages(2);
                videoControl.setDownloadStop();//停止下载
                videoControl.setHandlerStop(3);
                EventBus.getDefault().unregister(this);
                M3U8Service.finish();
                onBackPressed();
            }
        });
    }

    //初始化控件
    private void init() {
        //初始化声音控制
        am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        mSurfaceView= (SurfaceView) findViewById(R.id.surfview);
        toolbar= (Toolbar) findViewById(R.id.play_toobar);
        linShareNext= (LinearLayout) findViewById(R.id.line_share_next);
        linInclude= (LinearLayout) findViewById(R.id.lin_include);
        mPause= (ImageView) linInclude.findViewById(R.id.image_pause);
        mVoice= (ImageView) linInclude.findViewById(R.id.image_voice);
        mRatioText= (TextView) linInclude.findViewById(R.id.textview_ratio);
        mSeekBar= (SeekBar) linInclude.findViewById(R.id.seekbar);
        mCurrentTime= (TextView) linInclude.findViewById(R.id.textview_current_time);
        mAllTime= (TextView) linInclude.findViewById(R.id.textview_all_time);
        mNext= (TextView) findViewById(R.id.textview_next);
        mLessonUp= (TextView) findViewById(R.id.textview_up);
        mStudyOver= (TextView) linInclude.findViewById(R.id.textview_study_over);
        mLinDowmContril= (LinearLayout) linInclude.findViewById(R.id.lin_down_control);
        mLinHeadToobar= (LinearLayout) findViewById(R.id.lin_head_toobar);
        progressBar=  findViewById(R.id.academy_progressBar);
        //popu的子view
        mSoundView = LayoutInflater.from(this).inflate(R.layout.acadecy_voice_seekbar, null);
        mRatioLayout= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_player_ration, null);
        //初始化Voice的seekbar和分辨率的popu
        initRation(mRatioLayout);
        mLinDowmContril.setVisibility(View.GONE);
        FileDele.deleteAll();

    }

    private void initRation(View mRatioLayout) {
        mFraOrigin= (FrameLayout) mRatioLayout.findViewById(R.id.definitionOrigin);
        mFraHD=(FrameLayout) mRatioLayout.findViewById(R.id.definitionHD);
        mFra720P=(FrameLayout) mRatioLayout.findViewById(R.id.definition720P);
        mFra2K=(FrameLayout) mRatioLayout.findViewById(R.id.definition2K);
        mFraOrigin.setOnClickListener(new RationListener());
        mFraHD.setOnClickListener(new RationListener());
        mFra720P.setOnClickListener(new RationListener());
        mFra2K.setOnClickListener(new RationListener());
    }

    //分辨率的切换点击
    private class RationListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mPause.setClickable(false);
            switch (v.getId()){
                case R.id.definitionOrigin:

                    mHandler.removeMessages(CONTROLL_PROGRESS);//停止进度的发送
                    handler.removeMessages(UPDATE_SEEKBAR);//停止进度条的更新
                    restarePlay();
                    reInitDate(); //初始化所有的控制数据
                    videoControl.setdownThreadCon();
                    videoControl.setHandlerStop(3);
                    videoControl.setDownloadStop();
                    videoControl.setLoad_num(0);
                    handlers.removeMessages(2);
                    FileDele.deleteAll();
//                    setVideoListener(mp4);
                    playLocalM3u8(mp4);
                    mRatioText.setText(getApplicationContext().getResources().getString(R.string.origin_definition));
                    popupWindow.dismiss();
                    if(progressBar.getVisibility()==View.GONE){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                break;
                case R.id.definition720P:

                    mHandler.removeMessages(CONTROLL_PROGRESS);
                    handler.removeMessages(UPDATE_SEEKBAR);
                    restarePlay();
                    reInitDate(); //初始化所有的控制数据
                    videoControl.setdownThreadCon();
                    videoControl.setHandlerStop(3);
                    videoControl.setDownloadStop();
                    videoControl.setLoad_num(0);
                    handlers.removeMessages(2);
                    FileDele.deleteAll();
//                    setVideoListener(p720);
                    playLocalM3u8(p720);
                    mRatioText.setText("720P");
                    popupWindow.dismiss();
                    if(progressBar.getVisibility()==View.GONE){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                break;
                case R.id.definitionHD:

                    mHandler.removeMessages(CONTROLL_PROGRESS);
                    handler.removeMessages(UPDATE_SEEKBAR);
                    restarePlay();
                    reInitDate(); //初始化所有的控制数据
                    videoControl.setdownThreadCon();
                    videoControl.setHandlerStop(3);
                    videoControl.setDownloadStop();
                    videoControl.setLoad_num(0);
                    handlers.removeMessages(2);
                    FileDele.deleteAll();
//                    setVideoListener(hd);
                    playLocalM3u8(hd);
                    mRatioText.setText("HD");
                    popupWindow.dismiss();
                    if(progressBar.getVisibility()==View.GONE){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.definition2K:

                    mHandler.removeMessages(CONTROLL_PROGRESS);
                    handler.removeMessages(UPDATE_SEEKBAR);
                    restarePlay();
                    reInitDate(); //初始化所有的控制数据
                    videoControl.setdownThreadCon();
                    videoControl.setHandlerStop(3);
                    videoControl.setDownloadStop();
                    handlers.removeMessages(2);
                    videoControl.setLoad_num(0);
                    FileDele.deleteAll();
//                    setVideoListener(k2);
                    playLocalM3u8(k2);

                    mRatioText.setText("2K");
                    popupWindow.dismiss();
                    if(progressBar.getVisibility()==View.GONE){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    break;

            }
        }
    }
    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyNewPlayActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);

            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                int i = response.optInt("i");
                if(i==30103){
                    GaiaApp.showToast(getString(R.string.academy_uri_error));
                }else {
                    GaiaApp.showToast(getString(R.string.academy_play));
                }
                popupHandler.sendEmptyMessageDelayed(1,3000);

            }
        };

        AcademyApiHelper.getVideo(cid,chapterId,hidList.get(childPosition),getApplicationContext(),handler);

    }
    private String defaultUri="";
    private void parasJson(JSONObject response) {

        if(response.length()==0||response.equals("null")||response.toString().isEmpty()){
            GaiaApp.showToast(getString(R.string.academy_play));
//            popupHandler.sendEmptyMessageDelayed(1,3000);
            return;
        }

        JSONObject o = response.optJSONObject("o");
        watchProgress=o.optLong("watchLen");
        id=o.optLong("lrid");
        defaultUri = selectDefaultUri(o);
        //拦截播放,播放本地m3u8
        playLocalM3u8(defaultUri);
    }
    /******************* new ***********************************/

    private int initView=0;
    //下载完成,播放的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPlay(EventPosition event) {
        //判断快进快退的进度
        tsString = videoControl.getTsString();
        mPause.setClickable(true);
        videoControl.setDownloadTag(event.position);//下载一次就加入一个值
        //Log.i("----posit+initView+size",event.position+"+"+initView+"+"+ tsString.size());
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

    //下载好了,初始化播放器
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInitView(EventInitView event) {
        int play = event.play;
        mPause.setClickable(true);
        //开始播放
        setVideoListener();

    }

    public void setVideoListener() {
        try {
            mPlayerProxy.setDataSource(serviceUri);
            mPlayerProxy.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayerProxy.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(IMediaPlayer mp) {
                    mp.setDisplay(mSurfaceView.getHolder());
                    mp.start();
                    //加载的progressbar
                    progressBar.setVisibility(View.GONE);
                    //获取到视频的最大时间
                    videoDuration = (int) videoControl.getMaxTime()*1000;
                    String duration = mStringUtil.stringForTime(videoDuration);
                    mAllTime.setText(duration);//时间
//                    //进度最大
                    mSeekBar.setMax((int) videoControl.getMaxTime());
//                    //发送消息，实时更新进度条
//                    handler.sendEmptyMessage(UPDATE_SEEKBAR);
                    handler.sendEmptyMessageDelayed(HINT_CONTROL,4000);
//                    mHandler.sendEmptyMessageDelayed(CONTROLL_PROGRESS,60000);//获取进度的handler
                    mSeekBar.setOnSeekBarChangeListener(new onSeekBarChangeListener());
                    mLinDowmContril.setVisibility(View.VISIBLE);
                    handlers.sendEmptyMessageDelayed(2,1);
                    tagClick=true;
                    mPause.setImageResource(R.mipmap.pause);
                    if(progressBar.getVisibility()==View.VISIBLE){
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e) {
            GaiaApp.showToast(getString(R.string.play_error));
            e.printStackTrace();
        }

        mPlayerProxy.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                GaiaApp.showToast(getString(R.string.play_error));
                return false;
            }
        });
        //开始播放
        startPlay();
        //设置上一次播放
        tryWatch();
    }

    private void playLocalM3u8(String uri) {
        getUri(uri);
    }

    private void getUri(final String uri) {
        if(uri==null){
            return;
        }

        OkHttpUtils.get().url(uri).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception error, int id) {
                Log.i("-------m3u8error",error.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                videoControl.setM3u8String(response);
//                Log.i("-------m3u8",response.toString());
            }
        });
    }


    private void tryWatch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(watchTag){
                    try {
                        Thread.sleep(1000);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(mPlayerProxy.isPlaying()&&mPlayerProxy.getCurrentPosition()+seekChange*1000>videoDuration*0.05){
                                    if(mIjkMediaPlayer!=null){
                                        pauseVideo();
                                        mPlayerProxy.stop();
                                        mPause.setClickable(false);
                                        mLinHeadToobar.setVisibility(View.VISIBLE);
                                        GaiaApp.showToast("试看结束,请先购买");
                                        mSeekBar.setEnabled(false);
                                    }
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //进度条的监听
    private class onSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser/*&&mPlayerProxy!=null&&mIjkMediaPlayer!=null*/){
                int pro = progress / 5;
                mPause.setClickable(false);
                videoControl.setLoad_num(pro);
                //控制进度条的误差.必须减去余数
                seekChange = progress-progress%5;
                initView = progress/5;
                load_time = progress/5;
                if(progressBar.getVisibility()==View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            videoControl.setHandlerStop(1);
            videoControl.setHandlerStop(3);
            handlers.removeMessages(2);
            restarePlay();
            FileDele.delete("test.m3u8");
            FileDele.deleteAll();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//            videoControl.writeHead();
            videoControl.setDownLoadOver(3);
            videoControl.load();
        }
    }

    Handler handlers=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 2:
                    //删除控制
                    tsString = videoControl.getTsString();
                    Log.i("-------currentTime", (+seekChange) + "+" + load_time);
                    for (int i = 0; i < tsString.size(); i++) {
                        if (mPlayerProxy != null) {
                            if ((mPlayerProxy.getCurrentPosition() / 1000) + seekChange - load_time * 5 > 10) {
                                videoControl.deletePlayedTs(load_time);
                                load_time++;
                            }
                        }
                    }

                    if (mCurrentTime != null) {
                        mCurrentTime.setText(mStringUtil.stringForTime((int)(mPlayerProxy.getCurrentPosition() + seekChange * 1000)));
                    }
                    mSeekBar.setProgress((int) mPlayerProxy.getCurrentPosition() / 1000 + seekChange);
                    handlers.sendEmptyMessageDelayed(2, 1000);
                    break;
            }
        }
    };
    private void setListener() {
        initSeekbar();
        mPause.setOnClickListener(this);
        mVoice.setOnClickListener(this);
        mRatioText.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mLessonUp.setOnClickListener(this);
        mStudyOver.setOnClickListener(this);
        linShareNext.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_pause:
                if(tagClick){
                    isFirstPlay=true;
                    startPlay();//保证视频准备好了才能点击开始
                }
                break;
            case R.id.image_voice:
                showPopVoice();//show声音控制的popu
                break;
            case R.id.textview_ratio:
                if(watchTag){
                    GaiaApp.showToast(getString(R.string.please_buy));
                    return;
                }
                showPopUp();//show分辨率切换的popu
                break;
            case R.id.textview_up:
                UpPlay();
                break;
            case R.id.textview_next:
                nextPlay();
                break;
            case R.id.textview_study_over:
                if(watchTag){
                    return;
                }
                studyOver();
                break;
            case R.id.line_share_next:
                linShareNext.setVisibility(View.GONE);
                break;
        }
    }
    //播放上一节
    private void UpPlay() {
        if(childPosition-1>=0){
            hidList.get(childPosition-1);
            childPosition--;

            tagClick=false;
            toolbar.setTitle(lessons.get(childPosition)+"");//设置title的值
            mHandler.removeMessages(CONTROLL_PROGRESS);//停止进度的发送
            handler.removeMessages(UPDATE_SEEKBAR);//停止进度条的更新

            watchTag=false;
            dealTag();//判断是否为试看
            mPause.setClickable(false);

            restarePlay();
            reInitDate(); //初始化所有的控制数据
            videoControl.setdownThreadCon();
            videoControl.setHandlerStop(3);
            videoControl.setDownloadStop();
            videoControl.setLoad_num(0);
            handlers.removeMessages(2);
            FileDele.deleteAll();

            getInfo();
            watchLen=0;
            if(progressBar.getVisibility()==View.GONE){
                progressBar.setVisibility(View.VISIBLE);
            }
            linShareNext.setVisibility(View.GONE);

            if(!watchTag){
                mStudyOver.setBackgroundResource(R.drawable.shape_play_unpressed);
                mStudyOver.setTextColor(getResources().getColor(R.color.color_ff5773));
            }

        }else {
            GaiaApp.showToast(getString(R.string.academy_no_up_lesson));
        }
    }

    // 播放下一节
    private void nextPlay() {
        if(childPosition+1<hidList.size()){
            hidList.get(childPosition+1);
            childPosition++;

            tagClick=false;
            toolbar.setTitle(lessons.get(childPosition)+"");//设置title的值
            mHandler.removeMessages(CONTROLL_PROGRESS);//停止进度的发送
            handler.removeMessages(UPDATE_SEEKBAR);//停止进度条的更新

            watchTag=false;
            dealTag();//判断是否为试看
            mPause.setClickable(false);

            restarePlay();//初始化所有的控制数据
            reInitDate();
            videoControl.setdownThreadCon();
            videoControl.setHandlerStop(3);
            videoControl.setDownloadStop();
            videoControl.setLoad_num(0);
            handlers.removeMessages(2);
            FileDele.deleteAll();
            //重新赋值
            getInfo();
            watchLen=0;
            if(progressBar.getVisibility()==View.GONE){
                progressBar.setVisibility(View.VISIBLE);
            }
            linShareNext.setVisibility(View.GONE);
            if(!watchTag){
                mStudyOver.setBackgroundResource(R.drawable.shape_play_unpressed);
                mStudyOver.setTextColor(getResources().getColor(R.color.color_ff5773));
            }
        }else{

            GaiaApp.showToast(getString(R.string.academy_play_study_over));
        }
    }

    //播放下一节课程
    public void studyOver(){
        //移除3s隐藏控制栏
        getProgress(mPlayerProxy.getDuration()/1000);

        handler.removeMessages(HINT_CONTROL);
        linShareNext.setVisibility(View.VISIBLE);
        mStudyOver.setBackgroundResource(R.drawable.shape_play_pressed);
        mStudyOver.setTextColor(getResources().getColor(R.color.white));
        progressBar.setVisibility(View.GONE);
//        mSeekBar.setProgress(videoDuration);
        pauseVideo();

    }
    //判断是否是第一次播放
    private boolean isFirstPlay=false;
    //开始播放
    public void startPlay(){
        if(mPlayerProxy!=null){
            if (mPlayerProxy.isPlaying()) {
                pauseVideo();
            }else if (mIjkMediaPlayer.isPlayable()) {
                if (!isFirstPlay) {
                    mPlayerProxy.prepareAsync();
                    isFirstPlay = true;
                    mPause.setImageResource(R.mipmap.pause);
                }else {
                    startVideo();
                }
            }
        }
    }


    private PopupWindow popupWindow;
    private FrameLayout  mFraOrigin, mFra720P,mFraHD,mFra2K;
    private View mRatioLayout;
    //show 分辨率的 popu
    private void showPopUp() {

        popupWindow = new PopupWindow(mRatioLayout,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        mRatioLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = mRatioLayout.getMeasuredHeight();
        setPopupWindow(popupWindow, mRatioText, -ScreenUtils.dp2Px(getApplicationContext(), 50), -height - ScreenUtils.dp2Px(getApplicationContext(), 40));
    }
    //声音的popu
    private PopupWindow mPopupSound;

    private void showPopVoice() {
        if (mPopupSound == null) {
            mPopupSound = new PopupWindow(mSoundView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupSound.setBackgroundDrawable(getResources().getDrawable(R.color.black60));
            mPopupSound.setFocusable(true);
            mPopupSound.setOutsideTouchable(true);
            mPopupSound.setBackgroundDrawable(new BitmapDrawable());
        }

        mSoundView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = mSoundView.getMeasuredHeight();
        int weight=mSoundView.getMeasuredWidth();
        setPopupWindow(mPopupSound, mVoice, weight/2+ScreenUtils.dp2Px(this, 1), -height- ScreenUtils.dp2Px(this, 40));
    }


    private SeekBar mSoundSeek;
    private View mSoundView;

    public void initSeekbar(){
            mSoundSeek = (SeekBar) mSoundView.findViewById(R.id.soundSeek);
            mSoundSeek.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));//android音量级数
            //设置为系统当前音量
            mSoundSeek.setProgress(am.getStreamVolume(AudioManager.STREAM_MUSIC));

            mSoundSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) {
                        return;
                    }

                    //根据progress的改变而改变音量
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
    }


    private String k2="";
    private String p720="";
    private String hd="";
    private String mp4="";

    //默认的uri
    private String selectDefaultUri(JSONObject o) {
        String defaultPath = null;
        k2 = o.optString("k2");
        p720 = o.optString("p720");
        hd = o.optString("hd");
        mp4 = o.optString("mp4");
        //mp4
        if (!TextUtils.isEmpty(mp4)&&!mp4.equals("null")&&mp4.length()!=0) {
            mRatioText.setText(this.getResources().getString(R.string.origin_definition));
            defaultPath = mp4;
        }
        //HD
        if (!TextUtils.isEmpty(hd)&&!hd.equals("null")&&hd.length()!=0) {

            if(mFraHD.getVisibility()==View.GONE){
                mFraHD.setVisibility(View.VISIBLE);
            }
            mRatioText.setText("HD");
            defaultPath = hd;

        } else {
            mFraHD.setVisibility(View.GONE);
        }
        //p720
        if (!TextUtils.isEmpty(p720)&&!p720.equals("null")&&p720.length()!=0) {
            if(mFra720P.getVisibility()==View.GONE){
                mFra720P.setVisibility(View.VISIBLE);
            }
            mRatioText.setText("720P");
            defaultPath = p720;
        } else {
            mFra720P.setVisibility(View.GONE);
        }

        //2K
        if (!TextUtils.isEmpty(k2)&&!k2.equals("null")&&k2.length()!=0) {
            if(mFra2K.getVisibility()==View.GONE){
                mFra2K.setVisibility(View.VISIBLE);
            }
            defaultPath = k2;
            mRatioText.setText("2K");
        } else {
            mFra2K.setVisibility(View.GONE);
        }

        return defaultPath;
    }

    /**
     * 开始
     */
    private void startVideo() {
        mPlayerProxy.start();
        linShareNext.setVisibility(View.GONE);
        mPause.setImageResource(R.mipmap.pause);
    }

    /**
     * 暂停
     */
    private void pauseVideo() {
        mPlayerProxy.pause();
        mLinHeadToobar.setVisibility(View.VISIBLE);
        mPause.setImageResource(R.mipmap.play);
    }

    //返回键
    public void AcademyPlayBack(View view) {
        EventBus.getDefault().post(new OnEventId(0,0));
        getProgress(mPlayerProxy.getCurrentPosition()/1000);
        mHandler.removeMessages(CONTROLL_PROGRESS);
        handlers.removeMessages(2);
        videoControl.setDownloadStop();//停止下载
        videoControl.setHandlerStop(3);
        EventBus.getDefault().unregister(this);
        FileDele.deleteAll();
        M3U8Service.finish();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
        FileDele.deleteAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeLock.release();
        watchTag=false;
        if(null!=handler){
            handler.removeMessages(UPDATE_SEEKBAR);//移除进度的刷新的handler
        }

        if(null!=mHandler){
            mHandler.removeMessages(CONTROLL_PROGRESS);//移除播放片段到的记录的handler
        }
        if(mIjkMediaPlayer!=null&&mPlayerProxy!=null){
//            mPlayerProxy.stop();
//            mPlayerProxy.release();
            mIjkMediaPlayer.stop();
            mIjkMediaPlayer.release();
        }

        handlers.removeMessages(2);
        videoControl.setDownloadStop();//停止下载
        videoControl.setHandlerStop(3);
        EventBus.getDefault().unregister(this);
        FileDele.deleteAll();
        M3U8Service.finish();
    }

    public void setPopupWindow(PopupWindow popupWindow, View view, int offsetX, int offsetY) {
        //在其上方显示控件
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            LogUtil.d(GMediaController.GMediaPlayerControl.class, "offsetX:" + offsetX + ",offsetY:" + offsetY);
            popupWindow.showAsDropDown(view, offsetX, offsetY);
        }
    }

    boolean showOrHint=true;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            if(showOrHint){
                showCon();
                showOrHint=false;
                handler.sendEmptyMessageDelayed(HINT_CONTROL,6000);
            }else if(!showOrHint){
                hint();
                showOrHint=true;
                handler.removeMessages(HINT_CONTROL);
            }
        }

        return super.onTouchEvent(event);
    }


    private void showCon(){
        mLinDowmContril.setVisibility(View.VISIBLE);
        mLinHeadToobar.setVisibility(View.VISIBLE);
    }

    private void hint(){
        mLinDowmContril.setVisibility(View.GONE);
        mLinHeadToobar.setVisibility(View.GONE);
        if(popupWindow!=null&&popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        if(mPopupSound!=null&&mPopupSound.isShowing()){
            mPopupSound.dismiss();
        }
    }

    public void getProgress(double leng){
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyNewPlayActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
            }
        };
        AcademyApiHelper.stareStudyRecord(id,seekChange+leng+1,getApplicationContext(),handler);
    }

    //发送进度
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                if(msg.what==CONTROLL_PROGRESS){
                    Long watchLen=mPlayerProxy.getCurrentPosition();
                    getProgress(watchLen/1000);
                    Log.i("AcademyPlayActivity",watchProgress*1000+"ms");
                }
            mHandler.sendEmptyMessageDelayed(CONTROLL_PROGRESS,60000);
        }
    };
    //判断资源的handler
    private Handler popupHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mPlayerProxy.seekTo((int)watchProgress*1000);
                    mSeekBar.setProgress((int)watchProgress*1000);
                    Log.i("AcademyPlayActivity",watchProgress*1000+"ms");
                    break;
                case 1:
                    finish();
                    break;
            }
        }
    };

    public void restarePlay(){
        isFirstPlay=false;
        mIjkMediaPlayer.stop();
        mIjkMediaPlayer.release();
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mIjkMediaPlayer=null;
        mIjkMediaPlayer = new IjkMediaPlayer();
        mPlayerProxy = new MediaPlayerProxy(mIjkMediaPlayer);
    }
    public void reInitDate(){
        seekChange=0;
        initView=0;
        load_time=0;
    }

}
