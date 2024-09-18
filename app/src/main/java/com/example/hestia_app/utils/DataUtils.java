package com.example.hestia_app.utils;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class DataUtils {

    /**
     * Verifica se algum usuário já está logado
     */
     public static FirebaseUser verificarAutenticacao() {
         FirebaseAuth auth = FirebaseAuth.getInstance();
         FirebaseUser user = auth.getCurrentUser();

         if (user != null) {
             return user;
         } else {
             return null;
         }
     }

    /**
     * Se nenhum usuário estiver logado, criar login
     */
    public static Task<Void> salvarUsuario(String nome, String email, String senha, String imagem) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        // Obter usuário atual
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nome)
                                .setPhotoUri(Uri.parse(imagem))
                                .build();
                        // Atualizar o perfil e retornar a task resultante
                        return user.updateProfile(profile);
                    } else {
                        // Se a criação do usuário falhar, lançar a exceção para propagar o erro
                        throw task.getException();
                    }
                });
    }

    /**
     * Login do usuário
     */
    public static Task<AuthResult> login(String email, String senha) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.signInWithEmailAndPassword(email, senha);
    }

    /**
     * Logout do usuário
     */
    public static void logout() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
    }

    /**
     * Retorna o nome do usuário
     */
    public static String getNome() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        } else {
            return "";
        }
    }

    /**
     * Retorna o email do usuário
     */
    public static String getEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return "";
        }
    }

    /**
     * Retorna a imagem do usuário
     */
    public static String getImagem() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getPhotoUrl().toString();
        } else {
            return "";
        }
    }

    /**
     * Retorna o id do usuário
     */
    public static String getId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return "";
        }
    }
}
