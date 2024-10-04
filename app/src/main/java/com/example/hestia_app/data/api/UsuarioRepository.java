package com.example.hestia_app.data.api;

import com.example.hestia_app.data.models.Usuario;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuarioRepository {

    @GET("/user/{email}")
    Call<Usuario> getUserOrigin(@Path("email") String email);
}
