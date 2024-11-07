package com.example.hestia_app.data.api.callbacks;


import com.example.hestia_app.domain.models.Pagamento;

public interface RegistroPagamentoCallback {
    void onRegistroSuccess(boolean isPaid, Pagamento pagamento);
    void onRegistroFailure(boolean isPaid);
}
