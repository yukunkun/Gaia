package com.gaiamount.util;

import android.util.Log;

import com.gaiamount.util.encrypt.AES128;
import com.gaiamount.util.encrypt.SHA256;

/**
 * Created by xiaoyu-liu on 16-3-1.
 *
 * Data progress
 */
public class DataProgressor {

    public static final int TYPE_EMAIL = 1;
    public static final int TYPE_PHONE = 2;
    private DataProgressor mInstance = new DataProgressor();
    private final String TAG = this.getClass().getSimpleName();

    public DataProgressor instance(){ return mInstance; }


    /**
     * AES加密，key为account经过SHA256加密后的前32位，iv为后32位
     * @param account 账号
     * @param plainText 明文密码
     * @return 加密后的字符串
     */
    public static String encyptPwd(String account,String plainText) {
        //SHA加密
        String str_sha = SHA256.bin2hex(account);
        Log.i("TAG",str_sha);
        //计算
        String seed = str_sha.substring(0, 32);
        String iv = str_sha.substring(32,64);

        //AES加密
        String encryptPwd = null;
        try {
            encryptPwd = AES128.encrypt(seed, plainText, iv);
        } catch (Exception e) {
            Log.e("TAG","错误");
            e.printStackTrace();
        }
        return encryptPwd;
    }

}
