package com.example.hestia_app.presentation.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.FiltroCadastroCallback;
import com.example.hestia_app.data.services.FiltroCadastroService;
import com.example.hestia_app.domain.models.FiltroCadastro;
import com.example.hestia_app.utils.CadastroManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CadastroUniversitarioEtapa extends Fragment {
    HashMap<String, String> usuario;
    CadastroManager cadastroManager = new CadastroManager();

    ChipGroup chipGroup;
    ChipGroup chipGroup2;
    ChipGroup chipGroup3;
    ChipGroup chipGroup4;
    ChipGroup chipGroup5;
    List<String> listaum = new ArrayList<>();
    List<String> listadois = new ArrayList<>();
    List<String> listatres = new ArrayList<>();
    List<String> listaquatro = new ArrayList<>();
    List<String> listacinco = new ArrayList<>();

    public CadastroUniversitarioEtapa() {
        // Required empty public constructor
    }

    public static CadastroUniversitarioEtapa newInstance(HashMap<String, String> usuario) {
        CadastroUniversitarioEtapa fragment = new CadastroUniversitarioEtapa();
        Bundle args = new Bundle();
        args.putSerializable("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            usuario = (HashMap<String, String>) getArguments().getSerializable("usuario");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_universitario_etapa, container, false);

        // Inicializar views
        Button bt_acao = view.findViewById(R.id.bt_acao);
        ImageButton bt_voltar = view.findViewById(R.id.voltar);
        chipGroup = view.findViewById(R.id.chipGroup);
        chipGroup2 = view.findViewById(R.id.chipGroup2);
        chipGroup3 = view.findViewById(R.id.chipGroup3);
        chipGroup4 = view.findViewById(R.id.chipGroup4);
        chipGroup5 = view.findViewById(R.id.chipGroup5);

        // Carregar os filtros e adicionar chips após a resposta
        carregarFiltros("animal", chipGroup, listaum, false);
        carregarFiltros("genero", chipGroup2, listadois, true);
        carregarFiltros("pessoa", chipGroup3, listatres, true);
        carregarFiltros("fumo", chipGroup4, listaquatro, true);
        carregarFiltros("bebida", chipGroup5, listacinco, true);

        bt_acao.setOnClickListener(v -> {
            usuario.put("categoria1", listaum.toString());
            usuario.put("categoria2", listadois.toString());
            usuario.put("categoria3", listatres.toString());
            usuario.put("categoria4", listaquatro.toString());
            usuario.put("categoria5", listacinco.toString());
            CadastroFotoFragment fragment = CadastroFotoFragment.newInstance(usuario, "universitario");
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        bt_voltar.setOnClickListener(v -> {
            cadastroManager.setEtapaAtual(3);
            CadastroAnuncianteUniversitario fragment = CadastroAnuncianteUniversitario.newInstance("universitario", cadastroManager);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void carregarFiltros(String categoria, ChipGroup chipGroup, List<String> selecionados, Boolean unico) {
        FiltroCadastroService service = new FiltroCadastroService();
        service.getFiltrosPorCategoria(categoria, new FiltroCadastroCallback() {
            @Override
            public void onFiltroCadastroSuccess(List<FiltroCadastro> filtros) {
                Log.d("API Response", "onFiltroCadastroSuccess: " + filtros);
                if (!filtros.isEmpty()) {
                    for (FiltroCadastro filtro : filtros) {
                        adicionarChips(filtro.getNome(), chipGroup, selecionados, unico);
                    }
                    Log.d("ChipGroup", "Número de chips: " + chipGroup.getChildCount());
                } else {
                    Log.d("API Response", "A lista de filtros veio vazia.");
                }
            }


            @Override
            public void onFiltroCadastroFailure(String errorMessage) {
                Log.d("API Response", "onFiltroCadastroFailure: " + errorMessage);
            }
        });
    }

    public void adicionarChips(String chips, ChipGroup chipGroup, List<String> selecionados, Boolean unico) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (unico) {
            chipGroup.setSingleSelection(true);
        }

        Chip chip = (Chip) inflater.inflate(R.layout.chip_layout, chipGroup, false);
        chip.setText(chips);
        chip.setId(View.generateViewId());

        chipGroup.addView(chip);

        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selecionados.add(chip.getText().toString());
                } else {
                    selecionados.remove(chip.getText().toString());
                }
                Log.d("Chip", "Selecionados: " + selecionados.toString());
            }
        });
    }
}
