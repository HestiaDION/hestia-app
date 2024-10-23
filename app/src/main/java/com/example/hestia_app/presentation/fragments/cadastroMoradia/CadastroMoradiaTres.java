package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.hestia_app.R;

import java.util.HashMap;

public class CadastroMoradiaTres extends Fragment {
    public HashMap<String, String> moradia;
    public CadastroMoradiaTres() {
        // Required empty public constructor
    }

    public static CadastroMoradiaTres newInstance(HashMap<String, String> moradia) {
        CadastroMoradiaTres fragment = new CadastroMoradiaTres();
        Bundle args = new Bundle();
        args.putSerializable("moradia", moradia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            moradia = (HashMap<String, String>) getArguments().getSerializable("moradia");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_moradia_tres, container, false);
        ImageButton btFechar = view.findViewById(R.id.fechar);
        ImageButton btVoltar = view.findViewById(R.id.voltar);
        Button btProximo = view.findViewById(R.id.bt_acao);

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
                moradia.clear();
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}