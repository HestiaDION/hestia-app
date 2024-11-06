package com.example.hestia_app.data.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.ListaMoradiasCallback;
import com.example.hestia_app.data.api.callbacks.MoradiaByIdCallback;
import com.example.hestia_app.data.api.callbacks.RegistroMoradiaCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.data.api.repo.postgres.MoradiaRepository;
import com.example.hestia_app.domain.models.Moradia;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoradiaService {
    private static final int MAX_RETRIES = 5; // Número máximo de tentativas
    MoradiaRepository moradiaRepository = RetrofitPostgresClient.getClient().create(MoradiaRepository.class);

    private String token = "";
    private SharedPreferences sharedPreferences;
    // Construtor que recebe o contexto e inicializa o SharedPreferences
    public MoradiaService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    }

    public void registrarMoradia(Moradia moradia, RegistroMoradiaCallback callback) {
        token = sharedPreferences.getString("token", null);
        Call<Moradia> call = moradiaRepository.registerMoradia(token, moradia);
        executeWithRetry(call, callback, 0);
    }

    public void getMoradiasByAdvertiser(String email, ListaMoradiasCallback callback) {
        token = sharedPreferences.getString("token", null);
        Call<List<Moradia>> call = moradiaRepository.getMoradiasByAdvertiser(token, email);
        executeWithRetry(call, callback, 0);
    }


    //                      =--=-=-=-=--=-=-=-=-= MÉTODOS DE RETRY =--==--=-==-=-=-=-=-=-


    private void executeWithRetry(Call<Moradia> call, RegistroMoradiaCallback callback, int retryCount) {
        call.clone().enqueue(new Callback<Moradia>() {
            @Override
            public void onResponse(@NonNull Call<Moradia> call, @NonNull Response<Moradia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Moradia", "Registro de moradia bem-sucedido: " + response.body().toString());
                    callback.onRegistroSuccess(true, response.body());
                } else if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de resposta
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    Log.e("Moradia", "Falha no registro de moradia após tentativas: Código de resposta " + response.code());
                    callback.onRegistroFailure(false);
                }
            }

            @Override
            public void onFailure(Call<Moradia> call, Throwable t) {
                if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de conexão
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    Log.e("Moradia", "Erro na chamada de registro de moradia após tentativas: " + t.getMessage(), t);
                    callback.onRegistroFailure(false);
                }
            }
        });
    }

    private void executeWithRetry(Call<List<Moradia>> call, ListaMoradiasCallback callback, int retryCount) {
        call.clone().enqueue(new Callback<List<Moradia>>() {
            @Override
            public void onResponse(@NonNull Call<List<Moradia>> call, @NonNull Response<List<Moradia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de resposta
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    Log.e("Moradia", "Falha na listagem de moradias após tentativas: " + response.message());
                    callback.onFailure(new Exception("Falha na resposta da API: " + response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Moradia>> call, @NonNull Throwable t) {
                if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de conexão
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    Log.e("Moradia", "Erro na chamada da listagem de moradias após tentativas: " + t.getMessage(), t);
                    callback.onFailure(t);
                }
            }
        });
    }

    public void getMoradiaById(UUID id, MoradiaByIdCallback callback) {
        token = sharedPreferences.getString("token", null);
        Call<Moradia> call = moradiaRepository.getMoradiaById(token, id);
        executeWithRetry(call, callback, 0);
    }

    private void executeWithRetry(Call<Moradia> call, MoradiaByIdCallback callback, int retryCount) {
        call.clone().enqueue(new Callback<Moradia>() {
            @Override
            public void onResponse(@NonNull Call<Moradia> call, @NonNull Response<Moradia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de resposta
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    Log.e("Moradia", "Falha na listagem de moradias através do id: Código de resposta " + response.code());
                    callback.onFailure("Falha na resposta da API: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Moradia> call, @NonNull Throwable t) {
                if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de conexão
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    Log.e("Moradia", "Erro na chamada da listagem de moradias através do id: " + t.getMessage(), t);
                    callback.onFailure(t.getMessage());
                }
            }
        });
    }
}
