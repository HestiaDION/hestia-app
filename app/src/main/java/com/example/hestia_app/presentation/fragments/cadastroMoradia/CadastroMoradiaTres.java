package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hestia_app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CadastroMoradiaTres extends Fragment {
    public HashMap<String, String> moradia;
    public CadastroMoradiaTres() {
        // Required empty public constructor
    }

    public static CadastroMoradiaTres newInstance(HashMap<String, String> moradia) {
        CadastroMoradiaTres fragment = new CadastroMoradiaTres();
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
        View view = inflater.inflate(R.layout.fragment_cadastro_moradia_tres, container, false);
        ImageButton btFechar = view.findViewById(R.id.fechar);
        ImageButton btVoltar = view.findViewById(R.id.voltar);
        Button btProximo = view.findViewById(R.id.bt_acao);
        EditText info1 = view.findViewById(R.id.info1);
        EditText info2 = view.findViewById(R.id.info2);
        EditText info3 = view.findViewById(R.id.info3);
        EditText info4 = view.findViewById(R.id.info4);
        EditText info5 = view.findViewById(R.id.info5);
        HashMap<String, String> infos = new HashMap<>();

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

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!info1.getText().toString().equals("")) {
                    infos.put("info1", info1.getText().toString());
                    info1.setText("");
                }
                if (!info2.getText().toString().equals("")) {
                    infos.put("info2", info2.getText().toString());
                    info2.setText("");
                }
                if (!info3.getText().toString().equals("")) {
                    infos.put("info3", info3.getText().toString());
                    info3.setText("");
                }
                if (!info4.getText().toString().equals("")) {
                    infos.put("info4", info4.getText().toString());
                    info4.setText("");
                }
                if (!info5.getText().toString().equals("")) {
                    infos.put("info5", info5.getText().toString());
                    info5.setText("");
                }

                if (infos.size() < 3 || infos.size() > 5) {
                    Toast.makeText(getContext(), "Adicione no mínimo 3 informações!", Toast.LENGTH_SHORT).show();
                } else {
                    List<String> values = new ArrayList<>(infos.values());
                    moradia.put("infosAdicionais", values.toString());
                    CadastroMoradiaQuatro fragment = CadastroMoradiaQuatro.newInstance(moradia);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }
}