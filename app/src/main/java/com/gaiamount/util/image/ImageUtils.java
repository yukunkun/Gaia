package com.gaiamount.util.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.util.encrypt.MD5Encoder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by haiyang-lu on 16-3-24.
 * 图片加载的工具类，单例
 */
public class ImageUtils {

    /**
     * 队列
     */
    private RequestQueue queue;
    /**
     * 图片加载器
     */
    private ImageLoader imageLoader;
    /**
     * 当前类的实例
     */
    private static ImageUtils instance;
    /**
     * 图片监听器
     */
    ImageLoader.ImageListener listener;
    /**
     * 本地缓存的策略
     */
    DiskLruCache mDiskLruCache;

    private ImageUtils(Context context) {
        queue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(queue, new MImageCache());
        try {
            mDiskLruCache = DiskLruCache.open(getDirFile(Configs.MNT_SDCARD_GAIA), getAppVersion(context), 1, Configs.DIKS_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得应用version号码
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public static ImageUtils getInstance(Context context) {
        if (instance == null) {
            instance = new ImageUtils(context);
        }
        return instance;
    }

    /**
     * 获取网络图片 显示其宽高为320:180
     *
     * @param imageView
     * @param url
     */
    public void getNetworkBitmap(ImageView imageView, String url) {
        listener = ImageLoader.getImageListener(imageView, R.mipmap.bg_general, R.mipmap.bg_general);
        if (imageLoader != null && url != null && !TextUtils.isEmpty(url) && !"null".equals(url)) {
            url = Configs.COVER_PREFIX + url.replace(".", "_18.");
            imageLoader.get(url, listener);
        }
    }


    /**
     * 获取封面（如作品，素材，学院等）不限制其宽高
     *
     * @param imageView
     * @param url
     */
    public void getCover(ImageView imageView, String url) {
        listener = ImageLoader.getImageListener(imageView, R.mipmap.bg_general, R.mipmap.bg_general);
        if (imageLoader != null && url != null && !TextUtils.isEmpty(url) && !"null".equals(url)) {
            imageLoader.get(Configs.COVER_PREFIX + url, listener);
        }
    }

    /**
     * 获取用户头像 宽高为240:240
     *
     * @param imageView 显示头像的控件实例
     * @param url       用户头像的网络url
     */
    public void getAvatar(ImageView imageView, String url) {
        listener = ImageLoader.getImageListener(imageView, R.mipmap.ic_avatar_default, R.mipmap.ic_avatar_default);
        if (imageLoader != null) {
            if (url != null && !TextUtils.isEmpty(url) && !"null".equals(url)) {
                imageLoader.get(Configs.COVER_PREFIX + url, listener, 240, 240, ImageView.ScaleType.CENTER);
            }
        }
    }


    /**
     * 获取本地视频的视频缩略图
     *
     * @param filePath 本地视频路径
     * @return 图片对象
     */
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 显示创作（作品，素材，学院）的cover或者screenshot，优先级为cover>screenshot
     *
     * @param imageView
     * @param cover      封面url
     * @param screenshot 截图url
     */
    public void showImage(ImageView imageView, String cover, String screenshot) {
        if (cover != null && !TextUtils.isEmpty(cover) && !"null".equals(cover)) {
            getCover(imageView, cover);
        } else if (screenshot != null && !TextUtils.isEmpty(screenshot) && !"null".equals(screenshot)) {
            getNetworkBitmap(imageView, screenshot);

        }
    }


    /**
     * 图片裁剪
     *
     * @param context
     * @param uri         //     * @param outputX
     *                    //     * @param outputY
     * @param requestCode
     */
    public void cropImageUri(Activity context, Uri uri, int outputX, int outputY, int requestCode) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 16);
//        intent.putExtra("aspectY", 9);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", false);//默认不输出
        intent.putExtra("noFaceDetection", true);
        context.startActivityForResult(intent, requestCode);

    }

    public static File getDirFile(String dir) {
        File file = new File(dir);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return file;

    }

    /**
     * volley中图片缓存策略的实现
     */
    class MImageCache implements ImageLoader.ImageCache {
        LruCache<String, Bitmap> lruCache;

        public MImageCache() {
            lruCache = new LruCache<String, Bitmap>(Configs.MEMARY_MAX_SIZE) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            try {
                String key = MD5Encoder.encode(url);
                try {
                    if (mDiskLruCache.get(key) == null) {
                        return lruCache.get(url);
                    } else {
                        DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                        Bitmap bitmap = null;
                        if (snapShot != null) {
                            InputStream is = snapShot.getInputStream(0);
                            bitmap = BitmapFactory.decodeStream(is);
                        }
                        return bitmap;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return lruCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            try {
                String key = MD5Encoder.encode(url);
                if (null == mDiskLruCache.get(key)) {
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);

                        if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                    mDiskLruCache.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            lruCache.put(url, bitmap);
        }
    }

    public static void convertToBlur(Context context, String srceenShot,String cover) {
        RequestQueue queue = Volley.newRequestQueue(context);
        ImageRequest imageRequest;
        String blurUrl;
        if (cover==null||cover.isEmpty()) {
            blurUrl = Configs.COVER_PREFIX + srceenShot.replace(".", "_18.");
        }else {
            blurUrl = Configs.COVER_PREFIX + cover.replace(".", "_18.");
        }
        imageRequest = new ImageRequest(blurUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Bitmap bitmap = convertToBlur(response);
                EventBus.getDefault().post(bitmap);
            }
        }, 640, 360, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(imageRequest);
        queue.start();
    }

    /**
     * 高斯模糊
     *
     * @param bmp
     * @return
     */
    public static Bitmap convertToBlur(Bitmap bmp) {
        // 高斯矩阵
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap newBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 16; // 值越小图片会越亮，越大则越暗

        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        return newBmp;
    }


}
