package com.example.hestia_app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hestia_app.data.api.callbacks.PerfilUniversitarioCallback;
import com.example.hestia_app.data.api.callbacks.UpdatePerfilAnuncianteCallback;
import com.example.hestia_app.data.api.callbacks.UpdatePerfilUniversitarioCallback;
import com.example.hestia_app.data.services.FirebaseService;
import com.example.hestia_app.data.services.UniversitarioService;
import com.example.hestia_app.domain.models.Anunciante;
import com.example.hestia_app.domain.models.Universitario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class EditarPerfilUniversitario extends AppCompatActivity {

    EditText nome, bio;
    UniversitarioService universitarioService = new UniversitarioService();
    FirebaseService firebaseService = new FirebaseService();
    FirebaseAuth autenticar = FirebaseAuth.getInstance();
    FirebaseUser user = autenticar.getCurrentUser();
    Button salvar;
    ImageView editarImagem, imagem, goBack;
    Uri uri;
    HashMap<String, String> usuarioAcessoGaleria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_universitario);

        nome = findViewById(R.id.nomeCompleto);
        bio = findViewById(R.id.sobreMim);
        salvar = findViewById(R.id.salvar);
        editarImagem = findViewById(R.id.editarFoto);
        imagem = findViewById(R.id.profile_image);
        goBack = findViewById(R.id.goBackArrow);

        goBack = findViewById(R.id.goBackArrow);
        goBack.setOnClickListener(v -> finish());

        // preenchendo os hints dos campos com base nas informações atuais
        universitarioService.listarPerfilUniversitario(user.getEmail(), new PerfilUniversitarioCallback() {

            @Override
            public void onPerfilUniversitarioSuccess(Universitario universitario) {
                nome.setHint(user.getDisplayName());
                bio.setHint(universitario.getBio());
            }

            @Override
            public void onPerfilUniversitarioFailure(String errorMessage) {
                Log.e("Hint", "Erro ao preencher os hints dos campos com as informações da API: " + errorMessage);

            }
        });

        // preenchendo a foto com a foto atual
        String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;
        if (photoUrl != null) {
            Glide.with(this)
                    .load(photoUrl)
//                    .placeholder(R.drawable.placeholder_image)
//                    .error(R.drawable.error_image)
                    .into(imagem);
        }

        salvar.setOnClickListener(v -> {

            if(nome.getText().toString().isEmpty() ){
                nome.setText(nome.getHint());
            }

            if(bio.getText().toString().isEmpty() ){
                bio.setText(bio.getHint());
            }

            // salvando o nome e foto de perfil no firebase
            if(!nome.getText().toString().isEmpty()) {
                String nomeFormatado = firebaseService.formatarNome(nome.getText().toString());
                firebaseService.updateDisplayName(this, user, nomeFormatado);
            }


            // TODO: abrir a galeria para acessar nova foto de perfil
//            // abrindo a galeria
//            editarImagem.setOnClickListener(v2 -> {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                resultLauncherGaleria.launch(intent);
//                Toast.makeText(this, "Selecione uma imagem", Toast.LENGTH_SHORT).show();
//            });

            Log.d("Email", user.getEmail());
            Universitario universitarioAtualizado = new Universitario(nome.getText().toString(), bio.getText().toString());
            Log.d("Universitario", universitarioAtualizado.toString());
            universitarioService.atualizarUniversitarioProfile(user.getEmail(), universitarioAtualizado, new UpdatePerfilUniversitarioCallback() {

                @Override
                public void onUpdateSuccess(boolean isUpdated) {
                    Log.d("Update Universitário", "Universitário atualizado com sucesso: " + isUpdated);

                }

                @Override
                public void onUpdateFailure(String errorMessage) {
                    Log.d("Update Universitário", "Erro ao atualizar universitário: " + errorMessage);
                    Toast.makeText(EditarPerfilUniversitario.this, "Erro ao atualizar universitário: " + errorMessage, Toast.LENGTH_SHORT).show();

                }
            });
            finish();

        });

    }
}