package com.example.hestia_app.data.api.repo.postgres;

import com.example.hestia_app.domain.models.Anunciante;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AnuncianteRepository {

    @GET("/advertiser/profile/{email}")
    Call<Anunciante> getAnuncianteProfile(@Header("Authorization") String token, @Path("email") String email);

    @POST("/advertiser/register")
    Call<Anunciante> registerAnunciante(@Header("Authorization") String token, @Body Anunciante anunciante); // <Anunciante>

    @PATCH("/advertiser/updateProfile/{email}")
    Call<Anunciante> updateAnuncianteProfile(@Header("Authorization") String token, @Path("email") String email, @Body Anunciante anunciante);

}
