package com.example.hestia_app.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.hestia_app.R;
import com.example.hestia_app.domain.models.Moradia;
import com.example.hestia_app.presentation.fragments.cadastroMoradia.CadastroMoradiaDois;
import com.example.hestia_app.presentation.fragments.cadastroMoradia.CadastroMoradiaUm;
import com.example.hestia_app.presentation.view.AdicionarMoradia;
import com.example.hestia_app.presentation.view.adapter.MoradiaHomeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.widget.ImageView;
import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.PremiumScreenAnunciante;

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

        // Adicionar exemplos de moradias
        moradiaList.add(new Moradia("Moradia do Sol", "10/03/2021", 2, Arrays.asList("https://i.pinimg.com/736x/e8/a1/52/e8a15286aec46a1ac01c9c4091c3d793.jpg", "https://wallpapers.com/images/featured/kawaii-fofo-8qk5ge09amecxnln.jpg")));
        moradiaList.add(new Moradia("Moradia da Lua", "20/03/2021", 4, Arrays.asList("https://i.pinimg.com/736x/e8/a1/52/e8a15286aec46a1ac01c9c4091c3d793.jpg", "https://wallpapers.com/images/featured/kawaii-fofo-8qk5ge09amecxnln.jpg")));

        // Criar o adapter e setar no RecyclerView
        MoradiaHomeAdapter adapter = new MoradiaHomeAdapter(getContext(), moradiaList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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