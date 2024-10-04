package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.data.models.Usuario;

public interface UsuarioCallback {
    void onUsuarioReceived(String origem); // Sucesso
    void onFailure(String errorMessage);     // Falha
}
