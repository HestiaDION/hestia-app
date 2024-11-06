package com.example.hestia_app.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.ListaMoradiasCallback;
import com.example.hestia_app.data.services.MoradiaService;
import com.example.hestia_app.domain.models.Moradia;
import com.example.hestia_app.presentation.fragments.cadastroMoradia.CadastroMoradiaDois;
import com.example.hestia_app.presentation.fragments.cadastroMoradia.CadastroMoradiaUm;
import com.example.hestia_app.presentation.view.AdicionarMoradia;
import com.example.hestia_app.presentation.view.adapter.MoradiaHomeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import android.widget.ImageView;
import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.PremiumScreenAnunciante;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAnunciante extends Fragment {

    ImageButton btAdicionarMoradia;
    ImageView premiumButton;

    public HomeAnunciante() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_anunciante, container, false);
        btAdicionarMoradia = view.findViewById(R.id.btAdicionarMoradia);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_ad);
        List<Moradia> moradiaList = new ArrayList<>();

        // pegar as moradias cadastradas no banco
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        MoradiaService moradiaService = new MoradiaService();
        moradiaService.getMoradiasByAdvertiser(user.getEmail(), new ListaMoradiasCallback() {
            @Override
            public void onSuccess(List<Moradia> moradias) {
                moradiaList.addAll(moradias);
                // Criar o adapter e setar no RecyclerView
                MoradiaHomeAdapter adapter = new MoradiaHomeAdapter(getContext(), moradiaList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
                Log.d("Moradia", "onSuccess: " + moradias);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Moradia", "onFailure: " + t.getMessage());
            }
        });

        btAdicionarMoradia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdicionarMoradia.class);
                startActivity(intent);
            }
        });

        premiumButton = view.findViewById(R.id.premiumButton);

        premiumButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PremiumScreenAnunciante.class);
            startActivity(intent);
        });

        return view;
    }
}