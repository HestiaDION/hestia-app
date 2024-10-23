package com.example.hestia_app.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.PremiumScreenAnunciante;


public class HomeAnunciante extends Fragment {

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
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_anunciante, container, false);

        premiumButton = view.findViewById(R.id.premiumButton);

        premiumButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PremiumScreenAnunciante.class);
            startActivity(intent);
        });


        return view;
    }
}