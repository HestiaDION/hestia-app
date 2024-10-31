package com.example.hestia_app.presentation.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hestia_app.R;

import com.example.hestia_app.data.api.InfoUserRepository;
import com.example.hestia_app.data.api.callbacks.InfosUserCallback;
import com.example.hestia_app.data.api.callbacks.RegistroAnuncianteCallback;
import com.example.hestia_app.data.api.callbacks.RegistroUniversitarioCallback;
import com.example.hestia_app.data.services.InfosUserService;
import com.example.hestia_app.data.services.UniversitarioService;
import com.example.hestia_app.domain.models.Anunciante;

import com.example.hestia_app.data.api.callbacks.FiltrosTagsCallback;

import com.example.hestia_app.domain.models.FiltrosTags;

import com.example.hestia_app.data.services.AnuncianteService;
import com.example.hestia_app.data.services.FiltrosTagsService;
import com.example.hestia_app.data.services.FirebaseService;
import com.example.hestia_app.domain.models.InfosUser;
import com.example.hestia_app.domain.models.Universitario;
import com.example.hestia_app.presentation.view.MainActivityNavbar;
import com.example.hestia_app.presentation.view.Splashscreen;
import com.example.hestia_app.presentation.view.TelaAviso;
import com.example.hestia_app.presentation.view.UserTerms;
import com.example.hestia_app.utils.CadastroManager;
import com.example.hestia_app.presentation.view.camera.FotoActivity;
import com.example.hestia_app.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public class CadastroFotoFragment extends Fragment {
    HashMap<String, String> usuario;
    String tipo;
    CadastroManager cadastroManager = new CadastroManager();
    Uri uri;
    ImageView foto_usuario, foto_ilustrativa;
    TextView txt_adicionar;
    CheckBox checkBox;
    private ProgressBar progressBar;

    // lista de permissões
    public static final String[] REQUIRED_PERMISSIONS;

    static {
        List<String> requiredPermissions = new ArrayList<>();
//        requiredPermissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
        requiredPermissions.add("android.permission.READ_MEDIA_IMAGES");
//        requiredPermissions.add("android.permission.READ_EXTERNAL_STORAGE");
        REQUIRED_PERMISSIONS = requiredPermissions.toArray(new String[0]);
    }

    // services
    AnuncianteService anuncianteService = new AnuncianteService();
    UniversitarioService universitarioService = new UniversitarioService();
    FirebaseService firebaseService = new FirebaseService();
    FiltrosTagsService filtrosTagsService = new FiltrosTagsService();
    InfosUserService infosUserService = new InfosUserService();


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
        progressBar = view.findViewById(R.id.progress_bar);

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
                progressBar.setVisibility(View.VISIBLE); // Exibir barra de progresso
                salvarFirebaseUsuario();
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

    private void salvarFirebaseUsuario() {
        String nome = usuario.get("nome");
        String email = usuario.get("email");
        String senha = usuario.get("senha");

        // Firebase
        firebaseService.salvarUsuario(getContext(), nome, email, senha, uri, checkBox.isChecked());

        // Salvar InfosUser
        InfosUser infosUser = new InfosUser(email, senha, uri.toString());
        salvarInfosUserComRetry(infosUser);
    }

    private void salvarInfosUserComRetry(InfosUser infosUser) {
        infosUserService.addInfosUser(infosUser, new InfosUserCallback() {
            @Override
            public void onSuccess(InfosUser response) {
                Log.d("InfosUser", "Sucesso ao enviar objeto InfosUser ao MongoDB: " + response.toString());
                if (tipo.equals("anunciante")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            salvarAnuncianteComRetry();
                        }
                    }, 5000);
                } else if (tipo.equals("universitario")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            salvarUniversitarioComRetry();
                        }
                    }, 5000);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("InfosUser", "Falha ao enviar objeto InfosUser ao MongoDB: " + t.toString());
                // Reintenta a requisição em caso de falha
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        salvarInfosUserComRetry(infosUser);
                    }
                }, 5000);
            }
        });
    }

    private void salvarAnuncianteComRetry() {
        Anunciante anunciante = new Anunciante(
                usuario.get("nome"),
                usuario.get("municipio"),
                usuario.get("telefone"),
                usuario.get("genero"),
                usuario.get("dt_nascimento"),
                usuario.get("email")
        );

        anuncianteService.registrarAnunciante(anunciante, new RegistroAnuncianteCallback() {
            @Override
            public void onRegistroSuccess(boolean isRegistered) {
                Log.d("Registro", "Anunciante registrado com sucesso!");
                progressBar.setVisibility(View.GONE); // Ocultar barra de progresso
                Toast.makeText(getContext(), "Registro concluído!", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("textExplanation", "Anunciante registrado com sucesso!");
                bundle.putInt("lottieAnimation", R.raw.house);
                bundle.putString("tipo", "anunciante");
                bundle.putString("tela", "MainActivityNavbar");
                Intent intent = new Intent(getContext(), TelaAviso.class);
                intent.putExtras(bundle);
                startActivity(intent);
                requireActivity().finish();
            }

            @Override
            public void onRegistroFailure(boolean isRegistered) {
                Log.e("Registro", "Falha ao registrar o anunciante.");
                // Reintenta a requisição em caso de falha
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        salvarAnuncianteComRetry();
                    }
                }, 5000);
            }
        });
    }

    private void salvarUniversitarioComRetry() {
        Universitario universitario = new Universitario(
                usuario.get("nome"),
                usuario.get("email"),
                usuario.get("dne"),
                usuario.get("municipio"),
                usuario.get("universidade"),
                usuario.get("genero"),
                usuario.get("telefone"),
                usuario.get("dt_nascimento")
        );

        universitarioService.registrarUniversitario(universitario, new RegistroUniversitarioCallback() {
            @Override
            public void onRegistroSuccess(boolean isRegistered, UUID universitarioId) {
                Log.d("Registro", "Universitário registrado com sucesso!");
                // salvar filtros
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        salvarFiltrosComRetry(universitarioId);
                    }
                }, 5000);
            }

            @Override
            public void onRegistroFailure(boolean isRegistered) {
                Log.e("Registro", "Falha ao registrar o universitário.");
                // Reintenta a requisição em caso de falha
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        salvarUniversitarioComRetry();
                    }
                }, 5000);
            }
        });
    }

    private void salvarFiltrosComRetry(UUID universitarioId) {
        // pegar o que foi salvo no hashmap
        List<String> animais = new ArrayList<>(), casa = new ArrayList<>();
        String generos = "", pessoas = "", fumo = "", bebida = "";
        for (String categoria : usuario.keySet()) {
            if (categoria.startsWith("categoria ")) {
                switch (categoria) {
                    case "categoria animal":
                        animais = transformarLista(usuario.get(categoria));
                        break;
                    case "categoria genero":
                        generos = transformarLista(usuario.get(categoria)).get(0);
                        break;
                    case "categoria pessoa":
                        pessoas = transformarLista(usuario.get(categoria)).get(0);
                        break;
                    case "categoria fumo":
                        fumo = transformarLista(usuario.get(categoria)).get(0);
                        break;
                    case "categoria bebida":
                        bebida = transformarLista(usuario.get(categoria)).get(0);
                        break;
                    case "categoria casa":
                        casa = transformarLista(usuario.get(categoria));
                        break;
                }
            }
        }

        FiltrosTags filtros = new FiltrosTags(universitarioId,
                                              tipo,
                                              animais,
                                              generos,
                                              pessoas,
                                              fumo,
                                              bebida,
                                              casa);

        filtrosTagsService.addFiltrosTag(filtros, new FiltrosTagsCallback() {
            @Override
            public void onFiltroCadastroSuccess(boolean IsRegistered) {
                Log.d("Filtros", "Filtros salvos com sucesso!");
                progressBar.setVisibility(View.GONE); // Ocultar barra de progresso após o sucesso
                Toast.makeText(getContext(), "Registro concluído!", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("textExplanation", "Universitário registrado com sucesso!");
                bundle.putInt("lottieAnimation", R.raw.university);
                bundle.putString("tipo", "universitário");
                bundle.putString("tela", "MainActivityNavbar");
                Intent intent = new Intent(getContext(), TelaAviso.class);
                intent.putExtras(bundle);
                startActivity(intent);
                requireActivity().finish();
            }

            @Override
            public void onFiltroCadastroFailure(boolean IsRegistered) {
                Log.e("Filtros", "Falha ao salvar os filtro!");
                // Reintenta a requisição em caso de falha
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        salvarFiltrosComRetry(universitarioId);
                    }
                }, 5000);
            }
        });
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