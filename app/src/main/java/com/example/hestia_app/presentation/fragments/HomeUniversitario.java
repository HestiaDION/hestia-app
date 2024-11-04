package com.example.hestia_app.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hestia_app.PagamentoEmAprovacaoDialogFragment;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.PagamentoPorUserEmail;
import com.example.hestia_app.data.services.PagamentoService;
import com.example.hestia_app.domain.models.Pagamento;
import com.example.hestia_app.presentation.view.AnuncioCasa;
import com.example.hestia_app.presentation.view.OnSwipeTouchListener;
import com.example.hestia_app.presentation.view.PremiumScreenUniversitario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HomeUniversitario extends Fragment {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_IS_PREMIUM = "isUserPremium";

    private FrameLayout frameLayout; // Contêiner para os cartões
    private List<AnuncioCasa> anuncioCasaList; // Lista de anúncios para o swipe
    private int currentIndex = 0; // Índice do anúncio atual
    private ImageView premiumButton;
    private TextView txt_card;
    PagamentoService pagamentoService = new PagamentoService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anuncioCasaList = getMoradia(); // Obtém a lista de anúncios
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_universitario, container, false);
        premiumButton = view.findViewById(R.id.premiumButtonUniversity);
        frameLayout = view.findViewById(R.id.frame_cards);
        txt_card = view.findViewById(R.id.txt_card);
        txt_card.setVisibility(View.INVISIBLE);

        checkUserPaymentStatus();

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Exibe o diálogo de pagamento em aprovação se estiver pendente
        if (prefs.getBoolean("pagamentoEmAprovacao", false)) {
            PagamentoEmAprovacaoDialogFragment dialog = new PagamentoEmAprovacaoDialogFragment();
            dialog.show(getParentFragmentManager(), "PagamentoEmAprovacaoDialog");
            prefs.edit().putBoolean("pagamentoEmAprovacao", false).apply(); // Limpa o status após exibir o diálogo
        }

        premiumButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PremiumScreenUniversitario.class);
            startActivity(intent);
        });

        if (!anuncioCasaList.isEmpty()) {
            addNextCard();
        }

        return view;
    }

    // Método para verificar o status de pagamento do usuário atual
    private void checkUserPaymentStatus() {
        // Obtenha o SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isPremium = prefs.getBoolean(KEY_IS_PREMIUM, false); // Padrão: false se não encontrado

        // Se já for premium, esconda o botão
        if (isPremium) {
            premiumButton.setVisibility(View.GONE);
            return;
        }

        // Caso contrário, faça a chamada à API
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null) {
                pagamentoService.getPagamentoByUserEmail(email, new PagamentoPorUserEmail() {
                    @Override
                    public void onFindSuccess(boolean isPaid, Pagamento pagamento) {
                        // Armazene o resultado no SharedPreferences
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(KEY_IS_PREMIUM, isPaid);
                        editor.apply();

                        // Se o usuário já tiver pago, oculte o botão premium
                        if (isPaid) {
                            premiumButton.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFindFailure(boolean isPaid) {
                        // Caso o usuário não tenha pago, mantenha o botão visível
                        premiumButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

    // Método para adicionar o próximo cartão
    private void addNextCard() {
        if (currentIndex >= anuncioCasaList.size()) {
            txt_card.setVisibility(View.VISIBLE); // Mostra o texto de fim de lista
            Toast.makeText(getActivity(), "Você viu todos os anúncios!", Toast.LENGTH_SHORT).show();
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
        valor.setText("R$ " + anuncioCasa.getnValor().toString());

        frameLayout.addView(card);

        card.setOnTouchListener(new OnSwipeTouchListener(getActivity(), card) {
            @Override
            public void onSwipeLeft() {
                card.animate()
                        .translationX(-card.getWidth())
                        .setDuration(300)
                        .withEndAction(() -> {
                            Toast.makeText(getActivity(), "Deslizou para a esquerda: " + anuncioCasa.getcNmMoradia(), Toast.LENGTH_SHORT).show();
                            removeCard(card);
                            currentIndex++;
                            addNextCard();
                        })
                        .start();
            }

            @Override
            public void onSwipeRight() {
                card.animate()
                        .translationX(card.getWidth())
                        .setDuration(300)
                        .withEndAction(() -> {
                            Toast.makeText(getActivity(), "Deslizou para a direita: " + anuncioCasa.getcNmMoradia(), Toast.LENGTH_SHORT).show();
                            removeCard(card);
                            currentIndex++;
                            addNextCard();
                        })
                        .start();
            }
        });
    }

    private void removeCard(View card) {
        frameLayout.removeView(card);
    }

    private List<AnuncioCasa> getMoradia() {
        List<AnuncioCasa> anuncio = new ArrayList<>();
        anuncio.add(new AnuncioCasa("Moradia do sol", 2, 6, BigDecimal.valueOf(354), "https://i.pinimg.com/474x/a9/1a/1e/a91a1e756276722550f4982a71632251.jpg"));
        anuncio.add(new AnuncioCasa("Casa Rosa", 7, 15, BigDecimal.valueOf(900), "https://i.pinimg.com/474x/06/fd/90/06fd908860f0996771a587f9cdb8e730.jpg"));
        anuncio.add(new AnuncioCasa("Bom Lar", 8, 20, BigDecimal.valueOf(800), "https://i.pinimg.com/474x/ee/32/6f/ee326fe4147e0af122a3234255004eaf.jpg"));
        anuncio.add(new AnuncioCasa("Pousada das montanhas", 23, 30, BigDecimal.valueOf(1500), "https://i.pinimg.com/474x/18/e7/1b/18e71b1c6f158f03ce00fd78b1ecfa4b.jpg"));

        return anuncio;
    }
}
