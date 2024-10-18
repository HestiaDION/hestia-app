package com.example.hestia_app.data.api.callbacks;


import com.example.hestia_app.domain.models.Universitario;

public interface PerfilUniversitarioCallback {

    void onPerfilUniversitarioSuccess(Universitario universitario);
    void onPerfilUniversitarioFailure(String errorMessage);
}
