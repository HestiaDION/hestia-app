package com.example.hestia_app.presentation.fragments;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.PerfilAnuncianteCallback;
import com.example.hestia_app.data.models.Anunciante;
import com.example.hestia_app.data.services.AnuncianteService;
import com.example.hestia_app.presentation.view.LoginActivity;
import com.example.hestia_app.presentation.view.MainActivityNavbar;
import com.example.hestia_app.utils.ViewUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAnunciante extends Fragment {
    String tipo_usuario;
    String USER_DEFAULT_BIO = "Insira uma descrição sobre você na área de edição!";
    AnuncianteService anuncianteService = new AnuncianteService();

    public PerfilAnunciante(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
        // Required empty public constructor
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
        if (getArguments() != null) {
            tipo_usuario = getArguments().getString("tipo_usuario");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser user = autenticar.getCurrentUser();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_anunciante, container, false);
        ImageButton logout= view.findViewById(R.id.logout);

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
                // Verifica se o contexto não é nulo, importante em Fragments
                if (getContext() != null && getActivity() != null) {
                    // Criar o modal de confirmação de logout
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_logout_confirmation, null);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();

                    // Pegando os botões do layout do modal
                    Button btnConfirmLogout = dialogView.findViewById(R.id.btn_confirm_logout);
                    Button btnCancelLogout = dialogView.findViewById(R.id.btn_cancel_logout);

                    // Quando o usuário confirmar o logout
                    btnConfirmLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Logout do Firebase
                            FirebaseAuth.getInstance().signOut();

                            // Redirecionar para a tela de login
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);

                            // Fechar o modal
                            alertDialog.dismiss();

                            // Fechar a Activity atual
                            getActivity().finish();
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
        // gênero, universidade, bio, data de nascimento;
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
        if (user_bio == null || user_bio.isEmpty()) {
            return true;
        }
        return false;
    }



}