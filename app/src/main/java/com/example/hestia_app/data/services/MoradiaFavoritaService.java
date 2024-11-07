package com.example.hestia_app.data.services;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.MoradiaFavoritaCallback;
import com.example.hestia_app.data.api.clients.RetrofitMongoClient;
import com.example.hestia_app.data.api.repo.MoradiaFavoritaRepository;
import com.example.hestia_app.domain.models.MoradiaFavorita;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoradiaFavoritaService {
    private static final int MAX_RETRIES = 5; // Número máximo de tentativas
    private static final int RETRY_DELAY = 5000; // Atraso em milissegundos (5000ms = 5 segundos)
    private final MoradiaFavoritaRepository moradiaFavoritaRepository = RetrofitMongoClient.getClient().create(MoradiaFavoritaRepository.class);

    public void addMoradiasFavoritas(MoradiaFavorita moradiaFavorita, MoradiaFavoritaCallback callback) {
        addMoradiasFavoritasWithRetry(moradiaFavorita, callback, 0);
    }

    private void addMoradiasFavoritasWithRetry(MoradiaFavorita moradiaFavorita, MoradiaFavoritaCallback callback, int attempt) {
        Call<MoradiaFavorita> call = moradiaFavoritaRepository.addMoradiasFavoritas(moradiaFavorita);
        call.enqueue(new Callback<MoradiaFavorita>() {
            @Override
            public void onResponse(@NonNull Call<MoradiaFavorita> call, @NonNull Response<MoradiaFavorita> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MoradiaFavorita moradiaFavorita = response.body();
                    callback.moradiaFavoritaOnSuccess(moradiaFavorita);
                } else {
                    callback.moradiaFavoritaOnFailure("Erro ao favoritar moradia!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoradiaFavorita> call, @NonNull Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (attempt < MAX_RETRIES) {
                    Log.d("Api response", "Tentando novamente... (" + (attempt + 1) + "/" + MAX_RETRIES + ")");

                    // Usar Handler para adicionar atraso antes da próxima tentativa
                    new Handler().postDelayed(() -> {
                        addMoradiasFavoritasWithRetry(moradiaFavorita, callback, attempt + 1);
                    }, RETRY_DELAY);
                } else {
                    callback.moradiaFavoritaOnFailure("Falha na chamada da API após " + MAX_RETRIES + " tentativas.");
                }
            }
        });
    }

    public void getMoradiasFavoritas(UUID idUsuario, MoradiaFavoritaCallback callback) {
        getMoradiasFavoritasWithRetry(idUsuario, callback, 0);
    }

    private void getMoradiasFavoritasWithRetry(UUID idUsuario, MoradiaFavoritaCallback callback, int attempt) {
        Call<MoradiaFavorita> call = moradiaFavoritaRepository.getMoradiasFavoritas(idUsuario);
        call.enqueue(new Callback<MoradiaFavorita>() {
            @Override
            public void onResponse(@NonNull Call<MoradiaFavorita> call, @NonNull Response<MoradiaFavorita> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MoradiaFavorita moradiasFavoritas = response.body();
                    callback.moradiaFavoritaOnSuccess(moradiasFavoritas);
                } else {
                    callback.moradiaFavoritaOnFailure("Erro ao buscar moradias favoritas!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoradiaFavorita> call, @NonNull Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (attempt < MAX_RETRIES) {
                    Log.d("Api response", "Tentando novamente... (" + (attempt + 1) + "/" + MAX_RETRIES + ")");

                    // Usar Handler para adicionar atraso antes da próxima tentativa
                    new Handler().postDelayed(() -> {
                        getMoradiasFavoritasWithRetry(idUsuario, callback, attempt + 1);
                    }, RETRY_DELAY);
                } else {
                    callback.moradiaFavoritaOnFailure("Falha na chamada da API após " + MAX_RETRIES + " tentativas.");
                }
            }
        });
    }

    public void deleteMoradiasFavoritas(UUID idUsuario, UUID idMoradia, MoradiaFavoritaCallback callback) {
        deleteMoradiasFavoritasWithRetry(idUsuario, idMoradia, callback, 0);
    }

    private void deleteMoradiasFavoritasWithRetry(UUID idUsuario, UUID idMoradia, MoradiaFavoritaCallback callback, int attempt) {
        Call<MoradiaFavorita> call = moradiaFavoritaRepository.deleteMoradiasFavoritas(idMoradia, idUsuario);
        call.enqueue(new Callback<MoradiaFavorita>() {
            @Override
            public void onResponse(@NonNull Call<MoradiaFavorita> call, @NonNull Response<MoradiaFavorita> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MoradiaFavorita moradiasFavoritas = response.body();
                    callback.moradiaFavoritaOnSuccess(moradiasFavoritas);
                } else {
                    callback.moradiaFavoritaOnFailure("Erro ao deletar moradias favoritas!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoradiaFavorita> call, @NonNull Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (attempt < MAX_RETRIES) {
                    Log.d("Api response", "Tentando novamente... (" + (attempt + 1) + "/" + MAX_RETRIES + ")");

                    // Usar Handler para adicionar atraso antes da segunda tentativa
                    new Handler().postDelayed(() -> {
                        deleteMoradiasFavoritasWithRetry(idMoradia, idUsuario, callback, attempt + 1);
                    }, RETRY_DELAY);
                } else {
                    callback.moradiaFavoritaOnFailure("Falha na chamada da API após " + MAX_RETRIES + " tentativas.");
                }
            }
        });
    }
}
