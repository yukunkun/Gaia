package com.gaiamount.module_down_up_load.upload_manage;

import android.content.Context;

import com.gaiamount.R;
import com.gaiamount.widgets.common_adapter.CommonAdapter;
import com.gaiamount.widgets.common_adapter.CommonViewHolder;

import java.util.List;

/**
 * Created by haiyang-lu on 16-7-28.
 * 上传列表
 */
public class UploadListCommenAdapter extends CommonAdapter<UploadedWorks> {
    public UploadListCommenAdapter(Context context, int itemLayoutId, List<UploadedWorks> data) {
        super(context, itemLayoutId, data);
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, UploadedWorks item) {
        viewHolder.setImageForView(R.id.cover, item.getCover(), item.getScreenshot(), getContext());
        viewHolder.setTextForTextView(R.id.title, item.getName());

        String statusStr = "状态：";
        int status = item.getStatus();
        switch (status) {
            case 0:
                statusStr += "上传中";
                break;
            case 1:
                statusStr += "转码中";
                break;
            case 2:
                statusStr += "切片中";
                break;
            case 3:
                statusStr += "上传失败";
                break;
            case 4:
                statusStr += "转码失败";
                break;
            case 5:
                statusStr += "切片失败";
                break;
            case 6:
                statusStr += "待审核";
                break;
            case 7:
                statusStr += "已审核";
                break;
            default:
                statusStr += "未知错误";
                break;

        }
        viewHolder.setTextForTextView(R.id.status,statusStr);
        String type = item.getType();
        String replacedType = type.replace(",", "");
        viewHolder.setTextForTextView(R.id.type, replacedType);
    }
}
