package com.example.hestia_app.presentation.fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.FiltroCadastroCallback;
import com.example.hestia_app.domain.models.FiltroCadastro;
import com.example.hestia_app.data.services.FiltroCadastroService;
import com.example.hestia_app.utils.CadastroManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.protobuf.Internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CadastroUniversitarioEtapa extends Fragment {
    HashMap<String, String> usuario;
    CadastroManager cadastroManager = new CadastroManager();
    Boolean unico;

    // Novo mapa para armazenar seleções por categoria
    HashMap<String, List<String>> selecoesPorCategoria = new HashMap<>();

    public CadastroUniversitarioEtapa() {
        // Required empty public constructor
    }

    public static CadastroUniversitarioEtapa newInstance(HashMap<String, String> usuario) {
        CadastroUniversitarioEtapa fragment = new CadastroUniversitarioEtapa();
        Bundle args = new Bundle();
        args.putSerializable("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (HashMap<String, String>) getArguments().getSerializable("usuario");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_universitario_etapa, container, false);

        // Inicializar views
        Button bt_acao = view.findViewById(R.id.bt_acao);
        ImageButton bt_voltar = view.findViewById(R.id.voltar);
        LinearLayout filtros = view.findViewById(R.id.filtros);

        // Carregar os filtros e adicionar chips após a resposta
        carregarCategorias(filtros);

        bt_acao.setOnClickListener(v -> {
            // Salva as seleções no mapa 'usuario'
            for (String categoria : selecoesPorCategoria.keySet()) {
                usuario.put("categoria " + categoria, selecoesPorCategoria.get(categoria).toString());
            }
            CadastroFotoFragment fragment = CadastroFotoFragment.newInstance(usuario, "universitario");
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        bt_voltar.setOnClickListener(v -> {
            cadastroManager.setEtapaAtual(3);
            CadastroAnuncianteUniversitario fragment = CadastroAnuncianteUniversitario.newInstance("universitario", cadastroManager);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void carregarCategorias(LinearLayout filtrosLinearLayout) {
        FiltroCadastroService service = new FiltroCadastroService();
        service.getCategorias(new FiltroCadastroCallback() {
            @Override
            public void onFiltroCadastroSuccess(List<FiltroCadastro> filtros, List<String> categorias) {
                if (!categorias.isEmpty()) {
                    for (String categoria : categorias) {
                        // Inicializa a lista de seleções para essa categoria no mapa
                        selecoesPorCategoria.put(categoria, new ArrayList<>());

                        // Criar um TextView para a categoria
                        TextView categoriaTextView = new TextView(getContext());
                        categoriaTextView.setTextSize(16);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(10, 0, 0, 12);
                        categoriaTextView.setLayoutParams(layoutParams);

                        // Define o texto e ícone da categoria
                        String texto;
                        int imagem;
                        switch (categoria.toLowerCase()) {
                            case "animal":
                                texto = "Você tem animais de estimação?";
                                imagem = R.drawable.patinha;
                                unico = false;
                                break;
                            case "genero":
                                texto = "Gostaria de morar com?";
                                imagem = R.drawable.gender;
                                unico = true;
                                break;
                            case "pessoa":
                                texto = "Número máximo de pessoas";
                                imagem = R.drawable.pessoas;
                                unico = true;
                                break;
                            case "fumo":
                                texto = "Com que frequência você fuma?";
                                imagem = R.drawable.fumo;
                                unico = true;
                                break;
                            case "bebida":
                                texto = "Com que frequência você bebe?";
                                imagem = R.drawable.bebida;
                                unico = true;
                                break;
                            case "casa":
                                texto = "Móveis & Outros";
                                imagem = R.drawable.bed;
                                unico = false;
                                break;
                            default:
                                texto = "";
                                imagem = 0;
                                unico = false;
                                break;
                        }

                        if (imagem != 0) {
                            categoriaTextView.setText(texto);
                            categoriaTextView.setCompoundDrawablesWithIntrinsicBounds(imagem, 0, 0, 0);
                            categoriaTextView.setCompoundDrawablePadding(10);
                        }

                        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.poppins_semi_bold);
                        categoriaTextView.setTypeface(typeface);

                        categoriaTextView.setTextColor(getContext().getResources().getColor(R.color.black));

                        filtrosLinearLayout.addView(categoriaTextView);

                        // Criar um ChipGroup para os filtros
                        ChipGroup chipGroup = new ChipGroup(getContext());
                        LinearLayout.LayoutParams layoutParamsChip = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParamsChip.setMargins(10, 0, 10, 10);
                        chipGroup.setLayoutParams(layoutParamsChip);
                        chipGroup.setSingleSelection(unico);
                        filtrosLinearLayout.addView(chipGroup);

                        // Adicionar chips ao ChipGroup
                        service.getFiltrosPorCategoria(categoria, new FiltroCadastroCallback() {
                            @Override
                            public void onFiltroCadastroSuccess(List<FiltroCadastro> filtros, List<String> categorias) {
                                for (FiltroCadastro filtro : filtros) {
                                    adicionarChips(filtro.getNome(), chipGroup, selecoesPorCategoria.get(categoria));
                                }
                            }

                            @Override
                            public void onFiltroCadastroFailure(String errorMessage) {
                                Log.d("API response", "onFiltroCadastroFailure: " + errorMessage);
                            }
                        });
                    }
                } else {
                    Log.d("API Response", "A lista de categorias veio vazia.");
                }
            }

            @Override
            public void onFiltroCadastroFailure(String errorMessage) {
                Log.d("API response", "onFiltroCadastroFailure: " + errorMessage);
            }
        });
    }

    public void adicionarChips(String chipText, ChipGroup chipGroup, List<String> selecionados) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Chip chip = (Chip) inflater.inflate(R.layout.chip_layout, chipGroup, false);
        chip.setText(chipText);
        chip.setId(View.generateViewId());

        chipGroup.addView(chip);

        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selecionados.add(chip.getText().toString());
            } else {
                selecionados.remove(chip.getText().toString());
            }
            Log.d("Chip", "Selecionados para categoria: " + selecionados.toString());
        });
    }
}
