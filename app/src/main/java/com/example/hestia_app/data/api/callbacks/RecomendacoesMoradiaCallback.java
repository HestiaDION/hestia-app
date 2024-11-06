package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.RecomendacoesMoradia;

public interface RecomendacoesMoradiaCallback {

    void onRecomendacoesSuccess(boolean success, RecomendacoesMoradia recomendacoesMoradia);
    void onRecomendacoesFailure(boolean failure);
}
