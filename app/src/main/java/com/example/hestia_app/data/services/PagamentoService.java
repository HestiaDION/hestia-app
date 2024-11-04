package com.example.hestia_app.data.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.PagamentoPorUserEmail;
import com.example.hestia_app.data.api.callbacks.RegistroPagamentoCallback;
import com.example.hestia_app.data.api.clients.RetrofitMongoClient;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.data.api.repo.PagamentoRepository;

import com.example.hestia_app.domain.models.Pagamento;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagamentoService {

    private final PagamentoRepository pagamentoRepository = RetrofitPostgresClient.getClient().create(PagamentoRepository.class);


    public void registrarPagamento(Pagamento pagamentoInfo, RegistroPagamentoCallback callback) {
        Call<Pagamento> call = pagamentoRepository.registerPagamento(pagamentoInfo);

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
        Call<Pagamento> call = pagamentoRepository.getPagamentoByUserEmail(email);

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
