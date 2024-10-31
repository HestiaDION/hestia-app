package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hestia_app.R;

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
        lottieAnimation.playAnimation();

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
                    Intent intent = new Intent(TelaAviso.this, tela.getClass());
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }

}