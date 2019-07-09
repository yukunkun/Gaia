package com.gaiamount.module_academy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import android.widget.SeekBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.media.GMediaController;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayerProxy;

//kun  academy播放页
public class AcademyPlayActivity extends AppCompatActivity implements View.OnClickListener {
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
    private CircularProgressBar progressBar;
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
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==UPDATE_SEEKBAR){

                long videoCachedDuration = mIjkMediaPlayer.getVideoCachedDuration();
                int currentPosition = (int) mIjkMediaPlayer.getCurrentPosition();
                String currenPos = mStringUtil.stringForTime(currentPosition);
                mSeekBar.setProgress(currentPosition);
                mSeekBar.setSecondaryProgress(currentPosition+(int) videoCachedDuration);
                if(currentPosition<=mPlayerProxy.getDuration()){
                    mCurrentTime.setText(currenPos);
                }

                if(mPlayerProxy.getCurrentPosition()==mPlayerProxy.getDuration()){
                    watchLen=mPlayerProxy.getDuration();
//                    getProgress(watchLen/1000);
                    handler.removeMessages(UPDATE_SEEKBAR);
                    mCurrentTime.setText(mAllTime.getText());
                    mHandler.removeMessages(CONTROLL_PROGRESS);
                    mPause.setImageResource(R.mipmap.play);
                }
            }
            if(msg.what==HINT_CONTROL){
                hint();
                showOrHint=true;
            }
            handler.sendEmptyMessageDelayed(UPDATE_SEEKBAR,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_academy_play);

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
        progressBar= (CircularProgressBar) findViewById(R.id.academy_progressBar);
        //popu的子view
        mSoundView = LayoutInflater.from(this).inflate(R.layout.acadecy_voice_seekbar, null);
        mRatioLayout= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_player_ration, null);
        //初始化Voice的seekbar和分辨率的popu
        initRation(mRatioLayout);
        mLinDowmContril.setVisibility(View.GONE);
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
            switch (v.getId()){
                case R.id.definitionOrigin:

                    mHandler.removeMessages(CONTROLL_PROGRESS);//停止进度的发送
                    handler.removeMessages(UPDATE_SEEKBAR);//停止进度条的更新
                    restarePlay();
                    setVideoListener(mp4);

                    mRatioText.setText(getApplicationContext().getResources().getString(R.string.origin_definition));
                    popupWindow.dismiss();
                break;
                case R.id.definition720P:

                    mHandler.removeMessages(CONTROLL_PROGRESS);
                    handler.removeMessages(UPDATE_SEEKBAR);
                    restarePlay();
                    setVideoListener(p720);

                    mRatioText.setText("720P");
                    popupWindow.dismiss();
                break;
                case R.id.definitionHD:

                    mHandler.removeMessages(CONTROLL_PROGRESS);
                    handler.removeMessages(UPDATE_SEEKBAR);
                    restarePlay();
                    setVideoListener(hd);

                    mRatioText.setText("HD");
                    popupWindow.dismiss();
                    break;
                case R.id.definition2K:

                    mHandler.removeMessages(CONTROLL_PROGRESS);
                    handler.removeMessages(UPDATE_SEEKBAR);
                    restarePlay();
                    setVideoListener(k2);

                    mRatioText.setText("2K");
                    popupWindow.dismiss();
                    break;

            }
        }
    }
    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyPlayActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                Log.i("-----academy","-"+response.toString());
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
            popupHandler.sendEmptyMessageDelayed(1,3000);
            return;
        }

        JSONObject o = response.optJSONObject("o");
        watchProgress=o.optLong("watchLen");
        id=o.optLong("lrid");
        defaultUri = selectDefaultUri(o);

        setVideoListener(defaultUri);
    }

    public void setVideoListener(String uri) {
        try {
            if(uri==null){
                return;
            }

            mPlayerProxy.setDataSource(uri);
            mPlayerProxy.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayerProxy.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(IMediaPlayer mp) {
                    mp.setDisplay(mSurfaceView.getHolder());
                    mp.start();
                    //加载的progressbar
                    progressBar.setVisibility(View.GONE);
                    //获取到视频的最大时间
                    videoDuration = (int) mp.getDuration();
                    String duration = mStringUtil.stringForTime(videoDuration);
                    mAllTime.setText(duration);//时间
                    //进度最大
                    mSeekBar.setMax(videoDuration);
                    //获取当前的进度
                    int currentPosition = (int)mp.getCurrentPosition();
                    String currentTime = mStringUtil.stringForTime(currentPosition);
                    mCurrentTime.setText(currentTime);
                    //发送消息，实时更新进度条
                    handler.sendEmptyMessage(UPDATE_SEEKBAR);
                    handler.sendEmptyMessageDelayed(HINT_CONTROL,4000);
                    mHandler.sendEmptyMessageDelayed(CONTROLL_PROGRESS,60000);//获取进度的handler
                    mSeekBar.setOnSeekBarChangeListener(new onSeekBarChangeListener());
                    mLinDowmContril.setVisibility(View.VISIBLE);
                    mPause.setImageResource(R.mipmap.pause);//更新按钮
                    tagClick=true;
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
        popupHandler.sendEmptyMessageDelayed(0,1000);
        tryWatch();
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
                                if(mPlayerProxy.isPlaying()&&mPlayerProxy.getCurrentPosition()>360000){
                                    if(mIjkMediaPlayer!=null){
                                        pauseVideo();
                                        GaiaApp.showToast("试看结束,请先购买");
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
            if (fromUser&&mPlayerProxy!=null&&mIjkMediaPlayer!=null){
                seekBar.setProgress(progress);
                mPlayerProxy.seekTo(progress);
                String current = mStringUtil.stringForTime(progress);
                mCurrentTime.setText(current);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

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
                    startPlay();//保证视频没准备好了才能点击开始
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

            restarePlay();
            getInfo();
            watchLen=0;

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

            restarePlay();
            getInfo();
            watchLen=0;

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
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(AcademyPlayActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
            }
        };
        AcademyApiHelper.stareStudyRecord(id,leng+1,getApplicationContext(),handler);
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
                    Log.i("AcademyPlayActivity-22-",watchProgress*1000+"ms");
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

}
