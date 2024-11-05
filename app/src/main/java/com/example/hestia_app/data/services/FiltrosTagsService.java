package com.example.hestia_app.data.services;

import android.os.Handler;
import android.util.Log;

import com.example.hestia_app.data.api.repo.mongo.FiltrosTagsRepository;
import com.example.hestia_app.data.api.callbacks.FiltrosTagsCallback;
import com.example.hestia_app.data.api.clients.RetrofitMongoClient;
import com.example.hestia_app.domain.models.FiltrosTags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiltrosTagsService {
    private static final int MAX_RETRIES = 5; // Número máximo de tentativas
    private static final int RETRY_DELAY = 5000; // Atraso em milissegundos (2000ms = 2 segundos)
    private final FiltrosTagsRepository filtrosTagsRepository = RetrofitMongoClient.getClient().create(FiltrosTagsRepository.class);

    public void addFiltrosTag(FiltrosTags filtrosTags, FiltrosTagsCallback filtrosTagsCallback) {
        addFiltrosTagWithRetry(filtrosTags, filtrosTagsCallback, 0);
    }

    private void addFiltrosTagWithRetry(FiltrosTags filtrosTags, FiltrosTagsCallback filtrosTagsCallback, int attempt) {
        Call<FiltrosTags> call = filtrosTagsRepository.addFiltrosTag(filtrosTags);
        call.enqueue(new Callback<FiltrosTags>() {
            @Override
            public void onResponse(Call<FiltrosTags> call, Response<FiltrosTags> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FiltrosTags filtrosTags = response.body();
                    Log.d("Api response", "Filtros/tags registrados com sucesso: " + filtrosTags);
                    filtrosTagsCallback.onFiltroCadastroSuccess(true);
                } else {
                    Log.e("Api response", "Erro ao registrar filtros/tags: " + response.message());
                    filtrosTagsCallback.onFiltroCadastroFailure(false);
                }
            }

            @Override
            public void onFailure(Call<FiltrosTags> call, Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (attempt < MAX_RETRIES) {
                    Log.d("Api response", "Tentando novamente... (" + (attempt + 1) + "/" + MAX_RETRIES + ")");

                    // Usar Handler para adicionar atraso antes da próxima tentativa
                    new Handler().postDelayed(() -> {
                        addFiltrosTagWithRetry(filtrosTags, filtrosTagsCallback, attempt + 1);
                    }, RETRY_DELAY);
                } else {
                    filtrosTagsCallback.onFiltroCadastroFailure(false);
                }
            }
        });
    }

    public void getFiltrosTag(String id_usuario_moradia, FiltrosTagsCallback filtrosTagsCallback) {
        Call<FiltrosTags> call = filtrosTagsRepository.getFiltrosTag(id_usuario_moradia);
        call.enqueue(new Callback<FiltrosTags>() {
            @Override
            public void onResponse(Call<FiltrosTags> call, Response<FiltrosTags> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FiltrosTags filtrosTags = response.body();
                    Log.d("Api response", "Filtros/tags listados com sucesso: " + filtrosTags);
                    filtrosTagsCallback.onFiltroCadastroSuccess(true);
                } else {
                    Log.e("Api response", "Erro ao listar filtros/tags: " + response.message());
                    filtrosTagsCallback.onFiltroCadastroFailure(false);
                }
            }

            @Override
            public void onFailure(Call<FiltrosTags> call, Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                filtrosTagsCallback.onFiltroCadastroFailure(false);
            }
        });
    }
}
