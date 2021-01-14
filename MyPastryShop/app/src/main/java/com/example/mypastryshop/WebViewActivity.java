package com.example.mypastryshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//沒有notifationbar

        webView = (WebView) findViewById(R.id.web);
        webView.clearCache(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //WebChromeClient tab = new  WebChromeClient();
        webView.setWebChromeClient(new WebChromeClient());

//        webView.loadUrl("https://clouddata-9cbe1.web.app");
        webView.loadUrl("http://192.168.0.41:8081/UVShopping/");
//        webView.loadUrl("http://192.168.200.33:8081/UVShopping/login/");
    }
}