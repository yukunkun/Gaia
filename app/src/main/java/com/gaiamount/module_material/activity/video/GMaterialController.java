package com.gaiamount.module_material.activity.video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_material.activity.util.EventRatio;
import com.gaiamount.module_material.activity.util.EventbusProgress;
import com.gaiamount.module_material.activity.util.FileDele;
import com.gaiamount.module_material.activity.util.M3U8Service;
import com.gaiamount.module_material.activity.util.VideoControl;
import com.gaiamount.module_material.bean.MaterialDetailInfo;
import com.gaiamount.module_player.OnEventRatio;
import com.gaiamount.module_player.bean.VideoDetailInfo;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.ScreenUtils;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by haiyang-lu on 16-7-12.
 * gaiamount媒体控制栏控件
 */
public class GMaterialController extends FrameLayout implements IGMediaController {
    private static final int sDefaultTimeout = 5000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private Context mContext;
    private Toolbar mToolbar;
    private TextView mCurrentTime;
    private SeekBar mProgress;
    private TextView mEndTime;
    private View mContentView;

    /**
     * 实现了该接口的实例
     */
    private GMaterialPlayerControl mPlayer;
    private ImageView mIv_play;

    /**
     * 是否正在显示中
     */
    private boolean mShowing;
    /**
     * 是否正在拖动
     */
    private boolean mDragging;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private View mAnchor;
    private ImageView mIv_sound;
    private ImageView mIv_mask;
    private TextView mTv_definition;
    private ImageView mFullScreen;
    private AudioManager am;
    /**
     * 控制声音的seekbar
     */
    private SeekBar mSoundSeek;
    private final View mLightView;
    private SeekBar mLightSeek;
    private VideoControl videoControl;
    private int seekChange=0;
    private int initView=0;
    private int load_time=0;
    private ArrayList<String> tsString;
    private String format = String.format("http://localhost:%d", M3U8Service.PORT);
    private String serviceUri = format+"/gaiamount/gaia/test.m3u8";
    private String ratio="";

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public GMaterialController(Context context) {
        super(context);
        mContext = context;
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mSoundView = inflater.inflate(R.layout.item_player_sound, null);
        mDefinitionView = inflater.inflate(R.layout.item_player_definition, null);
        mMaskView = inflater.inflate(R.layout.item_player_mask, null);
        mLightView = inflater.inflate(R.layout.item_player_lightness, null);
    }

    public void setVideoControl(VideoControl videoControl) {
        if(this.videoControl!=null){
            this.videoControl=null;
        }
        this.videoControl = videoControl;
    }

    private void initContentView(View root) {
        mCurrentTime = (TextView) root.findViewById(R.id.time_current);
        mProgress = (SeekBar) root.findViewById(R.id.mediacontroller_progress);
        mProgress.setMax((int) videoControl.getMaxTime());
        mEndTime = (TextView) root.findViewById(R.id.tv_time);

        mIv_play = (ImageView) root.findViewById(R.id.play_btn);

        //只有全屏才显示的控件
        mIv_sound = (ImageView) root.findViewById(R.id.sound);
        mIv_mask = (ImageView) root.findViewById(R.id.mask);
        mTv_definition = (TextView) root.findViewById(R.id.definition);
        mFullScreen = (ImageView) root.findViewById(R.id.full_screen);

        //播放暂停
        if (mIv_play != null) {
            mIv_play.setOnClickListener(mPlayListener);
        }

        //全屏非全屏
        setFullScreenVisibility(false);
        mFullScreen.setOnClickListener(mFullScreenListener);

        //拖动进度条快进
        mProgress.setOnSeekBarChangeListener(mSeekBarChangeListener);

        //点击音量按钮显示音量调节控件（全屏）
        mIv_sound.setOnClickListener(mSoundListener);

        //点击显示遮罩选项
        mIv_mask.setOnClickListener(mMaskListener);

        //点击显示选择清晰度选项
        mTv_definition.setOnClickListener(mDefinitionListener);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        initPopupWindowView();
        //默认的全屏
        mPlayer.changeRenderSize(3);

    }

