package com.example.hestia_app.data.api.repo;

import com.example.hestia_app.domain.models.Moradia;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MoradiaRepository {

    @POST("/ad/register")
    Call<Moradia> registerMoradia(@Body Moradia moradia);

    @GET("/ad/property/listAllByAdvertiser/{email}")
    Call <List<Moradia>> getMoradiasByAdvertiser(@Path("email") String email);
}
