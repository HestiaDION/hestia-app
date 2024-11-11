package com.example.hestia_app.data.services;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.hestia_app.data.api.callbacks.JoinMoradiaCallback;
import com.example.hestia_app.data.api.callbacks.ListUniversitariosCallback;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.data.api.repo.postgres.PagamentoRepository;
import com.example.hestia_app.data.api.repo.postgres.UniversitarioMoradiaRepository;
import com.example.hestia_app.domain.models.Pagamento;
import com.example.hestia_app.domain.models.Universitario;
import com.example.hestia_app.domain.models.UniversitarioMoradia;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UniversitarioMoradiaService {

    private final UniversitarioMoradiaRepository universitarioMoradiaRepository = RetrofitPostgresClient.getClient().create(UniversitarioMoradiaRepository.class);
    private String token = "";
    SharedPreferences sharedPreferences;

    // Construtor que recebe o contexto e inicializa o SharedPreferences
    public UniversitarioMoradiaService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    }

    public void entrarEmUmaMoradia(UniversitarioMoradia universitarioMoradia, JoinMoradiaCallback callback){

        token = sharedPreferences.getString("token", null);
        Call<UniversitarioMoradia> call = universitarioMoradiaRepository.joinMoradia(token, universitarioMoradia);

        call.enqueue(new Callback<UniversitarioMoradia>() {
            @Override
            public void onResponse(@NonNull Call<UniversitarioMoradia> call, @NonNull Response<UniversitarioMoradia> response) {

            }

            @Override
            public void onFailure(@NonNull Call<UniversitarioMoradia> call, @NonNull Throwable t) {

            }
        });
    }

    public void getUniversitariosByImovelId(String imovelId, ListUniversitariosCallback callback) {

        token = sharedPreferences.getString("token", null);
        Call<List<Universitario>> call = universitarioMoradiaRepository.getUniversitariosByImovelId(token, imovelId);

        call.enqueue(new Callback<List<Universitario>>() {
            @Override
            public void onResponse(Call<List<Universitario>> call, Response<List<Universitario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onListSuccess(response.body());
                } else {
                    callback.onListError(new Exception("Erro ao buscar universitários: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Universitario>> call, Throwable t) {
                callback.onListError(new Exception("Falha na requisição: " + t.getMessage()));
            }
        });
    }
}
