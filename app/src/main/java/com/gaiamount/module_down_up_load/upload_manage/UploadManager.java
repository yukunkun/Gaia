package com.gaiamount.module_down_up_load.upload_manage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.gaiamount.aidl.IUploadService;
import com.gaiamount.service.UploadService;

/**
 * Created by haiyang-lu on 16-7-28.
 */
public class UploadManager {

    public static final int MAX_PROGRESS = 1;
    private static UploadManager mUploadManager;
    private IUploadService.Stub mIUploadService;
    private Context mContext;
    private ServiceConnection mConnection;

    private UploadManager(Context context) {

        mContext = context;
    }

    public static UploadManager getInstance(Context context) {
        if (mUploadManager==null) {
            mUploadManager = new UploadManager(context);
        }
        return mUploadManager;
    }

    public boolean isUploading() {
        if (mIUploadService==null) {
            return false;
        }
        try {
            return mIUploadService.isUploading();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getProgress() {
        if (mIUploadService==null) {
            return 0;
        }
        if (isUploading()) {
            try {
                return mIUploadService.getUploadProgress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return MAX_PROGRESS;
    }

    public void startUpload(final String inputKey, final String token, final String filePath) {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIUploadService = (IUploadService.Stub) service;
                try {
                    mIUploadService.uploadVideo(inputKey, token, filePath);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(mContext, UploadService.class);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    public void cancelUpload() {
        if (mIUploadService==null) {
            return;
        }
        try {
            mIUploadService.stopUpdate();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        release();

    }


    public void release() {
        mContext.unbindService(mConnection);
        mIUploadService = null;
        mUploadManager = null;
    }
}
