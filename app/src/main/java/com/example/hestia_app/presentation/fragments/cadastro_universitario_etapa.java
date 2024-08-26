package com.example.hestia_app.presentation.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hestia_app.R;

public class cadastro_universitario_etapa extends Fragment {

    public cadastro_universitario_etapa() {
        // Required empty public constructor
    }

    /**
     * É usado esse Factory method para criar uma nova instância
     * do fragmento.
     * @return A new instance of fragment cadastro_universitario_etapa.
     */
    public static cadastro_universitario_etapa newInstance() {
        cadastro_universitario_etapa fragment = new cadastro_universitario_etapa();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);
        Button bt_acao = view.findViewById(R.id.bt_acao);
        
        // Infla o layout para esse fragmento
        return inflater.inflate(R.layout.fragment_cadastro_universitario_etapa, container, false);
    }
}