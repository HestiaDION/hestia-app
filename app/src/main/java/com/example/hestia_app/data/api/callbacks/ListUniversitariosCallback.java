package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.Universitario;

import java.util.List;

public interface ListUniversitariosCallback {

    void onListSuccess(List<Universitario> universitarios);
    void onListError(Exception e);
}
