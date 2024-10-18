package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.data.models.FiltroCadastro;

import java.util.List;

public interface FiltroCadastroCallback {
    void onFiltroCadastroSuccess(List<FiltroCadastro> filtros);
    void onFiltroCadastroFailure(String errorMessage);
}
