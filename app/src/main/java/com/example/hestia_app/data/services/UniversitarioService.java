package com.example.hestia_app.data.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.RegistroUniversitarioCallback;
import com.example.hestia_app.data.api.repo.UniversitarioRepository;
import com.example.hestia_app.data.api.callbacks.PerfilUniversitarioCallback;
import com.example.hestia_app.data.api.callbacks.RegistroAnuncianteCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.domain.models.Universitario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UniversitarioService {

    UniversitarioRepository universitarioRepository = RetrofitPostgresClient.getClient().create(UniversitarioRepository.class);


    public void registrarUniversitario(Universitario universitario, RegistroAnuncianteCallback callback) {
        Call<Universitario> call = universitarioRepository.registerUniversitario(universitario);

        call.enqueue(new Callback<Universitario>() {
            @Override
            public void onResponse(@NonNull Call<Universitario> call, @NonNull Response<Universitario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Universitario universitarioResponse = response.body();
                    Log.d("Registro", "Universitário registrado com sucesso: " + universitarioResponse.getNome());
                    callback.onRegistroSuccess(true);
                } else {
                    Log.e("Registro", "Erro ao registrar universitário: " + response.message());
                    callback.onRegistroSuccess(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Universitario> call, @NonNull Throwable t) {
                Log.e("Registro", "Falha na chamada da API: " + t.getMessage());
                callback.onRegistroSuccess(false);
            }
        });
    }

    public void atualizarUniversitarioProfile(String email, Universitario universitario, RegistroUniversitarioCallback callback){
        Call<Universitario> call = universitarioRepository.updateUniversitarioProfile(email, universitario);

        call.enqueue(new Callback<Universitario>() {
            @Override
            public void onResponse(@NonNull Call<Universitario> call, @NonNull Response<Universitario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Universitario universitarioResponse = response.body();
                    Log.d("Update", "Universitário atualizado com sucesso: " + universitarioResponse.getNome());
                } else{
                    Log.e("Update", "Erro ao atualizar universitário: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Universitario> call, @NonNull Throwable t) {

            }
        });

    }

    public void listarPerfilUniversitario(String email, PerfilUniversitarioCallback callback) {
        Call<Universitario> call = universitarioRepository.getUniversityProfile(email);

        call.enqueue(new Callback<Universitario>() {
            @Override
            public void onResponse(@NonNull Call<Universitario> call, @NonNull Response<Universitario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Universitario universitarioResponse = response.body();
                    callback.onPerfilAnuncianteSuccess(universitarioResponse);
                } else {
                    callback.onPerfilAnuncianteFailure("Erro ao buscar perfil");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Universitario> call, @NonNull Throwable t) {
                callback.onPerfilAnuncianteFailure(t.getMessage());
            }
        });
    }


}
