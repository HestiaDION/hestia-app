package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.fragments.cadastroMoradia.CadastroMoradiaUm;

public class AdicionarMoradia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_moradia);

        // Criar a primeira instância do fragmento
        CadastroMoradiaUm fragment = CadastroMoradiaUm.newInstance();

        // Adicionar o fragmento à MainActivity
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }
}