package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.hestia_app.R;

public class MoradiasFavoritas extends AppCompatActivity {
    ImageButton voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moradias_favoritas);

        voltar = findViewById(R.id.voltar);
        voltar.setOnClickListener(v -> finish());
    }
}