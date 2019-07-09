package com.gaiamount.module_user.personal_album.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.dialog.AlbumPwdValidateDialog;
import com.gaiamount.module_creator.sub_module_album.AlbumCreationType;
import com.gaiamount.module_creator.sub_module_album.AlbumDetail;
import com.gaiamount.module_creator.sub_module_album.AlbumDetailMoreActivity;
import com.gaiamount.module_creator.sub_module_group.constant.GroupPower;
import com.gaiamount.module_user.personal_album.fragment.AlbumPersonCollageFrag;
import com.gaiamount.module_user.personal_album.fragment.AlbumPersonMaterialFrag;
import com.gaiamount.module_user.personal_album.fragment.AlbumPersonWorkFrag;
import com.gaiamount.util.ShareUtil;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 专辑详情 包括专辑的封面等信息，专辑的作品
 * 作品类型{@link AlbumCreationType}
 */
public class PersonAlbumDetailActivity extends AppCompatActivity implements AlbumPwdValidateDialog.OnContactWithActivity {

    public static final String AID = "aid";
    public static final String ALBUM_TYPE = "album_type";
    public static final String IS_PUBLIC = "is_public";
    public static final String GID = "gid";
    public int allowCleanCreation;

    @Bind(R.id.title)
    TextView mTV_Title;
    /**
     * toolbar控件
     */
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    /**
     * 专辑封面
     */
    @Bind(R.id.album_cover)
    ImageView mIV_AlbumCover;
    /**
     * 专辑描述
     */
    @Bind(R.id.album_desc)
    TextView mTV_AlbumDesc;
    /**
     * tabLayout控件
     */
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    /**
     * 分页控件
     */
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    /**
     * 时间
     */
    @Bind(R.id.album_time)
    TextView mTextViewTime;

    @Bind(R.id.album_more)
    ImageView mImageViewMore;
    /**
     * 专辑id
     */
    private long mAid;
    /**
     * 专辑类型，有小组或者个人
     */
    private int mAlbumType;
    /**
     * 是否公开,如果是加密视频，费小组成员需要输入密码才能访问
     */
    private int mIsPublic;
    /**
     * 当前访问者是否是小组成员
     */
    private int mIsJoin;
    private int mHavePower;
    private long mGid;
    private AlbumDetail albumDetail;
    private String pwds="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        ButterKnife.bind(this);

        mAid = getIntent().getLongExtra(AID, -1);
        mAlbumType = getIntent().getIntExtra(ALBUM_TYPE, -1);
        mIsPublic = getIntent().getIntExtra(IS_PUBLIC, -1);
        pwds = getIntent().getStringExtra("pwd");
        mGid=getIntent().getLongExtra("gid",-1);

        if(pwds==null){
            pwds="";
        }
        setToolbar();
        setListener();

        if (mIsPublic == 0) {//加密且非小组成员
            //弹出对话框
//            AlbumPwdValidateDialog albumPwdValidateDialog = AlbumPwdValidateDialog.newInstance(mAid);
//            albumPwdValidateDialog.show(getSupportFragmentManager(), "album_pwd");
//            albumPwdValidateDialog.setOnContactWithActivity(this);

        } else {
            getDetails(null);
        }
        //传递密码.保证能够获取到值
        if(!pwds.equals("")){
            getDetails(pwds);
        }
    }

    private void setListener() {
        mImageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonAlbumDetailActivity.this,AlbumDetailMoreActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("Info",albumDetail);
                intent.putExtras(bundle);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
            }
        });
    }

    public void prepareFragments() {
        //准备fragment
        Info info=new Info();
        info.gid=mGid;
        GroupPower groupPower= new GroupPower(-1,mHavePower,allowCleanCreation,-1);//allowExamine,未传 .allowCleanMember未传
        info.groupPower=groupPower;
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(AlbumPersonWorkFrag.newInstance(mAid, 0,info));
        fragmentList.add(AlbumPersonMaterialFrag.newInstance(mAid, 0, info));
        fragmentList.add(AlbumPersonCollageFrag.newInstance(mAid, 0,info, AlbumCreationType.WORK));
//        fragmentList.add(AlbumPersonScriptFrag.newInstance(mAid, 0, info));
        mViewPager.setAdapter(new MAdapter(getSupportFragmentManager(), fragmentList));
        //关联
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);
    }

    public int isHavePower() {
        return mHavePower;
    }


    /**
     * 设置toolbar事件
     */
    private void setToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (mHavePower == 1) {
            mToolbar.inflateMenu(R.menu.album_detail);
        } else {
            mToolbar.inflateMenu(R.menu.share);
        }
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_delete_album) {
                    //删除专辑
                    MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(PersonAlbumDetailActivity.class){
                        @Override
                        public void onGoodResponse(JSONObject response) {
                            super.onGoodResponse(response);
                            GaiaApp.showToast("专辑已删除");
                            finish();
                        }
                    };
                    AlbumApiHelper.deleteAlbum(mAid,PersonAlbumDetailActivity.this,handler);
                } else if (id == R.id.action_share_album) {

                    ShareUtil.newInstance(PersonAlbumDetailActivity.this).share("Gaiamount",albumDetail.getContent(),
                            Configs.COVER_PREFIX +albumDetail.getBgImg(),"https://gaiamount.com/profile/"+GaiaApp.getAppInstance().getUserInfo().id);
                }
                return true;
            }
        });
    }

    /**
     * @param pi
     * @param ps
     * @param handler
     */
    public void getAlbumWorks(int c, int pi, int ps, MJsonHttpResponseHandler handler) {
        AlbumApiHelper.searchAlbumWorks(mAid,1,c,0,0,pi,8,this,handler);
//        AlbumApiHelper.searchAlbumWorks(49, 1, c, 0, 0, 1, 100, this, handler);
    }

    @Override
    public void onOKButtonClicked(final DialogFragment dialogFragment, String pwd) {
        if(pwd!=null){
            getDetails(pwd);
            dialogFragment.dismiss();
        }
    }

    private void getDetails(String pwd) {
        //验证密码是否正确，同时如果正确，返回专辑详情
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(PersonAlbumDetailActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONObject a = response.optJSONObject("o");
                albumDetail = GsonUtil.getInstannce().getGson().fromJson(a.toString(), AlbumDetail.class);
                setUpView(albumDetail);
                prepareFragments();
            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                
            }
        };
        AlbumApiHelper.getAlbumDetail(mAid, pwd, PersonAlbumDetailActivity.this, handler);
    }

    /**
     * 填充视图数据
     *
     * @param albumDetail
     */
    private void setUpView(AlbumDetail albumDetail) {
        mTextViewTime.setText("作品 " + albumDetail.getWorksCount() + "|" +
                "素材 " + albumDetail.getMaterialCount() + "|" +
               /* "剧本 " + albumDetail.getScriptCount() + "|" +*/
                "学院 " + albumDetail.getCourseCount());
        //专辑图片
        Glide.with(this).load(Configs.COVER_PREFIX+albumDetail.getBgImg()).placeholder(R.mipmap.bg_general).into(mIV_AlbumCover);
        //专辑描述
        mTV_AlbumDesc.setText(albumDetail.getContent());

    }


    class MAdapter extends FragmentStatePagerAdapter {

        private String[] mStrings;
        private List<Fragment> mFragmentList;

        public MAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
            mStrings = getResources().getStringArray(R.array.album_type);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStrings[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mStrings.length;
        }
    }
}
