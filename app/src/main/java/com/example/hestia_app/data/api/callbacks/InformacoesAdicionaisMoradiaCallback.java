package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.InformacoesAdicionaisMoradia;

public interface InformacoesAdicionaisMoradiaCallback {
    void onSuccess(InformacoesAdicionaisMoradia response);
    void onFailure(Throwable t);
}
