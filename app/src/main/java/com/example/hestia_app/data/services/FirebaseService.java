package com.example.hestia_app.data.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hestia_app.presentation.view.MainActivityNavbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class FirebaseService {

    private final FirebaseAuth firebaseAuth;

    public FirebaseService() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void salvarUsuario(Context context, String txtNome, String txtEmail, String txtSenha, Uri uri, boolean isTermosAceitos) {
        String nome = formatarNome(txtNome);

        if (uri == null) {
            Toast.makeText(context, "Selecione uma imagem!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isTermosAceitos) {
            Toast.makeText(context, "É necessário aceitar os termos de uso!", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(txtEmail, txtSenha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Atualizar perfil
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nome)
                                    .setPhotoUri(uri)
                                    .build();

                            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, MainActivityNavbar.class);
                                        context.startActivity(intent);
                                    } else {
                                        // Mostrar erro
                                        mostrarErro(context, task, "Erro ao atualizar profile: ");
                                    }
                                }
                            });
                        } else {
                            // Mostrar erro
                            mostrarErro(context, task, "Erro ao efetuar o cadastro: ");
                        }
                    }
                });
    }

    private void mostrarErro(Context context, Task<?> task, String mensagem) {
        String msg = mensagem;
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (Exception e) {
            msg += e.getMessage();
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public String formatarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return "";
        }

        String[] partesDoNome = nome.trim().split("\\s+");
        String primeiroNome = capitalize(partesDoNome[0]);

        if (partesDoNome.length == 1) {
            return primeiroNome;
        }

        String ultimoNome = capitalize(partesDoNome[partesDoNome.length - 1]);
        return primeiroNome + " " + ultimoNome;
    }

    private String capitalize(String palavra) {
        if (palavra == null || palavra.isEmpty()) {
            return palavra;
        }
        return palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase();
    }

    public void updateDisplayName(Context context, FirebaseUser user, String newDisplayName) {

        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newDisplayName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Update Nome", "Name updated successfully!");
                        } else {
                            Log.e("Update Nome", "Name update failed", task.getException());
                        }
                    });
        } else {
            Toast.makeText(context, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateProfilePicture(Context context, FirebaseUser user, Uri newProfilePictureUri) {

        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(newProfilePictureUri)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Foto de perfil atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Erro ao atualizar a foto de perfil", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
        }
    }

}


