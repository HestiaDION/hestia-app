package com.example.hestia_app.data.services;

import android.os.Handler;
import android.util.Log;

import com.example.hestia_app.data.api.repo.mongo.FiltrosTagsRepository;

import com.example.hestia_app.data.api.callbacks.FiltrosTagAllCallback;
import com.example.hestia_app.data.api.callbacks.FiltrosTagsCallback;
import com.example.hestia_app.data.api.callbacks.FiltrosTagsCallbackGet;
import com.example.hestia_app.data.api.clients.RetrofitMongoClient;
import com.example.hestia_app.domain.models.FiltrosTags;

import java.util.List;

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

    public void getFiltrosTag(String id_usuario_moradia, FiltrosTagsCallbackGet filtrosTagsCallbackGet) {
        int maxAttempts = 5; // Número máximo de tentativas
        getFiltrosTagWithRetries(id_usuario_moradia, filtrosTagsCallbackGet, maxAttempts);
    }

    private void getFiltrosTagWithRetries(String id_usuario_moradia, FiltrosTagsCallbackGet filtrosTagsCallbackGet, int remainingAttempts) {
        Call<FiltrosTags> call = filtrosTagsRepository.getFiltrosTag(id_usuario_moradia);
        call.enqueue(new Callback<FiltrosTags>() {
            @Override
            public void onResponse(Call<FiltrosTags> call, Response<FiltrosTags> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FiltrosTags filtrosTags = response.body();
                    Log.d("Api response", "Filtros/tags selecionados com sucesso: " + filtrosTags);
                    filtrosTagsCallbackGet.onSuccess(filtrosTags);
                } else {
                    Log.e("Api response", "Erro ao selecionar filtros/tags: " + response.message());
                    if (remainingAttempts > 0) {
                        Log.d("Api response", "Tentando novamente... (restantes: " + remainingAttempts + ")");
                        getFiltrosTagWithRetries(id_usuario_moradia, filtrosTagsCallbackGet, remainingAttempts - 1);
                    } else {
                        filtrosTagsCallbackGet.onFailure(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<FiltrosTags> call, Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (remainingAttempts > 0) {
                    Log.d("Api response", "Tentando novamente... (restantes: " + remainingAttempts + ")");
                    getFiltrosTagWithRetries(id_usuario_moradia, filtrosTagsCallbackGet, remainingAttempts - 1);
                } else {
                    filtrosTagsCallbackGet.onFailure(t.getMessage());
                }
            }
        });
    }

    public void getAllFiltrosTag(FiltrosTagAllCallback filtrosTagAllCallback) {
        int maxAttempts = 3; // Número máximo de tentativas
        getAllFiltrosTagWithRetries(filtrosTagAllCallback, maxAttempts);
    }

    private void getAllFiltrosTagWithRetries(FiltrosTagAllCallback filtrosTagAllCallback, int remainingAttempts) {
        Call<List<FiltrosTags>> call = filtrosTagsRepository.getAllFiltrosTag();
        call.enqueue(new Callback<List<FiltrosTags>>() {
            @Override
            public void onResponse(Call<List<FiltrosTags>> call, Response<List<FiltrosTags>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FiltrosTags> filtrosTags = response.body();
                    Log.d("Api response", "Filtros/tags selecionados com sucesso: " + filtrosTags);
                    filtrosTagAllCallback.onSuccess(filtrosTags);
                } else {
                    Log.e("Api response", "Erro ao selecionar filtros/tags: " + response.message());
                    if (remainingAttempts > 0) {
                        Log.d("Api response", "Tentando novamente... (restantes: " + remainingAttempts + ")");
                        getAllFiltrosTagWithRetries(filtrosTagAllCallback, remainingAttempts - 1);
                    } else {
                        filtrosTagAllCallback.onFailure(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FiltrosTags>> call, Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (remainingAttempts > 0) {
                    Log.d("Api response", "Tentando novamente... (restantes: " + remainingAttempts + ")");
                    getAllFiltrosTagWithRetries(filtrosTagAllCallback, remainingAttempts - 1);
                } else {
                    filtrosTagAllCallback.onFailure(t.getMessage());
                }
            }
        });
    }

}
