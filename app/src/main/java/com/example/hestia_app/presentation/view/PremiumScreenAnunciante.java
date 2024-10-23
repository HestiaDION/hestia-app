package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hestia_app.R;

public class PremiumScreenAnunciante extends AppCompatActivity {

    Button assinar;
    ImageView goBack;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_screen_anunciante);


        // TODO: implementar tela de pagamento e botão de volta
        assinar.setOnClickListener(v -> {});
        goBack.setOnClickListener(v -> finish());
    }
}