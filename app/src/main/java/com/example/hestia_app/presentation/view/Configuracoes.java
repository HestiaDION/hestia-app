package com.example.hestia_app.presentation.view;

import android.app.admin.DelegatedAdminReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hestia_app.DashboardInformacoes;
import com.example.hestia_app.R;

public class Configuracoes extends AppCompatActivity {

    TextView sobreAplicativo;
    TextView termosPoliticas;
    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        goBack = findViewById(R.id.goBackArrow);

        goBack.setOnClickListener(v -> finish());
        sobreAplicativo = findViewById(R.id.sobre);
        termosPoliticas = findViewById(R.id.politicas);


        sobreAplicativo.setOnClickListener(v -> {

            Intent intent = new Intent(Configuracoes.this, DashboardInformacoes.class);
            startActivity(intent);
            finish();

        });

        termosPoliticas.setOnClickListener(v -> {

            Intent intent = new Intent(Configuracoes.this, UserTerms.class);
            startActivity(intent);
            finish();
        });
    }
}