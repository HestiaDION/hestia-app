package com.example.hestia_app.data.api.callbacks;

public interface UsuarioCallback {
    void onUsuarioReceived(String origem); // Sucesso
    void onFailure(String errorMessage);     // Falha
}