    private void initPopupWindowView() {
        //声音的popup～～～～
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
        //～～～～
        mLightView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mLightSeek = (SeekBar) mLightView.findViewById(R.id.lightSeek);
        mLightSeek.setMax(1 * 100);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        params.rightMargin = ScreenUtils.dp2Px(mContext, 80);
        mLightView.setLayoutParams(params);
        if (mAnchor instanceof ViewGroup) {
            ViewGroup anchor = (ViewGroup) mAnchor;
            anchor.removeView(mLightView);
            anchor.addView(mLightView);
            mLightView.setVisibility(View.GONE);
        }


    }

    public void showLightProgress() {
        if (mLightView != null) {
            mLightView.setVisibility(View.VISIBLE);
        }
    }

    public void hideLightProgress() {
        if (mLightView != null) {
            mLightView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLightView.setVisibility(View.GONE);
                }
            },2000);
        }
    }

    public void updateLightProgress(float light) {
        if (mLightSeek != null) {
            mLightSeek.setProgress((int) (light * 100));
        }
    }

    public void updateSoundProgress() {
        if (mSoundSeek != null) {
            //设置为系统当前音量
            mSoundSeek.setProgress(am.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
    }

    private void updateDefinition() {
        //清晰度的popup
        FrameLayout definitonOrigin = (FrameLayout) mDefinitionView.findViewById(R.id.definitionOrigin);
        FrameLayout definition4K = (FrameLayout) mDefinitionView.findViewById(R.id.definition4K);
        FrameLayout definition2K = (FrameLayout) mDefinitionView.findViewById(R.id.definition2K);
        FrameLayout definition1080P = (FrameLayout) mDefinitionView.findViewById(R.id.definition1080P);
        FrameLayout definition720P = (FrameLayout) mDefinitionView.findViewById(R.id.definition720P);

        final String mp4 = mDetailInfo.getResource().getMp4();
        final String k4 = mDetailInfo.getResource().getK4();
        final String k2 = mDetailInfo.getResource().getK2();
        final String p1080 = mDetailInfo.getResource().getP1080();
        final String p720 = mDetailInfo.getResource().getP720();

        final String[] array = getResources().getStringArray(R.array.definition_values);
        if (mDetailInfo != null) {
            //原画一定有
            definitonOrigin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //切换到原画
                    EventBus.getDefault().post(new EventRatio(mp4));
                    mPlayer.pause();
                    initView=0;
                    load_time=0;
//                    mPlayer.setVideoURI(Uri.parse(mp4));
                    mTv_definition.setText(array[4]);//原视频
                    ratio=array[4];
                    dismissPopWindow();
                    updateSwitch();
                }
            });
            if (mDetailInfo.getMaterial().getHave4K() == 1 && mDetailInfo.getResource().getK4() != null) {
                definition4K.setVisibility(View.VISIBLE);
                definition4K.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //切换到4k
                        EventBus.getDefault().post(new EventRatio(k4));
                        mPlayer.pause();
                        initView=0;
                        load_time=0;
//                        mPlayer.setVideoURI(Uri.parse(k4));
                        mTv_definition.setText(array[3]);//4k
                        ratio=array[3];
                        dismissPopWindow();
                        updateSwitch();
                    }
                });
            } else {
                definition4K.setVisibility(View.GONE);
            }

            if (mDetailInfo.getMaterial().getHave2K() == 1 && mDetailInfo.getResource().getK2() != null) {
                definition2K.setVisibility(View.VISIBLE);
                definition2K.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //切换到4k
                        EventBus.getDefault().post(new EventRatio(k2));
                        mPlayer.pause();
                        initView=0;
                        load_time=0;
//                        mPlayer.setVideoURI(Uri.parse(k2));
                        mTv_definition.setText(array[2]);
                        ratio=array[2];
                        dismissPopWindow();
                        updateSwitch();
                    }
                });
            } else {
                definition2K.setVisibility(View.GONE);
            }
            if (mDetailInfo.getMaterial().getHave1080() == 1 && mDetailInfo.getResource().getP1080() != null) {
                definition1080P.setVisibility(View.VISIBLE);
                definition1080P.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //切换到4k
                        EventBus.getDefault().post(new EventRatio(p1080));
                        mPlayer.pause();
                        initView=0;
                        load_time=0;
//                        mPlayer.setVideoURI(Uri.parse(p1080));
                        mTv_definition.setText(array[1]);
                        ratio=array[1];
                        dismissPopWindow();
                        updateSwitch();
                    }
                });
            } else {
                definition1080P.setVisibility(mContentView.GONE);
            }
            if (mDetailInfo.getMaterial().getHave720() == 1 && mDetailInfo.getResource().getP720() != null) {
                definition720P.setVisibility(View.VISIBLE);
                definition720P.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //切换到4k
                        EventBus.getDefault().post(new EventRatio(p720));
                        mPlayer.pause();
                        load_time=0;
//                        mPlayer.setVideoURI(Uri.parse(p720));
                        mTv_definition.setText(array[0]);
                        ratio=array[0];
                        updateSwitch();
                    }
                });
            } else {
                definition720P.setVisibility(View.GONE);
            }
        }
        //显示默认Uri的画质
        if (mDefaultUri != null) {
            if (mDefaultUri.equals(mp4)) {
                ratio=array[4];
                mTv_definition.setText(array[4]);
            } else if (mDefaultUri.equals(k4)) {
                ratio=array[3];
                mTv_definition.setText(array[3]);
            } else if (mDefaultUri.equals(k2)) {
                ratio=array[2];
                mTv_definition.setText(array[2]);
            } else if (mDefaultUri.equals(p1080)) {
                ratio=array[1];
                mTv_definition.setText(array[1]);
            } else {
                ratio=array[0];
                mTv_definition.setText(array[0]);
            }
        }
    }

    private FrameLayout lastMaskLayout;

    private void updateMask() {
        final FrameLayout mask235 = (FrameLayout) mMaskView.findViewById(R.id.mask_2_35);
        final FrameLayout mask185 = (FrameLayout) mMaskView.findViewById(R.id.mask_1_85);
        final FrameLayout mask169 = (FrameLayout) mMaskView.findViewById(R.id.mask_16_9);
        final FrameLayout mask43 = (FrameLayout) mMaskView.findViewById(R.id.mask_4_3);
        mask235.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastMaskLayout != null)
                    lastMaskLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
