package com.example.hestia_app.presentation.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hestia_app.R;
import com.example.hestia_app.utils.CadastroManager;

import java.io.Serializable;

public class cadastro_anunciante_universitario extends Fragment {

    private String usuario;
    private CadastroManager cadastroManager;
    private ProgressBar progressBar;

    // construtor
    public cadastro_anunciante_universitario() {}

    // cria um novo fragment com base nos parâmetros
    public static cadastro_anunciante_universitario newInstance(String tipo_usuario, CadastroManager cadastroManager) {
        cadastro_anunciante_universitario fragment = new cadastro_anunciante_universitario();
        Bundle args = new Bundle();
        args.putString("tipo_usuario", tipo_usuario);
        args.putParcelable("cadastro_manager", cadastroManager);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = getArguments().getString("tipo_usuario");
            cadastroManager = getArguments().getParcelable("cadastro_manager");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);

        // declarando os objetos
        Button bt_acao = view.findViewById(R.id.bt_acao);
        EditText campo1 = view.findViewById(R.id.campo1);
        EditText campo2 = view.findViewById(R.id.campo2);
        EditText campo3 = view.findViewById(R.id.campo3);
        EditText campo4 = view.findViewById(R.id.campo4);
        TextView tipo_usuario = view.findViewById(R.id.tipo_usuario);
        View view1 = view.findViewById(R.id.view1);
        View view2 = view.findViewById(R.id.view2);
        View view3 = view.findViewById(R.id.view3);
        View view4 = view.findViewById(R.id.view4);
        progressBar = view.findViewById(R.id.progresso);

        // deixar todos os campos e views invisíveis
        campo1.setVisibility(View.INVISIBLE);
        campo2.setVisibility(View.INVISIBLE);
        campo3.setVisibility(View.INVISIBLE);
        campo4.setVisibility(View.INVISIBLE);

        view1.setVisibility(View.INVISIBLE);
        view2.setVisibility(View.INVISIBLE);
        view3.setVisibility(View.INVISIBLE);
        view4.setVisibility(View.INVISIBLE);

        // colocar a descrição de acordo com o tipo de usuário
        String[] campos;
        if (usuario.equals("anunciante")) {
            tipo_usuario.setTextColor(getResources().getColor(R.color.azul));
            tipo_usuario.setText("ANUNCIANTE");
            campos = cadastroManager.getCamposDaEtapaAnunciante();
        } else {
            tipo_usuario.setTextColor(getResources().getColor(R.color.vermelho));
            tipo_usuario.setText("UNIVERSITÁRIO");
            campos = cadastroManager.getCamposDaEtapaUniversitario();
        }

        // Adicionar os hints se tiver vindo no parâmetro
        if (campos[0] != null) {
            campo1.setHint(campos[0]);
            campo1.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
        }
        if (campos[1] != null) {
            campo2.setHint(campos[1]);
            campo2.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        }
        if (campos[2] != null) {
            campo3.setHint(campos[2]);
            campo3.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
        }
        if (campos[3] != null) {
            campo4.setHint(campos[3]);
            campo4.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
        }

        // colocar o foco no primeiro campo
        campo1.requestFocus();

        // Configurar o botão de cadastro
        bt_acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarProgressBar(usuario, cadastroManager);
                if (usuario.equals("anunciante")) {
                    if (cadastroManager.hasNextEtapaAnunciante()) {
                        cadastroManager.nextEtapaAnunciante();

                        cadastro_anunciante_universitario fragment = cadastro_anunciante_universitario.newInstance("anunciante", cadastroManager);
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        // Concluir o cadastro
                        Toast.makeText(getActivity(), "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                } else if (usuario.equals("universitario")) {
                    if (cadastroManager.hasNextEtapaUniversitario()) {
                        cadastroManager.nextEtapaUniversitario();

                        cadastro_anunciante_universitario fragment = cadastro_anunciante_universitario.newInstance("universitario", cadastroManager);
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });

        return view;
    }

    private void atualizarProgressBar(String tipo_usuario, CadastroManager cadastroManager) {
        // Calcular progresso como uma porcentagem do total de etapas

        if (tipo_usuario.equals("anunciante")) {
            if (cadastroManager.getEtapaAtual() == 1) {
                progressBar.setProgress(50, true);
            } else if (cadastroManager.getEtapaAtual() == 2) {
                progressBar.setProgress(100, true);
            }
        } else {
            if (cadastroManager.getEtapaAtual() == 1) {
                progressBar.setProgress(33, true);
            } else if (cadastroManager.getEtapaAtual() == 2) {
                progressBar.setProgress(66, true);
            } else if (cadastroManager.getEtapaAtual() == 3) {
                progressBar.setProgress(100, true);
            }
        }

    }
}