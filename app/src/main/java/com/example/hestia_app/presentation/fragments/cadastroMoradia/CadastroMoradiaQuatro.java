package com.example.hestia_app.presentation.fragments.cadastroMoradia;

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
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.FiltroCadastroCallback;
import com.example.hestia_app.data.services.FiltroCadastroService;
import com.example.hestia_app.domain.models.FiltroCadastro;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CadastroMoradiaQuatro extends Fragment {

    private HashMap<String, String> moradia;
    private HashMap<String, List<String>> filtrosSelecionados;

    public CadastroMoradiaQuatro() {
        // Required empty public constructor
    }

    public static CadastroMoradiaQuatro newInstance(HashMap<String, String> moradia) {
        CadastroMoradiaQuatro fragment = new CadastroMoradiaQuatro();
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
        View view = inflater.inflate(R.layout.fragment_cadastro_moradia_quatro, container, false);
        LinearLayout filtros = view.findViewById(R.id.filtros);
        ScrollView filtrosScroll = view.findViewById(R.id.filtrosScroll);
        TextView complementar = view.findViewById(R.id.txtComplementar);
        LinearLayout categoriasMoradia = view.findViewById(R.id.categoriasMoradia);
        ImageButton btFechar = view.findViewById(R.id.fechar);
        ImageButton btVoltar = view.findViewById(R.id.voltar);
        Button btProximo = view.findViewById(R.id.bt_acao);

        // Ouvir o resultado da seleção de filtros
        getParentFragmentManager().setFragmentResultListener("filtrosSelecionadosKey", this, (requestKey, bundle) -> {
            filtrosSelecionados = (HashMap<String, List<String>>) bundle.getSerializable("filtrosSelecionadosKey");

            if (filtrosSelecionados != null && !filtrosSelecionados.isEmpty()) {
                Log.d("filtros_resultado", "onCreateView: " + filtrosSelecionados);
                complementar.setVisibility(View.GONE);
                filtrosScroll.setVisibility(View.VISIBLE);
                carregarCategorias(filtros, filtrosSelecionados);
                categoriasMoradia.setClickable(false);
            } else {
                complementar.setVisibility(View.VISIBLE);
                filtrosScroll.setVisibility(View.GONE);
                categoriasMoradia.setClickable(true);
            }
        });


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

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moradia.put("animal", filtrosSelecionados.get("animal").toString());
                moradia.put("genero", filtrosSelecionados.get("genero").toString());
                moradia.put("pessoa", filtrosSelecionados.get("pessoa").toString());
                moradia.put("fumo", filtrosSelecionados.get("fumo").toString());
                moradia.put("bebida", filtrosSelecionados.get("bebida").toString());
                moradia.put("casa", filtrosSelecionados.get("casa").toString());

                CadastroMoradiaCinco fragment = CadastroMoradiaCinco.newInstance(moradia);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        categoriasMoradia.setOnClickListener(v -> {
            TagsMoradia fragment = TagsMoradia.newInstance();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void carregarCategorias(LinearLayout filtrosLinearLayout, HashMap<String, List<String>> filtrosSelecionados) {
        FiltroCadastroService service = new FiltroCadastroService();
        service.getCategorias(new FiltroCadastroCallback() {
            @Override
            public void onFiltroCadastroSuccess(List<FiltroCadastro> filtros, List<String> categorias) {
                if (!categorias.isEmpty()) {
                    for (String categoria : categorias) {
                        // Criar um TextView para a categoria
                        TextView categoriaTextView = new TextView(getContext());
                        categoriaTextView.setTextSize(12);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(10, 0, 0, 12);
                        categoriaTextView.setLayoutParams(layoutParams);

                        // Define o texto e ícone da categoria
                        String texto;
                        int imagem;
                        switch (categoria) {
                            case "animal":
                                texto = "Animais de estimação permitidos";
                                imagem = R.drawable.patinha;
                                break;
                            case "genero":
                                texto = "Preferência de moradores";
                                imagem = R.drawable.gender;
                                break;
                            case "pessoa":
                                texto = "Número máximo de pessoas";
                                imagem = R.drawable.pessoas;
                                break;
                            case "fumo":
                                texto = "Frequência de fumo permitida";
                                imagem = R.drawable.fumo;
                                break;
                            case "bebida":
                                texto = "Frequência de bebida permitida";
                                imagem = R.drawable.bebida;
                                break;
                            case "casa":
                                texto = "Móveis & Outros";
                                imagem = R.drawable.bed;
                                break;
                            default:
                                texto = "";
                                imagem = 0;
                                break;
                        }

                        if (imagem != 0) {
                            categoriaTextView.setText(texto);
                            categoriaTextView.setCompoundDrawablesWithIntrinsicBounds(imagem, 0, 0, 0);
                            categoriaTextView.setCompoundDrawablePadding(5);
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
                        filtrosLinearLayout.addView(chipGroup);

                        Log.d("filtrosSelecionados", "onFiltroCadastroSuccess: " + filtrosSelecionados);

                        for (int i = 0; i < filtrosSelecionados.get(categoria).size(); i++) {
                            Log.d("filtrosSelecionados", "onFiltroCadastroSuccess: " + filtrosSelecionados.get(categoria));
                            adicionarChips(filtrosSelecionados.get(categoria).get(i), chipGroup);
                        }
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

    public void adicionarChips(String chipText, ChipGroup chipGroup) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Chip chip = (Chip) inflater.inflate(R.layout.chip_layout, chipGroup, false);
        chip.setText(chipText);
        chip.setId(View.generateViewId());
        chip.setCheckable(false);
        chip.setChecked(true);

        chipGroup.addView(chip);
    }
}