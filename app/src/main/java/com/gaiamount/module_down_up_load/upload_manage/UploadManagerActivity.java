package com.gaiamount.module_down_up_load.upload_manage;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.apis.file_api.FileApiHelper;
import com.gaiamount.gaia_main.DrawerBaseActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.UploadActivity;
import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.encrypt.SHA256;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.swipe_menu_listview.SwipeMenu;
import com.gaiamount.widgets.swipe_menu_listview.SwipeMenuCreator;
import com.gaiamount.widgets.swipe_menu_listview.SwipeMenuItem;
import com.gaiamount.widgets.swipe_menu_listview.SwipeMenuListView;
import com.github.alexkolpa.fabtoolbar.FabToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * lhy 上传管理
 */
public class UploadManagerActivity extends DrawerBaseActivity {

    public static final int WHAT = 0;
    public static final int DELAY_MILLIS = 1000;

    @Bind(R.id.list_view)
    SwipeMenuListView mListView;
    @Bind(R.id.fab_toolbar)
    FabToolbar mFabToolbar;
    @Bind(R.id.video_thumb)
    ImageView mVideoThumb;
    @Bind(R.id.progressTxt)
    TextView mProgressTxt;
    @Bind(R.id.upload_progress)
    ProgressBar mUploadProgress;
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.empty_hint_text)
    TextView emptyHintTxt;

    private DecimalFormat df = new DecimalFormat("##0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_manager);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //父类方法
        initHeaderView();
        //父类方法
        updateUserData();
        //父类方法
        initToolbar();
        //设置标题
        title.setText(getString(R.string.title_activity_upload_manage));

        //获取数据
        FileApiHelper.getUploadList(this);


        setFabToolbar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mHandler.sendEmptyMessageDelayed(WHAT, DELAY_MILLIS);
    }

    private void setFabToolbar() {
        mFabToolbar.setColor(getResources().getColor(R.color.colorAccent));
        mFabToolbar.attachToListView(mListView);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFabToolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    if (UploadManager.getInstance(getApplicationContext()).isUploading()) {
                        //点击打开fabtoolbar
                        mFabToolbar.findViewById(R.id.button).setOnClickListener(fabOpenClickListener);
                        if (isFabShown) {
                            mFabToolbar.show();
                        } else {
                            mFabToolbar.hide();
                        }
                        mHandler.sendEmptyMessageDelayed(WHAT, DELAY_MILLIS);
                    } else {
                        mFabToolbar.findViewById(R.id.button).setOnClickListener(fabClickListener);
                    }
                }
            });
//        }

