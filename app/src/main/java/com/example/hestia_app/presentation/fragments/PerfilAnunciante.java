package com.example.hestia_app.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAnunciante extends Fragment {

    public PerfilAnunciante() {
        // Required empty public constructor
    }

    public static PerfilAnunciante newInstance() {
        PerfilAnunciante fragment = new PerfilAnunciante();
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

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser user = autenticar.getCurrentUser();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_anunciante, container, false);
        CircleImageView profile_image = view.findViewById(R.id.profile_image);
        TextView user_name = view.findViewById(R.id.user_name);
        TextView user_gender = view.findViewById(R.id.user_gender);
        TextView user_age = view.findViewById(R.id.user_age);
        ImageButton logout= view.findViewById(R.id.logout);

        popularCampos(profile_image, user_name, user_gender, user_age, user);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticar.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    // popular campos
    public void popularCampos(CircleImageView profile_image, TextView user_name, TextView user_gender, TextView user_age, FirebaseUser user) {
        // foto
        Glide.with(this).load(user.getPhotoUrl())
                .centerCrop()
                .into(profile_image);

        // username
        user_name.setText(user.getDisplayName());
    }
}