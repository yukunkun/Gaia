package com.gaiamount.module_down_up_load.upload.fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_down_up_load.upload.ChooseCategoryAdpater;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.upload.upload_bean.ChooseCategoryItem;
import com.gaiamount.module_down_up_load.upload.upload_bean.UpdateWorksBean;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;
import com.gaiamount.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-3-17.
 * 选择分类的页面
 */
public class ChooseCategoryFrag extends BaseUploadFrag {

    private ListView mListView;
    private ChooseCategoryAdpater mAdapter;
    private String[] mNames;
    private List<ChooseCategoryItem> mList;
    private int choosedCount = 0;
    @Override
    protected void setTitle(TextView title) {
        title.setText(getResources().getString(R.string.choose_category) + "最多可选三项");
    }

    @Override
    protected void restore() {
        UpdateWorksBean mUpdateWorksBean = GaiaApp.getAppInstance().getUpdateWorksBean();
        String typeString = mUpdateWorksBean.getA().getTypeStr();
        if (mUpdateWorksBean != null&&typeString!=null) {
            choosedCount = typeString.split(",").length;
            mAdapter.setType(typeString);
            mAdapter.notifyDataSetChanged();
        }
    }

    String type = "";
    String typeString = "";
    @Override
    protected void setSaveBtn(TextView saveBtn) {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.size()<1) {
                    GaiaApp.showToast("至少选择一项");
                    return;
                }
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isChecked) {
                        typeString +=mList.get(i).name+",";
                        type +=i+1+",";
                    }
                }
                LogUtil.i(ChooseCategoryFrag.class, type);
                GaiaApp.getAppInstance().getUpdateWorksBean().getA().setType(type);
                GaiaApp.getAppInstance().getUpdateWorksBean().getA().setTypeStr(typeString);

                onFragmentStateChangeListener.onFinish(UploadType.CHOOSE_CATEGORY);
            }
        });
    }

    @Override
    protected View getContent() {
        return getInflater().inflate(R.layout.fragment_choose_category, null);
    }

    @Override
    protected void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mNames = getResources().getStringArray(R.array.choose_categroy);

        mList = new ArrayList<>();
        for (int i = 0; i < mNames.length; i++) {
            mList.add(new ChooseCategoryItem(mNames[i], false));
        }
        mAdapter = new ChooseCategoryAdpater(getActivity(), mList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checked_cb);
                if (choosedCount>=3&&!checkBox.isChecked()) {
                    return;
                }
                checkBox.toggle();
                if (checkBox.isChecked()) {
                    mList.get(position).isChecked = true;
                    choosedCount++;
                } else {
                    mList.get(position).isChecked = false;
                    choosedCount--;
                }

            }
        });
    }
}
