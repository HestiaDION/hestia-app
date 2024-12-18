package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
        EditText complemento = view.findViewById(R.id.complemento);
        EditText univProxima = view.findViewById(R.id.universidade);
        EditText descricao = view.findViewById(R.id.descricaoLocal);
        ImageButton btFechar = view.findViewById(R.id.fechar);
        ImageButton btVoltar = view.findViewById(R.id.voltar);
        TextView erro1 = view.findViewById(R.id.erro1);
        TextView erro12 = view.findViewById(R.id.erro12);
        TextView erro2 = view.findViewById(R.id.erro2);
        TextView erro3 = view.findViewById(R.id.erro3);
        TextView erro32 = view.findViewById(R.id.erro32);
        TextView erro4 = view.findViewById(R.id.erro4);
        TextView erro5 = view.findViewById(R.id.erro5);
        TextView erro6 = view.findViewById(R.id.erro6);

        // cep
        cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cepString = s.toString().replaceAll("[^\\d]", ""); // Remove todos os caracteres não numéricos

                if (cepString.length() > 5) {
                    cepString = cepString.substring(0, 5) + "-" + cepString.substring(5, Math.min(cepString.length(), 8)); // Formata o CEP
                }

                // Remove o listener temporariamente para evitar loops infinitos
                cep.removeTextChangedListener(this);
                cep.setText(cepString);
                cep.setSelection(cepString.length()); // Move o cursor para o final
                cep.addTextChangedListener(this);
            }
        });

        // proximo fragment
        Button btnProximo = view.findViewById(R.id.bt_acao);
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erros1, erros2, erros3, erros4, erros5, erros6, erros7, erros8;

                erros1 = erros(cep, erro1, true, false);
                erros2 = erros(municipio, erro12, true, false);
                erros3 = erros(bairro, erro2, true, false);
                erros4 = erros(rua, erro3, true, false);
                erros5 = erros(numero, erro32, false, true);
                erros6 = erros(complemento, erro4, true, false);
                erros7 = erros(univProxima, erro5, true, false);
                erros8 = erros(descricao, erro6, true, false);

                if (erros1 || erros2 || erros3 || erros4 || erros5 || erros6 || erros7 || erros8) {
                    Toast.makeText(getContext(), "Verifique os erros!", Toast.LENGTH_SHORT).show();
                    Log.d("errosMoradia", "onClick: " + erros1 + erros2 + erros3 + erros4 + erros5 + erros6 + erros7 + erros8);
                } else {
                    // colocar no hasmap
                    moradia.put("cep", cep.getText().toString().replace("-", ""));
                    moradia.put("municipio", municipio.getText().toString());
                    moradia.put("bairro", bairro.getText().toString());
                    moradia.put("rua", rua.getText().toString());
                    moradia.put("numero", numero.getText().toString());
                    moradia.put("complemento", complemento.getText().toString());
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
                if (getContext() != null && getActivity() != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_logout_confirmation, null);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();

                    Button btnConfirmLogout = dialogView.findViewById(R.id.btn_confirm_logout);
                    Button btnCancelLogout = dialogView.findViewById(R.id.btn_cancel_logout);

                    btnConfirmLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();

                            requireActivity().finish();
                            moradia.clear();

                        }
                    });

                    // Se o usuário cancelar, apenas fecha o modal
                    btnCancelLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    // Exibir o modal
                    alertDialog.show();
                }
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