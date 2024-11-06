package com.example.hestia_app.data.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.GetUUIDByEmailCallback;
import com.example.hestia_app.data.api.callbacks.RegistroUniversitarioCallback;
import com.example.hestia_app.data.api.callbacks.UpdatePerfilUniversitarioCallback;
import com.example.hestia_app.data.api.repo.UniversitarioRepository;
import com.example.hestia_app.data.api.callbacks.PerfilUniversitarioCallback;
import com.example.hestia_app.data.api.callbacks.RegistroAnuncianteCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.domain.models.Universitario;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UniversitarioService {

    UniversitarioRepository universitarioRepository = RetrofitPostgresClient.getClient().create(UniversitarioRepository.class);

    public void registrarUniversitario(Universitario universitario, RegistroUniversitarioCallback callback) {
        Call<Universitario> call = universitarioRepository.registerUniversitario(universitario);

        call.enqueue(new Callback<Universitario>() {
            @Override
            public void onResponse(@NonNull Call<Universitario> call, @NonNull Response<Universitario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Universitario universitarioResponse = response.body();
                    Log.d("Registro", "Universitário registrado com sucesso: " + universitarioResponse.getNome());
                    Log.d("Registro", "ID do universitário registrado: " + universitarioResponse.getId());
                    callback.onRegistroSuccess(true, universitarioResponse.getId());
                } else {
                    Log.e("Registro", "Erro ao registrar universitário: " + response.message());
                    callback.onRegistroFailure(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Universitario> call, @NonNull Throwable t) {
                Log.e("Registro", "Falha na chamada da API: " + t.getMessage());
                callback.onRegistroFailure(false);
            }
        });
    }

    public void atualizarUniversitarioProfile(String email, Universitario universitario, UpdatePerfilUniversitarioCallback callback){
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
                    callback.onPerfilUniversitarioSuccess(universitarioResponse);
                } else {
                    callback.onPerfilUniversitarioFailure("Erro ao buscar perfil");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Universitario> call, @NonNull Throwable t) {
                callback.onPerfilUniversitarioFailure(t.getMessage());
            }
        });
    }

    public void getUniversitarioId(String email, GetUUIDByEmailCallback callback) {
        int maxRetries = 5; // Limite máximo de tentativas
        makeApiCall(email, callback, maxRetries);
    }

    private void makeApiCall(String email, GetUUIDByEmailCallback callback, int retriesLeft) {
        Call<UUID> call = universitarioRepository.getUniversitarioId(email);
        call.enqueue(new Callback<UUID>() {
            @Override
            public void onResponse(Call<UUID> call, Response<UUID> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UUID uuid = response.body();
                    Log.d("Api response", "Universitário selecionado com sucesso: " + uuid);
                    callback.onGetUUIDByEmailSuccess(uuid.toString());
                } else {
                    Log.e("Api response", "Erro ao selecionar universitário: " + response.message());
                    if (retriesLeft > 0) {
                        Log.d("Api response", "Tentando novamente... Tentativas restantes: " + retriesLeft);
                        makeApiCall(email, callback, retriesLeft - 1);
                    } else {
                        callback.onGetUUIDByEmailFailure(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<UUID> call, Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (retriesLeft > 0) {
                    Log.d("Api response", "Tentando novamente... Tentativas restantes: " + retriesLeft);
                    makeApiCall(email, callback, retriesLeft - 1);
                } else {
                    callback.onGetUUIDByEmailFailure(t.getMessage());
                }
            }
        });
    }

}
