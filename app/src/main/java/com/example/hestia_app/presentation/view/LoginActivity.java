package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hestia_app.R;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    TextView cadastroRedirect;
    EditText email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        cadastroRedirect = findViewById(R.id.cadastroRedirect);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.password);
    }
}