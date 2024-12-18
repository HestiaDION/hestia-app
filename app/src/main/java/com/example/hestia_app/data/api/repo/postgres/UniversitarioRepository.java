package com.example.hestia_app.data.api.repo.postgres;

import com.example.hestia_app.domain.models.Universitario;

import java.util.UUID;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UniversitarioRepository {

    @GET("/university/profile/{email}")
    Call<Universitario> getUniversityProfile(@Header("Authorization") String token, @Path("email") String email);

    @POST("/university/register")
    Call<Universitario> registerUniversitario(@Header("Authorization") String token, @Body Universitario universitario); // <Universitario>

    @PATCH("/university/updateProfile/{email}")
    Call<Universitario> updateUniversitarioProfile(@Header("Authorization") String token, @Path("email") String email, @Body Universitario universitario);

    @GET("/university/findId/{email}")
    Call<UUID> getUniversitarioId(@Header("Authorization") String token, @Path("email") String email);

}
    