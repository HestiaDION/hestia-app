package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.hestia_app.R;

public class DashboardInformacoes extends AppCompatActivity {

    WebView webView;
    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_informacoes);

        goBack = findViewById(R.id.goBackArrow);

        webView = findViewById(R.id.webview);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}