package com.example.hestia_app.data.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceRepository {
    @GET("/status")
    Call<Map<String, String>> wakeUpApi();
}
