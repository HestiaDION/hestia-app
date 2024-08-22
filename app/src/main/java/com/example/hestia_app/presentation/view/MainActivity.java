// MainActivity.java
package com.example.hestia_app.presentation.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.fragments.cadastro_anunciante_universitario;
import com.example.hestia_app.utils.CadastroManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pegar o tipo de usupario
        String tipo_usuario = getIntent().getStringExtra("tipo_usuario");

        // Inicializar o CadastroManager
        CadastroManager cadastro_manager = new CadastroManager();

        // Criar a primeira instância do fragmento
        cadastro_anunciante_universitario fragment = cadastro_anunciante_universitario.newInstance(tipo_usuario, cadastro_manager);

        // Adicionar o fragmento à MainActivity
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }
}
