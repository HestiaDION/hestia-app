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
import com.example.hestia_app.EditarPerfilAnunciante;
import com.example.hestia_app.EditarPerfilUniversitario;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.PerfilAnuncianteCallback;
import com.example.hestia_app.data.api.callbacks.PerfilUniversitarioCallback;
import com.example.hestia_app.data.services.UniversitarioService;
import com.example.hestia_app.domain.models.Anunciante;
import com.example.hestia_app.domain.models.Universitario;
import com.example.hestia_app.presentation.view.LoginActivity;
import com.example.hestia_app.utils.ViewUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilUniversitario extends Fragment {
    String tipo_usuario;
    Button editarPerfil;
    UniversitarioService universitarioService = new UniversitarioService();
    String USER_DEFAULT_BIO = "Insira uma descrição sobre você na área de edição!";


    public PerfilUniversitario(String tipoUsuario) {
        this.tipo_usuario = tipoUsuario;
    }


    public static PerfilUniversitario newInstance(String tipoUsuario) {
        PerfilUniversitario fragment = new PerfilUniversitario(tipoUsuario);
        Bundle args = new Bundle();
        args.putString("tipo_usuario", tipoUsuario);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_universitario, container, false);

        // edição de perfil
        editarPerfil = view.findViewById(R.id.bt_editar);
        editarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditarPerfilUniversitario.class);
            startActivity(intent);

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
        TextView user_university = view.findViewById(R.id.user_university);

        assert user != null;
        popularCampos(profile_image, user_name, user_gender, user_age, user_bio, user_university, user);


        // logout com modal de confirmação
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

                    btnCancelLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }



            }
        });



        return view;
    }

    // popular campos
    public void popularCampos(CircleImageView profile_image, TextView user_name, TextView user_gender,
                              TextView user_age, TextView user_bio, TextView user_university, FirebaseUser user) {

        // informações guardadas no firebase
        Glide.with(this).load(user.getPhotoUrl())
                .centerCrop()
                .into(profile_image);
        user_name.setText(user.getDisplayName());


        // informações guardadas no postgres (necessário fazer chamada)
        universitarioService.listarPerfilUniversitario(user.getEmail(), new PerfilUniversitarioCallback() {

            @Override
            public void onPerfilUniversitarioSuccess(Universitario universitario) {
                Log.d("Universitario", "Perfil: " + universitario.toString());

                int idadeUniversitario = ViewUtils.calcularIdade(universitario.getDt_nascimento());
                String idadeUniversitarioString = idadeUniversitario + " anos";

                user_gender.setText(universitario.getGenero());
                user_age.setText(idadeUniversitarioString);
                user_university.setText(universitario.getUniversidade());

                if(isBioNullOrEmpty(universitario.getBio())){
                    user_bio.setText(USER_DEFAULT_BIO);
                } else{
                    user_bio.setText(universitario.getBio());

                }
            }

            @Override
            public void onPerfilUniversitarioFailure(String errorMessage) {
                Log.e("Universitário", errorMessage);

            }

        });


    }

    public boolean isBioNullOrEmpty(String user_bio) {
        return user_bio == null || user_bio.isEmpty();
    }

}