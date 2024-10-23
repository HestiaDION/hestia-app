package com.example.hestia_app.data.services;

import androidx.annotation.NonNull;


import com.example.hestia_app.data.api.InfoUserRepository;
import com.example.hestia_app.data.api.callbacks.InfosUserCallback;
import com.example.hestia_app.data.api.clients.RetrofitMongoClient;
import com.example.hestia_app.domain.models.InfosUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfosUserService {

    private final InfoUserRepository infoUserRepository = RetrofitMongoClient.getClient().create(InfoUserRepository.class);

    public void addInfosUser(InfosUser infosUser, InfosUserCallback callback) {
        Call<InfoUserRepository> call = infoUserRepository.addFiltrosTag(infosUser);

        call.enqueue(new Callback<InfoUserRepository>() {
            @Override
            public void onResponse(@NonNull Call<InfoUserRepository> call, @NonNull Response<InfoUserRepository> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Erro ao enviar os dados: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<InfoUserRepository> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    public void updateProfilePhotoUrlMongoCollection(String email, InfosUser infosUser, InfosUserCallback callback) {
        Call<InfoUserRepository> call = infoUserRepository.updateProfilePhotoUrlMongoCollection(email, infosUser);

        call.enqueue(new Callback<InfoUserRepository>() {
            @Override
            public void onResponse(Call<InfoUserRepository> call, Response<InfoUserRepository> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Erro ao atualizar a URL da foto: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<InfoUserRepository> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
