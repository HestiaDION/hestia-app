package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.InfosUser;

public interface InfosUserCallback {
    void onSuccess(InfosUser response);
    void onFailure(Throwable t);
}