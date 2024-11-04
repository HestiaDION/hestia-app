package com.example.hestia_app.data.api.repo;

import com.example.hestia_app.domain.models.Pagamento;
import com.example.hestia_app.domain.models.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TokenJwtRepository {

    @GET("/token")
    Call<Token> getTokenAccess(@Path("email") String email);
}
