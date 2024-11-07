package com.example.hestia_app.presentation.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hestia_app.presentation.view.Configuracoes;
import com.example.hestia_app.presentation.view.EditarPerfilAnunciante;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.PerfilAnuncianteCallback;
import com.example.hestia_app.domain.models.Anunciante;
import com.example.hestia_app.data.services.AnuncianteService;
import com.example.hestia_app.presentation.view.Configuracoes;
import com.example.hestia_app.presentation.view.LoginActivity;
import com.example.hestia_app.utils.ViewUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAnunciante extends Fragment {
    String tipo_usuario;
    String USER_DEFAULT_BIO = "Insira uma descrição sobre você na área de edição!";
    AnuncianteService anuncianteService;
    Button editarPerfil;
    ImageView configuracoes;

    public PerfilAnunciante(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public static PerfilAnunciante newInstance(String tipo_usuario) {
        PerfilAnunciante fragment = new PerfilAnunciante(tipo_usuario);
        Bundle args = new Bundle();
        args.putString("tipo_usuario", tipo_usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        anuncianteService = new AnuncianteService(requireContext());
        if (getArguments() != null) {
            tipo_usuario = getArguments().getString("tipo_usuario");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_anunciante, container, false);

        configuracoes = view.findViewById(R.id.btn_config);
        configuracoes.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Configuracoes.class);
            startActivity(intent);
        });

        // edição de perfil
        editarPerfil = view.findViewById(R.id.bt_editar);
        editarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditarPerfilAnunciante.class);
            startActivity(intent);

        });

        // configurações
        configuracoes = view.findViewById(R.id.btn_config);
        configuracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Configuracoes.class);
                startActivity(intent);
            }
        });

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser user = autenticar.getCurrentUser();
        ImageButton logout = view.findViewById(R.id.logout);


        // campos do perfil
        CircleImageView profile_image = view.findViewById(R.id.profile_image);
        TextView user_name = view.findViewById(R.id.user_name);
        TextView user_gender = view.findViewById(R.id.user_gender);
        TextView user_age = view.findViewById(R.id.user_age);
        TextView user_bio = view.findViewById(R.id.user_bio);

        assert user != null;
        popularCampos(profile_image, user_name, user_gender, user_age, user_bio, user);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() != null && getActivity() != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_logout_confirmation, null);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();

                    Button btnConfirmLogout = dialogView.findViewById(R.id.btn_confirm_logout);
                    Button btnCancelLogout = dialogView.findViewById(R.id.btn_cancel_logout);

                    btnConfirmLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseAuth.getInstance().signOut();

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);


                            alertDialog.dismiss();

                            getActivity().finish();
                            autenticar.signOut();

                        }
                    });

                    // Se o usuário cancelar, apenas fecha o modal
                    btnCancelLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    // Exibir o modal
                    alertDialog.show();
                }



            }
        });


        return view;
    }

    // popular campos
    public void popularCampos(CircleImageView profile_image, TextView user_name, TextView user_gender, TextView user_age, TextView user_bio, FirebaseUser user) {
        // informações guardadas no firebase
        Glide.with(this).load(user.getPhotoUrl())
                .centerCrop()
                .into(profile_image);
        user_name.setText(user.getDisplayName());


        // informações guardadas no postgres (necessário fazer chamada)
        anuncianteService.listarPerfilAnunciante(user.getEmail(), new PerfilAnuncianteCallback() {
            @Override
            public void onPerfilAnuncianteSuccess(Anunciante anunciante) {
                Log.d("Anunciante", "Perfil: " + anunciante.toString());

                int idadeAnunciante = ViewUtils.calcularIdade(anunciante.getDt_nascimento());
                String idadeAnuncianteString = Integer.toString(idadeAnunciante) + " anos";

                user_gender.setText(anunciante.getGenero());
                user_age.setText(idadeAnuncianteString);

                if(isBioNullOrEmpty(anunciante.getBio())){
                    user_bio.setText(USER_DEFAULT_BIO);
                } else{
                    user_bio.setText(anunciante.getBio());

                }


            }

            @Override
            public void onPerfilAnuncianteFailure(String errorMessage) {
                Log.e("Anunciante", errorMessage);
            }
        });


    }

    public boolean isBioNullOrEmpty(String user_bio) {
        return user_bio == null || user_bio.isEmpty();
    }



}