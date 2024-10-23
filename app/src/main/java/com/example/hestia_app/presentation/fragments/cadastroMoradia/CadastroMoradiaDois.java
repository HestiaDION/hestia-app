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
        ViewPager2 imagens = view.findViewById(R.id.fotosMoradiaCadastro);
        LinearLayout escolher_foto = view.findViewById(R.id.escolher_foto);
        Button btProximo = view.findViewById(R.id.bt_acao);
        ImageButton btVoltar = view.findViewById(R.id.voltar);
        TextView txt_adicionar = view.findViewById(R.id.txt_adicionar);
        ImageView foto_ilustrativa = view.findViewById(R.id.foto_ilustrativa);

        if (moradia.get("imagens") != null) {
            imagePaths = transformarLista(moradia.get("imagens"));
            txt_adicionar.setVisibility(View.GONE);
            foto_ilustrativa.setVisibility(View.GONE);
            imagens.setVisibility(View.VISIBLE);
        } else {
            imagePaths = new ArrayList<>();
        }

        imageAdapter = new FotosCadastroAdapter(getContext(), imagePaths);
        imagens.setAdapter(imageAdapter);

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

        imagens.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txt_adicionar.setVisibility(View.GONE);
                foto_ilustrativa.setVisibility(View.GONE);
                imagens.setVisibility(View.VISIBLE);
                openGallery();
                return true;
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moradia.put("imagem", imagePaths.toString());
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
                            openGallery();
                        }
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            imagePaths.add(imageUri.toString());
                        }
                        moradia.put("imagens", imagePaths.toString());
                    } else if (result.getData().getData() != null) {
                        // Selecionou uma única imagem
                        Uri imageUri = result.getData().getData();
                        imagePaths.add(imageUri.toString());
                        moradia.put("imagens", imagePaths.toString());
                    }
                    imageAdapter.notifyDataSetChanged();
                } else {
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