package com.example.hestia_app.data.api.repo.postgres;

import com.example.hestia_app.domain.models.Moradia;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MoradiaRepository {

    @POST("/ad/register")
    Call<Moradia> registerMoradia(@Header ("Authorization") String token, @Body Moradia moradia);

    @GET("/ad/property/listAllByAdvertiser/{email}")
    Call <List<Moradia>> getMoradiasByAdvertiser(@Header ("Authorization") String token, @Path("email") String email);


    @GET("/ad/property/getPropertyByAdId/{id}")
    Call <Moradia> getMoradiaById(@Header ("Authorization") String token, @Path("id") UUID id);
}
