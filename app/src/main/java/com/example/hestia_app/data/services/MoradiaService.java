package com.example.hestia_app.data.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.ListaMoradiasCallback;
import com.example.hestia_app.data.api.callbacks.RegistroMoradiaCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.data.api.repo.AnuncianteRepository;
import com.example.hestia_app.data.api.repo.MoradiaRepository;
import com.example.hestia_app.domain.models.Anunciante;
import com.example.hestia_app.domain.models.Moradia;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoradiaService {

    MoradiaRepository moradiaRepository = RetrofitPostgresClient.getClient().create(MoradiaRepository.class);

    public void registrarMoradia(Moradia moradia, RegistroMoradiaCallback callback) {
        Call<Moradia> call = moradiaRepository.registerMoradia(moradia);

        call.enqueue(new Callback<Moradia>() {
            @Override
            public void onResponse(Call<Moradia> call, Response<Moradia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Moradia", "Registro de moradia bem-sucedido: " + response.body().toString());
                    callback.onRegistroSuccess(true);
                } else {
                    Log.e("Moradia", "Falha no registro de moradia: Código de resposta " + response.code());
                    callback.onRegistroFailure(false);
                }
            }

            @Override
            public void onFailure(Call<Moradia> call, Throwable t) {
                Log.e("Moradia", "Erro na chamada de registro de moradia: " + t.getMessage(), t);
                callback.onRegistroFailure(false);
            }
        });
    }


    public void getMoradiasByAdvertiser(String id, ListaMoradiasCallback callback) {
        Call<List<Moradia>> call = moradiaRepository.getMoradiasByAdvertiser(id);

        call.enqueue(new retrofit2.Callback<List<Moradia>>() {
            @Override
            public void onResponse(Call<List<Moradia>> call, retrofit2.Response<List<Moradia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    Log.d("Moradia", "Listagem de moradias por anunciante_id bem-sucedida: " + response.body().toString());
                    callback.onFailure(new Exception("Falha na resposta da API: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Moradia>> call, Throwable t) {
                Log.e("Moradia", "Erro na chamada da listagem de moradias por anunciante_id: " + t.getMessage(), t);
                callback.onFailure(t);
            }
        });
    }



}
