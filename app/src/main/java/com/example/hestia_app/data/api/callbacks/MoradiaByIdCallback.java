package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.Moradia;

public interface MoradiaByIdCallback {
    void onSuccess(Moradia moradias);
    void onFailure(String message);
}
