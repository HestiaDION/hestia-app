package com.example.hestia_app.data.api.repo;

import com.example.hestia_app.domain.models.InformacoesAdicionaisMoradia;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InformacoesAdicionaisMoradiaRepository {
    @POST("/infosMoradia/addInfosMoradias")
    Call<InformacoesAdicionaisMoradia> addInfosMoradias(@Body InformacoesAdicionaisMoradia informacoesAdicionaisMoradia);

    @GET("/infosMoradia/getInfosMoradias/{id_moradia}")
    Call<InformacoesAdicionaisMoradia> getInfosMoradias(@Path("id_moradia") UUID id_moradia);
}
