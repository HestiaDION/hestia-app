package com.example.hestia_app.presentation.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.LoginActivity;
import com.example.hestia_app.presentation.view.MainActivityNavbar;
import com.example.hestia_app.presentation.view.UserTerms;
import com.example.hestia_app.utils.CadastroManager;
import com.example.hestia_app.utils.FotoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;

public class CadastroFotoFragment extends Fragment {
    HashMap<String, String> usuario;
    String tipo;
    CadastroManager cadastroManager = new CadastroManager();
    Uri uri;
    ImageView foto_usuario, foto_ilustrativa;
    TextView txt_adicionar;
    CheckBox checkBox;

    public CadastroFotoFragment() {
        // Required empty public constructor
    }

    public static CadastroFotoFragment newInstance(HashMap<String, String> usuario, String tipo_usuario) {
        CadastroFotoFragment fragment = new CadastroFotoFragment();
        Bundle args = new Bundle();
        args.putSerializable("usuario", usuario);
        args.putString("tipo_usuario", tipo_usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (HashMap<String, String>) getArguments().getSerializable("usuario");
            tipo = getArguments().getString("tipo_usuario");
            System.out.println("usuario: " + usuario);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_foto, container, false);
        TextView termos = view.findViewById(R.id.termos_ler);
        TextView tipo_usuario = view.findViewById(R.id.tipo_usuario);
        ImageButton bt_voltar = view.findViewById(R.id.voltar);
        Button bt_acao = view.findViewById(R.id.bt_acao);
        LinearLayout tirar_escolher_foto = view.findViewById(R.id.tirar_escolher_foto);
        foto_usuario = view.findViewById(R.id.foto_usuario);
        foto_ilustrativa = view.findViewById(R.id.foto_ilustrativa);
        txt_adicionar = view.findViewById(R.id.txt_adicionar);
        checkBox = view.findViewById(R.id.checkbox);

        if (usuario.containsKey("imagem")) {
            Glide.with(getContext()).load(usuario.get("imagem")).into(foto_usuario);
            txt_adicionar.setText("Alterar imagem");
            foto_ilustrativa.setVisibility(View.GONE);
            foto_usuario.setVisibility(View.VISIBLE);
            uri = Uri.parse(usuario.get("imagem"));
        }

        termos.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UserTerms.class);
            startActivity(intent);
        });

        tirar_escolher_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoOptions(getContext());
            }
        });

        bt_acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = usuario.get("nome");
                String email = usuario.get("email");
                String senha = usuario.get("senha");

                salvarUsuario(nome, email, senha);
            }
        });

        if (tipo.equals("anunciante")) {
            tipo_usuario.setTextColor(getResources().getColor(R.color.azul));
            tipo_usuario.setText("ANUNCIANTE");
            bt_voltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cadastroManager.setEtapaAtual(2);
                    CadastroAnuncianteUniversitario fragment = CadastroAnuncianteUniversitario.newInstance("anunciante", cadastroManager);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {
            tipo_usuario.setTextColor(getResources().getColor(R.color.vermelho));
            tipo_usuario.setText("UNIVERSITÁRIO");
            bt_voltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CadastroUniversitarioEtapa fragment = CadastroUniversitarioEtapa.newInstance(usuario);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
        return view;
    }

    private void showPhotoOptions(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Escolha uma opção");
        builder.setItems(new CharSequence[]{"Tirar Foto", "Escolher da Galeria"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // abrir camera
                        Intent intent = new Intent(context, FotoActivity.class);
                        resultLauncherCamera.launch(intent);
                        break;
                    case 1:
                        // acessar a galeria
                        Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        resultLauncherGaleria.launch(intent2);
                        break;
                }
            }
        });
        builder.show();
    }

    private ActivityResultLauncher<Intent> resultLauncherGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    // imagem selecionada
                    uri = imageUri;

                    // salvar no map
                    usuario.put("imagem", uri.toString());

                    // Exibe a imagem selecionada
                    Glide.with(this).load(imageUri)
                            .centerCrop()
                            .into(foto_usuario);

                    // desaparecer a imagem padrão
                    foto_ilustrativa.setVisibility(View.GONE);
                    txt_adicionar.setVisibility(View.GONE);

                    // aparecer a imagem selecionada
                    foto_usuario.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Selecione uma imagem!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private ActivityResultLauncher<Intent> resultLauncherCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    // imagem selecionada
                    uri = imageUri;

                    // salvar no map
                    usuario.put("imagem", uri.toString());

                    // Exibe a imagem selecionada
                    Glide.with(this).load(imageUri)
                            .centerCrop()
                            .into(foto_usuario);

                    // desaparecer a imagem padrão
                    foto_ilustrativa.setVisibility(View.GONE);
                    txt_adicionar.setVisibility(View.GONE);

                    // aparecer a imagem selecionada
                    foto_usuario.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Selecione uma imagem!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void salvarUsuario(String txtNome, String txtEmail, String txtSenha) {
        String nome = txtNome.split(" ")[0] + " " + txtNome.split(" ")[txtNome.split(" ").length - 1];
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (foto_usuario.getVisibility() == View.GONE) {
            Toast.makeText(getContext(), "Selecione uma imagem!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkBox.isChecked()) {
            Toast.makeText(getContext(), "É necessário aceitar os termos de uso!", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(txtEmail, txtSenha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // atualizar profile
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nome)
                                    .setPhotoUri(uri)
                                    .build();

                            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        usuario.clear();
                                        Toast.makeText(getContext(), "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), MainActivityNavbar.class);
                                        startActivity(intent);
                                    } else {
                                        // mostrar erro
                                        String msg = "Erro ao atualizar profile: ";
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException e) {
                                            msg += "Usuário inválido!";
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            msg += "Email inválido!";
                                        } catch (Exception e) {
                                            msg += e.getMessage();
                                        }
                                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // mostrar erro
                            String msg = "Erro ao efetuar o cadastro: ";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                msg += "Usuário inválido!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                msg += "Email inválido!";
                            } catch (Exception e) {
                                msg += e.getMessage();
                            }
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}