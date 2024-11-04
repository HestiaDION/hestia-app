package com.example.hestia_app.data.api.repo;

import com.example.hestia_app.domain.models.Pagamento;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PagamentoRepository {

    @POST("/payment/registerPayment")
    Call<Pagamento> registerPagamento(@Body Pagamento pagamento);
}
