package com.example.hestia_app.data.api.repo.mongo;

import com.example.hestia_app.domain.models.ImagensMoradia;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ImagensMoradiaRepository {
    @POST("imagens_moradia/addImagensMoradia")
    Call<ImagensMoradia> addImagensMoradia(@Body ImagensMoradia imagensMoradia);

    @GET("imagens_moradia/getImagensMoradia/{id_moradia}")
    Call<ImagensMoradia> getImagensMoradia(@Path("id_moradia") UUID id_moradia);
}
