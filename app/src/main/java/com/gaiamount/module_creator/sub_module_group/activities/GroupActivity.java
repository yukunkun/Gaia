package com.gaiamount.module_creator.sub_module_group.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.GroupCreationsActivity;
import com.gaiamount.module_creator.beans.GroupDetailInfo;
import com.gaiamount.module_creator.sub_module_album.AlbumType;
import com.gaiamount.module_creator.sub_module_group.constant.MemberKind;
import com.gaiamount.module_creator.sub_module_group.fragment.GroupMainPageFrag;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create By luxiansheng
 * 小组主页
 */
public class GroupActivity extends AppCompatActivity {
    public static String GID = "gid";

    /**
     * 界面title
     */
    @Bind(R.id.group_custom_title)
    TextView mCustomTitle;
    /**
     * 界面toolbar控件
     */
    @Bind(R.id.group_toolbar)
    Toolbar mGroupToolbar;
    @Bind(R.id.creation_visit_member)
    TextView mCreationVisitMember;
    @Bind(R.id.username_time)
    TextView mUsernameTime;
    @Bind(R.id.group_description)
    TextView mGroupDescription;

    @Bind(R.id.group_tab_layout)
    TabLayout mGroupTabLayout;
    @Bind(R.id.main_page_container)
    FrameLayout mMainPageContainer;

    @Bind(R.id.group_top_bg)
    CollapsingToolbarLayout mGroupTopBg;
    @Bind(R.id.group_join)
    Button mGroupJoin;
    @Bind(R.id.group_joined)
    TextView mGroupJoined;
    @Bind(R.id.group_more_detail)
    TextView mGroupMoreDetail;

    @Bind(R.id.group_covers)
    ImageView mImageView;

    @Bind(R.id.icon_manager)
    ImageView mManagerView;
    /**
     * 小组id
     */
    private long mGid;
    /**
     * 小组-主页的片段
     */
    private GroupMainPageFrag mGroupMainPageFrag;
    /**
     * 小组详细信息
     */
    private GroupDetailInfo mGroupDetailInfo;
    /**
     * 管理员类型 0 非成员 1 普通成员 2 管理员 3小组长
     */
    private int memberType = MemberKind.VISITOR;

    /**
     * 当前用户是否加入了小组 (0审核中 1小组成员 2非小组成员）
     * {@link com.gaiamount.module_creator.beans.GroupDetailInfo.OBean.UserBean#isJoin}
     */
    private int mIsJoin;
    private int mIsPublic;
    private int allowExamine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        mGid = getIntent().getLongExtra(GID, -1);

        mGroupToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initTabLayout();

