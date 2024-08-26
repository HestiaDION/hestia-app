package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.fragments.cadastro_anunciante;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Receber os parâmetros do Intent
        Intent intent = getIntent();
        String tipo_usuario = intent.getStringExtra("tipo_usuario");
        String[] campos = intent.getStringArrayExtra("campos");
        String etapa = intent.getStringExtra("etapa");

        // Criar uma instância do fragmento com os parâmetros recebidos
        cadastro_anunciante fragment = cadastro_anunciante.newInstance(tipo_usuario, campos, etapa);

        // Adicionar o fragmento à MainActivity
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }
}