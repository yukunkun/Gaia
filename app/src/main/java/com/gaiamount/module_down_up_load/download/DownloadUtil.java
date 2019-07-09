package com.gaiamount.module_down_up_load.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by haiyang-lu on 16-5-19.
 */
public class DownloadUtil {

    private static DownloadUtil sDownloadUtil;

    private String serviceString = Context.DOWNLOAD_SERVICE;
    private final DownloadManager mDownloadManager;

    private HashMap<Uri,Long> mUriLongHashMap = new HashMap<>();
    private List<DownloadItem> mList;
    private Context mContext;

    private DownloadUtil(Context context){
        mContext = context;
        mDownloadManager = (DownloadManager) context.getSystemService(serviceString);


    }

    public static DownloadUtil getInstance(Context context) {
        if (sDownloadUtil ==null) {
            sDownloadUtil = new DownloadUtil(context);
        }
        return sDownloadUtil;
    }

    public void addTask(Uri uri,DownloadItem downloadItem) {
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);//限制在wifi条件下下载
        request.setTitle(downloadItem.videoName);
        request.setVisibleInDownloadsUi(true);
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                downloadItem.id+"_"+downloadItem.contentType+"_"+downloadItem.videoName+".mp4");//视频id_视频类型_视频名称
        request.setDestinationUri(Uri.fromFile(file));

        long enqueue = mDownloadManager.enqueue(request);
        mUriLongHashMap.put(uri,enqueue);
    }

    public void deleteTask(Uri uri) {
        //获取id
        Long id = mUriLongHashMap.get(uri);
        mDownloadManager.remove(id);
    }

    public long getTaskProgress(Uri uri) {
        //获取id
        Long id = mUriLongHashMap.get(uri);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = mDownloadManager.query(query);

        if (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                return cursor.getLong(columnIndex);
        }
        return 0l;
    }

    public long getTaskTotalSize(Uri uri) {
        //获取id
        Long id = mUriLongHashMap.get(uri);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            return cursor.getLong(columnIndex);
        }
        return 0l;
    }

    public List<DownloadItem> getTaskList() {
        mList = new ArrayList<>();
        DownloadManager.Query query = new DownloadManager.Query();
//        query.setFilterByStatus(DownloadManager.STATUS_RUNNING);
        Cursor cursor = mDownloadManager.query(query);
        while (cursor.moveToNext()) {
            DownloadItem downloadItem = new DownloadItem(false,false);
            int columnIndex_filename = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            int columnIndex_uri = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
            int columnIndex_data = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

            int columnIndex_id = cursor.getColumnIndex(DownloadManager.COLUMN_ID);
            int columnIndex_downloaded = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            int columnIndex_total_size = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);


            downloadItem.url = cursor.getString(columnIndex_uri);
            String fileName = cursor.getString(columnIndex_filename);
            if (fileName!=null) {
                String videoName = fileName.substring(fileName.lastIndexOf("/")+1);

                int start = videoName.indexOf("_");
                if (start!=-1) {
                    downloadItem.id = Long.valueOf(videoName.substring(0,start));
                    downloadItem.contentType = Integer.valueOf(videoName.substring(start +1, start+2));
                    downloadItem.videoName = videoName.substring(start+3);
                } else {
                    downloadItem.videoName = videoName;
                }
            }

            downloadItem.downloadedSize = cursor.getLong(columnIndex_downloaded);
            downloadItem.videoSize = cursor.getLong(columnIndex_total_size);
            downloadItem.data = cursor.getString(columnIndex_data);
            mUriLongHashMap.put(Uri.parse(downloadItem.url),cursor.getLong(columnIndex_id));

            mList.add(downloadItem);
        }

        return mList;
    }



}
