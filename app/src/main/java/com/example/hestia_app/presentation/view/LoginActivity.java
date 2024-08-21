package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.fragments.cadastro_anunciante;

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

        loginButton.setOnClickListener(v -> {
            // abrir main mandando os parâmetros
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("tipo_usuario", "anunciante");
            intent.putExtra("campos", new String[]{"Nome", "Cidade", "E-mail", "Telefone"});
            intent.putExtra("etapa", "etapa1");
            startActivity(intent);
        });
    }
}