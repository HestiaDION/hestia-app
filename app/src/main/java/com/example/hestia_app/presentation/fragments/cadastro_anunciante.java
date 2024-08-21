package com.example.hestia_app.presentation.fragments;

import static androidx.core.content.res.ResourcesCompat.getColor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hestia_app.R;

public class cadastro_anunciante extends Fragment {

    // declarando os objetos
    private EditText campo1, campo2, campo3, campo4;
    private Button bt_acao;
    private String tipo_usuario;
    private String etapa;
    private String[] campos;
    private TextView usuario;

    // construtor
    public cadastro_anunciante() {}

    // cria um novo fragment com base nos parâmetros
    public static cadastro_anunciante newInstance(String tipo_usuario, String[] campos, String etapa) {
        cadastro_anunciante fragment = new cadastro_anunciante();
        Bundle args = new Bundle();
        args.putString("tipo_usuario", tipo_usuario);
        args.putStringArray("campos", campos);
        args.putString("etapa", etapa);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tipo_usuario = getArguments().getString("etapa");
            campos = getArguments().getStringArray("campos");
            etapa = getArguments().getString("etapa");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_cadastro_anunciante, container, false);

        // Inicializar as views
        bt_acao = view.findViewById(R.id.bt_acao);
        campo1 = view.findViewById(R.id.campo1);
        campo2 = view.findViewById(R.id.campo2);
        campo3 = view.findViewById(R.id.campo3);
        campo4 = view.findViewById(R.id.campo4);
        usuario = view.findViewById(R.id.tipo_usuario);

        // deixar todos os campos invisíveis
        campo1.setVisibility(View.INVISIBLE);
        campo2.setVisibility(View.INVISIBLE);
        campo3.setVisibility(View.INVISIBLE);
        campo4.setVisibility(View.INVISIBLE);

        // Adicionar os hints se tiver vindo no parâmetro
        if (campos[0] != null) {
            campo1.setHint(campos[0]);
            campo1.setVisibility(View.VISIBLE);
        }
        if (campos[1] != null) {
            campo2.setHint(campos[1]);
            campo2.setVisibility(View.VISIBLE);
        }
        if (campos[2] != null) {
            campo3.setHint(campos[2]);
            campo3.setVisibility(View.VISIBLE);
        }
        if (campos[3] != null) {
            campo4.setHint(campos[3]);
            campo4.setVisibility(View.VISIBLE);
        }

        // colocar a descrição de acordo com o tipo de usuário
        if (tipo_usuario.equals("anunciante")) {
            usuario.setTextColor(getResources().getColor(R.color.azul));
            usuario.setText("ANUNCIANTE");
        } else {
            usuario.setTextColor(getResources().getColor(R.color.vermelho));
            usuario.setText("UNIVERSITÁRIO");
        }

        // colocar o foco no primeiro campo
        campo1.requestFocus();

        // Configurar o botão de cadastro
        bt_acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etapa.equals("fim")) {
                    // Passar para o próximo fragmento
                    cadastro_anunciante fragment = cadastro_anunciante.newInstance("anunciante", new String[]{"Telefone", "Senha", "Confirmar Senha"}, "fim");
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    // Concluir o cadastro
                    Toast.makeText(getActivity(), "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