//                mPlayer.changeRenderSize(1);
                EventBus.getDefault().post(new OnEventRatio(1));
                dismissPopWindow();
                mask235.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                lastMaskLayout = mask235;
            }
        });

        mask185.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastMaskLayout != null)
                    lastMaskLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
//                mPlayer.changeRenderSize(2);
                EventBus.getDefault().post(new OnEventRatio(2));
                dismissPopWindow();
                mask185.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                lastMaskLayout = mask185;
            }
        });
        mask169.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastMaskLayout != null)
                    lastMaskLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
//                mPlayer.changeRenderSize(3);
                EventBus.getDefault().post(new OnEventRatio(3));
                dismissPopWindow();
                mask169.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                lastMaskLayout = mask169;
            }
        });

        mask43.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastMaskLayout != null)
                    lastMaskLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
//                mPlayer.changeRenderSize(4);
                EventBus.getDefault().post(new OnEventRatio(4));
                dismissPopWindow();
                mask43.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                lastMaskLayout = mask43;
            }
        });

    }

    private void updateSwitch() {
        if (isFullScreen) {
            if (mFullScreen != null) {
                mFullScreen.setImageResource(R.mipmap.ic_collapse_n);
                setFullScreenVisibility(true);
            }
        } else {
            if (mFullScreen != null) {
                mFullScreen.setImageResource(R.mipmap.ic_fullscreen);
                setFullScreenVisibility(false);
            }
        }
    }

    public void setFullScreenVisibility(boolean visibility) {
        if (visibility) {
            mIv_sound.setVisibility(View.VISIBLE);
            mIv_mask.setVisibility(View.VISIBLE);
            mTv_definition.setVisibility(View.VISIBLE);
        } else {
            EventBus.getDefault().post(new OnEventRatio(3));
            mIv_sound.setVisibility(View.GONE);
            mIv_mask.setVisibility(View.GONE);
            mTv_definition.setVisibility(View.GONE);
        }
    }


    // This is called whenever mAnchor's layout bound changes
    private final OnLayoutChangeListener mLayoutChangeListener =
            new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right,
                                           int bottom, int oldLeft, int oldTop, int oldRight,
                                           int oldBottom) {

                }
            };

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing &&mPlayer!=null&& mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
//                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    private int setProgress() {
//        if (mPlayer == null || mDragging) {
//            return 0;
//        }
//        int position = mPlayer.getCurrentPosition();
//        int duration = mPlayer.getDuration();
//
//        if (mProgress != null) {
//            if (duration > 0) {
//                long pos = 1000L * position / duration;
//                mProgress.setProgress((int) pos);
//            }
//            int percent = mPlayer.getBufferPercentage();
//            mProgress.setSecondaryProgress(percent * 10);
//        }
//        if (mCurrentTime != null) {
//            mCurrentTime.setText(stringForTime((int) position));
//        }
//        if (mEndTime != null) {
//            mEndTime.setText(stringForTime((int) duration));
//        }

        return 0;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);

        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }

    }

    @Override
    public void hide() {
        updatePausePlay();
        if (mShowing) {
            mHandler.removeMessages(SHOW_PROGRESS);
            if (mAnchor instanceof ViewGroup) {
                ViewGroup anchor = (ViewGroup) mAnchor;
                anchor.removeView(this);
            }
        }

        if (mToolbar != null) {
            mToolbar.setVisibility(View.GONE);
        }
        dismissPopWindow();
        mShowing = false;
    }

    @Override
    public void show(int timeout) {
        updatePausePlay();
        if (!mShowing) {
            mHandler.sendEmptyMessage(SHOW_PROGRESS);

            if (mAnchor instanceof ViewGroup) {
                ViewGroup anchor = (ViewGroup) mAnchor;
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.BOTTOM;
                anchor.addView(this, params);
            }
            mShowing = true;
        }

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            Message message = mHandler.obtainMessage(FADE_OUT);
            mHandler.sendMessageDelayed(message, timeout);
        }

        if (mToolbar != null) {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        show(sDefaultTimeout);
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    @Override
    public void setAnchorView(View view) {
        if (mAnchor != null) {
            mAnchor.removeOnLayoutChangeListener(mLayoutChangeListener);
        }
        mAnchor = view;
        if (mAnchor != null) {
            mAnchor.addOnLayoutChangeListener(mLayoutChangeListener);
        }

        //点击事件
        if (mAnchor != null) {
            mAnchor.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowing()) {
                        hide();
                    } else {
                        show();
                    }
                }
            });

        }
        LayoutParams frameParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    private void dismissPopWindow() {
        if (mPopupSound != null && mPopupSound.isShowing()) {
            mPopupSound.dismiss();
        }
        if (mPopupMask != null && mPopupMask.isShowing()) {
            mPopupMask.dismiss();
        }
        if (mPopupDefinition != null && mPopupDefinition.isShowing()) {
            mPopupDefinition.dismiss();
        }
    }

    private View makeControllerView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.media_controller, null);

        initContentView(mContentView);

        return mContentView;
    }

    @Override
    public void showOnce(View view) {

    }

    @Override
    public void setMediaPlayer(GMaterialPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

//    @Override
//    public void setMediaPlayer(GMaterialController.GMediaPlayerControl player) {
//        mPlayer = player;
//        updatePausePlay();
//
//    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (isShowing()) {
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateScreen() {
        updatePausePlay();
    }

    private final OnClickListener mPlayListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //播放或暂停
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~萌萌的分割线～～～
    private PopupWindow mPopupDefinition;
    private View mDefinitionView;
    private final OnClickListener mDefinitionListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //如果有其他显示的，先dismiss掉
            dismissPopWindow();
            if (mPopupDefinition == null) {
                mPopupDefinition = new PopupWindow(mDefinitionView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupDefinition.setBackgroundDrawable(getResources().getDrawable(R.color.black60));
            }
            mDefinitionView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int height = mDefinitionView.getMeasuredHeight();
            setPopupWindow(mPopupDefinition, mTv_definition, -ScreenUtils.dp2Px(mContext, 50), -height - ScreenUtils.dp2Px(mContext, 40));
        }
    };

    //遮罩
    private PopupWindow mPopupMask;
    private View mMaskView;
    private final OnClickListener mMaskListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //如果有其他显示的，先dismiss掉
            dismissPopWindow();
            if (mPopupMask == null) {
                mPopupMask = new PopupWindow(mMaskView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupMask.setBackgroundDrawable(getResources().getDrawable(R.color.black60));
            }
            mMaskView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int height = mMaskView.getMeasuredHeight();
            setPopupWindow(mPopupMask, mIv_mask, -ScreenUtils.dp2Px(mContext, 50), -height - ScreenUtils.dp2Px(mContext, 40));
        }
    };

    //声音按钮
    private PopupWindow mPopupSound;
    private View mSoundView;
    private final OnClickListener mSoundListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //如果有其他显示的，先dismiss掉
            dismissPopWindow();
            if (mPopupSound == null) {
                mPopupSound = new PopupWindow(mSoundView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupSound.setBackgroundDrawable(getResources().getDrawable(R.color.black60));
            }

            mSoundView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int height = mSoundView.getMeasuredHeight();
            LogUtil.w(GMaterialPlayerControl.class, "measuredHeight:" + height);
            setPopupWindow(mPopupSound, mIv_sound, ScreenUtils.dp2Px(mContext, 10), -height - ScreenUtils.dp2Px(mContext, 40));

        }
    };

    public void setPopupWindow(PopupWindow popupWindow, View view, int offsetX, int offsetY) {
        //在其上方显示控件
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            LogUtil.d(GMaterialPlayerControl.class, "offsetX:" + offsetX + ",offsetY:" + offsetY);
            popupWindow.showAsDropDown(view, offsetX, offsetY);
        }
    }

    //进度条改变
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }
            int pro = progress / 5;
            videoControl.setLoad_num(pro);
            seekChange = progress-progress%5;  //控制进度条的误差.必须减去余数
            initView = progress/5;
            load_time = progress/5;

            long duration = mPlayer.getDuration();
            EventBus.getDefault().post(new EventbusProgress(1));
