package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hestia_app.R;

import java.util.HashMap;

public class CadastroMoradiaSeis extends Fragment {

    private HashMap<String, String> moradia;

    public CadastroMoradiaSeis() {
        // Required empty public constructor
    }

    public static CadastroMoradiaSeis newInstance(HashMap<String, String> moradia) {
        CadastroMoradiaSeis fragment = new CadastroMoradiaSeis();
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
        View view = inflater.inflate(R.layout.fragment_cadastro_moradia_seis, container, false);
        EditText preco = view.findViewById(R.id.preco);
        TextView precoFinal = view.findViewById(R.id.precoFinal);
        Button bt_acao = view.findViewById(R.id.bt_acao);
        ImageButton bt_voltar = view.findViewById(R.id.voltar);
        ImageButton bt_fechar = view.findViewById(R.id.fechar);

        bt_fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
                moradia.clear();
            }
        });

        bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        preco.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Verifica se a tecla Enter foi pressionada
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    // Executa a ação desejada ao pressionar Enter
                    String precoText = preco.getText().toString().trim();
                    if (!precoText.isEmpty()) {
                        double preco_final = Double.parseDouble(precoText) * 1.1;
                        String texto = "R$ " + String.format("%.2f", preco_final);
                        precoFinal.setText(texto);
                    } else {
                        precoFinal.setText("");
                    }

                    return true; // Indica que o evento foi consumido
                }
                return false; // Permite que o evento continue se não foi consumido
            }
        });



        bt_acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preco.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "O preço deve ser definido!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(preco.getText().toString()) <= 0) {
                    Toast.makeText(getContext(), "O preço deve ser positivo!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    moradia.put("preco", preco.getText().toString());
                    moradia.put("precoFinal", precoFinal.getText().toString());

                    // salvar no banco

                }
            }
        });

        return view;
    }
}