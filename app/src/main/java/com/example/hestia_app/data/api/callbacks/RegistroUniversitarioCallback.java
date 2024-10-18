package com.example.hestia_app.data.api.callbacks;

public interface RegistroUniversitarioCallback {

    void onRegistroSuccess(boolean isRegistered);
    void onRegistroFailure(boolean isRegistered);
}
