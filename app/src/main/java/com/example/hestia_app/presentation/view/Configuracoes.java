package com.example.hestia_app.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hestia_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class Configuracoes extends AppCompatActivity {

    TextView sobreAplicativo;
    TextView termosPoliticas;
    ImageView goBack;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        goBack = findViewById(R.id.goBackArrow);

        goBack.setOnClickListener(v -> finish());
        sobreAplicativo = findViewById(R.id.sobre);
        termosPoliticas = findViewById(R.id.politicas);
        logout = findViewById(R.id.logout);

        sobreAplicativo.setOnClickListener(v -> {
            Intent intent = new Intent(Configuracoes.this, DashboardInformacoes.class);
            startActivity(intent);

        });

        termosPoliticas.setOnClickListener(v -> {
            Intent intent = new Intent(Configuracoes.this, UserTerms.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
        });
    }
}