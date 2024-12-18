package com.example.hestia_app.data.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.UpdatePerfilAnuncianteCallback;
import com.example.hestia_app.data.api.repo.postgres.AnuncianteRepository;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.domain.models.Anunciante;
import com.example.hestia_app.data.api.callbacks.RegistroAnuncianteCallback;
import com.example.hestia_app.data.api.callbacks.PerfilAnuncianteCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnuncianteService {

    AnuncianteRepository anuncianteRepository = RetrofitPostgresClient.getClient().create(AnuncianteRepository.class);

    private String token = "";
    private SharedPreferences sharedPreferences;
    // Construtor que recebe o contexto e inicializa o SharedPreferences
    public AnuncianteService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    }

    public void registrarAnunciante(Anunciante anunciante, RegistroAnuncianteCallback callback) {
        token = sharedPreferences.getString("token", null);
        Call<Anunciante> call = anuncianteRepository.registerAnunciante(token, anunciante);

        call.enqueue(new Callback<Anunciante>() {
            @Override
            public void onResponse(@NonNull Call<Anunciante> call, @NonNull Response<Anunciante> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Anunciante anuncianteResponse = response.body();
                    Log.d("Registro", "Anunciante registrado com sucesso: " + anuncianteResponse.getNome());
                    callback.onRegistroSuccess(true);
                } else {
                    Log.e("Registro", "Erro ao registrar anunciante: " + response.message());
                    callback.onRegistroSuccess(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Anunciante> call, @NonNull Throwable t) {
                Log.e("Registro", "Falha na chamada da API: " + t.getMessage());
                callback.onRegistroSuccess(false);
            }
        });
    }

    public void atualizarAnuncianteProfile(String email, Anunciante anunciante, UpdatePerfilAnuncianteCallback callback){
        token = sharedPreferences.getString("token", null);
        Call <Anunciante> call = anuncianteRepository.updateAnuncianteProfile(token, email, anunciante);
        call.enqueue(new Callback<Anunciante>(){

            @Override
            public void onResponse(@NonNull Call<Anunciante> call, @NonNull Response<Anunciante> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Anunciante anuncianteResponse = response.body();
                    Log.d("Update", "Anunciante atualizado com sucesso: " + anuncianteResponse.getNome());
                } else{
                    Log.e("Update", "Erro ao atualizar anunciante: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Anunciante> call, @NonNull Throwable t) {
                Log.e("Update", "Erro ao atualizar anunciante: " + t.getMessage());
            }
        });

    }

    public void listarPerfilAnunciante(String email, PerfilAnuncianteCallback callback) {

        token = sharedPreferences.getString("token", null);

        if (token == null) {
            Log.e("TokenAnunciante", "Token nulo");
            return;
        } else {
            Log.d("TokenAnunciante", token.toString());
        }


        Call<Anunciante> call = anuncianteRepository.getAnuncianteProfile(token, email);

        call.enqueue(new Callback<Anunciante>() {
            @Override
            public void onResponse(@NonNull Call<Anunciante> call, @NonNull Response<Anunciante> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Anunciante anuncianteResponse = response.body();
                    callback.onPerfilAnuncianteSuccess(anuncianteResponse);
                } else {
                    callback.onPerfilAnuncianteFailure("Erro ao buscar perfil");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Anunciante> call, @NonNull Throwable t) {
                callback.onPerfilAnuncianteFailure(t.getMessage());
            }
        });
    }

}