        getDetailGroupInfo();
        //审核的请求
        getInfo();
    }

    /**
     * 设置tabLayout中的tab点击事件，主tab的fragment
     */
    public void initTabLayout() {


        //点击tab创作

        mGroupTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabClick(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setTabClick(tab);
            }
        });
    }

    private void setTabClick(TabLayout.Tab tab) {
        if (mGroupDetailInfo!=null) {
            if (tab.getPosition() == 1) {
                Intent intent = new Intent(GroupActivity.this, GroupCreationsActivity.class);
                intent.putExtra(GroupCreationsActivity.ALLOW_MANAGE_SPECIAL,mGroupDetailInfo.getO().getUser().getAllowManagerSpecial());
                intent.putExtra(GroupCreationsActivity.ALLOW_CLEAN_CREATION,mGroupDetailInfo.getO().getUser().getAllowCleanCreation());
                intent.putExtra(GroupCreationsActivity.MEMBER_TYPE,memberType);
                intent.putExtra(GroupCreationsActivity.GID, mGid);
                startActivity(intent);
            } else if (tab.getPosition() == 2) {
                int allowManagerSpecial = mGroupDetailInfo.getO().getUser().getAllowManagerSpecial();
                int allowCleanCreation = mGroupDetailInfo.getO().getUser().getAllowCleanCreation();
                ActivityUtil.startAlbumActivity(mGid, AlbumType.TYPE_GROUP, allowManagerSpecial, allowCleanCreation,mIsJoin, GroupActivity.this);
            } else if ((tab.getPosition() == 3)) {
                Intent intent = new Intent(GroupActivity.this, GroupMemberActivity.class);
                intent.putExtra(GroupMemberActivity.GID, mGid);
                intent.putExtra(GroupMemberActivity.RIGHTS, mGroupDetailInfo.getO().getUser().getAllowCleanCreation());
                intent.putExtra(GroupMemberActivity.MEMBER_TYPE, mGroupDetailInfo.getO().getUser().getMemberType());
                startActivity(intent);
            }
        }
    }

    /**
     * 联网获取小组的详细信息，必须等fragment的视图创建完成后调用
     */
    public void getDetailGroupInfo() {
        JSONObject response = new JSONObject();
        response.optJSONObject("o");
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                mGroupDetailInfo = GsonUtil.getInstannce().getGson().fromJson(response.toString(), GroupDetailInfo.class);
                //对isJoin赋值
                mIsJoin = mGroupDetailInfo.getO().getUser().getIsJoin();

                //传入GroupMainFragment
                mGroupMainPageFrag = GroupMainPageFrag.newInstance(mGroupDetailInfo);

                //设置视图
                setUpView();
                //显示下部分信息
                getSupportFragmentManager().beginTransaction().replace(R.id.main_page_container, mGroupMainPageFrag).commitAllowingStateLoss();
            }
        };
        long uid = GaiaApp.getUserInfo().id;
        GroupApiHelper.getGroupDetail(uid, mGid, "", this, jsonHttpResponseHandler);
    }

    boolean tag=false;//判断有没有审核的成员
    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(GroupExamineActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray a = response.optJSONArray("a");
                if(a!=null&&a.length()!=0){
                    tag=true;
                }
                if(tag&&(memberType==2||memberType==3)){
                    mManagerView.setVisibility(View.VISIBLE);
                }
            }
        };

        GroupApiHelper.getGroupExamine(mGid,1,getApplicationContext(),handler);
    }

    private void setUpView() {
        //小组名称
        GroupDetailInfo.OBean.GroupBean group = mGroupDetailInfo.getO().getGroup();
        GroupDetailInfo.OBean.UserBean user = mGroupDetailInfo.getO().getUser();
        mCustomTitle.setText(group.getName());
        //创作，访问，成员
        mCreationVisitMember.setText(group.getCreationCount() + "创作" + "·" + StringUtil.getInstance().setNum(group.getVisitCount()) + "访问" + "·" + StringUtil.getInstance().setNum(group.getMemberCount()) + "成员");
        //username 创立于××××
        mUsernameTime.setText(user.getNickName() + "创立于" + group.getCreateTime());
        mUsernameTime.setVisibility(View.GONE);
        //小组描述
        mGroupDescription.setText(group.getDescription());

        allowExamine = mGroupDetailInfo.getO().getUser().getAllowExamine();
        //根据当前用户身份动态改变显示内容
        memberType = mGroupDetailInfo.getO().getUser().getMemberType();
        //如果是member
        int isJoin = mGroupDetailInfo.getO().getUser().getIsJoin();
        if (isJoin == 1) {//小组成员
            mGroupJoined.setVisibility(View.VISIBLE);
            mGroupJoin.setText("已加入");
        } else if (isJoin==2){//不是小组成员且未提交申请
            mGroupJoined.setVisibility(View.GONE);
            mGroupJoin.setVisibility(View.VISIBLE);
        }else if(isJoin==0){//为0，待审核状态
            mGroupJoined.setText("待审核");
            mGroupJoined.setVisibility(View.VISIBLE);

        }else if(isJoin==3){
           mGroupJoin.setVisibility(View.VISIBLE);
            mGroupJoined.setVisibility(View.GONE);
        }
        //设置提醒
        if(tag&&(memberType==2||memberType==3)){
            if(mManagerView.getVisibility()==View.GONE){
                mManagerView.setVisibility(View.VISIBLE);
            }
        }
        if (memberType==MemberKind.MONITOR) {//自己创建
            //显示编辑小组资料的菜单选项
            mGroupToolbar.inflateMenu(R.menu.menu_group);

            mGroupToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.action_exit_group) {
                        //退出小组
                        exitGroup();
                    } else if (itemId == R.id.action_edit_group) {
                        //编辑小组资料
                        ActivityUtil.startGroupEditActivity(mGroupDetailInfo, GroupActivity.this);

                    }else if(itemId==R.id.action_manger_group){
                        Intent intent=new Intent(GroupActivity.this,GroupExamineActivity.class);
                        intent.putExtra("gid",mGid);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    return false;
                }
            });
        }

        if(memberType==MemberKind.ADMIN){//管理员

            mGroupToolbar.inflateMenu(R.menu.menu_group);
            Menu menu = mGroupToolbar.getMenu();
            MenuItem menuItemEdit= menu.findItem(R.id.action_edit_group);
            MenuItem menuItemManger= menu.findItem(R.id.action_manger_group);
            MenuItem menuItemExit= menu.findItem(R.id.action_exit_group);
            menuItemExit.setTitle("退出小组");
            menuItemEdit.setVisible(false);
            menuItemManger.setVisible(false);

            if(allowExamine==1){
                menuItemManger.setVisible(true);
            }

            mGroupToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.action_exit_group) {
                        //退出小组
                        exitGroup();
                    }else if(itemId==R.id.action_manger_group){
                        Intent intent=new Intent(GroupActivity.this,GroupExamineActivity.class);
                        intent.putExtra("gid",mGid);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    return false;
                }
            });
        }
        if(memberType==MemberKind.NORMAL){//普通成员
            mGroupToolbar.inflateMenu(R.menu.menu_group);
            Menu menu = mGroupToolbar.getMenu();
            MenuItem menuItemEdit= menu.findItem(R.id.action_edit_group);
            MenuItem menuItemManger= menu.findItem(R.id.action_manger_group);
            MenuItem menuItemExit= menu.findItem(R.id.action_exit_group);
            menuItemExit.setTitle("退出小组");
            menuItemEdit.setVisible(false);
            menuItemManger.setVisible(false);

            mGroupToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.action_exit_group) {
                        //退出小组
                        exitGroup();
                    }
                    return false;
                }
            });
        }

        //背景
        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();//设置宽高
        layoutParams.height=getHeight();
        mImageView.setLayoutParams(layoutParams);

        Glide.with(this).load(Configs.COVER_PREFIX + mGroupDetailInfo.getO().getGroup().getBackground()).into(mImageView);
        //小组背景
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        ImageRequest imageRequest = new ImageRequest(Configs.COVER_PREFIX + mGroupDetailInfo.getO().getGroup().getBackground(),
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        Drawable drawable = new BitmapDrawable(response);
////                        mGroupTopBg.setBackground(drawable);
//                        mImageView.setBackground(drawable);
//                    }
//                }, 750, 450, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //do nothing
//            }
//        });
//        requestQueue.add(imageRequest);
//        requestQueue.start();


    }
    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width/3));
        return itemHeight;
    }
    /**
     * 点击加入小组
     *
     * @param view
     */
    public void clickToJoinGroup(View view) {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                //提示
                GaiaApp.showToast("您已申请加入小组:" + mGroupDetailInfo.getO().getGroup().getName());
                //更新界面
                mGroupJoin.setVisibility(View.GONE);
                mGroupJoined.setVisibility(View.VISIBLE);
                mGroupJoined.setText("申请成功");
                mGroupMoreDetail.setVisibility(View.VISIBLE);

            }

            @Override
            public void onBadResponse(JSONObject response) {
                super.onBadResponse(response);
                int i = response.optInt("i");
                if (i == 31703) {
                    GaiaApp.showToast("小组不存在");
                } else if (i == 32303) {
                    GaiaApp.showToast("您已加入该小组 无须重复加入");
                } else if (response.optInt("i") == 32310) {//审核中
                    GaiaApp.showToast("已发送给管理员审核");
                    mGroupJoin.setVisibility(View.GONE);
                    mGroupJoined.setText("待审核");
                    mGroupJoined.setVisibility(View.VISIBLE);

                    mGroupMoreDetail.setVisibility(View.VISIBLE);
                }
            }
        };
        List<Long> list = new ArrayList<>();
        list.add(mGroupDetailInfo.getO().getGroup().getId());
        GroupApiHelper.joinGroup(list, this, jsonHttpResponseHandler);
    }

    /**
     * 点击查看小组详细信息
     *
     * @param view
     */
    public void clickToSeeGroupInfo(View view) {
        if(mGroupDetailInfo!=null){
            ActivityUtil.startGroupMoreActivity(GroupActivity.this, mGroupDetailInfo);
        }
    }

    /**
     * 退出小组
     */
    private void exitGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
        DialogInterface.OnClickListener posClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupActivity.class) {
                    @Override
                    public void onGoodResponse(JSONObject response) {
                        super.onGoodResponse(response);
                        if (memberType == MemberKind.MONITOR) {
                            //警告
                            GaiaApp.showToast("组长退出即表示解散小组");
                        }

                        GaiaApp.showToast("你已退出该小组");

                        //关闭此activity
                        finish();
                    }

                    @Override
                    public void onBadResponse(JSONObject response) {
                        super.onBadResponse(response);
                        int i = response.optInt("i");
                        if (i == 31804) {
                            GaiaApp.showToast("你不是这个小组的成员");
                        }
                    }
                };
                GroupApiHelper.exitGroup(mGroupDetailInfo.getO().getGroup().getId(), GroupActivity.this, jsonHttpResponseHandler);
            }
        };
        DialogInterface.OnClickListener negClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        };
        builder.setTitle("警告")
                .setMessage("你确定要这么做么")
                .setNegativeButton("取消", negClickListener)
                .setPositiveButton("是的", posClickListener)
                .create();
        builder.show();

    }

}
