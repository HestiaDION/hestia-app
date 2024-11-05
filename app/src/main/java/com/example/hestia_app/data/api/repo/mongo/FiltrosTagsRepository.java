package com.example.hestia_app.data.api.repo.mongo;

import com.example.hestia_app.domain.models.FiltrosTags;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FiltrosTagsRepository {
    @POST("/filtros_tag/addFiltrosTag")
    Call<FiltrosTags> addFiltrosTag(@Body FiltrosTags filtrosTags);

    @GET("/filtros_tag/getFiltrosTag/{id_usuario_moradia}")
    Call<FiltrosTags> getFiltrosTag(@Path("id_usuario_moradia") String id_usuario_moradia);
}
