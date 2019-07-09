package com.gaiamount.module_user.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_creator.PersonApiHelper;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.signin_signup.UserInfo;
import com.gaiamount.module_user.personal_album.adapter.MyAdapter;
import com.gaiamount.module_user.user_edit.UserInfoActivity;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.BroadcastUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-3-10.
 * 个人主页
 */
public class PersonalActivity extends AppCompatActivity {
    public static final String UID = "mTargetUserId";
    @Bind(R.id.avatar)
    RoundedImageView mAvatar;
    @Bind(R.id.private_chat)
    Button mPrivateChat;
    @Bind(R.id.add_attention)
    Button mAddAttention;
    @Bind(R.id.user_name)
    TextView mUserName;
    @Bind(R.id.real_address)
    TextView mRealAddress;
    @Bind(R.id.user_infos)
    TextView mUserInfos;
    @Bind(R.id.user_signature)
    TextView mUserSignature;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.draft_script_container)
    LinearLayout mDraftScriptContainer;
    @Bind(R.id.creations_container)
    LinearLayout mCreationsContainer;
    @Bind(R.id.special_container)
    LinearLayout mSpecialContainer;
    @Bind(R.id.friend_cicrlce_container)
    LinearLayout mFriendCicrlceContainer;
    @Bind(R.id.collection_container)
    LinearLayout mCollectionContainer;

    @Bind(R.id.draft_script_count)
    TextView mDraftScriptCount;

    @Bind(R.id.image_is_vip)
    ImageView mImageViewVip;

    @Bind(R.id.personal_bg)
    ImageView personal_bg;

    @Bind(R.id.creations_count)
    TextView mCreationsCount;

    @Bind(R.id.special_count)
    TextView mSpecialCount;

    @Bind(R.id.friend_cicrle_count)
    TextView mFriendCicrleCount;

    @Bind(R.id.collect_count)
    TextView mCollectCount;

    @Bind(R.id.fl_works)
    FrameLayout mFlWorks;
    @Bind(R.id.fl_material)
    FrameLayout mFlMaterial;
    @Bind(R.id.fl_script)
    FrameLayout mFlScript;
    @Bind(R.id.fl_collage)
    FrameLayout mFlCollage;


    private long mTargetUserId;
    private TextView textViewWork;
    private TextView textViewSuCai;
    private TextView textViewSctipt;
    private TextView textViewAcademy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.layout_personal);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolbar();

        initView();
        //获取用户数据
        mTargetUserId = getIntent().getLongExtra(UID, -1);
        //异步获取网络数据
        AccountApiHelper.getUserInfoData(mTargetUserId, this);

        //异步联网请求看是否当前用户关注了被查看的用户
        PersonApiHelper.isAttentioned(mTargetUserId, this);

        setListener();
    }

    private void initView() {
        textViewWork = (TextView) findViewById(R.id.work_num);
        textViewSuCai = (TextView) findViewById(R.id.suicai_num);
        textViewSctipt = (TextView) findViewById(R.id.script_num);
        textViewAcademy = (TextView) findViewById(R.id.academy_num);
    }

    private void setListener() {
        mAddAttention.setOnClickListener(addAttentionClickListener);
        mDraftScriptContainer.setOnClickListener(onTabClickListener);
        mCreationsContainer.setOnClickListener(onTabClickListener);
        mSpecialContainer.setOnClickListener(onTabClickListener);
        mFriendCicrlceContainer.setOnClickListener(onTabClickListener);
        mCollectionContainer.setOnClickListener(onTabClickListener);

        mFlWorks.setOnClickListener(onCardClickListener);
        mFlMaterial.setOnClickListener(onCardClickListener);
        mFlScript.setOnClickListener(onCardClickListener);
        mFlCollage.setOnClickListener(onCardClickListener);

        mPrivateChat.setOnClickListener(privateMessageClickListener);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AccountApiHelper.getUserInfoData(mTargetUserId, PersonalActivity.this);
        }
    };
    private View.OnClickListener onTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.creations_container) {//进入创作
                ActivityUtil.startPersonalWorkActivity(PersonalActivity.this, mTargetUserId,0);
            } else if (v.getId() == R.id.collection_container) {//进入收藏
                ActivityUtil.startCollectionActivity(PersonalActivity.this, mTargetUserId);
            } else if (v.getId() == R.id.friend_cicrlce_container) { //进入圈子
                ActivityUtil.startCirclesActivity(PersonalActivity.this);
            }else if (v.getId() == R.id. special_container) { //进入专辑
                ActivityUtil.startPersonalAlbumActivity(PersonalActivity.this,mTargetUserId,2);
            }else if(v.getId()==R.id.draft_script_container){ //进入手记
                ActivityUtil.startNotesActivity(PersonalActivity.this,mTargetUserId,0);
            }
        }
    };

    private View.OnClickListener onCardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.fl_works) {//进入作品
                ActivityUtil.startPersonalWorkActivity(PersonalActivity.this, mTargetUserInfo.id,0);
            } else if (id == R.id.fl_material) {//进入素材
                ActivityUtil.startPersonalWorkActivity(PersonalActivity.this, mTargetUserInfo.id,1);
            } else if (id == R.id.fl_script) {//剧本
                GaiaApp.showToast("敬请期待！");
//                ActivityUtil.startPersonalWorkActivity(PersonalActivity.this, mTargetUserInfo.id,3);
            } else if (id == R.id.fl_collage) {//学院
                ActivityUtil.startPersonalWorkActivity(PersonalActivity.this, mTargetUserInfo.id,2);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        BroadcastUtils.registerUserInfoChangedBroadcast(this, receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private View.OnClickListener privateMessageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityUtil.startContactChatActivity(PersonalActivity.this,mTargetUserInfo.avatar,String.valueOf(mTargetUserId),mTargetUserInfo.nickName);
        }
    };

    private View.OnClickListener addAttentionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //添加，取消对该用户的关注
            if (mEventIsAttention != null) {
                if (mEventIsAttention.getA() == 0) {//未关注
                    PersonApiHelper.addAttention(mTargetUserId, 1, PersonalActivity.this);
                } else if (mEventIsAttention.getA() == 1) {//已关注，则取消关注
                    PersonApiHelper.addAttention(mTargetUserId, 0, PersonalActivity.this);
                }
            }
        }
    };
    private UserInfo mTargetUserInfo;

    /**
     * 关注/取消关注的回调
     *
     * @param eventAddAttention
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddAttention(PersonApiHelper.EventAddAttention eventAddAttention) {
        //更新状态
        if (eventAddAttention.getB() == 1) {
            if (mEventIsAttention.getA() == 0) {
                GaiaApp.showToast(getString(R.string.add_attention_success));
            } else {
                GaiaApp.showToast(getString(R.string.cancel_attention_success));
            }
            PersonApiHelper.isAttentioned(mTargetUserId, this);
            //再一次获取用户数据
            AccountApiHelper.getUserInfoData(mTargetUserId, this);
        }
    }

    /***
     * 获取用户信息的回调
     *
     * @param userInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDataReturned(UserInfo userInfo) {
        mTargetUserInfo = userInfo;
//        if(userInfo.avatar!=null&&!userInfo.avatar.equals("null")){
//            if(!userInfo.avatar.endsWith(".jpg")&&!userInfo.avatar.endsWith(".png")&&!userInfo.avatar.endsWith(".jpeg")){
//                Glide.with(this).load(Configs.COVER_PREFIX+userInfo.avatar+".jpg").placeholder(R.mipmap.ic_avatar_default).into(mAvatar);
//            }else {
//                Glide.with(this).load(Configs.COVER_PREFIX+userInfo.avatar).placeholder(R.mipmap.ic_avatar_default).into(mAvatar);
//            }
//        }else {
            Glide.with(this).load(Configs.COVER_PREFIX+userInfo.avatar).placeholder(R.mipmap.ic_avatar_default).into(mAvatar);
//        }
        Glide.with(this).load(Configs.COVER_PREFIX+userInfo.background).into(personal_bg);

//        ImageUtils.getInstance(this).getAvatar(mAvatar, userInfo.avatar);
        mUserName.setText(userInfo.nickName);

        if(userInfo.isVip==1){
            if(userInfo.vipLevel==2){
                mImageViewVip.setImageResource(R.mipmap.pro);
            }else if(userInfo.vipLevel==3){
                mImageViewVip.setImageResource(R.mipmap.advs);
            }else if(userInfo.vipLevel==4){
                mImageViewVip.setImageResource(R.mipmap.bns);
            }
        }

        mRealAddress.setText(userInfo.address);
        mUserInfos.setText(userInfo.createCount+userInfo.collegeCount+userInfo.materialCount+getString(R.string.creations)+" "+
                StringUtil.getInstance().setNum(Integer.valueOf(userInfo.browseCount))+getString(R.string.visit) +" "+
                StringUtil.getInstance().setNum(Integer.valueOf(userInfo.fansCount))+getString(R.string.fans)  +" "+  "0"+
                getString(R.string.draft_script));
        mUserSignature.setText(getString(R.string.signature) +":"+ userInfo.signature);
        mToolbar.setTitle(userInfo.nickName);

        //各类型的数量
        mDraftScriptCount.setText(userInfo.noteCount+"");
        mCreationsCount.setText(String.valueOf(userInfo.createCount+userInfo.collegeCount+userInfo.materialCount/*+userInfo.works_collectCount*/));
        mSpecialCount.setText(userInfo.albumCount+"");
        mFriendCicrleCount.setText(String.valueOf(userInfo.circleCount));
        mCollectCount.setText(String.valueOf(userInfo.works_collectCount+userInfo.college_collect));
        updateStates();
        textViewWork.setText(userInfo.createCount+getApplicationContext().getResources().getString(R.string.works)+userInfo.works_collectCount+getApplicationContext().getResources().getString(R.string.collection));
        textViewAcademy.setText(userInfo.collegeCount+getApplicationContext().getResources().getString(R.string.collage)+userInfo.college_collect+getApplicationContext().getResources().getString(R.string.collection));
        textViewSuCai.setText(userInfo.materialCount+getApplicationContext().getResources().getString(R.string.material)+userInfo.college_collect+getApplicationContext().getResources().getString(R.string.collection));
    }

    PersonApiHelper.EventIsAttention mEventIsAttention;

    /**
     * 是否已关注的回调
     *
     * @param eventIsAttention
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventIsAttention(PersonApiHelper.EventIsAttention eventIsAttention) {
        mEventIsAttention = eventIsAttention;
        updateStates();
    }

    private void initToolbar() {
        mToolbar.inflateMenu(R.menu.menu_personal);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == android.R.id.home) {
                    finish();
                } else if (itemId == R.id.action_edit_personal) {
                    //进入个人资料编辑界面
                    startActivity(new Intent(PersonalActivity.this, UserInfoActivity.class));
                }
                return true;
            }
        });
    }

    /**
     * 更新控件状态
     */
    public void updateStates() {
        //判断此主页是否是当前登陆用户的主页
            if (GaiaApp.getAppInstance().getUserInfo().id == mTargetUserInfo.id) {
                //隐藏按钮 私信和关注
                mPrivateChat.setVisibility(View.INVISIBLE);
                mAddAttention.setVisibility(View.INVISIBLE);
            } else {
                //显示
                mPrivateChat.setVisibility(View.VISIBLE);
                mAddAttention.setVisibility(View.VISIBLE);
            }

            if (mEventIsAttention != null) {
                if (mEventIsAttention.getB() == 1 && mEventIsAttention.getA() == 1) {//关注了
                    mAddAttention.setBackgroundResource(R.drawable.shape_radius4_redstroke_whitebg);
                    mAddAttention.setText(R.string.have_attention);
                } else if (mEventIsAttention.getB() == 1 && mEventIsAttention.getA() == 0) {//还没关注
                    mAddAttention.setBackgroundResource(R.drawable.shape_btn_style3);
                    mAddAttention.setText(R.string.add_attention);
                }
            }

    }
}
