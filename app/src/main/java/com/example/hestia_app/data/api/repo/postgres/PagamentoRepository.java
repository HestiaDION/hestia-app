package com.example.hestia_app.data.api.repo.postgres;

import com.example.hestia_app.domain.models.Pagamento;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Header;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PagamentoRepository {

    @POST("/payment/registerPayment")
    Call<Pagamento> registerPagamento(@Header("Authorization") String token, @Body Pagamento pagamento);

    @GET("/payment/findByUserEmail/{email}")
    Call<Pagamento> getPagamentoByUserEmail(@Header("Authorization") String token, @Path("email") String email);

}

