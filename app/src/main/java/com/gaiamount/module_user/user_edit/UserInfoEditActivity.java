package com.gaiamount.module_user.user_edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.gaiamount.R;
import com.gaiamount.module_user.fragment.EditAvatarFrag;
import com.gaiamount.module_user.fragment.EditCityFrag;
import com.gaiamount.module_user.fragment.EditEmailFrag;
import com.gaiamount.module_user.fragment.EditJobFrag;
import com.gaiamount.module_user.fragment.EditSignatureFrag;
import com.gaiamount.module_user.fragment.EditUserNameFrag;
import com.gaiamount.module_user.fragment.ResumeFrag;
import com.gaiamount.util.UIUtils;

public class UserInfoEditActivity extends AppCompatActivity {
    public final static int AVATER = 1;
    public final static int USERNAME = 2;
    public final static int GENDER = 3;
    public final static int SIGNATURE = 4;
    public final static int JOB = 5;
    public final static int CITY = 6;
    public final static int RESUME = 7;
    public final static int EMAIL = 8;
    public final static int PHONE = 9;

    public static final int REQUEST_CODE = 2195;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int kind = getIntent().getIntExtra("kind", -1);
        int theme = getIntent().getIntExtra("theme", -1);

        if (theme == UIUtils.DialogTheme) {
            UIUtils.changeTheme(UIUtils.DialogTheme, this);
        } else if (theme == UIUtils.ActionBarTheme) {
            UIUtils.changeTheme(UIUtils.ActionBarTheme, this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);

        /**
         * fragment事务处理对象
         */
        FragmentTransaction mTransition = getSupportFragmentManager().beginTransaction();

        switch (kind) {
            case AVATER://设置或修改用户头像
                setActivityHeight(0.35f);
                Fragment userAvatarFrag = new EditAvatarFrag();
                mTransition.replace(R.id.content,userAvatarFrag);
                break;
            case GENDER://选择性别

                return;
            case USERNAME:
                setLabel("修改昵称");
                Fragment userNameFrag = new EditUserNameFrag();
                mTransition.replace(R.id.content,userNameFrag);
                break;
            case JOB:
                setLabel(getString(R.string.choose_job));
                Fragment jobFrag = new EditJobFrag();
                mTransition.replace(R.id.content,jobFrag);
                break;
            case CITY:
                setLabel(getString(R.string.choose_city));
                Fragment cityFrag = new EditCityFrag();
                mTransition.replace(R.id.content,cityFrag);
                break;
            case SIGNATURE:
                setLabel("修改个性签名");
                Fragment signatureFrag = new EditSignatureFrag();
                mTransition.replace(R.id.content,signatureFrag);
                break;
            case RESUME:
                setLabel("添加简介");
                Fragment resumeFrag = new ResumeFrag();
                mTransition.replace(R.id.content,resumeFrag);
                break;
            case EMAIL:
                setLabel("绑定邮箱");
                Fragment emailBindFrag = new EditEmailFrag();
                mTransition.replace(R.id.content,emailBindFrag);
                break;
            case PHONE:
                break;
            default:
                break;
        }
        //提交事务
        mTransition.commit();
    }

    private void setLabel(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null) {
            supportActionBar.setTitle(title);
        }
    }

    /**
     * 该方法用于关闭编辑器activity,通知编辑资料的activity更新用户信息，被他所管理的fragment调用
     */
    public void updateUserInfo() {
        Intent intent = new Intent();

        setResult(RESULT_OK,intent);

        finish();
    }

    /**
     * 设置界面的高度
     * @param height 0到1
     */
    private void setActivityHeight(float height) {
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        getWindow().setGravity(Gravity.CENTER);
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * height); // 高度设置为屏幕的0.8
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);
    }

}
