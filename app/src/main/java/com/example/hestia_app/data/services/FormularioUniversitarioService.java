package com.example.hestia_app.data.services;

import android.util.Log;

import com.example.hestia_app.data.api.clients.RetrofitFormsClient;
import com.example.hestia_app.data.api.repo.FormularioUniversitarioRepository;
import com.example.hestia_app.domain.models.FormularioUniversitario;
import com.example.hestia_app.domain.models.ProbabilityResponse;

import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;

public class FormularioUniversitarioService {

    FormularioUniversitarioRepository formularioUniversitarioRepository = RetrofitFormsClient.getClient().create(FormularioUniversitarioRepository.class);

    public ProbabilityResponse enviarFormularioUniversitario(FormularioUniversitario formularioUniversitario) {
        Call<ProbabilityResponse> call = formularioUniversitarioRepository.predictUser(formularioUniversitario);
        try {
            Response<ProbabilityResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                Log.d("Probabilidade", "Probabilidade: " + response.body().getProbability());
                return response.body();
            } else {

                return null;
            }
        } catch (IOException e) {
            Log.e("Probabilidade", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
