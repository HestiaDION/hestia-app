package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.Moradia;

public interface RegistroMoradiaCallback {
    void onRegistroSuccess(boolean isRegistered, Moradia moradia);
    void onRegistroFailure(boolean isRegistered);
}
