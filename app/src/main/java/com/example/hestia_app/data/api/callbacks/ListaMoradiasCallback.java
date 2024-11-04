package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.Moradia;

import java.util.List;

public interface ListaMoradiasCallback {

    void onSuccess(List<Moradia> moradias);
    void onFailure(Throwable t);
}
