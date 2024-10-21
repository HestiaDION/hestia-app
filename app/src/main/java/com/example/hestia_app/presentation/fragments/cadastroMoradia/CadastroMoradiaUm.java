package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.hestia_app.R;

import java.util.HashMap;
import java.util.Map;

public class CadastroMoradiaUm extends Fragment {

    public HashMap<String, String> moradia = new HashMap<>();

    public CadastroMoradiaUm() {
        // Required empty public constructor
    }

    public static CadastroMoradiaUm newInstance() {
        return new CadastroMoradiaUm();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_moradia_um, container, false);
        EditText nomeMoradia = view.findViewById(R.id.nomeMoradia);
        EditText descricaoMoradia = view.findViewById(R.id.descricaoMoradia);
        EditText quantidadePessoas = view.findViewById(R.id.quantMaximaMoradia);
        Button btnProximo = view.findViewById(R.id.bt_acao);
        ImageView btFechar = view.findViewById(R.id.fechar);

        if (nomeMoradia.getText().toString().isEmpty() || descricaoMoradia.getText().toString().isEmpty() || quantidadePessoas.getText().toString().isEmpty()) {
            if (nomeMoradia.getText().toString().isEmpty()) {
                nomeMoradia.setError("Por favor, insira um nome");
            } else if (descricaoMoradia.getText().toString().isEmpty()) {
                descricaoMoradia.setError("Por favor, insira uma descrição");
            } else if (quantidadePessoas.getText().toString().isEmpty()) {
                quantidadePessoas.setError("Por favor, insira um número");
            }
        }

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moradia.put("nomeMoradia", nomeMoradia.getText().toString());
                moradia.put("descricaoMoradia", descricaoMoradia.getText().toString());
                moradia.put("quantidadePessoas", quantidadePessoas.getText().toString());

                Log.d("moradia", "onClick: " + moradia);

                CadastroMoradiaDois fragment = CadastroMoradiaDois.newInstance(moradia);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
                moradia.clear();
            }
        });

        return view;
    }
}