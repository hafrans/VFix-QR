package com.example.plex.vfix.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.plex.vfix.R;

public class WebViewActivity extends AppCompatActivity {

    public static void startActivity(AppCompatActivity appCompatActivity,String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appCompatActivity.startActivity(intent);
    }

    public static void startActivityInternal(AppCompatActivity appCompatActivity,String url){
        Intent intent = new Intent(appCompatActivity,WebViewActivity.class);
        intent.putExtra("URL",url);
        appCompatActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = (WebView) findViewById(R.id.public_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(getIntent().getStringExtra("URL"));

    }

}
