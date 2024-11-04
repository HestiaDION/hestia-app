package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.ImagensMoradia;

public interface ImagensMoradiaCallback {
    void onSuccess(ImagensMoradia response);
    void onFailure(Throwable t);
}
