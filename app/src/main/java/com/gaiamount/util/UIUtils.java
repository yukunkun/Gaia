package com.gaiamount.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.gaiamount.R;

/**
 * Created by haiyang-lu on 16-3-11.
 * 跟主题和界面相关的工具类
 */
public class UIUtils {
    public static int DialogTheme  = 1;
    public static int ActionBarTheme  = 2;
    private static ProgressDialog progressDialog;

    public static void changeTheme(int theme,Context context) {
        if(theme ==DialogTheme) {
            context.setTheme(R.style.AppTheme_Float);
        } else {
            context.setTheme(R.style.AppTheme_ActionBar);
        }

    }


    /**
     * 设置沉浸式状态栏（透明状态栏）
     * @param view activity顶部控件的id
     */
    public static void setImmerseLayout(View view,Activity context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = ScreenUtils.getStatusBarHeight(context.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    public static void displayCustomProgressDialog(Context context,boolean flag) {
        if(progressDialog==null) {
            progressDialog = new ProgressDialog(context);
        }


        progressDialog.setMessage(context.getResources().getString(R.string.requesting));
        if(flag) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
            progressDialog = null;
        }


    }

    public static void displayCustomProgressDialog(Context context,String message,boolean flag) {
        if(progressDialog==null) {
            progressDialog = new ProgressDialog(context);
        }

        progressDialog.setMessage(message);
        if(flag) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
            progressDialog = null;
        }


    }

    public static void showSoftInputMethod(Activity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST|WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * 显示输入法
     * @param context 上下文
     * @param view 所依赖的视图（必须要有焦点）
     */
    public static void showSoftInputMethod(Activity context,View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInputMethod(Activity activity,View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0);
            view.clearFocus();

    }

}
