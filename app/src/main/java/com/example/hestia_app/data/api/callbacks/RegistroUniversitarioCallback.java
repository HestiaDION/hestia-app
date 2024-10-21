package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.Universitario;

import java.util.UUID;

public interface RegistroUniversitarioCallback {

    void onRegistroSuccess(boolean isRegistered, UUID idUniversitario);
    void onRegistroFailure(boolean isRegistered);
}
