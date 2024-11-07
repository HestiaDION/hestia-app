package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hestia_app.R;
import com.example.hestia_app.domain.models.FormularioUniversitario;

public class TelaAviso extends AppCompatActivity {

    TextView textExplanation;
    LottieAnimationView lottieAnimation;
    String tipo, tela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_aviso);

        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            return;
        }

        textExplanation = findViewById(R.id.textExplanation);
        lottieAnimation = findViewById(R.id.lottieAnimation);

        textExplanation.setText(bundle.getString("textExplanation"));
        lottieAnimation.setAnimation(bundle.getInt("lottieAnimation"));

        tipo = bundle.getString("tipo");

        if (tipo.equals("no_internet")) {
            // esperar alguns segundos e fechar app
            new Handler().postDelayed(this::finish, 5000);
        } else {
            // esperar alguns segundos e abrir a tela
            tela = bundle.getString("tela");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Class<?> activityClass = null;

                    // Verifica a tela a ser aberta com base no valor da string `tela`
                    if ("MainActivityNavbar".equals(tela)) {
                        activityClass = MainActivityNavbar.class;
                    } else{
                        activityClass = FormularioUniversitarioActivity.class;
                    }

                    Intent intent = new Intent(TelaAviso.this, activityClass);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }
}