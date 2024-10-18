package com.example.hestia_app.data.services;

import android.os.Handler;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.hestia_app.data.api.FiltroCadastroRepository;
import com.example.hestia_app.data.api.callbacks.FiltroCadastroCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresPrimeiroClient;
import com.example.hestia_app.domain.models.FiltroCadastro;

import java.util.List;

public class FiltroCadastroService {
    private static final int MAX_RETRIES = 5; // Número máximo de tentativas
    private static final int RETRY_DELAY = 5000; // Atraso em milissegundos (2000ms = 2 segundos)
    private final FiltroCadastroRepository filtroCadastroRepository = RetrofitPostgresPrimeiroClient.getClient().create(FiltroCadastroRepository.class);

    public void getFiltrosPorCategoria(String categoria, FiltroCadastroCallback callback) {
        getFiltrosPorCategoriaWithRetry(categoria, callback, 0);
    }

    private void getFiltrosPorCategoriaWithRetry(String categoria, FiltroCadastroCallback callback, int attempt) {
        Call<List<FiltroCadastro>> call = filtroCadastroRepository.getFiltrosPorCategoria(categoria);
        call.enqueue(new Callback<List<FiltroCadastro>>() {
            @Override
            public void onResponse(Call<List<FiltroCadastro>> call, Response<List<FiltroCadastro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FiltroCadastro> filtroCadastro = response.body();
                    Log.d("Api response", "onResponse: " + filtroCadastro);
                    callback.onFiltroCadastroSuccess(filtroCadastro);
                } else {
                    Log.e("Api response", "Erro ao buscar filtro por categoria: " + response.message());
                    callback.onFiltroCadastroFailure("Erro ao buscar filtro por categoria!");
                }
            }

            @Override
            public void onFailure(Call<List<FiltroCadastro>> call, Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (attempt < MAX_RETRIES) {
                    Log.d("Api response", "Tentando novamente... (" + (attempt + 1) + "/" + MAX_RETRIES + ")");

                    // Usar Handler para adicionar atraso antes da próxima tentativa
                    new Handler().postDelayed(() -> {
                        getFiltrosPorCategoriaWithRetry(categoria, callback, attempt + 1);
                    }, RETRY_DELAY);
                } else {
                    callback.onFiltroCadastroFailure("Falha na chamada da API após " + MAX_RETRIES + " tentativas.");
                }
            }
        });
    }
}
