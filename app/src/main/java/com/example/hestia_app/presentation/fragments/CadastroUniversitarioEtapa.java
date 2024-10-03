package com.example.hestia_app.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hestia_app.R;
import com.example.hestia_app.utils.CadastroManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CadastroUniversitarioEtapa extends Fragment {
    HashMap<String, String> usuario;
    CadastroManager cadastroManager = new CadastroManager();
    ChipGroup chipGroup;

    public CadastroUniversitarioEtapa() {
        // Required empty public constructor
    }

    /**
     * É usado esse Factory method para criar uma nova instância
     * do fragmento.
     * @return A new instance of fragment cadastro_universitario_etapa.
     */
    public static CadastroUniversitarioEtapa newInstance(HashMap<String, String> usuario) {
        CadastroUniversitarioEtapa fragment = new CadastroUniversitarioEtapa();
        Bundle args = new Bundle();
        args.putSerializable("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            usuario = (HashMap<String, String>) getArguments().getSerializable("usuario");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_universitario_etapa, container, false);
        Button bt_acao = view.findViewById(R.id.bt_acao);
        ImageButton bt_voltar = view.findViewById(R.id.voltar);
        chipGroup = view.findViewById(R.id.chipGroup);

        ArrayList<String> chips = new ArrayList<>();
        chips.add("Ciência da Computação");
        chips.add("Ciências da Computação");
        chips.add("Engenharia da Computação");
        chips.add("Engenharia Eletrônica");

        Random random = new Random();
        for (String chip : chips) {
            Chip c = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.chip_layout, null);
            c.setText(chip);
            c.setId(random.nextInt());
            chipGroup.addView(c);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // O chip foi clicado
                        Log.d("Chip", "Chip está selecionado");
                    } else {
                        // O chip foi desmarcado
                        Log.d("Chip", "Chip está desmarcado");
                    }
                }
            });
        }

        bt_acao.setOnClickListener(v -> {
            CadastroFotoFragment fragment = CadastroFotoFragment.newInstance(usuario, "universitario");
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastroManager.setEtapaAtual(3);
                CadastroAnuncianteUniversitario fragment = CadastroAnuncianteUniversitario.newInstance("universitario", cadastroManager);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Retorna o layout inflado e configurado
        return view;
    }

}