package com.example.hestia_app.data.api;

import com.example.hestia_app.domain.models.InfosUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InfoUserRepository {

    @POST("/infosUser/addInfosUser")
    Call<InfoUserRepository> addFiltrosTag(@Body InfosUser infosUser);


    @PATCH("/infosUser/updateInfosUser")
    Call<InfoUserRepository> updateProfilePhotoUrlMongoCollection(@Path("email") String email, @Body InfosUser urlPhoto);
}
