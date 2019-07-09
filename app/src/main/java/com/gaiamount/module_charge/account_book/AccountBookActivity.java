package com.gaiamount.module_charge.account_book;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gaiamount.R;

/**
 * Created by haiyang-lu on 16-5-20.
 * 账本界面
 */
public class AccountBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.account_book_vp);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.account_book_tl);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private String[] titles = new String[]{"收入记录","支出记录"};
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0) {
                GetAccountBookFrag getAccountBookFrag = GetAccountBookFrag.newInstance();
                return getAccountBookFrag;
            }else {
                OutAccountBookFrag outAccountBookFrag = OutAccountBookFrag.newInstance();
                return outAccountBookFrag;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
