package com.example.hestia_app.data.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.GetUUIDByEmailCallback;
import com.example.hestia_app.data.api.callbacks.RegistroUniversitarioCallback;
import com.example.hestia_app.data.api.callbacks.UpdatePerfilUniversitarioCallback;
import com.example.hestia_app.data.api.repo.postgres.UniversitarioRepository;
import com.example.hestia_app.data.api.callbacks.PerfilUniversitarioCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.domain.models.Universitario;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UniversitarioService {

    UniversitarioRepository universitarioRepository = RetrofitPostgresClient.getClient().create(UniversitarioRepository.class);
    private SharedPreferences sharedPreferences;
    String token = "";


    // Construtor que recebe o contexto e inicializa o SharedPreferences
    public UniversitarioService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    }

    public void registrarUniversitario(Universitario universitario, RegistroUniversitarioCallback callback) {
        token = sharedPreferences.getString("token", null);
        Call<Universitario> call = universitarioRepository.registerUniversitario(token, universitario);

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
        token = sharedPreferences.getString("token", null);
        Call<Universitario> call = universitarioRepository.updateUniversitarioProfile(token, email, universitario);

        call.enqueue(new Callback<Universitario>() {
            @Override
            public void onResponse(@NonNull Call<Universitario> call, @NonNull Response<Universitario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Universitario universitarioResponse = response.body();
                    callback.onUpdateSuccess(true);
                    Log.d("Update", "Universitário atualizado com sucesso: " + universitarioResponse.getNome());
                } else{
                    callback.onUpdateFailure(response.message());
                    Log.e("Update", "Erro ao atualizar universitário: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Universitario> call, @NonNull Throwable t) {

            }
        });

    }

    public void listarPerfilUniversitario(String email, PerfilUniversitarioCallback callback) {
        token = sharedPreferences.getString("token", null);
        Call<Universitario> call = universitarioRepository.getUniversityProfile(token, email);

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
        token = sharedPreferences.getString("token", null);
        Call<UUID> call = universitarioRepository.getUniversitarioId(token, email);
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
