package com.example.hestia_app.data.services;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.ImagensMoradiaCallback;
import com.example.hestia_app.data.api.clients.RetrofitMongoClient;
import com.example.hestia_app.data.api.repo.mongo.ImagensMoradiaRepository;
import com.example.hestia_app.domain.models.ImagensMoradia;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagensMoradiaService {
    private static final int MAX_RETRIES = 5; // Número máximo de tentativas
    private ImagensMoradiaRepository imagensMoradiaRepository = RetrofitMongoClient.getClient().create(ImagensMoradiaRepository.class);

    public void addImagensMoradias(ImagensMoradia imagensMoradia, ImagensMoradiaCallback imagensMoradiaCallback) {
        Call<ImagensMoradia> call = imagensMoradiaRepository.addImagensMoradia(imagensMoradia);
        executeWithRetry(call, imagensMoradiaCallback, 0);
    }

    public void getImagensMoradias(UUID id_moradia, ImagensMoradiaCallback imagensMoradiaCallback) {
        Call<ImagensMoradia> call = imagensMoradiaRepository.getImagensMoradia(id_moradia);
        executeWithRetry(call, imagensMoradiaCallback, 0);
    }

    private void executeWithRetry(Call<ImagensMoradia> call, ImagensMoradiaCallback callback, int retryCount) {
        call.clone().enqueue(new Callback<ImagensMoradia>() {
            @Override
            public void onResponse(@NonNull Call<ImagensMoradia> call, @NonNull Response<ImagensMoradia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de resposta
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    callback.onFailure(new Exception("Erro ao enviar os dados: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImagensMoradia> call, @NonNull Throwable t) {
                if (retryCount < MAX_RETRIES) {
                    // Tentar novamente em caso de falha de conexão
                    executeWithRetry(call.clone(), callback, retryCount + 1);
                } else {
                    callback.onFailure(t);
                }
            }
        });
    }
}
