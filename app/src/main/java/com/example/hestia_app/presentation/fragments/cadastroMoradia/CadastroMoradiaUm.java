package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hestia_app.R;

import java.util.Map;

public class CadastroMoradiaUm extends Fragment {

    private Map<String, String> moradia;

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

        moradia.put("nomeMoradia", nomeMoradia.getText().toString());
        moradia.put("descricaoMoradia", descricaoMoradia.getText().toString());
        moradia.put("quantidadePessoas", quantidadePessoas.getText().toString());

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastroMoradiaDois fragment = new CadastroMoradiaDois();
                Bundle args = new Bundle();
                args.putParcelable("moradia", (Parcelable) moradia);
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
        return view;
    }
}