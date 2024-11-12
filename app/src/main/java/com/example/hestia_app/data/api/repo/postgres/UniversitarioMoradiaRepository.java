package com.example.hestia_app.data.api.repo.postgres;

import com.example.hestia_app.domain.models.Universitario;
import com.example.hestia_app.domain.models.UniversitarioMoradia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UniversitarioMoradiaRepository {

    @POST("/property/joinProperty")
    Call<UniversitarioMoradia> joinMoradia(@Header("Authorization") String token, @Body UniversitarioMoradia universitarioMoradia);

    @GET("/listUniversitiesByImovelId/{imovelId}")
    Call<List<Universitario>> getUniversitariosByImovelId(@Header("Authorization") String token, @Header("imovelId") String imovelId);
}
