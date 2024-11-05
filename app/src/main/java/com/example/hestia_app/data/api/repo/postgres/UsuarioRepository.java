package com.example.hestia_app.data.api.repo.postgres;

import com.example.hestia_app.domain.models.Usuario;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UsuarioRepository {

    @GET("/user/{email}")
    Call<Usuario> getUserOrigin(@Header("Authorization") String token, @Path("email") String email);
}
