package com.example.hestia_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hestia_app.presentation.PaymentDialogFragment;

public class ConfirmacaoPagamento extends AppCompatActivity {

    Button confirmacao, cancelar;
    TextView planoType;
    ImageView logoPlano;
    private static final String PREFS_NAME = "UserPreferences";
    private static final String USER_ORIGIN_KEY = "user_origin";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao_pagamento);

        confirmacao = findViewById(R.id.pagamentoConfirmacao);
        cancelar = findViewById(R.id.cancelar);
        planoType = findViewById(R.id.planoType);
        logoPlano = findViewById(R.id.logoPlano);

        sharedPreferences  = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String origemUsuario = sharedPreferences.getString(USER_ORIGIN_KEY, null);

        assert origemUsuario != null;
        if (origemUsuario.equals("anunciante")) {
            planoType.setText("Plano Labareda");
            logoPlano.setImageResource(R.drawable.logo_premium_anunciante);
        } else if (origemUsuario.equals("universitario")) {
            planoType.setText("Plano Faísca");
            logoPlano.setImageResource(R.drawable.universitario_premium_logo);
        }

        confirmacao.setOnClickListener(v -> {
            PaymentDialogFragment paymentDialog = new PaymentDialogFragment();
            paymentDialog.show(getSupportFragmentManager(), "PaymentDialog");

        });

        cancelar.setOnClickListener(v -> finish());
    }
}