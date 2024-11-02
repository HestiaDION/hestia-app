package com.example.hestia_app.data.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.UpdatePerfilAnuncianteCallback;
import com.example.hestia_app.data.api.repo.AnuncianteRepository;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.domain.models.Anunciante;
import com.example.hestia_app.data.api.callbacks.RegistroAnuncianteCallback;
import com.example.hestia_app.data.api.callbacks.PerfilAnuncianteCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnuncianteService {

    AnuncianteRepository anuncianteRepository = RetrofitPostgresClient.getClient().create(AnuncianteRepository.class);

    public void registrarAnunciante(Anunciante anunciante, RegistroAnuncianteCallback callback) {
        Call<Anunciante> call = anuncianteRepository.registerAnunciante(anunciante);


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
        Call <Anunciante> call = anuncianteRepository.updateAnuncianteProfile(email, anunciante);
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
        Call<Anunciante> call = anuncianteRepository.getAnuncianteProfile(email);

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
