// IUploadService.aidl.aidl
package com.gaiamount.aidl;

// Declare any non-default types here with import statements

    interface IUploadService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

        /**
           暂停上传
         */
        void pauseUpload();
        /**
        * 继续上传
        */
        void resumeUpload();
        /**
           获取上传进度
         */
        double getUploadProgress();
        /**
           停止上传
         */
        void stopUpdate();
        /**
           开始上传
         */
        void uploadVideo(String inputKey,String token,String videoPath);

        boolean isUploading();
}
