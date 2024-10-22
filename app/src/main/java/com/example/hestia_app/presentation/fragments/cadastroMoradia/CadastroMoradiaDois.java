package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.adapter.FotosCadastroAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroMoradiaDois extends Fragment {

    public HashMap<String, String> moradia;
    private static final int PICK_IMAGES = 1;
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

        if (moradia.get("imagens") != null) {
            imagePaths = Arrays.asList(moradia.get("imagens").split(","));
            imageAdapter = new FotosCadastroAdapter(getContext(), imagePaths);
            imagens.setAdapter(imageAdapter);
            imagens.setCurrentItem(imagePaths.size() - 1);
            imagens.setUserInputEnabled(false);
        } else {
            imagePaths = new ArrayList<>();
        }

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
                imageAdapter = new FotosCadastroAdapter(getContext(), imagePaths);
                imagens.setAdapter(imageAdapter);

                openGallery();
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moradia.put("imagem", imagePaths.toString());
                CadastroMoradiaDois fragment = CadastroMoradiaDois.newInstance(moradia);
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
                        if (count > 10) {
                            Toast.makeText(getContext(), "Selecione entre 3 e 10 images!", Toast.LENGTH_SHORT).show();
                            return;
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        resultLauncherGaleria.launch(intent);
    }
}