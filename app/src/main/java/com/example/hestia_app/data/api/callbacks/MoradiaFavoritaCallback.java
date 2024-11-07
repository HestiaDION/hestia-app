package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.MoradiaFavorita;

public interface MoradiaFavoritaCallback {
    void moradiaFavoritaOnSuccess(MoradiaFavorita moradiaFavorita);
    void moradiaFavoritaOnFailure(String message);
}
