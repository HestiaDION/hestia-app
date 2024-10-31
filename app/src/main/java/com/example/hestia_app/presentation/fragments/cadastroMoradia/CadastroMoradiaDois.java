package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import static android.app.Activity.RESULT_OK;

import static com.example.hestia_app.presentation.fragments.CadastroFotoFragment.REQUIRED_PERMISSIONS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.fragments.CadastroFotoFragment;
import com.example.hestia_app.presentation.view.adapter.FotosCadastroAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroMoradiaDois extends Fragment {

    public HashMap<String, String> moradia;
    private List<String> imagePaths;
    private FotosCadastroAdapter imageAdapter;
    ViewPager2 imagens;
    TextView txt_adicionar;
    ImageView foto_ilustrativa;

    public CadastroMoradiaDois() {
        // Required empty public constructor
    }

    public static CadastroMoradiaDois newInstance(HashMap<String, String> moradia) {
        CadastroMoradiaDois fragment = new CadastroMoradiaDois();
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
        View view = inflater.inflate(R.layout.fragment_cadastro_moradia_dois, container, false);
        ImageButton btFechar = view.findViewById(R.id.fechar);
        imagens = view.findViewById(R.id.fotosMoradiaCadastro);
        LinearLayout escolher_foto = view.findViewById(R.id.escolher_foto);
        Button btProximo = view.findViewById(R.id.bt_acao);
        ImageButton btVoltar = view.findViewById(R.id.voltar);
        txt_adicionar = view.findViewById(R.id.txt_adicionar);
        foto_ilustrativa = view.findViewById(R.id.foto_ilustrativa);
        Handler handler;
        Runnable runnable;

        if (moradia.get("imagens") != null) {
            Log.d("imagensMoradia", "onCreateView: " + moradia.get("imagens"));
            imagePaths = transformarLista(moradia.get("imagens"));
            txt_adicionar.setVisibility(View.GONE);
            foto_ilustrativa.setVisibility(View.GONE);
            imagens.setVisibility(View.VISIBLE);
        } else {
            imagePaths = new ArrayList<>();
        }

        imageAdapter = new FotosCadastroAdapter(getContext(), imagePaths);
        imagens.setAdapter(imageAdapter);

        // Inicializa o Handler e o Runnable para mudar as imagens automaticamente
        handler = new Handler();
        runnable = new Runnable() {
            int currentIndex = 0;

            @Override
            public void run() {
                if (currentIndex < imagePaths.size()) {
                    imagens.setCurrentItem(currentIndex++, true);
                } else {
                    currentIndex = 0; // Reinicia para o primeiro item
                }
                handler.postDelayed(this, 5000); // Altera a imagem a cada 3 segundos
            }
        };
        handler.post(runnable); // Inicia o processo


        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
                moradia.clear();
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moradia.remove("imagens");
                getParentFragmentManager().popBackStack();
            }
        });

        escolher_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_adicionar.setVisibility(View.GONE);
                foto_ilustrativa.setVisibility(View.GONE);
                imagens.setVisibility(View.VISIBLE);
                openGallery();
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imagePaths.isEmpty() || imagePaths.size() < 3 || imagePaths.size() > 10) {
                    Toast.makeText(getContext(), "Selecione entre 3 e 10 images!", Toast.LENGTH_SHORT).show();
                    imagePaths.clear();
                    imagens.setVisibility(View.GONE);
                    txt_adicionar.setVisibility(View.VISIBLE);
                    foto_ilustrativa.setVisibility(View.VISIBLE);
                    return;
                }

                moradia.put("imagens", imagePaths.toString());
                CadastroMoradiaTres fragment = CadastroMoradiaTres.newInstance(moradia);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private ActivityResultLauncher<Intent> resultLauncherGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    if (result.getData().getClipData() != null) {
                        // Selecionou múltiplas imagens
                        int count = result.getData().getClipData().getItemCount();
                        if (count > 10 || count < 3) {
                            Toast.makeText(getContext(), "Selecione entre 3 e 10 images!", Toast.LENGTH_SHORT).show();
                            imagePaths.clear();
                            imagens.setVisibility(View.GONE);
                            txt_adicionar.setVisibility(View.VISIBLE);
                            foto_ilustrativa.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                imagePaths.add(imageUri.toString());
                            }
                        }
                    }
                    imageAdapter.notifyDataSetChanged();
                } else {
                    imagePaths.clear();
                    imagens.setVisibility(View.GONE);
                    txt_adicionar.setVisibility(View.VISIBLE);
                    foto_ilustrativa.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Selecione entre 3 e 10 images!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    // Abre a galeria para selecionar imagens
    private void openGallery() {
        if (allPermissionsGranted()) {
            // permitir galeria
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            resultLauncherGaleria.launch(intent);
        } else {
            // pedir permissão
            requestPermissions();
        }
    }

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
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    resultLauncherGaleria.launch(intent);
                }
            });

    public List<String> transformarLista(String string) {
        string = string.replace("[", "").replace("]", "").replace("'", "").trim();
        String[] array = string.split(",\\s*");
        return new ArrayList<>(Arrays.asList(array));
    }
}