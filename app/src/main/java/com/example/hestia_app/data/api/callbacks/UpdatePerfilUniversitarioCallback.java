package com.example.hestia_app.data.api.callbacks;

public interface UpdatePerfilUniversitarioCallback {

    void onUpdateSuccess(boolean isUpdated);
    void onUpdateFailure(String errorMessage);
}
