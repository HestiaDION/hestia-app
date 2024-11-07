package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.example.hestia_app.data.api.callbacks.FiltrosTagsCallback;
import com.example.hestia_app.data.api.callbacks.ImagensMoradiaCallback;
import com.example.hestia_app.data.api.callbacks.InformacoesAdicionaisMoradiaCallback;
import com.example.hestia_app.data.api.callbacks.RegistroMoradiaCallback;
import com.example.hestia_app.data.services.FiltrosTagsService;
import com.example.hestia_app.data.services.ImagensMoradiaService;
import com.example.hestia_app.data.services.InformacoesAdicionaisMoradiaService;
import com.example.hestia_app.data.services.MoradiaService;
import com.example.hestia_app.domain.models.FiltrosTags;
import com.example.hestia_app.domain.models.ImagensMoradia;
import com.example.hestia_app.domain.models.InformacoesAdicionaisMoradia;
import com.example.hestia_app.domain.models.Moradia;
import com.example.hestia_app.presentation.view.TelaAviso;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
                bt_acao.setEnabled(false);

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
                    Log.d("moradia", "onClick: " + moradia);

                    // salvar moradia
                    MoradiaService service = new MoradiaService(requireContext());
                    FirebaseAuth autenticar = FirebaseAuth.getInstance();
                    FirebaseUser user = autenticar.getCurrentUser();

                    Moradia moradia1 = new Moradia(user.getEmail(),
                                                   Integer.parseInt(moradia.get("quantidadePessoas")),
                                                   null,
                                                   moradia.get("nomeMoradia"),
                                                   Double.parseDouble(moradia.get("preco")) * 1.1,
                                                   moradia.get("descricaoMoradia"),
                                                   moradia.get("regrasMoradia"),
                                                   Integer.parseInt(moradia.get("quantidadeQuartos")),
                                                   moradia.get("univProxima"),
                                                   moradia.get("cep"),
                                                   moradia.get("municipio"),
                                                   moradia.get("bairro"),
                                                   moradia.get("rua"),
                                                   moradia.get("numero"),
                                                   moradia.get("complemento"));
                    Log.d("Moradia", "onClick: " + moradia1);

                    service.registrarMoradia(moradia1, new RegistroMoradiaCallback() {
                        @Override
                        public void onRegistroSuccess(boolean isRegistered, Moradia moradiaRetorno) {
                            if (isRegistered) {
                                // salvar imagens no banco
                                Log.d("MoradiaRetorno", "onRegistroSuccess: " + moradiaRetorno);
                                ImagensMoradiaService service = new ImagensMoradiaService();
                                ImagensMoradia imagensMoradia = new ImagensMoradia(moradiaRetorno.getId(), transformarLista(moradia.get("imagens")));
                                Log.d("Moradia", "onRegistroSuccess: " + imagensMoradia);
                                service.addImagensMoradias(imagensMoradia, new ImagensMoradiaCallback() {
                                    @Override
                                    public void onSuccess(ImagensMoradia response) {
                                        // salvar as informações adicionais
                                        InformacoesAdicionaisMoradiaService service = new InformacoesAdicionaisMoradiaService();
                                        InformacoesAdicionaisMoradia informacoesAdicionaisMoradia = new InformacoesAdicionaisMoradia(moradiaRetorno.getId(), transformarLista(moradia.get("infosAdicionais")));
                                        Log.d("Moradia", "onSuccess: " + informacoesAdicionaisMoradia);
                                        service.addInfosMoradias(informacoesAdicionaisMoradia, new InformacoesAdicionaisMoradiaCallback() {
                                            @Override
                                            public void onSuccess(InformacoesAdicionaisMoradia response) {
                                                Log.d("Moradia", "onSuccess: " + response);
                                                FiltrosTagsService filtrosTagsService = new FiltrosTagsService();
                                                FiltrosTags filtrosTags = new FiltrosTags(moradiaRetorno.getId(), "moradia", transformarLista(moradia.get("animal")),
                                                        transformarLista(moradia.get("genero")).get(0), transformarLista(moradia.get("pessoa")).get(0), transformarLista(moradia.get("fumo")).get(0), transformarLista(moradia.get("bebida")).get(0),
                                                        transformarLista(moradia.get("casa")));

                                                filtrosTagsService.addFiltrosTag(filtrosTags, new FiltrosTagsCallback() {
                                                    @Override
                                                    public void onFiltroCadastroSuccess(boolean IsRegistered) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("textExplanation", "Moradia registrada com sucesso!");
                                                        bundle.putInt("lottieAnimation", R.raw.house);
                                                        bundle.putString("tipo", "moradia");
                                                        bundle.putString("tela", "MainActivityNavbar");
                                                        Intent intent = new Intent(getContext(), TelaAviso.class);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                        requireActivity().finish();
                                                    }

                                                    @Override
                                                    public void onFiltroCadastroFailure(boolean IsRegistered) {
                                                        Log.d("Moradia", "onFiltroCadastroFailure: " + IsRegistered);
                                                        bt_acao.setEnabled(true);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.d("Moradia", "onFailure: " + t.getMessage());
                                                bt_acao.setEnabled(true);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Log.d("Moradia", "onFailure: " + t.getMessage());
                                        bt_acao.setEnabled(true);
                                    }
                                });
                            }
                            Log.d("Moradia", "onRegistroSuccess: " + isRegistered);
                        }

                        @Override
                        public void onRegistroFailure(boolean isRegistered) {
                            if (!isRegistered) {
                                Toast.makeText(getContext(), "Erro ao registrar moradia!", Toast.LENGTH_SHORT).show();
                            }
                            Log.d("Moradia", "onRegistroFailure: " + isRegistered);
                            bt_acao.setEnabled(true);
                        }
                    });
                }
            }

        });

        return view;
    }

    public List<String> transformarLista(String string) {
        // Remove os colchetes e aspas da string e faz o trim.
        string = string.replace("[", "").replace("]", "").replace("'", "").trim();

        // Substitui a vírgula em "não tenho, mas amo" por um caractere temporário.
        string = string.replace("não tenho, mas amo", "não tenho|mas amo");

        // Divide a string em uma lista, usando vírgula como separador.
        String[] array = string.split(",\\s*");

        // Converte o array em lista e restaura o texto original.
        List<String> result = new ArrayList<>();
        for (String item : array) {
            result.add(item.replace("não tenho|mas amo", "não tenho, mas amo"));
        }

        return result;
    }

}