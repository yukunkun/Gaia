package com.gaiamount.gaia_main.settings.about_us;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.gaiamount.R;

/**
 * kun
 */
public class IntroduceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        WebView webView= (WebView) findViewById(R.id.webview);
        WebSettings setting = webView.getSettings();
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.loadUrl("file:///android_asset/introduce.html");
    }

    public void IntroduceBack(View view) {
        finish();
        overridePendingTransition(0,R.anim.push_left_out);
    }
}
