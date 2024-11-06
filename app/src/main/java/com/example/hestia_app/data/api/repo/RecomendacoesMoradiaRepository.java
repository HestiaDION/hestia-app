package com.example.hestia_app.data.api.repo;

import com.example.hestia_app.domain.models.RecomendacoesMoradia;
import com.example.hestia_app.domain.models.UniversityRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RecomendacoesMoradiaRepository {
    @POST("recommended-homes")
    Call<RecomendacoesMoradia> getRecomendacoesMoradia(@Body UniversityRequest universityRequest);
}
