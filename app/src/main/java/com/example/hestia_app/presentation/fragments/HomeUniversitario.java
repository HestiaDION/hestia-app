package com.example.hestia_app.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide; // Adicionar importação do Glide
import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.AnuncioCasa;
import com.example.hestia_app.presentation.view.OnSwipeTouchListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.PremiumScreenUniversitario;

public class HomeUniversitario extends Fragment {

    private FrameLayout frameLayout; // Contêiner para os cartões
    private List<AnuncioCasa> anuncioCasaList; // Lista de usuários para o swipe
    private int currentIndex = 0; // Índice do usuário atual
    ImageView premiumButton;

    TextView txt_card;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anuncioCasaList = getMoradia(); // Obtém a lista de usuários//
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_universitario, container, false);
        premiumButton = view.findViewById(R.id.premiumButtonUniversity);

        premiumButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PremiumScreenUniversitario.class);
            startActivity(intent);
        });


        frameLayout = view.findViewById(R.id.frame_cards);
        txt_card = view.findViewById(R.id.txt_card);
        txt_card.setVisibility(View.INVISIBLE);

        if (!anuncioCasaList.isEmpty()) {
            addNextCard();
        }

        return view;
    }

    // Método para adicionar o próximo cartão
    private void addNextCard() {
        if (currentIndex >= anuncioCasaList.size()) {
            txt_card.setVisibility(View.VISIBLE); // Mostra o texto de fim de lista
            Toast.makeText(getActivity(), "Você viu todos os anuncios!", Toast.LENGTH_SHORT).show();
            return;
        }

        AnuncioCasa anuncioCasa = anuncioCasaList.get(currentIndex);
        View card = LayoutInflater.from(getActivity()).inflate(R.layout.activity_item_moradia, null); // Infla o layout do cartão

        ImageView houseImg = card.findViewById(R.id.houseImg);
        TextView nmMoradia = card.findViewById(R.id.cNmMoradia);
        TextView qntPessoas = card.findViewById(R.id.qntPessoas);
        TextView qntQuartos = card.findViewById(R.id.iQntQuartos);
        TextView qntPessoasMax = card.findViewById(R.id.iQntPessoasMax);
        TextView valor = card.findViewById(R.id.nValor);

        String imgUrl = anuncioCasa.getHouseImg();
        Glide.with(getActivity())
                .load(imgUrl)
                .into(houseImg);

        nmMoradia.setText(anuncioCasa.getcNmMoradia());

        qntQuartos.setText(anuncioCasa.getiQntQuartos() + " quartos");

        qntPessoasMax.setText("Capacidade de " + anuncioCasa.getiQntPessoasMax() + " pessoas");

        // Carregando o valor do imóvel
        valor.setText("R$ " + anuncioCasa.getnValor().toString());

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
                            Toast.makeText(getActivity(), "Deslizou para a esquerda: " + anuncioCasa.getcNmMoradia(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(), "Deslizou para a direita: " + anuncioCasa.getcNmMoradia(), Toast.LENGTH_SHORT).show();
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
    private List<AnuncioCasa> getMoradia() {
        List<AnuncioCasa> anuncio = new ArrayList<>();
        anuncio.add(new AnuncioCasa("Moradia do sol", 2, 6, BigDecimal.valueOf(354), "https://i.pinimg.com/474x/a9/1a/1e/a91a1e756276722550f4982a71632251.jpg"));
        anuncio.add(new AnuncioCasa("Casa Rosa", 7, 15, BigDecimal.valueOf(900), "https://i.pinimg.com/474x/06/fd/90/06fd908860f0996771a587f9cdb8e730.jpg"));
        anuncio.add(new AnuncioCasa("Bom Lar", 8, 20, BigDecimal.valueOf(800), "https://i.pinimg.com/enabled/474x/ee/32/6f/ee326fe4147e0af122a3234255004eaf.jpg"));
        anuncio.add(new AnuncioCasa("Pousada das montanhas", 23, 30, BigDecimal.valueOf(1500), "https://i.pinimg.com/474x/18/e7/1b/18e71b1c6f158f03ce00fd78b1ecfa4b.jpg"));

        return anuncio;
    }

}

