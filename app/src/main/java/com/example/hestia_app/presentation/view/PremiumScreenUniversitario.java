package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hestia_app.ConfirmacaoPagamento;
import com.example.hestia_app.R;

public class PremiumScreenUniversitario extends AppCompatActivity {

    Button assinar;
    ImageView goBack;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_IS_PREMIUM = "isPremium";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_screen_universitario);

        assinar = findViewById(R.id.assinar);
        goBack = findViewById(R.id.goBackArrow);

        sharedPreferences  = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isPremium = sharedPreferences.getBoolean(KEY_IS_PREMIUM, false);

        if(isPremium){
            assinar.setVisibility(View.GONE);
        }

        assinar.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), ConfirmacaoPagamento.class);
            startActivity(intent);
            finish();
        });

        goBack.setOnClickListener(v -> finish());
    }


}