package com.example.hestia_app.data.api.repo.postgres;

import com.example.hestia_app.domain.models.Pagamento;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PagamentoRepository {

    @POST("/payment/registerPayment")
    Call<Pagamento> registerPagamento(@Header("Authorization") String token, @Body Pagamento pagamento);
}
