package com.gaiamount.gaia_main.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.empty_hint)
    TextView mEmptyHint;
    private HistoryDBManager mManager;
    private HistoryListAdapter mAdapter;
    private List<OnEventHistory> mList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        mTitle.setText(R.string.history);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_history);
        mToolbar.setOnMenuItemClickListener(listener);

        mManager = new HistoryDBManager(this);
        mList = mManager.query();
        mProgressBar.setVisibility(View.GONE);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HistoryListAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
        update();
    }

    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_clear_history) {
                if (mManager != null) {
                    createDialog().show();
                }
            }
            return true;
        }
    };

    private AlertDialog.Builder createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
        builder.setMessage(R.string.sure_to_clear_history)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mManager.clear();
                        GaiaApp.showToast(getString(R.string.clear_history_success));
                        mList.clear();
                        update();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ignore
                    }
                }).create();
        return builder;
    }


    private void update() {
        if (mList!=null&&mList.size() == 0) {
            mEmptyHint.setVisibility(View.VISIBLE);
        } else {
            mEmptyHint.setVisibility(View.INVISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放数据库资源
        mManager.closeDB();
    }
}
