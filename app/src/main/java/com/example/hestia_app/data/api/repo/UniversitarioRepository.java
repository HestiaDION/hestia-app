package com.example.hestia_app.data.api.repo;

import com.example.hestia_app.domain.models.Universitario;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UniversitarioRepository {

    @GET("/university/profile/{email}")
    Call<Universitario> getUniversityProfile(@Path("email") String email);

    @POST("/university/register")
    Call<Universitario> registerUniversitario(@Body Universitario universitario); // <Universitario>

    @PATCH("/university/updateProfile/{email}")
    Call<Universitario> updateUniversitarioProfile(@Path("email") String email, @Body Universitario universitario);

}
    