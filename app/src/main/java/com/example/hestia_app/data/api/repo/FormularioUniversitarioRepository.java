package com.example.hestia_app.data.api.repo;

import com.example.hestia_app.domain.models.FormularioUniversitario;
import com.example.hestia_app.domain.models.ProbabilityResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FormularioUniversitarioRepository {

    @POST("/predict_user")
    Call<ProbabilityResponse> predictUser(@Body FormularioUniversitario formularioUniversitario);
}
