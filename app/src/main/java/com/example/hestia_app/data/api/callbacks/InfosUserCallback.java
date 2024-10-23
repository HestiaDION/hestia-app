package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.data.api.InfoUserRepository;

public interface InfosUserCallback {
    void onSuccess(InfoUserRepository response);
    void onFailure(Throwable t);
}