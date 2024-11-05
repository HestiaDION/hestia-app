package com.example.hestia_app.data.services;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.InformacoesAdicionaisMoradiaCallback;
import com.example.hestia_app.data.api.clients.RetrofitMongoClient;
import com.example.hestia_app.data.api.repo.mongo.InformacoesAdicionaisMoradiaRepository;
import com.example.hestia_app.domain.models.InformacoesAdicionaisMoradia;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformacoesAdicionaisMoradiaService {
    private static final int MAX_RETRIES = 5; // Número máximo de tentativas
    private final InformacoesAdicionaisMoradiaRepository informacoesAdicionaisMoradiaRepository = RetrofitMongoClient.getClient().create(InformacoesAdicionaisMoradiaRepository.class);

    public void addInfosMoradias(InformacoesAdicionaisMoradia informacoesAdicionaisMoradia, InformacoesAdicionaisMoradiaCallback callback) {
        Call<InformacoesAdicionaisMoradia> call = informacoesAdicionaisMoradiaRepository.addInfosMoradias(informacoesAdicionaisMoradia);
        executeWithRetry(call, callback, 0);
    }

    public void getInfosMoradias(UUID id_moradia, InformacoesAdicionaisMoradiaCallback callback) {
        Call<InformacoesAdicionaisMoradia> call = informacoesAdicionaisMoradiaRepository.getInfosMoradias(id_moradia);
        executeWithRetry(call, callback, 0);
    }

    private void executeWithRetry(Call<InformacoesAdicionaisMoradia> call, InformacoesAdicionaisMoradiaCallback callback, int retryCount) {
        call.clone().enqueue(new Callback<InformacoesAdicionaisMoradia>() {
            @Override
            public void onResponse(@NonNull Call<InformacoesAdicionaisMoradia> call, @NonNull Response<InformacoesAdicionaisMoradia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de resposta
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    callback.onFailure(new Exception("Erro ao enviar os dados após tentativas: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<InformacoesAdicionaisMoradia> call, @NonNull Throwable t) {
                if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de conexão
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    callback.onFailure(new Exception("Erro ao enviar os dados após tentativas: " + t.getMessage(), t));
                }
            }
        });
    }
}
