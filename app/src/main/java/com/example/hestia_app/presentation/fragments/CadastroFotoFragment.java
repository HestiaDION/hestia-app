package com.example.hestia_app.presentation.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.KeyEvent;
import android.util.Log;
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
import com.example.hestia_app.data.api.callbacks.FiltrosTagsCallback;
import com.example.hestia_app.data.api.callbacks.RegistroAnuncianteCallback;
import com.example.hestia_app.data.models.Anunciante;
import com.example.hestia_app.data.models.FiltrosTags;
import com.example.hestia_app.data.services.AnuncianteService;
import com.example.hestia_app.data.services.FiltrosTagsService;
import com.example.hestia_app.data.services.FirebaseService;
import com.example.hestia_app.presentation.view.MainActivityNavbar;
import com.example.hestia_app.presentation.view.UserTerms;
import com.example.hestia_app.utils.CadastroManager;
import com.example.hestia_app.presentation.view.camera.FotoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LaunchStage;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroFotoFragment extends Fragment {
    HashMap<String, String> usuario;
    String tipo;
    CadastroManager cadastroManager = new CadastroManager();
    Uri uri;
    ImageView foto_usuario, foto_ilustrativa;
    TextView txt_adicionar;
    CheckBox checkBox;
    // lista de permissões
    private static final String[] REQUIRED_PERMISSIONS;

    static {
        List<String> requiredPermissions = new ArrayList<>();
//        requiredPermissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
        requiredPermissions.add("android.permission.READ_MEDIA_IMAGES");
//        requiredPermissions.add("android.permission.READ_EXTERNAL_STORAGE");
        REQUIRED_PERMISSIONS = requiredPermissions.toArray(new String[0]);
    }

    // service
    AnuncianteService anuncianteService = new AnuncianteService();
    FirebaseService firebaseService = new FirebaseService();
    FiltrosTagsService filtrosTagsService = new FiltrosTagsService();

    final String ANUNCIANTE = "ANUNCIANTE";
    final String UNIVERSITARIO = "UNIVERSITÁRIO";

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

                // salvando informações no firebase
                assert nome != null;
                Log.d("Registro", "FIREAUTH REGISTROU UM USUÁRIO");
                firebaseService.salvarUsuario(getContext(), nome, email, senha, uri, checkBox.isChecked());

                // salvando informações no postgres
                if(tipo.equals("anunciante")) {

                    Log.d("Registro", "CAIU NO TIPO ANUNCIANTE");

                    Anunciante anunciante = new Anunciante(
                            nome,
                            usuario.get("municipio"),
                            usuario.get("telefone"),
                            usuario.get("genero"),
                            usuario.get("dt_nascimento"),
                            email
                    );

                    Log.d("ANUNCIANTE BODY", anunciante.toString());

                  anuncianteService.registrarAnunciante(anunciante, new RegistroAnuncianteCallback() {
                      @Override
                      public void onRegistroSuccess(boolean isRegistered) {
                          if (isRegistered) {
                              Log.d("Registro", "Anunciante registrado com sucesso!");
                          } else {
                              Log.d("Registro", "Falha ao registrar o anunciante.");
                          }
                      }

                      @Override
                      public void onRegistroFailure(boolean isRegistered) {
                          if (isRegistered) {
                              Log.d("Registro", "Anunciante registrado com sucesso!");
                          } else {
                              Log.d("Registro", "Falha ao registrar o anunciante.");
                          }
                      }


                  });

                } else if (tipo.equals("universitario")) {
                    // salvar os filtros no mongo
                    Log.d("Registro", "CAIU NO TIPO UNIVERSITÁRIO");

                    // recuperar as informções

                    List<String> categoria1 = transformarLista(usuario.get("categoria1"));
                    List<String> categoria2 = transformarLista(usuario.get("categoria2"));
                    List<String> categoria3 = transformarLista(usuario.get("categoria3"));
                    List<String> categoria4 = transformarLista(usuario.get("categoria4"));
                    List<String> categoria5 = transformarLista(usuario.get("categoria5"));

                    FiltrosTags filtrosTags = new FiltrosTags(
                            UUID.randomUUID(), // TODO: trocar pelo id do universitario
                            tipo,
                            categoria1,
                            categoria2.get(0),
                            categoria3.get(0),
                            categoria4.get(0),
                            categoria5.get(0)
                    );

                    Log.d("FILTROS", "BODY FILTROS: " + filtrosTags);

                    filtrosTagsService.addFiltrosTag(filtrosTags, new FiltrosTagsCallback() {
                        @Override
                        public void onFiltroCadastroSuccess(boolean IsRegistered) {
                            if (IsRegistered) {
                                Log.d("Registro", "Filtros salvos com sucesso!");
                            } else {
                                Log.d("Registro", "Falha ao salvar os filtros.");
                            }
                        }

                        @Override
                        public void onFiltroCadastroFailure(boolean IsRegistered) {
                            if (IsRegistered) {
                                Log.d("Registro", "Filtros salvos com sucesso!");
                            } else {
                                Log.d("Registro", "Falha ao salvar os filtros.");
                            }
                        }
                    });

                } else{
                    Log.d("Registro", "NÃO CAIU NO REGISTRO");
                }
            }
        });

        if (tipo.equals("anunciante")) {
            tipo_usuario.setTextColor(getResources().getColor(R.color.azul));
            tipo_usuario.setText(ANUNCIANTE);
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
            tipo_usuario.setText(UNIVERSITARIO);
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

        // bloquear botão de voltar
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });

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

                        // Request de permissão
                        if (allPermissionsGranted()) {
                            // permitir galeria
                            Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            resultLauncherGaleria.launch(intent2);
                        } else {
                            // pedir permissão
                            requestPermissions();
                        }
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

    public boolean allPermissionsGranted() {
        // verificar permissões
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS);
    }
    private ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                // Handle Permission granted/rejected
                boolean permissionGranted = true;
                for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                    if (Arrays.asList(REQUIRED_PERMISSIONS).contains(entry.getKey()) && !entry.getValue()) {
                        permissionGranted = false;
                        break;
                    }
                }
                if (!permissionGranted) {
                    Toast.makeText(requireContext(),"Permissão NEGADA. Tente novamente.",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    resultLauncherGaleria.launch(intent2);
                }
            });

    public List<String> transformarLista(String string) {
        string = string.replace("[", "").replace("]", "").replace("'", "").trim();
        String[] array = string.split(",\\s*");
        return new ArrayList<>(Arrays.asList(array));
    }
}