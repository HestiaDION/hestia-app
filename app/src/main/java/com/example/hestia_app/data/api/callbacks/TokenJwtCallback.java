package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.Token;

public interface TokenJwtCallback {

    void onSuccess(Token token);
    void onFailure(String t);
}
