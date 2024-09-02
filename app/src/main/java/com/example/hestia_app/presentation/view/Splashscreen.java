package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.hestia_app.R;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Pegando o logo
        ImageView logo = findViewById(R.id.logo);

        // Carregando a animação
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(logoAnimation);

        // Navegar para MainActivity após 3 segundos
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splashscreen.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Fecha a SplashActivity
        }, 3000);  // 3 segundos
    }
}