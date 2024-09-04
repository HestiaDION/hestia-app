package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hestia_app.R;

public class EscolherUsuario extends AppCompatActivity {

    RadioGroup opcoes;
    Button bt_acao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_usuario);

        opcoes = findViewById(R.id.opcoes);
        bt_acao = findViewById(R.id.bt_acao);

        bt_acao.setOnClickListener(view -> {
            int opcaoSelecionada = opcoes.getCheckedRadioButtonId();

            if (opcaoSelecionada == -1) {
                Toast.makeText(this, "Por favor, selecione um objetivo.", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selecionado = findViewById(opcaoSelecionada);
                String tipo_usuario = selecionado.getText().toString();
                Intent intent = new Intent(EscolherUsuario.this, MainActivity.class);
                if (tipo_usuario.toLowerCase().contains("anunciar")) {
                    tipo_usuario = "anunciante";
                } else if (tipo_usuario.toLowerCase().contains("alugar")) {
                    tipo_usuario = "universitario";
                }
                intent.putExtra("tipo_usuario", tipo_usuario);
                startActivity(intent);
            }
        });
    }
}