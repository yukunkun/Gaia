package com.gaiamount.module_down_up_load.upload.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_down_up_load.upload.WorkInfoAdpater;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;


/**
 * Created by haiyang-lu on 16-3-17.
 * 添加唉作品信息页面
 */
public class WorkInfoFrag extends BaseUploadFrag{

    Context context;
    private ListView mListView;

    private UpdateWorksBean.ABean a = GaiaApp.getAppInstance().getUpdateWorksBean().getA();
    private String[] names;
    private String[] data = {"","","","","","","",""};
    private WorkInfoAdpater mAdapter;
    private InputMethodManager imm;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        names = context.getResources().getStringArray(R.array.work_info_list);
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void setTitle(TextView title) {
        title.setText(getResources().getString(R.string.work_info));
    }

    @Override
    protected void restore() {
        UpdateWorksBean updateWorksBean = GaiaApp.getAppInstance().getUpdateWorksBean();
        UpdateWorksBean.ABean a = updateWorksBean.getA();
        if (a!=null) {
            data[0] = a.getName();
            data[1] = a.getPhotographer();
            data[2] = a.getCutter();
            data[3] = a.getColorist();
            data[4] = a.getDirector();
            data[5] = a.getMachine();
            data[6] = a.getLens();
            data[7] = a.getAddress();
        }
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setSaveBtn(TextView saveBtn) {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToABean();
                Log.d("作品信息", a.toString());
                GaiaApp.getAppInstance().getUpdateWorksBean().setA(a);
                onFragmentStateChangeListener.onFinish(UploadType.WORK_INFO);
            }
        });
    }

    @Override
    protected View getContent() {
        return getInflater().inflate(R.layout.fragment_work_info,null);
    }

    @Override
    protected void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview);
        mAdapter = new WorkInfoAdpater(getActivity(), names, data);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(onItemClickListener);
    }

    private void addToABean() {
        a.setName(data[0]);
        a.setPhotographer(data[1]);
        a.setCutter(data[2]);
        a.setColorist(data[3]);
        a.setDirector(data[4]);
        a.setMachine(data[5]);
        a.setLens(data[6]);
        a.setAddress(data[7]);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final EditText editText = new EditText(getActivity());
            editText.setHint("请输入对应信息");

            AlertDialog ad = new AlertDialog.Builder(getActivity())
                .setTitle("请设置")
                .setView(editText)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = editText.getText().toString();
                        data[position] = s;
                        mAdapter.notifyDataSetChanged();
                    }
                })
            .create();

            ad.show();

            //显示输入法
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(editText.getWindowToken(),InputMethodManager.SHOW_IMPLICIT);

        }
    };
}
