package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.GaiaApp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by yukun on 16-8-29.
 */
public class TuWenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private MyViewHolder holder;
    private ArrayList<String> stringsList;

    public TuWenAdapter( ArrayList<String> stringsList,Context context) {
        this.context = context;
        this.stringsList = stringsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.web, null);
        holder = new MyViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).webView.getSettings().setJavaScriptEnabled(true);

        //webview 加载html5的本地存储, dom必须由webview加载
        ((MyViewHolder) holder).webView.getSettings().setDomStorageEnabled(true);
        String appCachePath = context.getCacheDir().getAbsolutePath();
        ((MyViewHolder) holder).webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        ((MyViewHolder) holder).webView.getSettings().setAppCachePath(appCachePath);
        ((MyViewHolder) holder).webView.getSettings().setAllowFileAccess(true);
        ((MyViewHolder) holder).webView.getSettings().setAppCacheEnabled(true);
        ((MyViewHolder) holder).webView.loadUrl(stringsList.get(position));

//        Log.i("---url",stringsList.get(position));

        ((MyViewHolder)holder).webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                ((MyViewHolder) holder).textView.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                ((MyViewHolder) holder).textView.setVisibility(View.GONE);
                GaiaApp.showToast(context.getString(R.string.load_fail));
            }

        });

    }

    @Override
    public int getItemCount() {
        return stringsList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        WebView webView;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            webView= (WebView) itemView.findViewById(R.id.web);
            textView= (TextView) itemView.findViewById(R.id.text_loads);
        }
    }
}
