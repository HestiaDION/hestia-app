package com.example.hestia_app.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide; // Adicionar importação do Glide
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.FiltroCadastroCallback;
import com.example.hestia_app.data.api.callbacks.FiltrosTagsCallback;
import com.example.hestia_app.data.api.callbacks.FiltrosTagsCallbackGet;
import com.example.hestia_app.data.api.callbacks.GetCategoriaByNomeCallback;
import com.example.hestia_app.data.api.callbacks.GetUUIDByEmailCallback;
import com.example.hestia_app.data.api.callbacks.ImagensMoradiaCallback;
import com.example.hestia_app.data.api.callbacks.MoradiaByIdCallback;
import com.example.hestia_app.data.api.callbacks.MoradiaFavoritaCallback;
import com.example.hestia_app.data.api.callbacks.RecomendacoesMoradiaCallback;
import com.example.hestia_app.data.services.FiltroCadastroService;
import com.example.hestia_app.data.services.FiltrosTagsService;
import com.example.hestia_app.data.services.ImagensMoradiaService;
import com.example.hestia_app.data.services.MoradiaFavoritaService;
import com.example.hestia_app.data.services.MoradiaService;
import com.example.hestia_app.data.services.RecomendacoesMoradiasService;
import com.example.hestia_app.data.services.UniversitarioService;
import com.example.hestia_app.domain.models.FiltroCadastro;
import com.example.hestia_app.domain.models.FiltrosTags;
import com.example.hestia_app.domain.models.ImagensMoradia;
import com.example.hestia_app.domain.models.Moradia;
import com.example.hestia_app.domain.models.MoradiaFavorita;
import com.example.hestia_app.domain.models.RecomendacoesMoradia;
import com.example.hestia_app.domain.models.UniversityRequest;
import com.example.hestia_app.presentation.view.AnuncioCasa;
import com.example.hestia_app.presentation.view.MoradiasFavoritasActivity;
import com.example.hestia_app.presentation.view.OnSwipeTouchListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.PremiumScreenUniversitario;
import com.example.hestia_app.presentation.view.adapter.CustomArrayAdapter;
import com.example.hestia_app.presentation.view.adapter.HouseImgAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeUniversitario extends Fragment {

    private FrameLayout frameLayout;
    private int currentIndex = 0;

    private final FirebaseAuth autenticar = FirebaseAuth.getInstance();
    private final FirebaseUser user = autenticar.getCurrentUser();

    private static final List<Moradia> moradiasLista = new ArrayList<>();
    private static final HashMap<String, List<String>> selecoesPorCategoria = new HashMap<>();
    private static final ArrayList<UUID> moradiasFavoritadas = new ArrayList<>();

    TextView txt_card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMoradia();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_universitario, container, false);

        ImageView premiumButton = view.findViewById(R.id.premiumButtonUniversity);
        LinearLayout filtrosSelecionados = view.findViewById(R.id.filtrosSelecionados);
        ImageView favoriteHouses = view.findViewById(R.id.favoriteHouses);

        favoriteHouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MoradiasFavoritasActivity.class);
                startActivity(intent);
            }
        });

        UniversitarioService univService = new UniversitarioService();
        univService.getUniversitarioId(user.getEmail(), new GetUUIDByEmailCallback() {
            @Override
            public void onGetUUIDByEmailSuccess(String uuid) {
                Log.d("uuid", "onGetUUIDByEmailSuccess: " + uuid);

                FiltroCadastroService service = new FiltroCadastroService();
                service.getCategorias(new FiltroCadastroCallback() {
                    @Override
                    public void onFiltroCadastroSuccess(List<FiltroCadastro> filtros, List<String> categorias) {
                        // Criar uma lista para armazenar todas as sugestões de filtro
                        List<String> suggestions = new ArrayList<>();

                        // Iterar sobre cada categoria para buscar os filtros correspondentes
                        for (String categoria : categorias) {
                            service.getFiltrosPorCategoria(categoria, new FiltroCadastroCallback() {
                                @Override
                                public void onFiltroCadastroSuccess(List<FiltroCadastro> filtros, List<String> categorias) {
                                    for (FiltroCadastro filtro : filtros) {
                                        // Adicionar o nome do filtro à lista de sugestões, evitando duplicados
                                        if (!suggestions.contains(filtro.getNome())) {
                                            suggestions.add(filtro.getNome());
                                        }
                                    }

                                    // Verifica se o fragmento está anexado ao contexto antes de atualizar a UI
                                    if (isAdded()) {
                                        // Atualizar o adaptador no final da coleta de todas as sugestões
                                        CustomArrayAdapter adapter = new CustomArrayAdapter(requireContext(), suggestions);
                                        MultiAutoCompleteTextView multiAutoCompleteTextView = view.findViewById(R.id.searchFilters);

                                        multiAutoCompleteTextView.setAdapter(adapter);
                                        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                                        // Listener para o item selecionado
                                        multiAutoCompleteTextView.setOnItemClickListener((parent, v, position, id) -> {
                                            String selectedItem = (String) parent.getItemAtPosition(position);
                                            Log.d("selectedItem", "onFiltroCadastroSuccess: " + selectedItem);
                                            service.getCategoriaByNome(selectedItem, new GetCategoriaByNomeCallback() {
                                                @Override
                                                public void onGetCategoriaByNomeCallbackSuccess(HashMap<String, String> categoria) {

                                                    // Verifica se o fragmento está anexado ao contexto antes de acessar a UI
                                                    if (isAdded()) {
                                                        LinearLayout filtrosSelecionados = view.findViewById(R.id.filtrosSelecionados);
                                                        for (int i = 0; i < filtrosSelecionados.getChildCount(); i++) {
                                                            View child = filtrosSelecionados.getChildAt(i);

                                                            // Verifica se o filho é uma instância de ChipGroup
                                                            if (child instanceof ChipGroup) {
                                                                ChipGroup chipGroup = (ChipGroup) child;

                                                                if (categoria.get("categoria").equals(child.getContentDescription())) {
                                                                    // Adiciona um chip ao ChipGroup encontrado
                                                                    adicionarChips(selectedItem, chipGroup, getContext());
                                                                    multiAutoCompleteTextView.setText(""); // Limpar o texto após a seleção
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onGetCategoriaByNomeCallbackFailure(String errorMessage) {
                                                    Log.d("erroGetCategoriaByNome", "onGetCategoriaByNomeCallbackFailure: " + errorMessage);
                                                }
                                            });
                                        });
                                    }
                                }

                                @Override
                                public void onFiltroCadastroFailure(String errorMessage) {
                                    Log.d("onFiltroCadastroFailure", "onFiltroCadastroFailure: " + errorMessage);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFiltroCadastroFailure(String errorMessage) {
                        Log.d("onFiltroCadastroFailure", "onFiltroCadastroFailure: " + errorMessage);
                    }
                });
            }

            @Override
            public void onGetUUIDByEmailFailure(String errorMessage) {
                Log.d("uuid", "onGetUUIDByEmailFailure: " + errorMessage);
            }
        });

        carregarCategorias(filtrosSelecionados, getContext());

        premiumButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PremiumScreenUniversitario.class);
            startActivity(intent);
        });

        frameLayout = view.findViewById(R.id.frame_cards);
        txt_card = view.findViewById(R.id.txt_card);
        txt_card.setVisibility(View.INVISIBLE);

        return view;
    }

    // Método para adicionar o próximo cartão
    private void addNextCard() {

        Log.d("addNextCard", "addNextCard: " + moradiasLista.size());

        final List<String>[] imageList = new List[]{new ArrayList<>()};

        if (currentIndex >= moradiasLista.size()) {
            txt_card.setVisibility(View.VISIBLE); // Mostra o texto de fim de lista
            Toast.makeText(getActivity(), "Você viu todos os anuncios!", Toast.LENGTH_SHORT).show();
            return;
        }

        Moradia anuncioCasa = moradiasLista.get(currentIndex);
        View card = LayoutInflater.from(getActivity()).inflate(R.layout.activity_item_moradia, null); // Infla o layout do cartão

        ViewPager2 houseImg = card.findViewById(R.id.houseImg);
        TextView nmMoradia = card.findViewById(R.id.cNmMoradia);
        TextView qntPessoas = card.findViewById(R.id.qntPessoas);
        TextView qntQuartos = card.findViewById(R.id.iQntQuartos);
        TextView qntPessoasMax = card.findViewById(R.id.iQntPessoasMax);
        TextView valor = card.findViewById(R.id.nValor);

        Log.d("moradia", "addNextCard: " + anuncioCasa);

        nmMoradia.setText(anuncioCasa.getNomeCasa());

        qntQuartos.setText(anuncioCasa.getQuantidadeQuartos() + " quartos");

        qntPessoasMax.setText("Capacidade de " + anuncioCasa.getQuantidadeMaximaPessoas() + " pessoas");

        // Carregando o valor do imóvel
        valor.setText(String.format("R$ %.2f", anuncioCasa.getAluguel()));

        // pegar as imagens do mongo
        ImagensMoradiaService imagensMoradiaService = new ImagensMoradiaService();
        Log.d("uuidMoradia", "addNextCard: " + anuncioCasa.getId());
        imagensMoradiaService.getImagensMoradias(anuncioCasa.getId(), new ImagensMoradiaCallback() {
            @Override
            public void onSuccess(ImagensMoradia response) {
                Log.d("Imagens", "onSuccess: " + response.getImagens());
                imageList[0] = response.getImagens();
                // Configure o ViewPager2 com o adapter de imagens
                HouseImgAdapter houseImgAdapter = new HouseImgAdapter(getContext(), imageList[0]);
                houseImg.setAdapter(houseImgAdapter);
                houseImgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("imagens", "onFailure: " + t.getMessage());
            }
        });

        // Adiciona o cartão ao FrameLayout
        frameLayout.addView(card);

        // Define o listener para detectar swipes
        card.setOnTouchListener(new OnSwipeTouchListener(getActivity(), card) {
            @Override
            public void onSwipeLeft() {
                // Animação ao deslizar para a esquerda
                card.animate()
                        .translationX(-card.getWidth()) // desliza para a esquerda
                        .setDuration(300) // tempo de animação
                        .withEndAction(() -> {
                            removeCard(card); // Remove o cartão
                            currentIndex++; // Avança para o próximo
                            addNextCard(); // Adiciona o próximo cartão
                        })
                        .start();
            }

            @Override
            public void onSwipeRight() {
                // Animação ao deslizar para a direita
                card.animate()
                        .translationX(card.getWidth()) // desliza para a direita
                        .setDuration(300) // tempo de animação
                        .withEndAction(() -> {
                            // adiciona a moradia para favoritas
                            addMoradiaFavorita(anuncioCasa.getId());
                            removeCard(card); // Remove o cartão
                            currentIndex++; // Avança para o próximo
                            addNextCard(); // Adiciona o próximo cartão
                        })
                        .start();
            }
        });
    }

    // Método para remover um cartão
    private void removeCard(View card) {
        frameLayout.removeView(card);
    }

    // Método para obter a lista de moradias
    private void getMoradia() {
        UniversitarioService univService = new UniversitarioService();
        univService.getUniversitarioId(user.getEmail(), new GetUUIDByEmailCallback() {
            @Override
            public void onGetUUIDByEmailSuccess(String uuid) {
                // pegar as moradias do mongo e repetir a requisição até que de certo
                RecomendacoesMoradiasService service = new RecomendacoesMoradiasService();
                service.getRecomendacoesMoradia(new UniversityRequest(UUID.fromString(uuid)), new RecomendacoesMoradiaCallback() {
                    @Override
                    public void onRecomendacoesSuccess(boolean success, RecomendacoesMoradia recomendacoesMoradia) {
                        List<HashMap<String, String>> moradias = recomendacoesMoradia.getHouses();
                        for (HashMap<String, String> moradia2 : moradias) {
                            MoradiaService serviceMoradia = new MoradiaService();
                            Log.d("uuid", "onRecomendacoesSuccess: " + moradia2);
                            serviceMoradia.getMoradiaById(UUID.fromString(moradia2.get("uid")), new MoradiaByIdCallback() {
                                @Override
                                public void onSuccess(Moradia moradia) {
                                    if (!moradiasLista.contains(moradia)) {
                                        moradiasLista.add(moradia);
                                        if(isAdded()) {
                                            addNextCard();
                                        }
                                    }
                                    Log.d("moradiasFinal", "onSuccess: " + moradiasLista);
                                }

                                @Override
                                public void onFailure(String message) {
                                    Log.d("Recomendacao", "onFailure: " + message);
                                }
                            });
                        }
                    }

                    @Override
                    public void onRecomendacoesFailure(boolean failure) {
                        Log.d("Recomendacao", "onFailure: " + failure);
                    }
                });
            }

            @Override
            public void onGetUUIDByEmailFailure(String erroMessage) {
                Log.d("falha", "onGetUUIDByEmailFailure: " + erroMessage);
            }
        });
    }

    private void addMoradiaFavorita(UUID id) {
        UniversitarioService universitarioService = new UniversitarioService();
        universitarioService.getUniversitarioId(user.getEmail(), new GetUUIDByEmailCallback() {
            @Override
            public void onGetUUIDByEmailSuccess(String uuid) {
                if(!moradiasFavoritadas.contains(UUID.fromString(uuid))) {
                    moradiasFavoritadas.add(id);
                }
                MoradiaFavoritaService service = new MoradiaFavoritaService();
                service.addMoradiasFavoritas(new MoradiaFavorita(UUID.fromString(uuid), moradiasFavoritadas), new MoradiaFavoritaCallback() {
                    @Override
                    public void moradiaFavoritaOnSuccess(MoradiaFavorita moradiaFavorita) {
                        Log.d("moradiaFavorita", "moradiaFavoritaOnSuccess: " + moradiaFavorita);
                    }

                    @Override
                    public void moradiaFavoritaOnFailure(String message) {
                        Log.d("moradiaFavorita", "moradiaFavoritaOnFailure: " + message);
                    }
                });
            }

            @Override
            public void onGetUUIDByEmailFailure(String erroMessage) {
                Log.d("falha", "onGetUUIDByEmailFailure: " + erroMessage);
            }
        });
    }

    private void carregarCategorias(LinearLayout layout, Context context) {
        // pegar uuid do universitário
        UniversitarioService universitarioService = new UniversitarioService();
        universitarioService.getUniversitarioId(user.getEmail(), new GetUUIDByEmailCallback() {
            @Override
            public void onGetUUIDByEmailSuccess(String uuid) {
                FiltrosTagsService service = new FiltrosTagsService();
                service.getFiltrosTag(uuid, new FiltrosTagsCallbackGet() {
                    @Override
                    public void onSuccess(FiltrosTags tags) {
                        // carregar as listas
                        Log.d("uuid", "onSuccess: " + tags);
                        if (tags != null) {
                            if (!tags.getAnimais_estimacao().isEmpty()){
                                selecoesPorCategoria.put("animal", tags.getAnimais_estimacao());
                            }
                            if (!tags.getPreferencia_genero().isEmpty()){
                                selecoesPorCategoria.put("genero", Collections.singletonList(tags.getPreferencia_genero()));
                            }
                            if (!tags.getFrequencia_bebida().isEmpty()){
                                selecoesPorCategoria.put("bebida", Collections.singletonList(tags.getFrequencia_bebida()));
                            }
                            if (!tags.getFrequencia_fumo().isEmpty()){
                                selecoesPorCategoria.put("fumo", Collections.singletonList(tags.getFrequencia_fumo()));
                            }
                            if (!tags.getPreferencias_moveis_outro().isEmpty()){
                                selecoesPorCategoria.put("casa", tags.getPreferencias_moveis_outro());
                            }
                            if (!tags.getNumero_maximo_pessoas().isEmpty()){
                                selecoesPorCategoria.put("pessoa", Collections.singletonList(tags.getNumero_maximo_pessoas()));
                            }

                            carregarCategoriasTags(layout, context);
                        } else {
                            Log.d("API Response", "Os filtros cadastrados vieram vazios.");
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.d("API Response", "onFailure: " + errorMessage);
                    }
                });
            }

            @Override
            public void onGetUUIDByEmailFailure(String erroMessage) {
                Log.d("falha", "onGetUUIDByEmailFailure: " + erroMessage);
            }
        });
    }


    private void carregarCategoriasTags(LinearLayout filtrosLinearLayout, Context context) {
        for (String categoria : selecoesPorCategoria.keySet()) {

            Log.d("selecoerPorCategoria", "carregarCategoriasTags: " + categoria);
            // Criar um TextView para a categoria
            TextView categoriaTextView = new TextView(context);
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
            if (categoria.equals("animal")) {
                texto = "Animais de estimação permitidos";
                imagem = R.drawable.patinha;
            } else if (categoria.equals("genero")) {
                texto = "Preferência de moradores";
                imagem = R.drawable.gender;
            } else if (categoria.equals("pessoa")) {
                texto = "Número máximo de pessoas";
                imagem = R.drawable.pessoas_black;
            } else if (categoria.equals("fumo")) {
                texto = "Frequência de fumo permitida";
                imagem = R.drawable.fumo;
            } else if (categoria.equals("bebida")) {
                texto = "Frequência de bebida permitida";
                imagem = R.drawable.bebida;
            } else if (categoria.equals("casa")) {
                texto = "Móveis & Outros";
                imagem = R.drawable.bed;
            } else {
                texto = "";
                imagem = 0;
            }

            if (imagem != 0) {
                categoriaTextView.setText(texto);
                categoriaTextView.setCompoundDrawablesWithIntrinsicBounds(imagem, 0, 0, 0);
                categoriaTextView.setCompoundDrawablePadding(7);
            }

            Typeface typeface = ResourcesCompat.getFont(context, R.font.poppins_semi_bold);
            categoriaTextView.setTypeface(typeface);

            categoriaTextView.setTextColor(context.getResources().getColor(R.color.black));

            filtrosLinearLayout.addView(categoriaTextView);

            // Criar um ChipGroup para os filtros
            ChipGroup chipGroup = new ChipGroup(context);
            chipGroup.setContentDescription(categoria);
            LinearLayout.LayoutParams layoutParamsChip = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParamsChip.setMargins(10, 0, 10, 10);
            chipGroup.setLayoutParams(layoutParamsChip);
            filtrosLinearLayout.addView(chipGroup);

            // Adicionar chips ao ChipGroup
            if (!Objects.requireNonNull(selecoesPorCategoria.get(categoria)).isEmpty()) {
                for (String filtro : Objects.requireNonNull(selecoesPorCategoria.get(categoria))) {
                    adicionarChips(filtro, chipGroup, context);
                }
            }
        }
    }

    public void adicionarChips(String chipText, ChipGroup chipGroup, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        Chip chip = (Chip) inflater.inflate(R.layout.chip_layout2, chipGroup, false);
        chip.setText(chipText);
        chip.setId(View.generateViewId());
        chip.setClickable(false);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(chip);;
            }
        });

        chipGroup.addView(chip);
    }
}

