package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.FiltrosTags;

import java.util.List;

public interface FiltrosTagsCallbackGet {
    void onSuccess(FiltrosTags tags);
    void onFailure(String errorMessage);
}
