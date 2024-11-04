package com.example.hestia_app.data.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.TokenJwtCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.data.api.repo.PagamentoRepository;
import com.example.hestia_app.data.api.repo.TokenJwtRepository;
import com.example.hestia_app.domain.models.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenJwtService {

    private final TokenJwtRepository tokenJwtRepository = RetrofitPostgresClient.getClient().create(TokenJwtRepository.class);

    public void getAccessToken(String email, TokenJwtCallback callback) {
        Call<Token> call = tokenJwtRepository.getTokenAccess(email);
        call.enqueue(new Callback<Token>() {

            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Token tokenResponse = response.body();
                    Log.d("Token", "Token gerado com sucesso: " + tokenResponse.getToken());
                    callback.onSuccess(tokenResponse);
                } else {
                    Log.e("Token", "Erro ao gerar token: " + response.message());
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                Log.e("Token", "Falha na chamada da API: " + t.getMessage());
                callback.onFailure(t.getMessage());
            }
        });
    }

}
