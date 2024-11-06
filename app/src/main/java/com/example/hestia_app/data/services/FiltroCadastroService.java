package com.example.hestia_app.data.services;

import android.os.Handler;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.hestia_app.data.api.FiltroCadastroRepository;
import com.example.hestia_app.data.api.callbacks.FiltroCadastroCallback;
import com.example.hestia_app.data.api.callbacks.GetCategoriaByNomeCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresPrimeiroClient;
import com.example.hestia_app.domain.models.FiltroCadastro;

import java.util.HashMap;
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
                    callback.onFiltroCadastroSuccess(filtroCadastro, null);
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

    public void getCategorias(FiltroCadastroCallback callback) {
        getCategoriasWithRetry(callback, 0);
    }

    private void getCategoriasWithRetry(FiltroCadastroCallback callback, int attempt) {
        Call<List<String>> call = filtroCadastroRepository.getCategorias();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> categorias = response.body();
                    Log.d("Api response", "onResponse: " + categorias);
                    callback.onFiltroCadastroSuccess(null, categorias);
                } else {
                    Log.e("Api response", "Erro ao buscar categorias: " + response.message());
                    callback.onFiltroCadastroFailure("Erro ao buscar categorias!");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (attempt < MAX_RETRIES) {
                    Log.d("Api response", "Tentando novamente... (" + (attempt + 1) + "/" + MAX_RETRIES + ")");

                    // Usar Handler para adicionar atraso antes da último tentativa
                    new Handler().postDelayed(() -> {
                        getCategoriasWithRetry(callback, attempt + 1);
                    }, RETRY_DELAY);
                } else {
                    callback.onFiltroCadastroFailure("Falha na chamada da API após " + MAX_RETRIES + " tentativas.");
                }
            }
        });
    }

    public void getCategoriaByNome(String cnome, GetCategoriaByNomeCallback callback) {
        getCategoriaByNomeWithRetry(cnome, callback, 0);
    }

    private void getCategoriaByNomeWithRetry(String cnome, GetCategoriaByNomeCallback callback, int attempt) {
        Call<HashMap<String, String>> call = filtroCadastroRepository.getCategoriaByNome(cnome);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HashMap<String, String> categoria = response.body();
                    Log.d("Api response", "onResponse: " + categoria);
                    callback.onGetCategoriaByNomeCallbackSuccess(categoria);
                } else {
                    Log.e("Api response", "Erro ao buscar categoria: " + response.message());
                    callback.onGetCategoriaByNomeCallbackFailure("Erro ao buscar categoria!");
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.e("Api response", "Falha na chamada da API: " + t.getMessage());
                if (attempt < MAX_RETRIES) {
                    Log.d("Api response", "Tentando novamente... (" + (attempt + 1) + "/" + MAX_RETRIES + ")");

                    // Usar Handler para adicionar atraso antes da último tentativa
                    new Handler().postDelayed(() -> {
                        getCategoriaByNomeWithRetry(cnome, callback, attempt + 1);
                    }, RETRY_DELAY);
                } else {
                    callback.onGetCategoriaByNomeCallbackFailure("Falha na chamada da API após " + MAX_RETRIES + " tentativas.");
                }
            }
        });
    }
}
