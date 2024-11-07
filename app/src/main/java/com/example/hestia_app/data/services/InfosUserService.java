package com.example.hestia_app.data.services;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.repo.mongo.InfoUserRepository;
import com.example.hestia_app.data.api.callbacks.InfosUserCallback;
import com.example.hestia_app.data.api.clients.RetrofitMongoClient;
import com.example.hestia_app.domain.models.InfosUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfosUserService {

    private final InfoUserRepository infoUserRepository = RetrofitMongoClient.getClient().create(InfoUserRepository.class);

    public void addInfosUser(InfosUser infosUser, InfosUserCallback callback) {
        Call<InfosUser> call = infoUserRepository.addFiltrosTag(infosUser);

        call.enqueue(new Callback<InfosUser>() {
            @Override
            public void onResponse(@NonNull Call<InfosUser> call, @NonNull Response<InfosUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Erro ao enviar os dados: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<InfosUser> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    public void updateProfilePhotoUrlMongoCollection(String email, InfosUser infosUser, InfosUserCallback callback) {
        Call<InfosUser> call = infoUserRepository.updateProfilePhotoUrlMongoCollection(email, infosUser);

        call.enqueue(new Callback<InfosUser>() {
            @Override
            public void onResponse(Call<InfosUser> call, Response<InfosUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Erro ao atualizar a URL da foto: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<InfosUser> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
