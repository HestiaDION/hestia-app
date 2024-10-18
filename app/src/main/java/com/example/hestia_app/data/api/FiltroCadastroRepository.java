package com.example.hestia_app.data.api;

import com.example.hestia_app.data.models.FiltroCadastro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FiltroCadastroRepository {
    @GET("/filtros/categoria/{categoria}")
    Call<List<FiltroCadastro>> getFiltrosPorCategoria(@Path("categoria") String categoria);
}
