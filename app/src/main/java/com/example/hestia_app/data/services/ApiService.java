package com.example.hestia_app.data.services;

import android.util.Log;

import com.example.hestia_app.data.api.repo.ApiServiceRepository;

import com.example.hestia_app.data.api.callbacks.ApiServiceCallback;
import com.example.hestia_app.data.api.clients.RetrofitMongoClient;
import com.example.hestia_app.data.api.clients.RetrofitPostgresPrimeiroClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiService {
    ApiServiceRepository repository = RetrofitMongoClient.getClient().create(ApiServiceRepository.class);
    ApiServiceRepository repository2 = RetrofitPostgresPrimeiroClient.getClient().create(ApiServiceRepository.class);

    public void wakeUpApi(ApiServiceCallback callback) {
        Call<Map<String, String>> call = repository.wakeUpApi();
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, String> message = response.body();
                    Log.d("Api response", "onResponse 1: " + message);
                    callback.onApiServiceSuccess(message);
                } else {
                    callback.onApiServiceFailure("Erro ao buscar status!");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.d("Api response", "onFailure 1: " + t);
                callback.onApiServiceFailure(t.getMessage());
            }
        });

        Call<Map<String, String>> call2 = repository2.wakeUpApi();
        call2.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, String> message = response.body();
                    Log.d("Api response", "onResponse 2: " + message);
                    callback.onApiServiceSuccess(message);
                } else {
                    callback.onApiServiceFailure("Erro ao buscar status!");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.d("Api response", "onFailure 2: " + t);
                callback.onApiServiceFailure(t.getMessage());
            }
        });
    }
}
