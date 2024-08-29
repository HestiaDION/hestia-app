package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.hestia_app.R;

public class UserTerms extends AppCompatActivity {


    ImageView goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_terms);

        goBack = findViewById(R.id.goBack);
        goBack.setOnClickListener(v -> finish());
    }
}