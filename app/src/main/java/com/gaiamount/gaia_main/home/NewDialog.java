package com.gaiamount.gaia_main.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.OnEventId;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yukun on 16-9-5.
 * 强制更新
 */
public class NewDialog extends DialogFragment {

    private SeekBar seekBar;
    private Button buttonCancel;
    private Button buttonOk;
    private String minimum;
    private String latest;
    private int versionCode;
    private Handler handler=new Handler();
    private TextView textView;

    public static NewDialog getInstance(String minimum, String latest, int versionCode){
        NewDialog newDialog=new NewDialog();

        Bundle bundle=new Bundle();
        bundle.putString("minimum",minimum);
        bundle.putString("latest",latest);
        bundle.putInt("versionCode",versionCode);
        newDialog.setArguments(bundle);

        return newDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        minimum=arguments.getString("minimum");
        latest=arguments.getString("latest");
        versionCode=arguments.getInt("versionCode");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.homefrag_new, null);
        init(inflate);
        setListener();
        return inflate;
    }

    private void init(View inflate) {
//        seekBar = (SeekBar) inflate.findViewById(R.id.new_seekbar);
        buttonCancel = (Button) inflate.findViewById(R.id.new_cancel);
        buttonOk = (Button) inflate.findViewById(R.id.new_ok);
        textView = (TextView) inflate.findViewById(R.id.text_new);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
    }

    private void setListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(versionCode<Integer.valueOf(minimum)){
                    getActivity().finish();
                }else {
                    getDialog().dismiss();
                }
            }
        });
        //确定
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                textView.setText("更新中,请等待...");
//                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(Configs.WanDouJiaLoad));
//                it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//                getActivity().startActivity(it);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(Configs.WanDouJiaLoad);
                intent.setData(content_url);
                startActivity(intent);

            }
        });
    }

//    private void LoadApp() {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                LoadUrtil loadUrtil=new LoadUrtil();
//                loadUrtil.Download(url);
//            }
//        }).start();
//
//        //更新seekbar
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (!isCompleted){
//                    try {
//                        Thread.sleep(1000);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                            seekBar.setProgress(downloadLength);
//                            }
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }
//
//    public String getSDPath() {
//        File sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState()
//                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
//        if (sdCardExist) {
//            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
//        }
//        return sdDir.toString();
//    }
//    public long getSDFreeSize(){//判断内存是否充足
//        //取得SD卡文件路径
//        File path = Environment.getExternalStorageDirectory();
//        StatFs sf = new StatFs(path.getPath());
//        //获取单个数据块的大小(Byte)
//        long blockSize = sf.getBlockSize();
//        //空闲的数据块的数量
//        long freeBlocks = sf.getAvailableBlocks();
//        //返回SD卡空闲大小
//        return (freeBlocks * blockSize)/1024 /1024; //单位MB
//    }
//
//    int downloadLength=0;
//
//    //下载的类
//    public class LoadUrtil {
//
//        private InputStream inputStream;
//        private FileOutputStream buffer;
//
//        public  void Download(String url){
//            try {
//                URL url1=new URL(url);
//                HttpURLConnection connection = (HttpURLConnection) url1
//                        .openConnection();
//
//                connection.setConnectTimeout(10000);
//                connection.connect();
//
//                seekBar.setMax(connection.getContentLength());//进度条的长度
//
//                String storage=getSDPath();
//                long SDSize=getSDFreeSize();
//
//                if(storage==null||SDSize<100){
//                    GaiaApp.showToast("请确定内存是否充足");
//                    return;
//                }
//
//                File file=new File(storage+"/gaiamount");
//                //判断是否有文件夹
//                if(!file.exists()){
//                    file.mkdirs();
//                }
//
//                File fileName=new File(storage+"/gaiamount","Gaiament.apk");
//
//                inputStream = connection.getInputStream();
//                byte[] bytes=new byte[2048];
//
//                buffer = new FileOutputStream(fileName);
//                int len;
//
//                while ((len = inputStream.read(bytes)) != -1) {
//                    buffer.write(bytes, 0, len);
//                    downloadLength += len;
//                }
//                isCompleted = true;
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            finally {
//                try {
//                    if(buffer!=null){
//                        buffer.close();
//                    }
//                    if(inputStream!=null){
//                        inputStream.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            EventBus.getDefault().post(new OnEventId(-1));//表示下载完成,可以安装了
//        }
//
//    }

}
