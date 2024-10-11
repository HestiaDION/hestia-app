package com.example.hestia_app.data.api.callbacks;

public interface UpdatePerfilAnuncianteCallback {

    void onUpdateSuccess(boolean isUpdated);
    void onUpdateFailure(String errorMessage);
}
