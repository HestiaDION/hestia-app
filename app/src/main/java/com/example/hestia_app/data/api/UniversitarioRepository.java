package com.example.hestia_app.data.api;

import com.example.hestia_app.data.models.Universitario;

import java.util.UUID;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;

public interface UniversitarioRepository {

    @GET("/university/profile/{id}")
    Call<Universitario> getUniversityProfile(@Path("id") UUID id);


}
    