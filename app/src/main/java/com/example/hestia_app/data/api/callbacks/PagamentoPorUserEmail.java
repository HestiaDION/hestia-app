package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.Pagamento;

public interface PagamentoPorUserEmail {

    void onFindSuccess(boolean wasFound, Pagamento pagamento);
    void onFindFailure(boolean wasFound);
}
