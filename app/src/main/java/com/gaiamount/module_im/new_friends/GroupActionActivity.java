package com.gaiamount.module_im.new_friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gaiamount.R;

public class GroupActionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private GroupActionAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_action);
        init();
        initToolbar();
        getInfo();
        setAdapter();
        setListener();

    }
    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.attent_recyclerview);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
            }
        });
    }

    private void getInfo() {

    }

    private void setAdapter() {
        groupAdapter = new GroupActionAdapter(getApplicationContext());
        mRecyclerView.setAdapter(groupAdapter);
    }

    private void setListener() {

    }

}
