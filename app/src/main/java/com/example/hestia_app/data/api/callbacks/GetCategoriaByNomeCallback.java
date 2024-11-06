package com.example.hestia_app.data.api.callbacks;

import java.util.HashMap;

public interface GetCategoriaByNomeCallback {

    void onGetCategoriaByNomeCallbackSuccess(HashMap<String, String> categoria);
    void onGetCategoriaByNomeCallbackFailure(String errorMessage);
}
