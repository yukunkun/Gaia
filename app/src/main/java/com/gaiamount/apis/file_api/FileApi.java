package com.gaiamount.apis.file_api;

import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_works.WorksApi;

/**
 * Created by haiyang-lu on 16-3-24.
 * 上传/下载相关接口
 */
public class FileApi {
    public static final int TYPE_PUBLIC = 0;
    public static final int TYPE_PRIVATE = 1;

    public static final int PAUSE = 1;
    public static final int CANCEL = 2;
    public static final int ERROR = 3;

    public static final int UPLOAD_LIST_TYPE_UNFINISH = 0;
    public static final int UPLOAD_LIST_TYPE_FINISH = 4;
    public static final int SUCCESS = 10;
    public static final int FAIL = 11;

    public static final String FILE_URL = Configs.BASE_URL + "/file";
    /**
     * 上传图片
     */
    public static final String UPLOAD_IMG_URL = FILE_URL + "/uploadImg";
    /**
     *作品上传暂停/取消/报错(suspend)
     */
    public static final String SUSPEND = WorksApi.WORK_URL + "/suspend";
    /**
     * 上传
     */
    public static final String UPLOAD_URI = FILE_URL + "/getVideoToken?";

}