//            if (mCurrentTime != null)
//                mCurrentTime.setText(stringForTime((int) newPos+seekChange*1000));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if(mPlayer!=null&&mPlayer.isPlaying()){
                mPlayer.pause();
            }

            videoControl.setHandlerStop(1);
            videoControl.setHandlerStop(3);
            handlers.removeMessages(2);
            FileDele.delete("test.m3u8");
            FileDele.deleteAll();

            //长时间显示控制栏
            show(1000 * 1000);
            mDragging = true;
            //移除定时更新进度的消息
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

//            videoControl.writeHead();
            videoControl.setDownLoadOver(3);
            videoControl.load();
            //恢复默认时间
            show(sDefaultTimeout);
            mDragging = false;
            //恢复定时更新进度的消息
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    Handler handlers=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTv_definition.setText(ratio);
            switch (msg.what){

                case 2:
                    //删除控制
                    tsString = videoControl.getTsString();
                    Log.i("-------currentTime",(mPlayer.getCurrentPosition()/1000+seekChange)+"+"+load_time);
                    for (int i = 0; i < tsString.size(); i++) {
                        if(mPlayer!=null){
                            if((mPlayer.getCurrentPosition()/1000)+seekChange-load_time*5>10){
                                videoControl.deletePlayedTs(load_time);
                                load_time++;
                            }
                        }
                    }

                    if (mCurrentTime != null) {
                        mCurrentTime.setText(stringForTime(mPlayer.getCurrentPosition()+seekChange*1000));
                    }
                    if (mEndTime != null) {
                        mEndTime.setText(stringForTime((int) videoControl.getMaxTime()*1000));
                    }
                    mProgress.setProgress(mPlayer.getCurrentPosition()/1000+seekChange);
                    handlers.sendEmptyMessageDelayed(2,1000);
                    break;
                case 1:
                    mTv_definition.setText(ratio);
                    updateSwitch(isFullScreen);
                    break;
            }
        }
    };

    public void switchUri(){
        mPlayer.setVideoURI(Uri.parse(serviceUri));
        setHandlerStart(2);
        setHandlerStart(1);
    }

    public void setHandlerStart(int con){
        if(con==2){
            handlers.sendEmptyMessageDelayed(2,1);
        }else if(con==1){
            handlers.sendEmptyMessageDelayed(1,1);
        }
    }
    public void setHandlerStop(){
        handlers.removeMessages(2);
    }
    public int  getInitView(){
        return initView;
    }

    private boolean isFullScreen;
    private final OnClickListener mFullScreenListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            isFullScreen = !isFullScreen;
            //全屏或非全屏
            if (isFullScreen) {
                switchLand();

            } else {
                switchPortrait();

            }
            updateSwitch(isFullScreen);
        }
    };

    public void switchPortrait() {
        //全屏
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mAnchor.getLayoutParams();
            lp.width = LayoutParams.MATCH_PARENT;
            lp.height = LayoutParams.WRAP_CONTENT;
            mAnchor.setLayoutParams(lp);
        }
    }

    public void switchLand() {

        //竖屏
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mAnchor.getLayoutParams();
            lp.width = LayoutParams.MATCH_PARENT;
            lp.height = LayoutParams.MATCH_PARENT;
            mAnchor.setLayoutParams(lp);
        }
    }

    public void updateSwitch(boolean isLand) {
        this.isFullScreen = isLand;
        updateSwitch();
    }

    private boolean isPaused = false;

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            isPaused = true;
        } else {
            mPlayer.start();
            isPaused = false;
        }
        updatePausePlay();
    }

    public void updatePausePlay() {
        if (mIv_play == null) {
            return;
        }
        if (!isPaused || mPlayer.isPlaying()) {
            mIv_play.setImageResource(R.mipmap.ic_pause);
        } else {
            mIv_play.setImageResource(R.mipmap.ic_player);
        }
    }

    MaterialDetailInfo mDetailInfo;

    public void setVideoInfo(MaterialDetailInfo detailInfo) {
        if (detailInfo == null) {
            return;
        }
        this.mDetailInfo = detailInfo;

        updateDefinition();

        updateMask();
    }

    String mDefaultUri;

    public void setDefaultUri(String defaultUri) {
        this.mDefaultUri = defaultUri;
    }

    public interface GMaterialPlayerControl {
        void start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        /**
         * Get the audio session id for the player used by this VideoView. This can be used to
         * apply audio effects to the audio track of a video.
         *
         * @return The audio session, or 0 if there was an error.
         */
        int getAudioSessionId();

        void setVideoURI(Uri uri);

        void changeRenderSize(int index);
    }

    public void stop() {
        mHandler.removeCallbacksAndMessages(null);
    }

}
