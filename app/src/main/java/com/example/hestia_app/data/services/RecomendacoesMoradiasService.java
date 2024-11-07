package com.example.hestia_app.data.services;

import android.util.Log;

import com.example.hestia_app.data.api.callbacks.RecomendacoesMoradiaCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.data.api.clients.RetrofitRecomendacoesClient;
import com.example.hestia_app.data.api.repo.RecomendacoesMoradiaRepository;
import com.example.hestia_app.domain.models.RecomendacoesMoradia;
import com.example.hestia_app.domain.models.UniversityRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecomendacoesMoradiasService {
    private static final int MAX_RETRIES = 5; // Número máximo de tentativas
    RecomendacoesMoradiaRepository moradiaRepository = RetrofitRecomendacoesClient.getClient().create(RecomendacoesMoradiaRepository.class);


    public void getRecomendacoesMoradia(UniversityRequest universityRequest, RecomendacoesMoradiaCallback callback) {
        Call<RecomendacoesMoradia> call = moradiaRepository.getRecomendacoesMoradia(universityRequest);
        executeWithRetry(call, callback, 0);
    }

    private void executeWithRetry(Call<RecomendacoesMoradia> call, RecomendacoesMoradiaCallback callback, int retryCount) {
        call.enqueue(new Callback<RecomendacoesMoradia>() {
            @Override
            public void onResponse(Call<RecomendacoesMoradia> call, Response<RecomendacoesMoradia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Moradia", "Recomendacoes de moradia bem-sucedido: " + response.body().toString());
                    callback.onRecomendacoesSuccess(true, response.body());
                } else if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de resposta
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    Log.e("Moradia", "Falha no registro de moradia através da recomendacao: Código de resposta " + response.code());
                    callback.onRecomendacoesFailure(false);
                }
            }

            @Override
            public void onFailure(Call<RecomendacoesMoradia> call, Throwable t) {
                if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de conexão
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    Log.e("Moradia", "Erro na chamada de recomendacoes de moradia através da recomendacao: " + t.getMessage(), t);
                    callback.onRecomendacoesFailure(false);
                }
            }
        });
    }
}
