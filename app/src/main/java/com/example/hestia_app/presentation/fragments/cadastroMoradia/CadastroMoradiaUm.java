package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
        EditText quantidadeQuartos = view.findViewById(R.id.quantQuartos);
        EditText regrasMoradia = view.findViewById(R.id.regras);
        Button btnProximo = view.findViewById(R.id.bt_acao);
        ImageView btFechar = view.findViewById(R.id.fechar);
        TextView erro1 = view.findViewById(R.id.erro1);
        TextView erro2 = view.findViewById(R.id.erro2);
        TextView erro3 = view.findViewById(R.id.erro3);
        TextView erro32 = view.findViewById(R.id.erro32);
        TextView erro4 = view.findViewById(R.id.erro4);

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean erros1, erros2, erros3, erros4, erros5;

                erros1 = erros(nomeMoradia, erro1, true, false);
                erros2 = erros(descricaoMoradia, erro2, true, false);
                erros3 = erros(quantidadePessoas, erro3, false, true);
                erros4 = erros(quantidadeQuartos, erro32, false, true);
                erros5 = erros(regrasMoradia, erro4, true, false);

                if (erros1 || erros2 || erros3 || erros4 || erros5) {
                    Toast.makeText(getContext(), "Verifique os erros!", Toast.LENGTH_SHORT).show();
                } else {

                    moradia.put("nomeMoradia", nomeMoradia.getText().toString());
                    moradia.put("descricaoMoradia", descricaoMoradia.getText().toString());
                    moradia.put("quantidadePessoas", quantidadePessoas.getText().toString());
                    moradia.put("quantidadeQuartos", quantidadeQuartos.getText().toString());
                    moradia.put("regrasMoradia", regrasMoradia.getText().toString());

                    Log.d("moradia", "onClick: " + moradia);

                    CadastroMoradiaDois fragment = CadastroMoradiaDois.newInstance(moradia);
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

        return view;
    }

    private Boolean erros(EditText campo, TextView erro, Boolean vazio, Boolean num) {

        boolean retorno = false;
        // não pode ser vazio
        if (vazio) {
            if (campo.getText().toString().isEmpty() || campo.getText().toString().length() < 3) {
                erro.setText("Campo deve possuir pelo menos 3 caracteres!");
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