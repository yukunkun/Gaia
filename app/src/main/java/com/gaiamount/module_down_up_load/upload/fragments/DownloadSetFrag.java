package com.gaiamount.module_down_up_load.upload.fragments;

import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_down_up_load.download.DownloadSetExpandableListAdapter;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.upload_bean.DownloadSetting;
import com.gaiamount.module_down_up_load.upload.upload_bean.Price;
import com.gaiamount.module_down_up_load.upload.upload_bean.TransCoding;
import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;
import com.gaiamount.util.LogUtil;

/**
 * Created by haiyang-lu on 16-3-18.
 * 下载设置的页面
 */
public class DownloadSetFrag extends BaseUploadFrag {
    private ExpandableListView mExListView;
    int pre_position = 0;
    int now_position;
    private int[] tl = new int[]{0,0,0,0,0};//

    private ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            //排除第0个位置
            if(childPosition==0) {
                return false;
            }
            CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.ctv);
            ctv.toggle();
            if (ctv.isChecked()) {
                ctv.setCheckMarkDrawable(R.mipmap.checked);
                tl[childPosition] = 1;
            } else {
                ctv.setCheckMarkDrawable(null);
                tl[childPosition] = 0;
            }
            return true;
        }
    };
    private ExpandableListView.OnGroupClickListener onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            mExListView.collapseGroup(pre_position);
            mExListView.expandGroup(groupPosition, true);
            pre_position = groupPosition;
            return true;
        }
    };
    private ExpandableListView.OnGroupExpandListener onGroupExpandListener = new ExpandableListView.OnGroupExpandListener() {
        @Override
        public void onGroupExpand(int groupPosition) {
            now_position = groupPosition;
            downloadSetting.setAllowDownload(1);
            if (groupPosition == 0) {//免费
                downloadSetting.setPrice(new Price(0, 0, 0, 0, 0));
                downloadSetting.setAllowCharge(0);

            } else {//付费
                downloadSetting.setPrice(DownloadSetFrag.this.downloadSetting.getPrice());
                downloadSetting.setAllowCharge(1);
            }
            tl = new int[]{1,0,0,0,0};
            //不允许下载置为false
            mNot_allow_download.setCheckMarkDrawable(null);
            downloadSetting.setAllowDownload(1);//允许下载
        }
    };
    private CheckedTextView mNot_allow_download;
    private DownloadSetExpandableListAdapter mAdapter;
    private DownloadSetting downloadSetting;

    @Override
    protected void setTitle(TextView title) {
        title.setText(getResources().getString(R.string.download_set));
    }

    @Override
    protected void restore() {
        UpdateWorksBean updateWorksBean = GaiaApp.getAppInstance().getUpdateWorksBean();

        downloadSetting = new DownloadSetting();
        UpdateWorksBean.ABean a = updateWorksBean.getA();
        downloadSetting.setAllowDownload(a.getAllowDownload());
        downloadSetting.setAllowCharge(a.getAllowCharge());
        downloadSetting.setTransCoding(new TransCoding(a.getHave4k(),a.getHave2k(),a.getHave1080(),a.getHave720()));

        int allowDownload = a.getAllowDownload();
        int allowCharge = a.getAllowCharge();

        if (allowDownload==0) {
            setNotDownload();
        }else {
            mExListView.setAdapter(new DownloadSetExpandableListAdapter(getActivity(), downloadSetting));
            if (allowCharge==1) {
                mExListView.expandGroup(1,true);
            }else {
                mExListView.expandGroup(0,true);
            }

        }
    }


    @Override
    protected void setSaveBtn(TextView saveBtn) {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadSetExpandableListAdapter adapter = (DownloadSetExpandableListAdapter) mExListView.getExpandableListAdapter();
                DownloadSetting downloadSetting = adapter.getDownloadSetting();

                //保存转码设置
                TransCoding transCoding = new TransCoding(tl[1],tl[2],tl[3],tl[4]) ;
                downloadSetting.setTransCoding(transCoding);

                LogUtil.i(DownloadSetFrag.class,"下载设置:",downloadSetting.toString());
                UpdateWorksBean.ABean a = GaiaApp.getAppInstance().getUpdateWorksBean().getA();
                LogUtil.d(DownloadSetFrag.class,a.toString());

                a.setAllowDownload(downloadSetting.getAllowDownload());
                a.setAllowCharge(downloadSetting.getAllowCharge());
                a.setPriceOriginal(downloadSetting.getPrice().getPriceOriginal());
                a.setPrice4K(downloadSetting.getPrice().getPrice4k());
                a.setPrice2K(downloadSetting.getPrice().getPrice2k());
                a.setPrice1080(downloadSetting.getPrice().getPrice1080());
                a.setPrice720(downloadSetting.getPrice().getPrice720());

                GaiaApp.getAppInstance().getUpdateWorksBean().setA(a);

                onFragmentStateChangeListener.onFinish(UploadType.DOWNLOAD_SET);
            }
        });
    }

    @Override
    protected View getContent() {
        return getInflater().inflate(R.layout.fragment_download_set,null);
    }

    @Override
    protected void initView(View content) {
        //实例化对象
        mExListView = (ExpandableListView) content.findViewById(R.id.expandable_listview);
        if(downloadSetting==null) {
            downloadSetting = new DownloadSetting();
        }
        //设置适配器
        mAdapter = new DownloadSetExpandableListAdapter(getActivity(), downloadSetting);
        mExListView.setAdapter(mAdapter);
        //设置组的点击事件
        mExListView.setOnGroupClickListener(onGroupClickListener);
        //设置孩子的点击事件
        mExListView.setOnChildClickListener(onChildClickListener);
        //默认展开第一个group
        mExListView.expandGroup(0, true);

        mExListView.setOnGroupExpandListener(onGroupExpandListener);

        mNot_allow_download = (CheckedTextView) content.findViewById(R.id.not_allow_download);

        mNot_allow_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNot_allow_download.toggle();
                if (mNot_allow_download.isChecked()) {
                    setNotDownload();
                }
            }
        });
    }

    public void setNotDownload() {
        mNot_allow_download.setCheckMarkDrawable(R.mipmap.checked);
        downloadSetting.setAllowDownload(0);//不允许下载

        //收缩二级列表
        mExListView.collapseGroup(0);
        mExListView.collapseGroup(1);

        //tl
        tl = new int[]{0,0,0,0,0};
    }



}
