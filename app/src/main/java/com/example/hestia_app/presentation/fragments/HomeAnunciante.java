package com.example.hestia_app.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.hestia_app.PagamentoEmAprovacaoDialogFragment;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.ListaMoradiasCallback;
import com.example.hestia_app.data.api.callbacks.PagamentoPorUserEmail;
import com.example.hestia_app.data.services.MoradiaService;
import com.example.hestia_app.data.services.PagamentoService;
import com.example.hestia_app.domain.models.Moradia;
import com.example.hestia_app.domain.models.Pagamento;
import com.example.hestia_app.presentation.PaymentDialogFragment;
import com.example.hestia_app.presentation.view.AdicionarMoradia;
import com.example.hestia_app.presentation.view.adapter.MoradiaHomeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import com.example.hestia_app.presentation.view.PremiumScreenAnunciante;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAnunciante extends Fragment {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_IS_PREMIUM = "isPremium";
    private PagamentoService pagamentoService;

    ImageButton btAdicionarMoradia;
    ImageView premiumButton;

    public HomeAnunciante() {}

    public static HomeAnunciante newInstance() {
        HomeAnunciante fragment = new HomeAnunciante();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_anunciante, container, false);
        btAdicionarMoradia = view.findViewById(R.id.btAdicionarMoradia);
        premiumButton = view.findViewById(R.id.premiumButton);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_ad);
        List<Moradia> moradiaList = new ArrayList<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // services
        MoradiaService moradiaService = new MoradiaService(requireContext());
        pagamentoService = new PagamentoService(requireContext());


        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isPremium = prefs.getBoolean(KEY_IS_PREMIUM, false);

        // Exibe o diálogo de pagamento em aprovação se estiver pendente
        if (prefs.getBoolean("pagamentoEmAprovacao", false)) {
            PagamentoEmAprovacaoDialogFragment dialog = new PagamentoEmAprovacaoDialogFragment();
            dialog.show(getParentFragmentManager(), "PagamentoEmAprovacaoDialog");
            prefs.edit().putBoolean("pagamentoEmAprovacao", false).apply(); // Limpa o status após exibir o diálogo
        }

        assert user != null;
        if (isPremium) {
            premiumButton.setVisibility(View.GONE);
        } else {
            checkUserPaymentStatus(user.getEmail(), prefs);
        }

        moradiaService.getMoradiasByAdvertiser(user.getEmail(), new ListaMoradiasCallback() {
            @Override
            public void onSuccess(List<Moradia> moradias) {
                moradiaList.addAll(moradias);

                MoradiaHomeAdapter adapter = new MoradiaHomeAdapter(getContext(), moradiaList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Moradia", "onFailure: " + t.getMessage());
            }
        });

        btAdicionarMoradia.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AdicionarMoradia.class);
            startActivity(intent);
        });

        premiumButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PremiumScreenAnunciante.class);
            startActivity(intent);
        });

        return view;
    }

    private void checkUserPaymentStatus(String email, SharedPreferences prefs) {
        pagamentoService.getPagamentoByUserEmail(email, new PagamentoPorUserEmail() {
            @Override
            public void onFindSuccess(boolean wasFound, Pagamento pagamento) {
                // Se o pagamento foi encontrado, ocultar o ícone do plano e atualizar as preferências
                premiumButton.setVisibility(View.GONE);
                prefs.edit().putBoolean(KEY_IS_PREMIUM, true).apply();
            }

            @Override
            public void onFindFailure(boolean wasFound) {
                // Se o pagamento não foi encontrado, manter o ícone visível
                premiumButton.setVisibility(View.VISIBLE);
                prefs.edit().putBoolean(KEY_IS_PREMIUM, false).apply();
            }
        });
    }
}
