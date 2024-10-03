package com.example.hestia_app.data.api.callbacks;

public interface RegistroAnuncianteCallback {

    void onRegistroSuccess(boolean isRegistered);
    void onRegistroFailure(boolean isRegistered);
}
