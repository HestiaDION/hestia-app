package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.Anunciante;

public interface PerfilAnuncianteCallback {

    void onPerfilAnuncianteSuccess(Anunciante anunciante);
    void onPerfilAnuncianteFailure(String errorMessage);
}
