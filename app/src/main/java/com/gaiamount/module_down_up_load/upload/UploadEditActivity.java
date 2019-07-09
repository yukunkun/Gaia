package com.gaiamount.module_down_up_load.upload;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.gaiamount.R;
import com.gaiamount.module_down_up_load.upload.fragments.ChangeRatio;
import com.gaiamount.module_down_up_load.upload.upload_bean.UploadType;
import com.gaiamount.module_down_up_load.upload.fragments.BackStoryFrag;
import com.gaiamount.module_down_up_load.upload.fragments.BaseUploadFrag;
import com.gaiamount.module_down_up_load.upload.fragments.ChooseCategoryFrag;
import com.gaiamount.module_down_up_load.upload.fragments.DownloadSetFrag;
import com.gaiamount.module_down_up_load.upload.fragments.SetTagFrag;
import com.gaiamount.module_down_up_load.upload.fragments.VideoTypeFrag;
import com.gaiamount.module_down_up_load.upload.fragments.WorkInfoFrag;

/**
 * 负责管理上传逻辑的activity，具体交由其下的fragment
 */
public class UploadEditActivity extends AppCompatActivity implements BaseUploadFrag.OnFragmentStateChangeListener {
    public static final float LENGTH_SHORT = 0.5f;
    public static final float LENGTH_LONG = 0.8f;
    public static final float LENGTH_FULL = 1.0f;
    public static final int REQUEST_CODE = 121;

    public final String TAG = this.getClass().getSimpleName();

    /**
     * 设置界面的高度
     * @param height 0到1
     */
    private void setActivityHeight(float height) {
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        getWindow().setGravity(Gravity.LEFT | Gravity.BOTTOM);
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * height); // 高度设置为屏幕的0.8
        p.width = (int) (d.getWidth() * 1.0); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_edit);
        //获取传过来的标志，告诉此界面应当显示哪个fragment
        int tag = getTag();
        showFragment(tag);
    }

    private void showFragment(int type) {
        //
        FragmentManager fm = getFragmentManager();
        //
        FragmentTransaction ft  = fm.beginTransaction();
        BaseUploadFrag uploadFrag = null;
        switch (type) {
            case UploadType.VIDEO_TYPE:
                setActivityHeight(LENGTH_SHORT);
                uploadFrag= new VideoTypeFrag();
                break;
            case UploadType.WORK_INFO:
                setActivityHeight(LENGTH_LONG);
                uploadFrag= new WorkInfoFrag();
                break;
            case UploadType.CHOOSE_CATEGORY:
                setActivityHeight(LENGTH_LONG);
                uploadFrag= new ChooseCategoryFrag();
                break;
            case UploadType.SET_TAG:
                setActivityHeight(LENGTH_SHORT);
                uploadFrag= new SetTagFrag();

                break;
            case UploadType.DOWNLOAD_SET:
                setActivityHeight(LENGTH_LONG);
                uploadFrag= new DownloadSetFrag();
                break;
            /* kun */
            case UploadType.CHANGE_RATIO:
                setActivityHeight(LENGTH_LONG);
                uploadFrag = new ChangeRatio();
                break;

            case UploadType.BACK_STORY:
                setActivityHeight(LENGTH_LONG);
                uploadFrag = new BackStoryFrag();
                break;
            case UploadType.ADD_TO_SPECIAL:
                break;
            case UploadType.ADD_TO_GROUP:
                break;
            case UploadType.ADD_WATERMARK:
                break;
            default:
                Log.e(TAG,"传入的tag有误!");
                break;

        }

        if(uploadFrag!=null) {
            ft.replace(R.id.fl_content, uploadFrag);
             uploadFrag.setOnFragmentStateChangeListener(this);
        }
        //提交事务
        ft.commit();
    }

    /**
     *获取传过来的标志，告诉此界面应当显示哪个fragment
     * @return tag标志，由uploadActivity传过来
     */
    private int getTag() {
        return getIntent().getIntExtra("type",-1);
    }


    /**
     *重写返回键，提示用户是否放弃编辑
     */
    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("你确定放弃编辑么")
                .setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //退出
                        finish();
                        overridePendingTransition(R.anim.top_in,R.anim.bottom_out);
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onClose() {
        onBack();
    }

    @Override
    public void onFinish(int type) {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        setResult(RESULT_OK, intent);
        finish();
        //动画
        overridePendingTransition(R.anim.top_in,R.anim.bottom_out);
    }

}
