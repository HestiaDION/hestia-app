package com.example.hestia_app.data.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.PagamentoPorUserEmail;
import com.example.hestia_app.data.api.callbacks.RegistroPagamentoCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.data.api.repo.postgres.PagamentoRepository;

import com.example.hestia_app.domain.models.Pagamento;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagamentoService {

    private final PagamentoRepository pagamentoRepository = RetrofitPostgresClient.getClient().create(PagamentoRepository.class);
    private String token = "";
    SharedPreferences sharedPreferences;

    // Construtor que recebe o contexto e inicializa o SharedPreferences
    public PagamentoService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    }


    public void registrarPagamento(Pagamento pagamentoInfo, RegistroPagamentoCallback callback) {
        token = sharedPreferences.getString("token", null);
        Call<Pagamento> call = pagamentoRepository.registerPagamento(token, pagamentoInfo);

        call.enqueue(new Callback<Pagamento>() {

            @Override
            public void onResponse(Call<Pagamento> call, Response<Pagamento> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pagamento pagamentoResponse = response.body();
                    Log.d("Pagamento", "Pagamento efetuado com sucesso: " + pagamentoResponse.getEmailUsuarioAssinante());
                    callback.onRegistroSuccess(true, pagamentoResponse);
                } else {
                    Log.e("Pagamento", "Erro ao efetuar pagamento: " + response.message());
                    callback.onRegistroSuccess(false, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Pagamento> call, @NonNull Throwable t) {
                Log.e("Pagamento", "Falha na chamada da API: " + t.getMessage());
                callback.onRegistroSuccess(false, null);
            }
        });
    }

    public void getPagamentoByUserEmail(String email, PagamentoPorUserEmail callback) {
        token = sharedPreferences.getString("token", null);
        Call<Pagamento> call = pagamentoRepository.getPagamentoByUserEmail(token, email);

        call.enqueue(new Callback<Pagamento>() {
            @Override
            public void onResponse(@NonNull Call<Pagamento> call, @NonNull Response<Pagamento> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onFindSuccess(true, response.body());
                } else {
                    callback.onFindFailure(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Pagamento> call, @NonNull Throwable t) {
                callback.onFindFailure(false);
            }
        });
    }
}
