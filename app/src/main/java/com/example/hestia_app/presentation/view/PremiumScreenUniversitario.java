package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hestia_app.R;

public class PremiumScreenUniversitario extends AppCompatActivity {

    Button assinar;
    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_screen_universitario);

        assinar = findViewById(R.id.assinar);
        goBack = findViewById(R.id.goBackArrow);

        // TODO: implementar tela de pagamento
        assinar.setOnClickListener(v -> {
            Toast.makeText(this, "universidade", Toast.LENGTH_SHORT).show();
        });

        goBack.setOnClickListener(v -> finish());
    }


}