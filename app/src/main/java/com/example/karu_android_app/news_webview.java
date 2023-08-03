package com.example.karu_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class news_webview extends AppCompatActivity {
    private WebView news_view;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_webview);
        news_view = (WebView)findViewById(R.id.newsView);
        backBtn = findViewById(R.id.backBTN);
        news_view.setWebViewClient(new WebViewClient());
        news_view.loadUrl("https://www.apollo-magazine.com/category/art-market/");

        WebSettings webSettings = news_view.getSettings();
        webSettings.setJavaScriptEnabled(true);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (news_view.canGoBack()) {
            news_view.goBack();
        } else {
            super.onBackPressed();
        }
    }


}