//        mFabToolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                if (UploadManager.getInstance(getApplicationContext()).isUploading()) {
//                    //点击打开fabtoolbar
//                    mFabToolbar.findViewById(R.id.button).setOnClickListener(fabOpenClickListener);
//                    if (isFabShown) {
//                        mFabToolbar.show();
//                    } else {
//                        mFabToolbar.hide();
//                    }
//                    mHandler.sendEmptyMessageDelayed(WHAT, DELAY_MILLIS);
//                } else {
//                    mFabToolbar.findViewById(R.id.button).setOnClickListener(fabClickListener);
//                }
//            }
//        });


    }

    /**
     * fab点击监听
     */
    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityUtil.startUploadActivity(UploadManagerActivity.this);
        }
    };

    private boolean isFabShown;
    /**
     * fab点击监听
     */
    private View.OnClickListener fabOpenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFabToolbar.show();
            isFabShown = true;
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mFabToolbar != null) {
            mFabToolbar.hide();
            isFabShown = false;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param list
     * @see FileApiHelper#getUploadList(long, int, int, int, int, Context)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventGetUploadedWorks(final List<UploadedWorks> list) {

        setEmptyHintVisibility(list);

        final UploadListCommenAdapter adapter = new UploadListCommenAdapter(this, R.layout.item_uploaded_list, list);
        mListView.setAdapter(adapter);
        //item的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GaiaApp.showToast(getStatus(list.get(position).getStatus()));

//                UploadedWorks uploadedWorks = list.get(position);
//                ActivityUtil.startPlayerActivity(UploadManagerActivity.this, (int) uploadedWorks.getId(), 0);
            }
        });
        //创建列表条目侧滑的按钮和样式
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(UploadManagerActivity.this);
                //背景颜色
                deleteItem.setBackground(R.drawable.selector_swipe_delte_bg);
                deleteItem.setWidth(ScreenUtils.dp2Px(getApplicationContext(), 90));
                deleteItem.setIcon(R.mipmap.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(swipeMenuCreator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    //延迟300ms，等待侧滑完全关闭后再执行
                    mListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GaiaApp.showToast(getString(R.string.deleting));
                            final UploadedWorks uploadedWorks = list.get(position);
                            //延迟300ms启动
                            MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(FileApiHelper.class) {
                                @Override
                                public void onGoodResponse(JSONObject response) {
                                    super.onGoodResponse(response);
                                    GaiaApp.showToast(getString(R.string.delete_success));
                                    list.remove(uploadedWorks);
                                    adapter.notifyDataSetChanged();
                                    setEmptyHintVisibility(list);
                                }
                            };
                            FileApiHelper.cancelUpload(uploadedWorks.getId(), uploadedWorks.getSchedule(),
                                    getString(R.string.user_clicked_the_cancel_button), 2, UploadManagerActivity.this, handler);
                        }
                    }, 300);
                }
            }
        });


    }

    private void setEmptyHintVisibility(List list) {
        if (list.size() == 0) {
            emptyHintTxt.setVisibility(View.VISIBLE);
        } else {
            emptyHintTxt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UploadActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            //获取数据
            String inputKey = data.getStringExtra("input_key");
            String token = data.getStringExtra("token");
            String videoPath = data.getStringExtra("video_path");
//            Log.i("----tag","tag");
            UploadManager.getInstance(getApplicationContext()).startUpload(inputKey, token, videoPath);

            mHandler.sendEmptyMessageDelayed(WHAT, DELAY_MILLIS);
            mFabToolbar.show();
            mVideoThumb.setImageBitmap(ImageUtils.getVideoThumbnail(videoPath));

            setAddInfo();

        }
    }

    private void setAddInfo() {
        JSONObject jsonObject = prepareParams();
        try {
            jsonObject.put("fid",GaiaApp.getAppInstance().getUpdateWorksBean().getA().getFid());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(UploadManagerActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
            }
        };
        WorksApiHelper.uploadAddInfo(getApplicationContext(),jsonObject,handler);

    }

    @SuppressWarnings("HandlerLeak")
    private Handler mHandler = new Handler() {

        private UploadManager mUploadManager;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mUploadManager = UploadManager.getInstance(getApplicationContext());
            if (msg.what == WHAT) {
                //定时去的进度刷新
                double uploadProgress = mUploadManager.getProgress() * 100;
                mUploadProgress.setProgress((int) uploadProgress);
                mProgressTxt.setText(df.format(uploadProgress) + "%");
                if (uploadProgress == 100) {
                    //更换fab点击监听器
                    mFabToolbar.findViewById(R.id.button).setOnClickListener(fabClickListener);
                    mFabToolbar.hide();
                    mHandler.removeCallbacksAndMessages(null);
                    UploadManager.getInstance(getApplicationContext()).release();
                    //重新请求网络数据
                    FileApiHelper.getUploadList(UploadManagerActivity.this);
                }

                sendEmptyMessageDelayed(WHAT, DELAY_MILLIS);
            }
        }
    };
    private String getStatus(int status){
        switch (status) {
            case 0:
                return  "上传中";
            case 1:
                return  "转码中";
            case 2:
                return "切片中";
            case 3:
                return "上传失败";
            case 4:
                return "转码失败";
            case 5:
                return "切片失败";
            case 6:
                return "待审核";
            case 7:
                return "已审核";
            default:
                return "未知错误";
        }
    }
    /**
     * 准备上传参数
     *
     * @return
     */
    private JSONObject prepareParams() {
        UpdateWorksBean.ABean a = GaiaApp.getAppInstance().getUpdateWorksBean().getA();

        JSONObject jobj = new JSONObject();
        try {
            //必选参数
//            jobj.put("uid", GaiaApp.getUserInfo().id);
            jobj.put("name", a.getName());
            jobj.put("type", a.getType());
//            jobj.put("format", mVideoInfo.postFix);
            jobj.put("client", Configs.CLIENT_APP);
//            jobj.put("size", mVideoInfo.videoSize);
            jobj.put("keywords", a.getKeywords());
            int allowDownload = a.getAllowDownload();
            int allowCharge = a.getAllowCharge();
            jobj.put("allowDownload", allowDownload);
            jobj.put("allowCharge", a.getAllowCharge());
//            if (allowDownload == 1) {//设置允许下载的参数
//                jobj.put("have4k", a.getHave4k());
//                jobj.put("have2k", a.getHave2k());
//                jobj.put("have1080", a.getHave1080());
//                jobj.put("have720", a.getHave720());
//                jobj.put("allowCharge", allowCharge);
//
//                jobj.put("priceOriginal", a.getPriceOriginal());
//                jobj.put("price4K", a.getPrice4K());
//                jobj.put("price2K", a.getPrice2K());
//                jobj.put("price1080", a.getPrice1080());
//                jobj.put("price720", a.getPrice720());
//            }
            jobj.put("requirePassword", a.getRequirePassword());
            if(a.getRequirePassword()==1){
                jobj.put("password", SHA256.bin2hex(a.getPassword())); //加密上传
            }
            jobj.put("photographer", a.getPhotographer());  //
            jobj.put("cutter", a.getCutter());  //
            jobj.put("colorist", a.getColorist()); //
            jobj.put("director", a.getDirector()); //
            jobj.put("macheine", a.getMachine()); //
            jobj.put("lens", a.getLens());   //
            jobj.put("address", a.getAddress());  //
            jobj.put("description", a.getBackStory());  //
            jobj.put("yuv",a.getYuv());
            jobj.put("bit",a.getBit());
            jobj.put("crf",a.getCrf());
            jobj.put("cv",a.getAvc());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jobj;
    }
}
