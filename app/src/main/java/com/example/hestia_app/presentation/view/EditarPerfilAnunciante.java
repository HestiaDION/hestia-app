package com.example.hestia_app.presentation.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.InfosUserCallback;
import com.example.hestia_app.data.api.callbacks.PerfilAnuncianteCallback;
import com.example.hestia_app.data.api.callbacks.UpdatePerfilAnuncianteCallback;
import com.example.hestia_app.data.services.AnuncianteService;
import com.example.hestia_app.data.services.FirebaseService;
import com.example.hestia_app.data.services.InfosUserService;
import com.example.hestia_app.domain.models.Anunciante;
import com.example.hestia_app.domain.models.InfosUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditarPerfilAnunciante extends AppCompatActivity {

    EditText nome, bio;
    FirebaseService firebaseService = new FirebaseService();
    FirebaseAuth autenticar = FirebaseAuth.getInstance();
    FirebaseUser user = autenticar.getCurrentUser();
    Button salvar;
    ImageView editarImagem, imagem, goBack;
    Uri uri;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_anunciante);
        AnuncianteService anuncianteService = new AnuncianteService(getApplicationContext());

        nome = findViewById(R.id.nomeCompleto);
        bio = findViewById(R.id.sobreMim);
        salvar = findViewById(R.id.salvar);
        editarImagem = findViewById(R.id.editarFoto);
        imagem = findViewById(R.id.profile_image);
        goBack = findViewById(R.id.goBackArrow);
        progressBar = findViewById(R.id.progressBar2);

        // Inicialmente, torna a ProgressBar invisível
        progressBar.setVisibility(View.GONE);

        goBack.setOnClickListener(v -> finish());

        // Preenchendo os hints dos campos com base nas informações atuais
        anuncianteService.listarPerfilAnunciante(user.getEmail(), new PerfilAnuncianteCallback() {
            @Override
            public void onPerfilAnuncianteSuccess(Anunciante anunciante) {
                nome.setHint(user.getDisplayName());
                bio.setHint(anunciante.getBio());
            }

            @Override
            public void onPerfilAnuncianteFailure(String errorMessage) {
                Log.e("Hint", "Erro ao preencher os hints dos campos com as informações da API: " + errorMessage);
            }
        });

        // Preenchendo a foto com a foto atual
        String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;
        if (photoUrl != null) {
            Glide.with(this)
                    .load(photoUrl)
                    .into(imagem);
        }

        // Abrindo a galeria
        editarImagem.setOnClickListener(v2 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultLauncherGaleria.launch(intent);
        });

        // Salvando as novas informações dos campos
        salvar.setOnClickListener(v -> {


            if(nome.getText().toString().isEmpty()) {
                nome.setText(nome.getHint());
            }

            if(bio.getText().toString().isEmpty()) {
                bio.setText(bio.getHint());
            }

            // Torna a ProgressBar visível e desativa o botão de salvar
            progressBar.setVisibility(View.VISIBLE);
            salvar.setText("");
            salvar.setEnabled(false);

            // Salvando o nome e foto de perfil no Firebase
            if(!nome.getText().toString().isEmpty()) {
                String nomeFormatado = firebaseService.formatarNome(nome.getText().toString());
                firebaseService.updateDisplayName(this, user, nomeFormatado);
            }

            Anunciante anuncianteAtualizado = new Anunciante(nome.getText().toString(), bio.getText().toString());
            anuncianteService.atualizarAnuncianteProfile(user.getEmail(), anuncianteAtualizado, new UpdatePerfilAnuncianteCallback() {

                @Override
                public void onUpdateSuccess(boolean isUpdated) {
                    Log.d("Update Anunciante", "Anunciante atualizado com sucesso: " + isUpdated);

                    // Esconde a ProgressBar e reativa o botão de salvar
                    progressBar.setVisibility(View.GONE);
                    salvar.setEnabled(true);

                    // Finaliza a Activity após o sucesso
                    finish();
                }

                @Override
                public void onUpdateFailure(String errorMessage) {
                    Log.e("Update Anunciante", "Erro ao atualizar anunciante: " + errorMessage);

                    // Esconde a ProgressBar e reativa o botão de salvar em caso de erro
                    progressBar.setVisibility(View.GONE);
                    salvar.setEnabled(true);
                }
            });
        });
    }

    private ActivityResultLauncher<Intent> resultLauncherGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    // Imagem selecionada
                    uri = imageUri;

                    FirebaseService firebaseService = new FirebaseService();
                    firebaseService.updateProfilePicture(this, user, uri);

                    InfosUserService infosUserService = new InfosUserService();
                    InfosUser infosUser = new InfosUser(uri.toString());
                    infosUserService.updateProfilePhotoUrlMongoCollection(user.getEmail(), infosUser, new InfosUserCallback() {
                        @Override
                        public void onSuccess(InfosUser response) {
                            Log.d("updateFotoMongo", "onSuccess: " + response);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e("updateFotoMongo", "onFailure: " + t.getMessage());
                        }
                    });

                    // Exibe a imagem selecionada
                    Glide.with(this).load(imageUri)
                            .centerCrop()
                            .into(imagem);
                }
            }
    );
}
