package com.example.hestia_app.data.api;

import com.example.hestia_app.data.models.Anunciante;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AnuncianteRepository {

    @GET("/advertiser/profile/{email}")
    Call<Anunciante> getAnuncianteProfile(@Path("email") String email);

    @POST("/advertiser/register")
    Call<Anunciante> registerAnunciante(@Body Anunciante anunciante); // <Anunciante>

    @PATCH("/advertiser/profile/{email}")
    Call<Anunciante> updateAnuncianteProfile(@Path("email") String email, @Body Anunciante anunciante);

}
