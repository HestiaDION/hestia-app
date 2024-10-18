package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.data.models.FiltrosTags;


public interface FiltrosTagsCallback {
    void onFiltroCadastroSuccess(boolean IsRegistered);
    void onFiltroCadastroFailure(boolean IsRegistered);
}
