package com.gaiamount.module_down_up_load.download;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_down_up_load.upload.upload_bean.DownloadSetting;
import com.gaiamount.module_down_up_load.upload.upload_bean.Price;
import com.gaiamount.module_down_up_load.upload.upload_bean.TransCoding;

import java.util.HashMap;

/**
 * Created by haiyang-lu on 16-3-18.
 * 下载设置的二级列表的适配器
 */
public class DownloadSetExpandableListAdapter implements ExpandableListAdapter {

    private String[] parentStrings;
    private String[][] childStrings = {{"源视频版本", "4k转码版本", "2k转码版本", "1080P转码版本", "720P转码版本"}
            , {"源视频版本", "4k转码版本", "2k转码版本", "1080P转码版本", "720P转码版本"}};

    private Activity activity;
    private DownloadSetting downloadSetting;

    private EditText editText;
    private final int mHave4k;
    private final int mHave2k;
    private final int mHave1080;
    private final int mHave720;

    class PriceBtnClickListener implements View.OnClickListener {
        public Button priceBtn;
        public int childPosition;
        public PriceBtnClickListener(int childPostion,Button priceBtn) {
            this.childPosition = childPostion;
            this.priceBtn = priceBtn;
        }

        @Override
        public void onClick(View v) {
            setPriceDialog(childPosition,priceBtn);
        }
    }

    public DownloadSetExpandableListAdapter(Activity activity, DownloadSetting downloadSetting) {
        this.activity = activity;
        this.downloadSetting = downloadSetting;
        TransCoding transCoding = downloadSetting.getTransCoding();
        mHave4k = transCoding.getHave4k();
        mHave2k = transCoding.getHave2k();
        mHave1080 = transCoding.getHave1080();
        mHave720 = transCoding.getHave720();
        parentStrings = activity.getResources().getStringArray(R.array.download_set_parent);
        map = new HashMap<>();
    }

    private HashMap<Integer,HashMap<Integer,View>> map;


    public DownloadSetting getDownloadSetting() {
        return downloadSetting;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return parentStrings.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childStrings[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentStrings[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childStrings[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View parentView = View.inflate(activity, R.layout.item_upload_download_set_parent, null);
        TextView tv = (TextView) parentView.findViewById(R.id.tv);
        tv.setText(parentStrings[groupPosition]);

        ImageView pointer = (ImageView) parentView.findViewById(R.id.btn_more);
        if (isExpanded) {
            pointer.setBackgroundResource(R.drawable.pointer_gray_up);
        } else {
            pointer.setBackgroundResource(R.drawable.pointer_gray_down);
        }

        return parentView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childView = View.inflate(activity, R.layout.item_upload_download_set_pay, null);
        //设置可选择的textview
        CheckedTextView ctv = (CheckedTextView) childView.findViewById(R.id.ctv);
        ctv.setText(childStrings[groupPosition][childPosition]);

        if (childPosition==1&&mHave4k==1) {
            markTextView(ctv);
        }
        if (childPosition==2&&mHave2k==1) {
            markTextView(ctv);
        }
        if (childPosition==3&&mHave1080==1) {
            markTextView(ctv);
        }
        if (childPosition==4&&mHave720==1) {
            markTextView(ctv);
        }


        //设置售价
        final Button priceBtn = (Button) childView.findViewById(R.id.price);
        initPrice(childPosition,priceBtn);
        if (groupPosition == 0) {
            priceBtn.setVisibility(View.GONE);
        } else {
            priceBtn.setVisibility(View.VISIBLE);
        }
        //设置其点击事件
        priceBtn.setOnClickListener(new PriceBtnClickListener(childPosition,priceBtn));

        if (childPosition == 0) {
            ctv.setChecked(true);
            ctv.setCheckMarkDrawable(R.mipmap.checked);
        }

        HashMap<Integer, View> value = new HashMap<>();
        value.put(childPosition,childView);
        return childView;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

        downloadSetting.setAllowCharge(groupPosition == 1 ? 1 : 0);//根据展开的项设置是否付费

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    /**
     * 根据数据初始化每个位置的价格
     * @param childPosition 孩子的位置
     * @param priceBtn 价格的控件
     */
    public void initPrice(int childPosition, Button priceBtn) {
        switch (childPosition) {
            case 0:
                priceBtn.setText("售价:"+downloadSetting.getPrice().getPriceOriginal()+"元");
                break;
            case 1:
                priceBtn.setText("售价:"+downloadSetting.getPrice().getPrice4k()+"元" );
                break;
            case 2:
                priceBtn.setText("售价:"+downloadSetting.getPrice().getPrice2k() +"元");
                break;
            case 3:
                priceBtn.setText("售价:"+downloadSetting.getPrice().getPrice1080() +"元");
                break;
            case 4:
                priceBtn.setText("售价:"+downloadSetting.getPrice().getPrice720() +"元");
                break;
            default:
                break;
        }
    }

    private void setPriceDialog(final int childPosition, final Button priceBtn) {
        editText = new EditText(activity);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        new AlertDialog.Builder(activity)
                .setTitle("请设置价格")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = editText.getText().toString();
                        float price = Float.parseFloat(s);
                        Price p = new Price();
                        switch (childPosition) {
                            case 0:
                                p.setPriceOriginal(price);
                                break;
                            case 1:
                                p.setPrice4k(price);
                                break;
                            case 2:
                                p.setPrice2k(price);
                                break;
                            case 3:
                                p.setPrice1080(price);
                                break;
                            case 4:
                                p.setPrice720(price);
                                break;
                            default:
                                break;
                        }
                        downloadSetting.setPrice(p);
                        priceBtn.setText("售价:" + price + "元");

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .show();
    }

    public void markTextView(CheckedTextView ctv) {
        ctv.setChecked(true);
        ctv.setCheckMarkDrawable(R.mipmap.checked);
    }

}
