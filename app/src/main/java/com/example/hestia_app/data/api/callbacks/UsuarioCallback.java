package com.example.hestia_app.data.api.callbacks;

import retrofit2.Response;

public interface UsuarioCallback {
    void onUsuarioReceived(String origem); // Sucesso
    void onFailure(String errorMessage, Response<?> response);     // Falha
}
