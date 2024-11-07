package com.example.hestia_app.presentation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hestia_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class Configuracoes extends AppCompatActivity {

    private static final String PREFS_NAME = "UserPreferences";
    private static final String USER_ORIGIN_KEY = "user_origin";
    SharedPreferences sharedPreferences;
    TextView sobreAplicativo;
    TextView termosPoliticas;
    TextView plano;
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
        plano = findViewById(R.id.plano);
        logout = findViewById(R.id.logout);

        sobreAplicativo.setOnClickListener(v -> {
            Intent intent = new Intent(Configuracoes.this, DashboardInformacoes.class);
            startActivity(intent);

        });

        termosPoliticas.setOnClickListener(v -> {
            Intent intent = new Intent(Configuracoes.this, UserTerms.class);
            startActivity(intent);
        });

        plano.setOnClickListener(v -> {
            sharedPreferences  = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String origemUsuario = sharedPreferences.getString(USER_ORIGIN_KEY, null);

            if (origemUsuario.equals("anunciante")) {
                Intent intent = new Intent(Configuracoes.this, PremiumScreenAnunciante.class);
                startActivity(intent);
            } else if (origemUsuario.equals("universitario")) {
                Intent intent = new Intent(Configuracoes.this, PremiumScreenUniversitario.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
        });
    }
}