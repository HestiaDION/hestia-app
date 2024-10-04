package com.example.hestia_app.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.example.hestia_app.R;
import com.example.hestia_app.utils.CadastroManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CadastroUniversitarioEtapa extends Fragment {
    HashMap<String, String> usuario;
    CadastroManager cadastroManager = new CadastroManager();

    private List<String> categorias;
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
        chipGroup2 = view.findViewById(R.id.chipGroup2);
        chipGroup3 = view.findViewById(R.id.chipGroup3);
        chipGroup4 = view.findViewById(R.id.chipGroup4);
        chipGroup5 = view.findViewById(R.id.chipGroup5);

        ArrayList<String> chips = new ArrayList<>();
        chips.add("Cachorro");
        chips.add("Gato");
        chips.add("Réptil");
        chips.add("Anfíbio");
        chips.add("Pássaro");
        chips.add("Não tenho");
        chips.add("Gosto muito");
        chips.add("Alergia");
        chips.add("Não tenho, mas amo");

        adicionarChips(chips, chipGroup, listaum, false);

        ArrayList<String> chips2 = new ArrayList<>();
        chips2.add("Mulheres");
        chips2.add("Homens");
        chips2.add("Outros");
        chips2.add("Não me importo");

        adicionarChips(chips2, chipGroup2, listadois, true);

        ArrayList<String> chips3 = new ArrayList<>();
        chips3.add("1");
        chips3.add("2");
        chips3.add("3");
        chips3.add("4 ou mais");

        adicionarChips(chips3, chipGroup3, listatres, true);

        ArrayList<String> chips4 = new ArrayList<>();
        chips4.add("Fumo socialmente");
        chips4.add("Fumo quando bebo");
        chips4.add("Não fumo");
        chips4.add("Fumante");
        chips4.add("Tentando parar");

        adicionarChips(chips4, chipGroup4, listaquatro, true);

        ArrayList<String> chips5 = new ArrayList<>();
        chips5.add("Não bebo");
        chips5.add("Bebo com moderação");
        chips5.add("Ocasiões especiais");
        chips5.add("Todo dia");
        chips5.add("Já bebi, mas não bebo mais");
        chips5.add("Socialmente, aos fins de semana");

        adicionarChips(chips5, chipGroup5, listacinco, true);

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

        // bloquear botão de voltar
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });

        // Retorna o layout inflado e configurado
        return view;
    }

    public void adicionarChips(ArrayList<String> chips, ChipGroup chipGroup, List<String> selecionados, Boolean unico) {
        Random random = new Random();
        for (String chip : chips) {
            Chip c = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.chip_layout, null);
            c.setText(chip);
            c.setId(random.nextInt());
            chipGroup.addView(c);
            if (unico) {
                chipGroup.setSingleSelection(true);
            }
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // O chip foi clicado
                        Log.d("Chip", selecionados.toString());
                        selecionados.add(c.getText().toString());
                    } else {
                        // O chip foi desmarcado
                        Log.d("Chip", selecionados.toString());
                        selecionados.remove(c.getText().toString());
                    }
                }
            });
        }
    }
}