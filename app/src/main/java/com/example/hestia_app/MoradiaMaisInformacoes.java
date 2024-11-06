package com.example.hestia_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.hestia_app.data.api.callbacks.MoradiaByIdCallback;
import com.example.hestia_app.data.services.MoradiaService;
import com.example.hestia_app.domain.models.Moradia;

import java.util.UUID;

public class MoradiaMaisInformacoes extends AppCompatActivity {

    TextView nomeMoradia, quantidadePessoas, universidadePerto, quantidadeQuartos, capacidadePessoas,
    descricao;


    MoradiaService moradiaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moradia_mais_informacoes);

        nomeMoradia = findViewById(R.id.nomeDaMoradia);
        quantidadePessoas = findViewById(R.id.quantidadePessoas);
        universidadePerto = findViewById(R.id.universidadePerto);
        quantidadeQuartos = findViewById(R.id.quantidadeQuartos);
        capacidadePessoas = findViewById(R.id.capacidadePessoas);
        descricao = findViewById(R.id.descricaoTexto);

        moradiaService = new MoradiaService(getApplicationContext());
        String moradiaIdString = getIntent().getStringExtra("moradiaId");


        // get de informações
        moradiaService.getMoradiaById(UUID.fromString(moradiaIdString), new MoradiaByIdCallback() {
            @Override
            public void onSuccess(Moradia moradias) {

                // populando campos da tela
                nomeMoradia.setText(moradias.getNomeCasa());
                quantidadePessoas.setText(moradias.getQuantidadeMaximaPessoas());
                universidadePerto.setText(moradias.getUniversidadeProxima());
                quantidadeQuartos.setText(moradias.getQuantidadeQuartos());
                capacidadePessoas.setText(moradias.getQuantidadeMaximaPessoas());
                descricao.setText(moradias.getDescricao());

            }

            @Override
            public void onFailure(String message) {
                Log.e("Mais Informações", "Falha no Get Moradia by ID");
            }
        });


    }
}