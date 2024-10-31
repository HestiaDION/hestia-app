package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hestia_app.R;

import java.util.HashMap;

public class CadastroMoradiaCinco extends Fragment {

    private HashMap<String, String> moradia;

    public CadastroMoradiaCinco() {
        // Required empty public constructor
    }

    public static CadastroMoradiaCinco newInstance(HashMap<String,String> moradia) {
        CadastroMoradiaCinco fragment = new CadastroMoradiaCinco();
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
        View view = inflater.inflate(R.layout.fragment_cadastro_moradia_cinco, container, false);
        EditText cep = view.findViewById(R.id.cep);
        EditText municipio = view.findViewById(R.id.municipio);
        EditText bairro = view.findViewById(R.id.bairro);
        EditText rua = view.findViewById(R.id.rua);
        EditText numero = view.findViewById(R.id.num);
        EditText univProxima = view.findViewById(R.id.universidade);
        EditText descricao = view.findViewById(R.id.descricaoLocal);
        ImageButton btFechar = view.findViewById(R.id.fechar);
        ImageButton btVoltar = view.findViewById(R.id.voltar);
        TextView erro1 = view.findViewById(R.id.erro1);
        TextView erro2 = view.findViewById(R.id.erro2);
        TextView erro3 = view.findViewById(R.id.erro3);
        TextView erro4 = view.findViewById(R.id.erro4);
        TextView erro5 = view.findViewById(R.id.erro5);

        // proximo fragment
        Button btnProximo = view.findViewById(R.id.bt_acao);
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erros1, erros2, erros3, erros4, erros5, erros6, erros7;

                erros1 = erros(cep, erro1, true, false);
                erros2 = erros(municipio, erro1, true, false);
                erros3 = erros(bairro, erro2, true, false);
                erros4 = erros(rua, erro3, true, false);
                erros5 = erros(numero, erro3, false, true);
                erros6 = erros(univProxima, erro4, true, false);
                erros7 = erros(descricao, erro5, true, false);

                if (erros1 || erros2 || erros3 || erros4 || erros5 || erros6 || erros7) {
                    Toast.makeText(getContext(), "Verifique os erros!", Toast.LENGTH_SHORT).show();
                } else {
                    // colocar no hasmap
                    moradia.put("cep", cep.getText().toString());
                    moradia.put("municipio", municipio.getText().toString());
                    moradia.put("bairro", bairro.getText().toString());
                    moradia.put("rua", rua.getText().toString());
                    moradia.put("numero", numero.getText().toString());
                    moradia.put("univProxima", univProxima.getText().toString());
                    moradia.put("descricao", descricao.getText().toString());

                    // proximo
                    CadastroMoradiaSeis fragment = CadastroMoradiaSeis.newInstance(moradia);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

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

        return view;
    }

    private Boolean erros(EditText campo, TextView erro, Boolean vazio, Boolean num) {

        boolean retorno = false;
        // não pode ser vazio
        if (vazio) {
            if (campo.getText().toString().isEmpty() || campo.getText().toString().length() < 5) {
                erro.setText("Campo deve possuir pelo menos 5 caracteres!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        // é um número
        if (num) {
            if (!campo.getText().toString().isEmpty()){
                if (Integer.parseInt(campo.getText().toString()) <= 0) {
                    erro.setText("Campo deve ser maior que 0!");
                    erro.setVisibility(View.VISIBLE);
                    retorno = true;
                } else {
                    erro.setVisibility(View.GONE);
                }
            } else {
                erro.setText("Campo deve ser preenchido!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            }
        }

        return retorno;
    }
}