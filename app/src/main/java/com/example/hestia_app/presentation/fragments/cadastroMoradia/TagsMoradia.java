package com.example.hestia_app.presentation.fragments.cadastroMoradia;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.FiltroCadastroCallback;
import com.example.hestia_app.data.services.FiltroCadastroService;
import com.example.hestia_app.domain.models.FiltroCadastro;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TagsMoradia extends Fragment {
    Boolean unico;

    // Novo mapa para armazenar seleções por categoria
    HashMap<String, List<String>> selecoesPorCategoria = new HashMap<>();

    public TagsMoradia() {
        // Required empty public constructor
    }

    public static TagsMoradia newInstance() {
        TagsMoradia fragment = new TagsMoradia();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags_moradia, container, false);
        LinearLayout filtros = view.findViewById(R.id.filtros);
        Button bt_acao = view.findViewById(R.id.bt_acao);
        ImageButton voltar = view.findViewById(R.id.voltar);

        carregarCategorias(filtros);

        bt_acao.setOnClickListener(v -> {
            // Verificar se pelo menos uma categoria foi selecionada
            boolean selecionadas = false;
            for (List<String> selecao : selecoesPorCategoria.values()) {
                if (!selecao.isEmpty()) {
                    selecionadas = true;
                    break;
                }
            }
            if (!selecionadas) {
                // Se não houver nenhuma categoria selecionada, mostrar uma mensagem de erro
                Log.d("TAGS_MORADIA", "Nenhuma categoria selecionada");
                Toast.makeText(getContext(), "Selecione pelo menos uma categoria", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle result = new Bundle();
            result.putSerializable("filtrosSelecionadosKey", selecoesPorCategoria);
            getParentFragmentManager().setFragmentResult("filtrosSelecionadosKey", result);

            // Voltar para o fragmento anterior
            getParentFragmentManager().popBackStack();
        });

        voltar.setOnClickListener(v -> {
            // Limpar as seleções
            selecoesPorCategoria.clear();
            getParentFragmentManager().popBackStack();
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
                        if (categoria.equals("animal")) {
                            texto = "Animais de estimação permitidos";
                            imagem = R.drawable.patinha;
                            unico = false;
                        } else if (categoria.equals("genero")) {
                            texto = "Preferência de moradores";
                            imagem = R.drawable.gender;
                            unico = true;
                        } else if (categoria.equals("pessoa")) {
                            texto = "Número máximo de pessoas";
                            imagem = R.drawable.pessoas;
                            unico = true;
                        } else if (categoria.equals("fumo")) {
                            texto = "Frequência de fumo permitida";
                            imagem = R.drawable.fumo;
                            unico = true;
                        } else if (categoria.equals("bebida")) {
                            texto = "Frequência de bebida permitida";
                            imagem = R.drawable.bebida;
                            unico = true;
                        } else if (categoria.equals("casa")) {
                            texto = "Móveis & Outros";
                            imagem = R.drawable.bed;
                            unico = false;
                        } else {
                            texto = "";
                            imagem = 0;
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