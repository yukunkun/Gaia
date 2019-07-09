package com.gaiamount.util.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.PersistentCookieStore;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * Created by haiyang-lu on 16-3-9.
 * Cookie相关的操作类
 */
public class CookieUtils {

    private List<Cookie> cookies;

    public  List<Cookie> getCookies() {
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    public  void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    /**
     * 获取标准 Cookie
     */
    public static String getCookieText(Context context) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        List<Cookie> cookies = myCookieStore.getCookies();
        Log.d("cookie大小", "cookies.size() = " + cookies.size());
        new CookieUtils().setCookies(cookies);
        for (Cookie cookie : cookies) {
            Log.d("cookie：", cookie.getName() + " = " + cookie.getValue());
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            String cookieName = cookie.getName();
            String cookieValue = cookie.getValue();
            if (!TextUtils.isEmpty(cookieName)
                    && !TextUtils.isEmpty(cookieValue)) {
                sb.append(cookieName + "=");
                sb.append(cookieValue + ";");
            }
        }
        Log.e("cookie", sb.toString());
        return sb.toString();
    }
}
