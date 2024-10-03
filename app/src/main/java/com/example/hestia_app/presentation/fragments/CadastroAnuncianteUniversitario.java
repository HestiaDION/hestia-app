package com.example.hestia_app.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.EscolherUsuario;
import com.example.hestia_app.utils.CadastroManager;
import com.example.hestia_app.utils.ViewUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class CadastroAnuncianteUniversitario extends Fragment {

    private String usuario;
    private CadastroManager cadastroManager;
    private ProgressBar progressBar;
    Boolean erros1, erros2, erros3, erros4;

    // dicionários para armazenar as informações
    private static HashMap<String, String> anunciante = new HashMap<>();
    private static HashMap<String, String> universitario = new HashMap<>();

    // construtor
    public CadastroAnuncianteUniversitario() {
    }

    // cria um novo fragment com base nos parâmetros
    public static CadastroAnuncianteUniversitario newInstance(String tipo_usuario, CadastroManager cadastroManager) {
        CadastroAnuncianteUniversitario fragment = new CadastroAnuncianteUniversitario();
        Bundle args = new Bundle();
        args.putString("tipo_usuario", tipo_usuario);
        args.putParcelable("cadastro_manager", cadastroManager);
        args.putSerializable("dadosUniversitario", universitario);
        args.putSerializable("dadosAnunciante", anunciante);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = getArguments().getString("tipo_usuario");
            cadastroManager = getArguments().getParcelable("cadastro_manager");
            universitario = (HashMap<String, String>) getArguments().getSerializable("dadosUniversitario");
            anunciante = (HashMap<String, String>) getArguments().getSerializable("dadosAnunciante");
            Log.d("universitario", universitario.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
        TextView erro1 = view.findViewById(R.id.erro1);
        TextView erro2 = view.findViewById(R.id.erro2);
        TextView erro3 = view.findViewById(R.id.erro3);
        TextView erro4 = view.findViewById(R.id.erro4);
        ImageButton olho = view.findViewById(R.id.ver_senha);
        ImageButton olho2 = view.findViewById(R.id.ver_senha2);
        ImageButton olho3 = view.findViewById(R.id.ver_senha3);
        ImageButton calendario = view.findViewById(R.id.calendario);
        ImageButton bt_voltar = view.findViewById(R.id.voltar);
        ImageButton genero = view.findViewById(R.id.selecionar_genero);
        ImageButton genero2 = view.findViewById(R.id.selecionar_genero2);

        progressBar = view.findViewById(R.id.progresso);
        int etapaAtual = cadastroManager.getEtapaAtual();;

        // ao apertar no botão voltar, volta para a tela anterior
        bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastroManager.setEtapaAnterior();
                CadastroAnuncianteUniversitario fragment = CadastroAnuncianteUniversitario.newInstance(usuario, cadastroManager);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // deixar todos os campos e views invisíveis
        esconderCamposEViews(campo1, campo2, campo3, campo4,
                view1, view2, view3, view4,
                olho, olho2, calendario);

        if (etapaAtual < 1) {
            // voltar para etapa de escolha do usuário
            Intent intent = new Intent(getActivity(), EscolherUsuario.class);
            startActivity(intent);
            requireActivity().finish();
        }

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
        colocarHintTipo(campo1, view1, campo2, view2, campo3, view3, campo4, view4, campos, olho, olho2, calendario, genero, genero2, olho3);

        // preencher campos
        preencherCampos(etapaAtual, usuario, campo1, campo2, campo3, campo4);

        // colocar o foco no primeiro campo
        campo1.requestFocus();

        // Atualizar a barra de progresso
        atualizarProgressBar(usuario, etapaAtual);

        // Configurar o botão de cadastro
        bt_acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usuario.equals("anunciante")) {
                    if (cadastroManager.hasNextEtapaAnunciante()) {
                        // salvar as informações
                        if (cadastroManager.getEtapaAtual() == 1) {
                            erros1 = erros(campo1, "vazio", erro1, true, false, true, false, false, false, false, false, false, false, false);
                            erros2 = erros(campo2, "vazio", erro2, true, false, false, false, false, false, false, true, false, false, false);
                            erros3 = erros(campo3, "vazio", erro3, true, false, false, false, false, false, false, false, true, false, false);
                            erros4 = erros(campo4, "vazio", erro4, true, true, false, false, false, false, false, false, false, false, false);

                            if (erros1 || erros2 || erros3 || erros4) {
                                return;
                            } else {
                                anunciante.put("nome", campo1.getText().toString());
                                anunciante.put("municipio", campo2.getText().toString());
                                anunciante.put("dt_nascimento", campo3.getText().toString());
                                anunciante.put("email", campo4.getText().toString());
                            }
                        } else if (cadastroManager.getEtapaAtual() == 2) {
                            erros1 = erros(campo1, "vazio", erro1, true, false, false, true, false, false, false, false, false, false, false);
                            erros2 = erros(campo2, "vazio", erro2, true, false, false, false, false, false, false, false, false, false, true);
                            erros3 = erros(campo3, "vazio", erro3, true, false, false, false, true, false, false, false, false, false, false);
                            erros4 = erros(campo4, campo3.getText().toString(), erro4, true, false, false, false, true, false, false, false, false, true, false);

                            if (erros1 || erros2 || erros3 || erros4) {
                                return;
                            } else {
                                anunciante.put("telefone", campo1.getText().toString());
                                anunciante.put("genero", campo2.getText().toString());
                                anunciante.put("senha", campo3.getText().toString());
                                anunciante.put("conf_senha", campo4.getText().toString());
                            }
                        }

                        cadastroManager.nextEtapaAnunciante();

                        if (cadastroManager.getEtapaAtual() == 3) {
                            CadastroFotoFragment fragment = CadastroFotoFragment.newInstance(anunciante, "anunciante");
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            CadastroAnuncianteUniversitario fragment = CadastroAnuncianteUniversitario.newInstance(usuario, cadastroManager);
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                } else if (usuario.equals("universitario")) {
                    if (cadastroManager.hasNextEtapaUniversitario()) {
                        // salvando as informações
                        if (cadastroManager.getEtapaAtual() == 1) {
                            erros1 = erros(campo1, "vazio", erro1, true, false, true, false, false, false, false, false, false, false, false);
                            erros2 = erros(campo2, "vazio", erro2, true, false, false, false, false, false, false, true, false, false, false);
                            erros3 = erros(campo3, "vazio", erro3, true, false, false, false, false, false, false, false, true, false, false);
                            erros4 = erros(campo4, "vazio", erro4, true, false, false, false, false, false, false, false, false, false, true);
                            if (erros1 || erros2 || erros3 || erros4) {
                                return;
                            } else {
                                universitario.put("nome", campo1.getText().toString());
                                universitario.put("municipio", campo2.getText().toString());
                                universitario.put("dt_nascimento", campo3.getText().toString());
                                universitario.put("genero", campo4.getText().toString());
                            }
                        } else if (cadastroManager.getEtapaAtual() == 2) {
                            erros1 = erros(campo1, "vazio", erro1, true, true, false, false, false, false, false, false, false, false, false);
                            erros2 = erros(campo2, "vazio", erro2, true, false, false, true, false, false, false, false, false, false, false);
                            erros3 = erros(campo3, "vazio", erro3, false, false, false, false, false, false, true, false, false, false, false);

                            if (erros1 || erros2 || erros3) {
                                return;
                            } else {
                                universitario.put("email", campo1.getText().toString());
                                universitario.put("telefone", campo2.getText().toString());
                                universitario.put("universidade", campo3.getText().toString());
                            }
                        } else if (cadastroManager.getEtapaAtual() == 3) {
                            erros1 = erros(campo1, "vazio", erro1, true, false, false, false, false, true, false, false, false, false, false);
                            erros2 = erros(campo2, "vazio", erro2, true, false, false, false, true, false, false, false, false, false, false);
                            erros3 = erros(campo3, campo2.getText().toString(), erro3, true, false, false, false, true, false, false, false, false, true, false);

                            if (erros1 || erros2 || erros3) {
                                return;
                            } else {
                                universitario.put("dne", campo1.getText().toString());
                                universitario.put("senha", campo2.getText().toString());
                                universitario.put("conf_senha", campo3.getText().toString());
                            }
                        }

                        cadastroManager.nextEtapaUniversitario();
                        if (cadastroManager.getEtapaAtual() == 4) {
                            CadastroUniversitarioEtapa fragmentEtapa = CadastroUniversitarioEtapa.newInstance(universitario);
                                getParentFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, fragmentEtapa)
                                        .addToBackStack(null)
                                        .commit();

                        } else {
                            CadastroAnuncianteUniversitario fragment = CadastroAnuncianteUniversitario.newInstance(usuario, cadastroManager);
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                }
            }
        });
        return view;
    }

    private void atualizarProgressBar(String tipo_usuario, int etapa) {
        // Calcular progresso como uma porcentagem do total de etapas

        if (tipo_usuario.equals("anunciante")) {
            if (etapa == 1) {
                progressBar.setProgress(33, true);
            } else if (etapa == 2) {
                progressBar.setProgress(66, true);
            }
        } else {
            if (etapa == 1) {
                progressBar.setProgress(20, true);
            } else if (etapa == 2) {
                progressBar.setProgress(40, true);
            } else if (etapa == 3) {
                progressBar.setProgress(60, true);
            }
        }

    }

    private void esconderCamposEViews(View campo1, View campo2, View campo3, View campo4,
                                      View view1, View view2, View view3, View view4,
                                      View olho, View olho2, View calendario) {
        // Esconder campos
        campo1.setVisibility(View.GONE);
        campo2.setVisibility(View.GONE);
        campo3.setVisibility(View.GONE);
        campo4.setVisibility(View.GONE);

        // Esconder views
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);

        // Esconder ícones
        olho.setVisibility(View.GONE);
        olho2.setVisibility(View.GONE);
        calendario.setVisibility(View.GONE);
    }

    private void colocarHintTipo(EditText campo1, View view1, EditText campo2, View view2, EditText campo3, View view3, EditText campo4, View view4, String[] campos, ImageButton olho, ImageButton olho2, ImageButton calendario, ImageButton genero, ImageButton genero2, ImageButton olho3) {
        try {
            if (campos[0] != null) {
                campo1.setHint(campos[0]);
                campo1.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);

                // telefone
                if (campos[0].toLowerCase().contains("telefone")) {
                    // máximo de 15 caracteres
                    campo1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

                    // Adiciona o TextWatcher ao EditText
                    campo1.addTextChangedListener(new TextWatcher() {
                        Boolean isUpdating = false;

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (isUpdating) {
                                return;
                            }
                            isUpdating = true;
                            String unformatted = s.toString().replaceAll("[^\\d]", ""); // Remove tudo que não for número

                            if (unformatted.length() > 11) {
                                unformatted = unformatted.substring(0, 11); // Limita a 11 dígitos
                            }

                            StringBuilder formatted = new StringBuilder();
                            int length = unformatted.length();
                            if (length > 0) {
                                formatted.append("(");
                                formatted.append(unformatted.substring(0, Math.min(length, 2))); // DDD
                                if (length >= 3) {
                                    formatted.append(") ");
                                    formatted.append(unformatted.substring(2, Math.min(length, 7))); // Primeira parte do número
                                    if (length >= 8) {
                                        formatted.append("-");
                                        formatted.append(unformatted.substring(7)); // Segunda parte do número
                                    }
                                }
                            }
                            campo1.setText(formatted.toString());
                            // Verificação para garantir que a posição não exceda o comprimento do texto
                            int selectionPosition = formatted.length();
                            if (selectionPosition > campo1.getText().length()) {
                                selectionPosition = campo1.getText().length();
                            }
                            campo1.setSelection(selectionPosition); // Define a posição da seleção corretamente
                            isUpdating = false;
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    campo1.setInputType(InputType.TYPE_CLASS_PHONE);
                    campo1.setHint("(xx) xxxxx-xxxx");
                } else if (campos[0].toLowerCase().contains("e-mail")) { // email
                    campo1.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    campo1.setHint("email@email.com");
                } else if (campos[0].toLowerCase().contains("nome")) { // nome
                    campo1.setInputType(InputType.TYPE_CLASS_TEXT);
                } else { // dne
                    campo1.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
            if (campos[1] != null) {
                campo2.setHint(campos[1]);
                campo2.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);

                if (campos[1].toLowerCase().contains("município")) { // cidade
                    campo2.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (campos[1].toLowerCase().contains("telefone")) { // telefone

                    // máximo de 15 caracteres
                    campo2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

                    // Adiciona o TextWatcher ao EditText
                    campo2.addTextChangedListener(new TextWatcher() {

                        Boolean isUpdating = false;

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (isUpdating) {
                                return;
                            }
                            isUpdating = true;
                            String unformatted = s.toString().replaceAll("[^\\d]", ""); // Remove tudo que não for número

                            if (unformatted.length() > 11) {
                                unformatted = unformatted.substring(0, 11); // Limita a 11 dígitos
                            }

                            StringBuilder formatted = new StringBuilder();
                            int length = unformatted.length();
                            if (length > 0) {
                                formatted.append("(");
                                formatted.append(unformatted.substring(0, Math.min(length, 2))); // DDD
                                if (length >= 3) {
                                    formatted.append(") ");
                                    formatted.append(unformatted.substring(2, Math.min(length, 7))); // Primeira parte do número
                                    if (length >= 8) {
                                        formatted.append("-");
                                        formatted.append(unformatted.substring(7)); // Segunda parte do número
                                    }
                                }
                            }
                            campo2.setText(formatted.toString());
                            // Verificação para garantir que a posição não exceda o comprimento do texto
                            int selectionPosition = formatted.length();
                            if (selectionPosition > campo2.getText().length()) {
                                selectionPosition = campo2.getText().length();
                            }
                            campo2.setSelection(selectionPosition); // Define a posição da seleção corretamente
                            isUpdating = false;
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    campo2.setInputType(InputType.TYPE_CLASS_PHONE);
                    campo2.setHint("(xx) xxxxx-xxxx");
                } else if (campos[1].toLowerCase().contains("gênero")) { // gênero
                    campo2.setInputType(InputType.TYPE_CLASS_TEXT);
                    ViewUtils.showGenderPopup(campo2, genero2, getContext());
                } else { // senha
                    campo2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ViewUtils.setEyeIconVisibilityAndChangeIconOnClick(campo2, olho);
                }
            }
            if (campos[2] != null) {
                campo3.setHint(campos[2]);
                campo3.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);

                campo3.setFocusable(true);
                campo3.setFocusableInTouchMode(true);
                campo3.setClickable(true);

                // olho
                if (campos[2].toLowerCase().contains("senha")) { // senha
                    campo3.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ViewUtils.setEyeIconVisibilityAndChangeIconOnClick(campo3, olho2);
                } else if (campos[2].toLowerCase().contains("data")) { // data de nascimento
                    campo3.setInputType(InputType.TYPE_CLASS_DATETIME);
                    ViewUtils.setCalendarIconOnClick(campo3, calendario, getContext());
                } else { // universidade
                    campo3.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
            if (campos[3] != null) {
                campo4.setHint(campos[3]);
                campo4.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);

                if (campos[3].toLowerCase().contains("gênero")) { // gênero
                    campo4.setInputType(InputType.TYPE_CLASS_TEXT);
                    ViewUtils.showGenderPopup(campo4, genero, getContext());
                } else if (campos[3].toLowerCase().contains("senha")) {
                    campo4.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ViewUtils.setEyeIconVisibilityAndChangeIconOnClick(campo4, olho3);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
    }

    private Boolean erros(EditText campo, String confirmacao_senha, TextView erro, Boolean vazio, Boolean email, Boolean nome, Boolean telefone, Boolean senha, Boolean dne, Boolean universidade, Boolean municipio, Boolean data, Boolean conf_senha, Boolean genero) {

        Boolean retorno = false;
        // não pode ser vazio
        if (vazio) {
            if (campo.getText().toString().isEmpty()) {
                erro.setText("Este campo deve ser preenchido!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        // é um e-mail
        if (email) {
            if (!Patterns.EMAIL_ADDRESS.matcher(campo.getText().toString()).matches()) {
                erro.setText("E-mail inválido!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        // é um nome
        if (nome) {
            if (campo.getText().toString().length() < 3) {
                erro.setText("Nome inválido! Deve conter pelo menos 3 caracteres!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        // é um telefone
        if (telefone) {
            // verificar se telefone tem 11 dígitos

            if (!Patterns.PHONE.matcher(campo.getText().toString()).matches()) {

                if (campo.getText().toString().length() != 11) {
                    erro.setText("O telefone deve conter 11 dígitos!");
                    erro.setVisibility(View.VISIBLE);
                    retorno = true;
                } else{
                    erro.setVisibility(View.GONE);
                }

                erro.setText("Telefone inválido!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        // é uma senha
        if (senha) {
            if (campo.getText().toString().length() < 6) {
                erro.setText("Senha inválida! Deve conter pelo menos 6 caracteres!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        // é uma dne
        if (dne) {
            if (campo.getText().toString().isEmpty()) {
                // rpa de validação de dne
                erro.setText("DNE inválido!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        // é uma universidade
        if (universidade) {
            if (campo.getText().toString().isEmpty()) {
                // rpa de validação de universidade
                erro.setText("Universidade inválida!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        // é um municipio
        if (municipio) {
            if (campo.getText().toString().isEmpty()) {
                // rpa de validação de universidade
                erro.setText("Cidade inválida!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        // é uma data de nascimento
        if (data) {
            if (!campo.getText().toString().equals("")) {
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                    LocalDate data_nascimento = LocalDate.parse(campo.getText().toString(), formato);
                    LocalDate data_atual = LocalDate.now();

                    int idade = Period.between(data_nascimento, data_atual).getYears();

                    if (idade >= 18) {
                        erro.setVisibility(View.GONE);
                    } else {
                        throw new ParseException("Data inválida!", 0);
                    }
                } catch (ParseException e) {
                    erro.setText("Você deve ter 18 ou mais anos para se cadastrar!");
                    erro.setVisibility(View.VISIBLE);
                    retorno = true;
                }
            }
        }

        // é confirmação de senha
        if (conf_senha) {
            if (confirmacao_senha.isEmpty()) {
                erro.setText("Preencha a senha primeiro!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                if (!campo.getText().toString().equals(confirmacao_senha)) {
                    erro.setText("As senhas devem ser iguais!");
                    erro.setVisibility(View.VISIBLE);
                    retorno = true;
                } else {
                    erro.setVisibility(View.GONE);
                }
            }
        }

        // é um genero
        if (genero) {
            if (campo.getText().toString().isEmpty()) {
                // rpa de validação de genero
                erro.setText("É necessário selecionar um gênero!");
                erro.setVisibility(View.VISIBLE);
                retorno = true;
            } else {
                erro.setVisibility(View.GONE);
            }
        }

        return retorno;
    }

    // preencher campos
    private void preencherCampos(int etapa, String tipo_usuario, EditText campo1, EditText campo2, EditText campo3, EditText campo4) {
        if (etapa == 1) {
            if (tipo_usuario.equals("anunciante")) {
                if (!anunciante.isEmpty()) {
                    campo1.setText(anunciante.get("nome"));
                    campo2.setText(anunciante.get("municipio"));
                    campo3.setText(anunciante.get("dt_nascimento"));
                    campo4.setText(anunciante.get("email"));
                }
            } else {
                if (!universitario.isEmpty()) {
                    campo1.setText(universitario.get("nome"));
                    campo2.setText(universitario.get("municipio"));
                    campo3.setText(universitario.get("dt_nascimento"));
                    campo4.setText(universitario.get("genero"));
                }
            }
        } else if (etapa == 2) {
            if (tipo_usuario.equals("anunciante")) {
                if (!anunciante.isEmpty()) {
                    campo1.setText(anunciante.get("telefone"));
                    campo2.setText(anunciante.get("genero"));
                    campo3.setText(anunciante.get("senha"));
                    campo4.setText(anunciante.get("conf_senha"));
                }
            } else {
                // verifica se não existem informações salvas em anunciante
                if (!universitario.isEmpty()) {
                    campo1.setText(universitario.get("email"));
                    campo2.setText(universitario.get("telefone"));
                    campo3.setText(universitario.get("universidade"));
                }


            }
        } else if (etapa == 3) {
            if (tipo_usuario.equals("universitario")) {
                if (!universitario.isEmpty()) {
                    campo1.setText(universitario.get("dne"));
                    campo2.setText(universitario.get("senha"));
                    campo3.setText(universitario.get("conf_senha"));
                }
            }
        }
    }
}
