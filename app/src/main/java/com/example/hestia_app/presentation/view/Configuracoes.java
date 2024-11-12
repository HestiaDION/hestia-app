package com.example.hestia_app.presentation.view;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
    TextView notificacoes;
    ImageView goBack;
    TextView logout;
    FirebaseAuth autenticar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        goBack = findViewById(R.id.goBackArrow);

        autenticar = FirebaseAuth.getInstance();

        goBack.setOnClickListener(v -> finish());
        sobreAplicativo = findViewById(R.id.sobre);
        termosPoliticas = findViewById(R.id.politicas);
        plano = findViewById(R.id.plano);
        logout = findViewById(R.id.logout);
        notificacoes = findViewById(R.id.notificacao);

        notificacoes.setOnClickListener(v -> {
            Intent intent = new Intent(Configuracoes.this, Notificacoes.class);
            startActivity(intent);
        });

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
            if (getContext() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Configuracoes.this); // Use o contexto correto aqui
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_logout_confirmation, null);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                Button btnConfirmLogout = dialogView.findViewById(R.id.btn_confirm_logout);
                Button btnCancelLogout = dialogView.findViewById(R.id.btn_cancel_logout);

                btnConfirmLogout.setOnClickListener(v1 -> {
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(Configuracoes.this, LoginActivity.class);
                    startActivity(intent);

                    alertDialog.dismiss();
                    finish();
                    Configuracoes.this.finish();
                });

                btnCancelLogout.setOnClickListener(v1 -> alertDialog.dismiss());

                alertDialog.show();
            }
        });
    }
}