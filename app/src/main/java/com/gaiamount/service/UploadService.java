package com.gaiamount.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.gaiamount.R;
import com.gaiamount.aidl.IUploadService;
import com.gaiamount.apis.file_api.FileApi;
import com.gaiamount.util.BroadcastUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class UploadService extends Service {

    private UploadManager uploadManager;

    public UploadService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //实例化
        String filePath = getFilesDir().getAbsolutePath();
        try {
            Recorder recorder = new FileRecorder(filePath);
            KeyGenerator keyGen = new KeyGenerator() {
                public String gen(String key, File file) {
                    // 不必使用url_safe_base64转换，uploadManager内部会处理
                    // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
                    return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
                }
            };
            uploadManager = new UploadManager(recorder, keyGen);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UploadService", "文件路径有问题");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return service;
    }

    private boolean isCancel = false;


    private IUploadService.Stub service = new IUploadService.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void pauseUpload() throws RemoteException {
            isCancel = true;
        }

        @Override
        public void resumeUpload() throws RemoteException {
            isCancel = false;

        }

        @Override
        public double getUploadProgress() throws RemoteException {

            return progress;
        }

        @Override
        public void stopUpdate() throws RemoteException {
            isCancel = true;
        }

        @Override
        public boolean isUploading() throws RemoteException {
            if (progress!=1) {
                return true;
            }else {
                return false;
            }
        }

        @Override
        public void uploadVideo(String inputKey, String token, String videoPath) throws RemoteException {
            //开启状态栏通知
            Notification.Builder builder = new Notification.Builder(UploadService.this);
            builder.setSmallIcon(R.mipmap.icon_nav_upload);
            builder.setContentText("正在上传");
            //将isCancel置为false
            isCancel = false;
            Log.d("上传到七牛所需要的参数", inputKey + "," + token + "," + videoPath);
            //上传
            uploadManager.put(videoPath, inputKey, token, upCompletionHandler, upLoadOptions);
        }

        private double progress;
        private UploadOptions upLoadOptions = new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                Log.i("qiniu", key + ": " + percent);

                progress = percent;
                }
//            }
        }, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                if (isCancel) {
                    //发送广播通知
                    Intent intent = new Intent();
                    intent.setAction("com.gaiamount.upload.cancel");
                    sendBroadcast(intent);

                }
                return isCancel;
            }
        });


    };
    private UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject res) {
            //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
            Intent intent = new Intent();
            Log.i("qiniu-callback", info.toString());
            if (info.statusCode == 200) {//成功
                //上传完成
                intent.putExtra("result", FileApi.SUCCESS);
            } else {
                intent.putExtra("result", FileApi.FAIL);
            }
            intent.setAction(BroadcastUtils.ACTION_UPLOAD_FINISH);
            sendBroadcast(intent);

        }
    };


}
