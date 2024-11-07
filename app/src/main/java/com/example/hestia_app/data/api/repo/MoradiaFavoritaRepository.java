package com.example.hestia_app.data.api.repo;

import com.example.hestia_app.domain.models.MoradiaFavorita;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MoradiaFavoritaRepository {
    @POST("/moradiasFavoritas/addMoradiasFavoritas")
    Call<MoradiaFavorita> addMoradiasFavoritas(@Body MoradiaFavorita moradiaFavorita);

    @GET("/moradiasFavoritas/getMoradiasFavoritas/{id_usuario}")
    Call<MoradiaFavorita> getMoradiasFavoritas(@Path("id_usuario") UUID id_moradia);

    @DELETE("/moradiasFavoritas/deleteMoradiasFavoritas/{id_usuario}/{id_moradia}")
    Call<MoradiaFavorita> deleteMoradiasFavoritas(@Path("id_moradia") UUID id_moradia, @Path("id_usuario") UUID id_usuario);
}
