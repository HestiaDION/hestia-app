package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hestia_app.ConfirmacaoPagamento;
import com.example.hestia_app.R;

public class PremiumScreenAnunciante extends AppCompatActivity {

    Button assinar;
    ImageView goBack;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_screen_anunciante);

        assinar = findViewById(R.id.assinar);
        goBack = findViewById(R.id.goBackArrow);

        assinar.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), ConfirmacaoPagamento.class);
            startActivity(intent);
            finish();
        });

        goBack.setOnClickListener(v -> finish());
    }
}