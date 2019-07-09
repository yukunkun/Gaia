package com.gaiamount.module_creator;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_group.constant.GroupPower;
import com.gaiamount.module_creator.sub_module_group.creations.College;
import com.gaiamount.module_creator.sub_module_group.creations.Material;
import com.gaiamount.module_creator.sub_module_group.creations.Product;
import com.gaiamount.module_creator.sub_module_group.creations.Script;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LHY
 * 小组中的创作
 */
public class GroupCreationsActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 233;
    public static final String GID = "mGid";
    public static final String POS = "position";

    public static final String ALLOW_MANAGE_SPECIAL = "allow_manage_special";
    public static final String MEMBER_TYPE = "member_type";

    public static String ALLOW_CLEAN_CREATION = "allow_clean_creation";

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    /**
     * 视频id
     */
    private long vid = 0;
    /**
     * 小组id
     */
    private long mGid;
    /**
     * 视频类型（0作品 1剧本 2学院）
     */
    private int vType;
    /**
     * 是否允许清理作品
     */
    private int mAllowCleanCreation;
    /**
     * 是否允许加入视频到专辑中
     */
    private int mAllowManageSpecial;
    /**
     * 成员类型
     * @see com.gaiamount.module_creator.sub_module_group.constant.MemberKind
     */
    private int mMemberType;
    private List<Fragment> mFragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creations);
        ButterKnife.bind(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化fragment
        //获取gid
        mGid = getIntent().getLongExtra(GID, -1);
        //获取是否有管理(清理）视频的权限
        mAllowCleanCreation = getIntent().getIntExtra(ALLOW_CLEAN_CREATION,-1);
        //添加视频到专辑中的权限
        mAllowManageSpecial = getIntent().getIntExtra(ALLOW_MANAGE_SPECIAL,-1);
        //当前用户在小组中的成员类型
        mMemberType = getIntent().getIntExtra(MEMBER_TYPE,-1);

        prepareFragments();
        //设置适配器
        mViewPager.setAdapter(new MViewPagerAdapter(getSupportFragmentManager()));

        mViewPager.setOffscreenPageLimit(4);
        //关联tabLayout
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void prepareFragments() {
        Info info = new Info();
        info.gid = mGid;
        info.memberType = mMemberType;
        info.groupPower = new GroupPower(0,mAllowManageSpecial,mAllowCleanCreation,0);
        //作品池
        mFragmentList.add(Product.newInstance(info));
        //素材
        mFragmentList.add(Material.newInstance(info));
        //学院
        mFragmentList.add(College.newInstance(info));
        //剧本
//        mFragmentList.add(Script.newInstance(info));
    }


    class MViewPagerAdapter extends FragmentStatePagerAdapter {
        private String[] title;

        public MViewPagerAdapter(FragmentManager fm) {
            super(fm);
            title = getResources().getStringArray(R.array.create_person);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

}
