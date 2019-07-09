package com.gaiamount.module_down_up_load.download;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.BaseActionBarActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DownloadActivity extends BaseActionBarActivity implements View.OnClickListener /*,DownloadAsyncTask.OnTaskListChanged*/{

    private DownloadListAdapter mAdapter;
    private List<DownloadItem> mCheckedList = new ArrayList<>();
    private Button mCancelEdit;
    private Button mEditList;
    private Button mDelete;
    private Button mSelectAll;
    private ListView mDownloadList;

    @Bind(R.id.empty_hint_text)
    TextView tvEmptyHintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(R.string.download_set);
        initView();

        //从本地获取视频列表
//        getVideoListFromLocal();
        //设置长按监听
        getdownloadData();
        mDownloadList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //更新
                mCheckedList.get(position).isChecked = true;
                showCheckList();
                return true;
            }
        });
    }

    /**
     * 获取传入的Intent中的数据，如果有的话
     */
    private void getdownloadData() {
        List<DownloadItem> taskList = DownloadUtil.getInstance(this).getTaskList();
        if (taskList!=null&&taskList.size()>0) {
            tvEmptyHintText.setVisibility(View.GONE);
            mCheckedList.addAll(taskList);
            mAdapter = new DownloadListAdapter(this,mCheckedList);
            mDownloadList.setAdapter(mAdapter);

        }else if(taskList!=null&&taskList.size()==0) {
            tvEmptyHintText.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        mEditList = (Button) findViewById(R.id.download_edit_list);
        mCancelEdit = (Button) findViewById(R.id.download_cancel_edit);
        mDelete = (Button) findViewById(R.id.download_item_delete);
        mSelectAll = (Button) findViewById(R.id.download_selecte_all);
        mDownloadList = (ListView) findViewById(R.id.download_list);


        mEditList.setOnClickListener(this);
        mCancelEdit.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
    }

    private void getVideoListFromLocal() {
        mCheckedList = new ArrayList<>();
        // TODO: 16-5-17 从本地获取视频列表

        final Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        final String[] projection = new String[]{
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA,//绝对路径
                MediaStore.Video.Media.MIME_TYPE,
        };
        final ProgressDialog dialog = ProgressDialog.show(DownloadActivity.this, "", "");
        new AsyncTask<Void, Void, List<DownloadItem>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dialog.show();
            }

            @Override
            protected List<DownloadItem> doInBackground(Void... params) {
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                if (cursor == null) {
                    return null;
                }
                List<DownloadItem> checkedList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(0);
                    long duration = cursor.getLong(1);
                    long size = cursor.getLong(2);
                    String data = cursor.getString(3);
                    String mime = cursor.getString(4);
                    DownloadItem downloadItem = new DownloadItem(false, false, data, displayName, mime, size, duration);
                    checkedList.add(downloadItem);
                }
                return checkedList;
            }

            @Override
            protected void onPostExecute(List<DownloadItem> list) {
                super.onPostExecute(list);
                mCheckedList.addAll(list);
                mAdapter = new DownloadListAdapter(DownloadActivity.this, mCheckedList);
                mDownloadList.setAdapter(mAdapter);

                dialog.dismiss();

                //获取Intent中的数据
                getdownloadData();
            }
        }.execute();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.download_edit_list:
                if (mCheckedList.size() == 0) {
                    GaiaApp.showToast(getString(R.string.empty_list));
                    return;
                }
                showCheckList();
                break;
            case R.id.download_cancel_edit:
                hideCheckList();
                break;
            case R.id.download_item_delete:
                deleteChoosedItem();
                break;
            case R.id.download_selecte_all:
                selectAll();
                break;
            default:
                break;
        }
    }

    /**
     * 列表全选
     */
    private void selectAll() {
        for (int i = 0; i < mCheckedList.size(); i++) {
            mCheckedList.get(i).isChecked = true;
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 删除选中条目的视频
     */
    private void deleteChoosedItem() {
        final List<DownloadItem> tempList = new ArrayList<>();
        tempList.addAll(mCheckedList);

        int count = 0;
        for (int i = 0; i < mCheckedList.size(); i++) {
            DownloadItem downloadItem = mCheckedList.get(i);
            if (downloadItem.isChecked) {
                count++;
                tempList.remove(downloadItem);
            }
        }
        if (count == 0) {
            GaiaApp.showToast(getString(R.string.empty_reject));
            return;
        } else {
            //提示用户是否确认删除
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (StringUtil.getInstance().isZh(this)) {
                builder.setTitle(getString(R.string.delete)).setMessage("是否删除选中的" + count + "个" + "视频");
            } else {
                builder.setTitle(getString(R.string.delete)).setMessage("choosed videos count:" + count + " are you sure to delete?");
            }
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    hideCheckList();
                    for (int i = 0; i < mCheckedList.size(); i++) {
                        DownloadItem downloadItem = mCheckedList.get(i);
                        if (downloadItem.isChecked) {
                            DownloadUtil.getInstance(getApplicationContext()).deleteTask(Uri.parse(downloadItem.url));
                            GaiaApp.showToast("删除成功");
                        }
                    }

                    mCheckedList = tempList;
                    mAdapter.setCheckedLists(mCheckedList);
                    mAdapter.notifyDataSetChanged();
                }
            })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            builder.show();
        }


    }

    /**
     * 隐藏编辑框
     */
    private void hideCheckList() {
        for (int i = 0; i < mCheckedList.size(); i++) {
            mCheckedList.get(i).isShow = false;
        }
        mAdapter.notifyDataSetChanged();
        //显示取消
        mCancelEdit.setVisibility(View.GONE);
        mSelectAll.setVisibility(View.GONE);
        mDelete.setVisibility(View.GONE);

    }

    /**
     * 显示编辑框
     */
    private void showCheckList() {
        for (int i = 0; i < mCheckedList.size(); i++) {
            mCheckedList.get(i).isShow = true;
        }
        mAdapter.notifyDataSetChanged();
        //显示可见
        mCancelEdit.setVisibility(View.VISIBLE);
        mSelectAll.setVisibility(View.VISIBLE);
        mDelete.setVisibility(View.VISIBLE);
    }

}
